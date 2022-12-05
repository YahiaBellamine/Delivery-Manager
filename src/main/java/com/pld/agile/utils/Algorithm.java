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

    public static Tour ExecuteAlgorithm(Intersection warehouse, LinkedList<DeliveryRequest> deliveryRequests) {

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

        /* Dijkstra Algorithm to calculate tspCost */
        ArrayList<Intersection> deliveryLocations = new ArrayList<>();
        for (DeliveryRequest deliveryRequest : deliveryRequests) {
            deliveryLocations.add(deliveryRequest.getAddress());
        }

        ArrayList<Intersection> destinations = new ArrayList<>(deliveryLocations);
        dijkstra(warehouse, destinations);
        destinations.add(warehouse);
        for (Intersection deliveryLocation : deliveryLocations) {
            destinations.remove(deliveryLocation);
            dijkstra(deliveryLocation, destinations);
            destinations.add(deliveryLocation);
        }

        /* Branch and Bound Algorithm */
        bestSol = new LinkedList<>();
        bestSolCost = Double.MAX_VALUE;

        LinkedList<DeliveryRequest> visited = new LinkedList<>();
        visited.add(new DeliveryRequest(null, warehouse));
        LinkedList<DeliveryRequest> unvisited = new LinkedList<>(deliveryRequests);

        branchAndBound(visited, unvisited, 0.0);

        /* Calculating the optimal tour and updating passing time for each delivery request */
        // We start from warehouse at 8h
        Double passingTime = 8.0;
        for (int i = 0; i < bestSol.size(); i++) {
            DeliveryRequest deliveryRequest = bestSol.get(i);
            DeliveryRequest nextDeliveryRequest = bestSol.get((i + 1) % bestSol.size());

            // Add delivery requests to optimalTour, but not the warehouse
            if(i>0){
                optimalTour.addDeliveryRequest(deliveryRequest);
            }

            // Add the shortest path between deliveryRequest and nextDeliveryRequest to optimalTour
            optimalTour.getIntersections().addAll(shortestPathBetweenTwoDeliveryLocations.get(deliveryRequest.getAddress()).get(nextDeliveryRequest.getAddress()));

            // Calculating passingTime by adding the duration between deliveryRequest and nextDeliveryRequest
            passingTime += tspCost.get(deliveryRequest.getAddress().getId()).get(nextDeliveryRequest.getAddress().getId()) / (1000.0 * 15.0);

            // If the courier arrives to a delivery location before the start of its time window, they should wait until the start of the time window
            TimeWindow timeWindow = nextDeliveryRequest.getTimeWindow();
            if (timeWindow != null && passingTime < timeWindow.getStart()) {
                passingTime = (double) timeWindow.getStart();
            }
            nextDeliveryRequest.setPassingTime(passingTime);

            // The courier takes 5 minutes to perform a delivery
            passingTime += 5.0 / 60.0;
        }

        /* Calculating the duration of optimalTour */
        optimalTour.setTourDuration(bestSol.get(0).getPassingTime() - 8.0);

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

    private static void dijkstra(Intersection source, List<Intersection> destinations) {
        HashMap<Intersection, Intersection> previousIntersection = new HashMap<>();

        HashMap<Intersection, Double> settledVertices = new HashMap<>();
        HashMap<Intersection, Double> unsettledVertices = new HashMap<>();

        HashMap<Long, Double> hMap = new HashMap<>();
        hMap.put(source.getId(), 0.0);
        tspCost.put(source.getId(), hMap);

        unsettledVertices.put(source, 0.0);

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
        for (Intersection destination : destinations) {
            tspCost.get(source.getId()).put(destination.getId(), settledVertices.get(destination));
            LinkedList<Intersection> shortestPath = new LinkedList<>();
            shortestPath.add(destination);
            Intersection currentIntersection = destination;
            Intersection prevIntersection;
            while ((prevIntersection = previousIntersection.get(currentIntersection)) != null) {
                shortestPath.addFirst(prevIntersection);
                currentIntersection = prevIntersection;
            }
            shortestPathsFromSource.put(destination, shortestPath);
        }

        shortestPathBetweenTwoDeliveryLocations.put(source, shortestPathsFromSource);
    }

    /**
     * Method that must be defined in TemplateTSP subclasses
     *
     * @param lastVisitedVertex the last visited vertex
     * @param unvisited         the list of unvisited vertices
     * @param warehouse         the warehouse vertex
     * @return a lower bound of the cost of paths in the graph of delivery locations starting from <code>lastVisitedVertex</code>, visiting
     * every vertex in <code>unvisited</code> exactly once, and returning to <code>warehouse</code>.
     */
    private static Double bound(DeliveryRequest lastVisitedVertex, List<DeliveryRequest> unvisited, DeliveryRequest warehouse) {
        Double borneInf = Double.MAX_VALUE;

        int unvisitedSize = unvisited.size();

        // Finding the first and the second time windows
        TimeWindow firstTW = lastVisitedVertex.getTimeWindow();
        TimeWindow secondTW = unvisited.get(0).getTimeWindow();
        if (secondTW == firstTW) {
            int i = 0;
            while (i < unvisitedSize && unvisited.get(i).getTimeWindow() == firstTW) {
                ++i;
            }
            // If there is no second time window, then secondTW is null
            secondTW = (i < unvisitedSize ? unvisited.get(i).getTimeWindow() : null);
        }

        int i = 0;
        if (lastVisitedVertex == warehouse) {
            // Calculating the minimum distance between the warehouse and the delivery locations within the first time window of unvisited delivery requests
            while (i < unvisitedSize && unvisited.get(i).getTimeWindow() == secondTW) {
                Double distance = tspCost.get(lastVisitedVertex.getAddress().getId()).get(unvisited.get(i).getAddress().getId());
                if (distance < borneInf) {
                    borneInf = distance;
                }
                ++i;
            }
        } else {
            // Calculating the minimum distance between the lastVisitedVertex and the delivery locations within the first and second time windows of unvisited delivery requests
            while (i < unvisitedSize && (unvisited.get(i).getTimeWindow() == firstTW || unvisited.get(i).getTimeWindow() == secondTW)) {
                Double distance = tspCost.get(lastVisitedVertex.getAddress().getId()).get(unvisited.get(i).getAddress().getId());
                if (distance < borneInf) {
                    borneInf = distance;
                }
                ++i;
            }

            // Adding the distance between each unvisited vertex and the rest of unvisited vertices within the first and second time windows of unvisited delivery requests
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
                    borneInf += (li != Double.MAX_VALUE ? li : tspCost.get(unvisited.get(i).getAddress().getId()).get(warehouse.getAddress().getId()));
                }
            }
        }
        return borneInf;
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
                Long beforeLastVisitedIntersectionId = visited.get(visitedSize - 2).getAddress().getId();
                Long lastVisitedIntersectionId = visited.get(visitedSize - 1).getAddress().getId();
                branchAndBound(visited, unvisited, currentCost + tspCost.get(beforeLastVisitedIntersectionId).get(lastVisitedIntersectionId));

                visited.remove(vertex);
                unvisited.add(vertex);
            }
        }
    }

}