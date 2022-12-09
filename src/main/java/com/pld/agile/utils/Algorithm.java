package com.pld.agile.utils;

import com.pld.agile.model.DeliveryRequest;
import com.pld.agile.model.Intersection;
import com.pld.agile.model.RoadSegment;
import com.pld.agile.model.Tour;
import com.pld.agile.model.enums.TimeWindow;

import java.util.*;

public class Algorithm {

    static Tour optimalTour;
    static HashMap<Long, HashMap<Long, Double>> tspCost;
    static HashMap<Intersection, HashMap<Intersection, LinkedList<Intersection>>> shortestPathBetweenTwoDeliveryLocations;
    static LinkedList<DeliveryRequest> bestSol;
    static Double bestSolCost;

    public static Tour ExecuteAlgorithm(Intersection warehouse, List<DeliveryRequest> deliveryRequests) {

        // Instantiation of attributes
        optimalTour = new Tour();
        tspCost = new HashMap<>();
        shortestPathBetweenTwoDeliveryLocations = new HashMap<>();

        // Sorting delivery requests based on ascending TimeWindow
        deliveryRequests.sort(new Comparator<DeliveryRequest>() {
            @Override
            public int compare(DeliveryRequest o1, DeliveryRequest o2) {
                return o1.getTimeWindow().isBefore(o2.getTimeWindow());
            }
        });

        DeliveryRequest deliveryRequestWarehouse = new DeliveryRequest(null, warehouse);

        /* Dijkstra Algorithm to calculate tspCost */
        ArrayList<DeliveryRequest> destinations = new ArrayList<>(deliveryRequests);
        dijkstra(deliveryRequestWarehouse, destinations);
        System.out.println(tspCost);
        destinations.add(deliveryRequestWarehouse);
        for (DeliveryRequest deliveryRequest : deliveryRequests) {
            destinations.remove(deliveryRequest);
            dijkstra(deliveryRequest, destinations);
            System.out.println(tspCost);
            destinations.add(deliveryRequest);
        }

        /* Branch and Bound Algorithm */
        bestSol = new LinkedList<>();
        bestSolCost = Double.MAX_VALUE;

        LinkedList<DeliveryRequest> visited = new LinkedList<>();
        visited.add(deliveryRequestWarehouse);
        LinkedList<DeliveryRequest> unvisited = new LinkedList<>(deliveryRequests);

        branchAndBound(visited, unvisited, 0.0);

        /* Calculating the optimal tour and updating arrival time for each delivery request */
        // We start from warehouse at 8h
        Double arrivalTime = 8.0;
        for (int i = 0; i < bestSol.size(); i++) {
            DeliveryRequest deliveryRequest = bestSol.get(i);
            DeliveryRequest nextDeliveryRequest = bestSol.get((i + 1) % bestSol.size());

            // Add delivery requests to optimalTour, but not the warehouse
            if (i > 0) {
                optimalTour.addDeliveryRequest(deliveryRequest);
            }

            // Add the shortest path between deliveryRequest and nextDeliveryRequest to optimalTour
            optimalTour.getIntersections().addAll(shortestPathBetweenTwoDeliveryLocations.get(deliveryRequest.getAddress()).get(nextDeliveryRequest.getAddress()));

            // Calculating arrivalTime by adding the duration between deliveryRequest and nextDeliveryRequest
            arrivalTime += tspCost.get(deliveryRequest.getAddress().getId()).get(nextDeliveryRequest.getAddress().getId()) / (1000.0 * 15.0);

            // If the courier arrives to a delivery location before the start of its time window, they should wait until the start of the time window
            TimeWindow timeWindow = nextDeliveryRequest.getTimeWindow();
            if (timeWindow != null && arrivalTime < timeWindow.getStart()) {
                arrivalTime = (double) timeWindow.getStart();
            }
            nextDeliveryRequest.setArrivalTime(arrivalTime);

            // The courier takes 5 minutes to perform a delivery
            arrivalTime += 5.0 / 60.0;
        }

        /* Calculating the duration of optimalTour */
        optimalTour.setTourDuration(bestSol.get(0).getArrivalTime() - 8.0);

        return optimalTour;
    }

    private static Intersection getLowestDistanceIntersection(HashMap<Intersection, Double> unsettledVertices) {
        Intersection res = null;
        Double lowestDistance = Double.MAX_VALUE;
        for (Map.Entry<Intersection, Double> entry : unsettledVertices.entrySet()) {
            if (entry.getValue() < lowestDistance) {
                lowestDistance = entry.getValue();
                res = entry.getKey();
            }
        }
        return res;
    }

    private static void release(Intersection vi, Intersection vj, Double distIJ, HashMap<Intersection, Double> unsettledVertices, HashMap<Intersection, Intersection> previousIntersection) {
        Double di = unsettledVertices.get(vi);
        Double dj = unsettledVertices.get(vj);
        if (dj > di + distIJ) {
            dj = di + distIJ;
            // We update the distance dj from source to vertex j
            unsettledVertices.put(vj, dj);
            // We update the previous intersection of vertex j
            previousIntersection.put(vj, vi);
        }
    }

