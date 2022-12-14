package com.pld.agile.utils;

import com.pld.agile.model.DeliveryRequest;
import com.pld.agile.model.Intersection;
import com.pld.agile.model.RoadSegment;
import com.pld.agile.model.Tour;
import com.pld.agile.model.enums.TimeWindow;

import java.util.*;

/**
 * The algorithm calculating the optimatl tour.
 */
public class Algorithm {

    /**
     * optimalTour is the computed tour after executing TSP algorithm
     */
    static Tour optimalTour;
    /**
     * tspCost stores the cost between delivery requests
     */
    static HashMap<Long, HashMap<Long, Double>> tspCost;
    /**
     * shortestPath stores the shortest path between delivery requests
     */
    static HashMap<Intersection, HashMap<Intersection, LinkedList<Intersection>>> shortestPath;
    /**
     * bestSolCost corresponds to the minimal cost of the Hamiltonian circuit
     */
    static Double bestSolCost;
    /**
     * bestSol is the ordered list of delivery requests that minimises the cost of the Hamiltonian circuit
     */
    static LinkedList<DeliveryRequest> bestSol;

    /**
     * Method that executes the TSP algorithm.
     *
     * @param warehouse The warehouse
     * @param deliveryRequests The list of delivery requests.
     * @return the computed tour with TSP algorithm
     */
    public static Tour ExecuteAlgorithm(Intersection warehouse, List<DeliveryRequest> deliveryRequests) throws InaccessibleDestinationException {
        // Instantiation of attributes
        optimalTour = new Tour();
        if(tspCost == null)
            tspCost = new HashMap<>();
        if(shortestPath == null)
            shortestPath = new HashMap<>();

        // Sorting delivery requests based on ascending TimeWindow
        deliveryRequests.sort((o1, o2) -> o1.getTimeWindow().isBefore(o2.getTimeWindow()));

        DeliveryRequest deliveryRequestWarehouse = new DeliveryRequest(null, warehouse);

        /* Dijkstra Algorithm to calculate tspCost */
        List<DeliveryRequest> destinations = new LinkedList<>(deliveryRequests);
        dijkstra(deliveryRequestWarehouse, destinations);
        destinations.add(deliveryRequestWarehouse);
        for (DeliveryRequest deliveryRequest : deliveryRequests) {
            destinations.remove(deliveryRequest);
            dijkstra(deliveryRequest, destinations);
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
        double arrivalTime = 8.0;
        for (int i = 0; i < bestSol.size(); i++) {
            DeliveryRequest deliveryRequest = bestSol.get(i);
            DeliveryRequest nextDeliveryRequest = bestSol.get((i + 1) % bestSol.size());

            // Add delivery requests to optimalTour, but not the warehouse
            if (i > 0) {
                optimalTour.addDeliveryRequest(deliveryRequest);
            }

            // Add the shortest path between deliveryRequest and nextDeliveryRequest to optimalTour
            optimalTour.getIntersections().addAll(shortestPath.get(deliveryRequest.getAddress()).get(nextDeliveryRequest.getAddress()));

            // Calculating arrivalTime by adding the duration between deliveryRequest and nextDeliveryRequest
            arrivalTime += tspCost.get(deliveryRequest.getAddress().getId()).get(nextDeliveryRequest.getAddress().getId()) / (1000.0 * 15.0);

            // If the courier arrives to a delivery location before the start of its time window, they should wait until the start of the time window
            TimeWindow timeWindow = nextDeliveryRequest.getTimeWindow();
            if (timeWindow != null && arrivalTime < timeWindow.getStart()) {
                arrivalTime = timeWindow.getStart();
            }
            nextDeliveryRequest.setArrivalTime(arrivalTime);

            // The courier takes 5 minutes to perform a delivery
            arrivalTime += 5.0 / 60.0;
        }

        /* Calculating the duration of optimalTour */
        optimalTour.setTourDuration(bestSol.get(0).getArrivalTime() - 8.0);

        return optimalTour;
    }

    /**
     * Method used by Dijkstra algorithm
     *
     * @param unsettledVertices The vertices that are unsettled.
     * @return the Intersection that has the lowest distance.
     */
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

    /**
     * Method used by Dijkstra algorithm to update the distance of vertex j if the distance to his previous vertex i
     * added to the cost between vertex i and vertex j is lower than the actual distance to vertex j.
     * The previousIntersection structure is also updated.
     *
     * @param vi The vertex i.
     * @param vj The vertex j.
     * @param distIJ The distance between vertex i and vertex j.
     * @param unsettledVertices The unsettled vertices.
     * @param previousIntersection The structure containing the previous intersection for every intersection.
     */
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

    /**
     * Dijkstra algorithm for calculating the distance between the source and all destinations in order to update
     * tspCost structure for having the distance between every delivery request including the warehouse and the rest
     * of delivery requests.
     *
     * @param source The source.
     * @param destinations The list of destinations.
     */
    private static void dijkstra(DeliveryRequest source, List<DeliveryRequest> destinations) {
        HashMap<Intersection, Intersection> previousIntersection = new HashMap<>();

        HashMap<Intersection, Double> settledVertices = new HashMap<>();
        HashMap<Intersection, Double> unsettledVertices = new HashMap<>();

        if(!tspCost.containsKey(source.getAddress().getId())){
            HashMap<Long, Double> hashMap = new HashMap<>();
            hashMap.put(source.getAddress().getId(), 0.0);
            tspCost.put(source.getAddress().getId(), hashMap);
        }

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

        HashMap<Intersection, LinkedList<Intersection>> shortestPathFromSource = shortestPath.get(source.getAddress());
        if(shortestPathFromSource == null){
            shortestPathFromSource = new HashMap<>();
        }
        for (DeliveryRequest destination : destinations) {
            Intersection destinationIntersection = destination.getAddress();
            // If we have already calculated the shortest path to a destination, we don't recalculate it
            if(!shortestPathFromSource.containsKey(destinationIntersection)){
                /* If the arc between source and destination is valid, then we add the update the distance in tspCost.
                 * Otherwise, the distance is null.
                 */
                if (isArc(source, destination, destinations)) {
                    tspCost.get(source.getAddress().getId()).put(destinationIntersection.getId(), settledVertices.get(destinationIntersection));
                } else {
                    tspCost.get(source.getAddress().getId()).put(destinationIntersection.getId(), null);
                }

                LinkedList<Intersection> path = new LinkedList<>();
                path.add(destinationIntersection);
                Intersection currentIntersection = destinationIntersection;
                Intersection prevIntersection;
                while ((prevIntersection = previousIntersection.get(currentIntersection)) != null) {
                    path.addFirst(prevIntersection);
                    currentIntersection = prevIntersection;
                }
                shortestPathFromSource.put(destinationIntersection, path);
            }
        }

        shortestPath.put(source.getAddress(), shortestPathFromSource);
    }

    /**
     * If the source is the warehouse, we search for the first time window in the list of delivery requests (destinations).
     * Else, we search for the next time window from source such as the start of next time window
     * is greater or equal than the end of the source time window, and is the smallest.
     *
     * @param sourceTimeWindow The source TimeWindow.
     * @param destinations The list of destinations.
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
                if (deliveryRequest.getTimeWindow() != null && deliveryRequest.getTimeWindow().getStart() < nextTimeWindow.getStart() && deliveryRequest.getTimeWindow().getStart() >= sourceTimeWindow.getEnd()) {
                    nextTimeWindow = deliveryRequest.getTimeWindow();
                }
            }
        }
        return nextTimeWindow;
    }

    /**
     * Method that searches for the last time window in the list of destinations
     *
     * @param sourceTimeWindow The source TimeWindow.
     * @param destinations The list of destinations.
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
     * @param source The source.
     * @param destination The destination.
     * @param destinations The list of destinations.
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
        } else
            return sourceTimeWindow.getStart() == destination.getTimeWindow().getStart() || destination.getTimeWindow().getStart() == nextTimeWindow.getStart();
    }

    /**
     * Method that estimates a lower bound of the remaining distance to the warehouse
     *
     * @param lastVisitedVertex the last visited vertex
     * @param unvisited         the list of unvisited vertices
     * @return a lower bound of the cost of paths in the graph of delivery locations starting from <code>lastVisitedVertex</code>, visiting
     * every vertex in <code>unvisited</code> exactly once, and returning to <code>warehouse</code>.
     */
    private static Double bound(DeliveryRequest lastVisitedVertex, List<DeliveryRequest> unvisited) throws InaccessibleDestinationException {
        double lowerBound = Double.MAX_VALUE;

        // Calculating the minimum distance between the lastVisitedVertex and the unvisited delivery requests
        Double distance;
        for (DeliveryRequest vertex : unvisited) {
            if (isArc(lastVisitedVertex, vertex, unvisited)) {
                distance = tspCost.get(lastVisitedVertex.getAddress().getId()).get(vertex.getAddress().getId());
                if (distance == null) {
                    throw new InaccessibleDestinationException();
                }
                if (distance < lowerBound) {
                    lowerBound = distance;
                }
            }
        }
        return lowerBound;
    }

    /**
     * Branch and bound algorithm for solving the TSP in the graph of delivery requests for a Tour.
     *
     * @param visited     the sequence of vertices that have been already visited
     * @param unvisited   the set of vertex that have not yet been visited
     * @param currentCost the tspCost of the path corresponding to <code>visited</code>
     */
    private static void branchAndBound(LinkedList<DeliveryRequest> visited, LinkedList<DeliveryRequest> unvisited, Double currentCost) throws InaccessibleDestinationException {
        int visitedSize = visited.size();
        int unvisitedSize = unvisited.size();
        LinkedList<DeliveryRequest> unvisitedBeforePermutation = new LinkedList<>(unvisited);

        if (unvisitedSize == 0) {
            Long lastVisitedIntersectionId = visited.get(visitedSize - 1).getAddress().getId();
            Long warehouseIntersectionId = visited.get(0).getAddress().getId();
            if(tspCost.get(lastVisitedIntersectionId).get(warehouseIntersectionId) == null) {
                throw new InaccessibleDestinationException();
            }
            currentCost += tspCost.get(lastVisitedIntersectionId).get(warehouseIntersectionId);
            if (currentCost < bestSolCost) {
                bestSolCost = currentCost;
                bestSol = new LinkedList<>(visited);
            }

        } else if (currentCost + bound(visited.get(visitedSize - 1), unvisitedBeforePermutation) < bestSolCost) {

            for (DeliveryRequest vertex : unvisitedBeforePermutation) {
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

    /**
     * Reinitialize all the Algorithm attributes.
     */
    public static void reInitializeMapAttributes() {
        if(tspCost != null)
            tspCost.clear();
        if(shortestPath != null)
            shortestPath.clear();
    }

}