    private static void dijkstra(DeliveryRequest source, List<DeliveryRequest> destinations) {
        HashMap<Intersection, Intersection> previousIntersection = new HashMap<>();

        HashMap<Intersection, Double> settledVertices = new HashMap<>();
        HashMap<Intersection, Double> unsettledVertices = new HashMap<>();

        HashMap<Long, Double> hMap = new HashMap<>();
        hMap.put(source.getAddress().getId(), 0.0);
        tspCost.put(source.getAddress().getId(), hMap);

        unsettledVertices.put(source.getAddress(), 0.0);

        while (unsettledVertices.size() != 0) {
            Intersection intersection = getLowestDistanceIntersection(unsettledVertices);

            List<RoadSegment> outgoingSegments = intersection.getOutgoingSegments();

            for (RoadSegment outgoingSegment : outgoingSegments) {

                Intersection destination = outgoingSegment.getDestination();
                Double length = outgoingSegment.getLength();

                if (!settledVertices.containsKey(destination)) {

                    if (!unsettledVertices.containsKey(destination)) {
                        unsettledVertices.put(destination, Double.MAX_VALUE);
                    }
                    release(intersection, destination, length, unsettledVertices, previousIntersection);
                }
            }
            settledVertices.put(intersection, unsettledVertices.get(intersection));
            unsettledVertices.remove(intersection);
        }

        HashMap<Intersection, LinkedList<Intersection>> shortestPathsFromSource = new HashMap<>();
        for (DeliveryRequest destination : destinations) {
            Intersection destinationIntersection = destination.getAddress();

            /* If the arc between source and destination is valid, then we add the update the distance in tspCost.
            * Otherwise, the distance is null.
            */
            if (isArc(source, destination, destinations)) {
                tspCost.get(source.getAddress().getId()).put(destinationIntersection.getId(), settledVertices.get(destinationIntersection));
            } else {
                tspCost.get(source.getAddress().getId()).put(destinationIntersection.getId(), null);
            }

            LinkedList<Intersection> shortestPath = new LinkedList<>();
            shortestPath.add(destinationIntersection);
            Intersection currentIntersection = destinationIntersection;
            Intersection prevIntersection;
            while ((prevIntersection = previousIntersection.get(currentIntersection)) != null) {
                shortestPath.addFirst(prevIntersection);
                currentIntersection = prevIntersection;
            }
            shortestPathsFromSource.put(destinationIntersection, shortestPath);
        }

        shortestPathBetweenTwoDeliveryLocations.put(source.getAddress(), shortestPathsFromSource);
    }

    /**
     * If the source is the warehouse, we search for the first time window in the list of delivery requests (destinations).
     *
     * Else, we search for the next time window from source such as the start of next time window
     * is greater or equal than the end of the source time window, and is the smallest.
     *
     * @param sourceTimeWindow
     * @param destinations
     * @return the next time window.
     */
    private static TimeWindow getNextTimeWindow(TimeWindow sourceTimeWindow, List<DeliveryRequest> destinations) {
        TimeWindow nextTimeWindow;
        if (sourceTimeWindow == null) {
            nextTimeWindow = TimeWindow.TW_11_12;
            for (DeliveryRequest deliveryRequest : destinations) {
                if (deliveryRequest.getTimeWindow() != null && deliveryRequest.getTimeWindow().getStart() < nextTimeWindow.getStart()) {
                    nextTimeWindow = deliveryRequest.getTimeWindow();
                }
            }
        } else {
            nextTimeWindow = TimeWindow.TW_11_12;
            for (DeliveryRequest deliveryRequest : destinations) {
                if (deliveryRequest.getTimeWindow() != null
                        && deliveryRequest.getTimeWindow().getStart() < nextTimeWindow.getStart()
                        && deliveryRequest.getTimeWindow().getStart() >= sourceTimeWindow.getEnd()) {
                    nextTimeWindow = deliveryRequest.getTimeWindow();
                }
            }
        }
        return nextTimeWindow;
    }

    /**
     * Method that searches for the last time window in the list of destinations
     *
     * @param sourceTimeWindow
     * @param destinations
     * @return the last time window.
     */
    private static TimeWindow getLastTimeWindow(TimeWindow sourceTimeWindow, List<DeliveryRequest> destinations) {
        TimeWindow lastTimeWindow = sourceTimeWindow;
        for (DeliveryRequest destination : destinations) {
            if (destination.getTimeWindow() != null && destination.getTimeWindow().getStart() > lastTimeWindow.getStart()) {
                lastTimeWindow = destination.getTimeWindow();
            }
        }
        return lastTimeWindow;
    }

    /**
     * If the source is the warehouse, then the destination should have a time window equal to the first time
     * window.
     * If the destination is the warehouse, then the source should have the last time window.
     * Otherwise, the source and the destination should have the same time window, or the destination should have
     * the next time window of source time window.
     *
     * @param source
     * @param destination
     * @param destinations
     * @return true if it is valid to go from source to destination, and false otherwise.
     */
    private static boolean isArc(DeliveryRequest source, DeliveryRequest destination, List<DeliveryRequest> destinations) {
        TimeWindow sourceTimeWindow = source.getTimeWindow();
        TimeWindow nextTimeWindow = getNextTimeWindow(sourceTimeWindow, destinations);
        if (sourceTimeWindow == null) {
            return destination.getTimeWindow().getStart() == nextTimeWindow.getStart();
        } else if (destination.getTimeWindow() == null) {
            TimeWindow lastTimeWindow = getLastTimeWindow(sourceTimeWindow, destinations);
            return sourceTimeWindow.getStart() == lastTimeWindow.getStart();
        } else return sourceTimeWindow.getStart() == destination.getTimeWindow().getStart()
                || destination.getTimeWindow().getStart() == nextTimeWindow.getStart();
    }

    /**
     * Method that estimates a lower bound of the remaining distance to the warehouse
     *
     * @param lastVisitedVertex the last visited vertex
     * @param unvisited         the list of unvisited vertices
     * @param warehouse         the warehouse vertex
     * @return a lower bound of the cost of paths in the graph of delivery locations starting from <code>lastVisitedVertex</code>, visiting
     * every vertex in <code>unvisited</code> exactly once, and returning to <code>warehouse</code>.
     */
    private static Double bound(DeliveryRequest lastVisitedVertex, List<DeliveryRequest> unvisited, DeliveryRequest warehouse) {
        Double lowerBound = Double.MAX_VALUE;

        //int unvisitedSize = unvisited.size();

        // Finding the first and the second time windows
        /*TimeWindow firstTW = lastVisitedVertex.getTimeWindow();
        TimeWindow secondTW = getNextTimeWindow(firstTW, unvisited);*/

        // Calculating the minimum distance between the lastVisitedVertex and the unvisited delivery requests
        Double distance;
        for (DeliveryRequest vertex : unvisited) {
            if (isArc(lastVisitedVertex, vertex, unvisited)) {
                distance = tspCost.get(lastVisitedVertex.getAddress().getId()).get(vertex.getAddress().getId());
                if (distance < lowerBound) {
                    lowerBound = distance;
                }
            }
        }
        /*// Adding the distance between each unvisited vertex and the rest of unvisited vertices within the first and second time windows of unvisited delivery requests
        for (i = 0; i < unvisitedSize; i++) {
            if (unvisited.get(i).getTimeWindow() == firstTW || unvisited.get(i).getTimeWindow() == secondTW) {
                Double li = Double.MAX_VALUE;
                for (int j = 0; j < unvisitedSize; j++) {
                    if (i != j && (unvisited.get(j).getTimeWindow() == firstTW || unvisited.get(j).getTimeWindow() == secondTW)) {
                        Double distance = tspCost.get(unvisited.get(i).getAddress().getId()).get(unvisited.get(j).getAddress().getId());
                        if (distance < li) {
                            li = distance;
                        }
                    }
                }
                // If there is only one vertex in the list of unvisited vertices, then li is the distance between this vertex and the warehouse
                lowerBound += (li != Double.MAX_VALUE ? li : tspCost.get(unvisited.get(i).getAddress().getId()).get(warehouse.getAddress().getId()));
            }
        }*/
        return lowerBound;
    }

    /**
     * Template method of a branch and bound algorithm for solving the TSP in <code>g</code>.
     *
     * @param visited     the sequence of vertices that have been already visited
     * @param unvisited   the set of vertex that have not yet been visited
     * @param currentCost the tspCost of the path corresponding to <code>visited</code>
     */
    private static void branchAndBound(LinkedList<DeliveryRequest> visited, LinkedList<DeliveryRequest> unvisited, Double currentCost) {
        int visitedSize = visited.size();
        int unvisitedSize = unvisited.size();
        LinkedList<DeliveryRequest> unvisitedBeforePermutation = new LinkedList<>(unvisited);

        if (unvisitedSize == 0) {
            Long lastVisitedIntersectionId = visited.get(visitedSize - 1).getAddress().getId();
            Long warehouseIntersectionId = visited.get(0).getAddress().getId();
            currentCost += tspCost.get(lastVisitedIntersectionId).get(warehouseIntersectionId);

            if (currentCost < bestSolCost) {
                bestSolCost = currentCost;
                bestSol = new LinkedList<>(visited);
            }

        } else if (currentCost + bound(visited.get(visitedSize - 1), unvisitedBeforePermutation, visited.get(0)) < bestSolCost) {

            for (int i = 0; i < unvisitedBeforePermutation.size(); i++) {
                DeliveryRequest vertex = unvisitedBeforePermutation.get(i);

                visited.add(vertex);
                unvisited.remove(vertex);

                visitedSize = visited.size();

                DeliveryRequest beforeLastVisited = visited.get(visitedSize - 2);
                DeliveryRequest lastVisited = visited.get(visitedSize - 1);

                if (tspCost.get(beforeLastVisited.getAddress().getId()).get(lastVisited.getAddress().getId()) != null) {
                    branchAndBound(visited, unvisited, currentCost + tspCost.get(beforeLastVisited.getAddress().getId()).get(lastVisited.getAddress().getId()));
                }

                visited.remove(vertex);
                unvisited.add(vertex);
            }
        }
    }

}