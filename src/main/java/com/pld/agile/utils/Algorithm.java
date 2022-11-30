package com.pld.agile.utils;

import java.util.*;

import com.pld.agile.model.DeliveryRequest;
import com.pld.agile.model.Intersection;
import com.pld.agile.model.RoadSegment;

public class Algorithm {

    static HashMap<Long, HashMap<Long, Double>> tspCost;
    static List<Intersection> bestSol;
    static Double bestSolCost;

    public static LinkedList<Intersection> ExecuteAlgorithm(Intersection warehouse, List<DeliveryRequest> deliveryRequests) {

        tspCost = new HashMap<>();

        List<Intersection> deliveryLocations = new ArrayList<>();
        for (DeliveryRequest deliveryRequest : deliveryRequests) {
            deliveryLocations.add(deliveryRequest.getAddress());
        }

        HashMap<Intersection, HashMap<Intersection, LinkedList<Intersection>>> shortestPathBetweenTwoIntersections = new HashMap<>();

        List<Intersection> destinations = new ArrayList<>(deliveryLocations);
        shortestPathBetweenTwoIntersections.put(warehouse, dijkstra(warehouse, destinations));
        destinations.add(warehouse);
        for (Intersection deliveryLocation : deliveryLocations) {
            destinations.remove(deliveryLocation);
            shortestPathBetweenTwoIntersections.put(deliveryLocation, dijkstra(deliveryLocation, destinations));
            destinations.add(deliveryLocation);
        }

        List<Intersection> visited = new ArrayList<>();

        visited.add(warehouse);

        List<Intersection> unvisited = new ArrayList<>(deliveryLocations);

        bestSol = new ArrayList<>();
        bestSolCost = Double.MAX_VALUE;
        branchAndBound(visited, unvisited, 0.0);

        LinkedList<Intersection> optimalTour = new LinkedList<>();

        for (int i = 0; i < bestSol.size(); i++) {
            optimalTour.addAll(shortestPathBetweenTwoIntersections.get(bestSol.get(i)).get(bestSol.get((i+1)%bestSol.size())));
        }

        return optimalTour;
    }

    private static Map.Entry<Intersection, Double> getLowestDistanceIntersection(HashMap<Intersection, Double> unsettledVertices) {
        Map.Entry<Intersection, Double> res = null;
        Double lowestDistance = Double.MAX_VALUE;
        for(Map.Entry<Intersection, Double> entry: unsettledVertices.entrySet()){
            if(entry.getValue() < lowestDistance){
                lowestDistance = entry.getValue();
                res = entry;
            }
        }
        return res;
    }

    private static void release(Intersection vi, Intersection vj, Double distIJ, HashMap<Intersection, Double> unsettledVertices, HashMap<Intersection, Intersection> previousIntersection) {
        Double di = unsettledVertices.get(vi);
        Double dj = unsettledVertices.get(vj);
        if(dj > di + distIJ){
            dj = di + distIJ;
            unsettledVertices.put(vj, dj);
            previousIntersection.put(vj, vi);
        }
    }

    private static HashMap<Intersection, LinkedList<Intersection>> dijkstra(Intersection source, List<Intersection> destinations) {
        HashMap<Intersection, Intersection> previousIntersection = new HashMap<>();

        HashMap<Intersection, Double> settledVertices = new HashMap<>();
        HashMap<Intersection, Double> unsettledVertices = new HashMap<>();

        HashMap<Long, Double> hMap = new HashMap<>();
        hMap.put(source.getId(), 0.0);
        tspCost.put(source.getId(), hMap);

        unsettledVertices.put(source, 0.0);

        while (unsettledVertices.size()!=0) {
            Map.Entry<Intersection, Double> lowestDistanceIntersection = getLowestDistanceIntersection(unsettledVertices);

            Intersection intersection = lowestDistanceIntersection.getKey();
            Double lowestDistance = lowestDistanceIntersection.getValue();

            List<RoadSegment> outgoingSegments = intersection.getOutgoingSegments();
            
            for(RoadSegment outgoingSegment : outgoingSegments){
                
                Intersection destination = outgoingSegment.getDestination();
                Double length = outgoingSegment.getLength();

                if(!settledVertices.containsKey(destination)){
                    
                    if(!unsettledVertices.containsKey(destination)){
                        unsettledVertices.put(destination, Double.MAX_VALUE);
                    }
                    release(intersection, destination, length, unsettledVertices, previousIntersection);
                }
            }
            settledVertices.put(intersection, lowestDistance);
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

        return shortestPathsFromSource;
    }

    /**
	 * Method that must be defined in TemplateTSP subclasses
	 * @param lastVisitedVertex the last visited vertex in the list visited
     * @param visited the list of visited vertices
	 * @param unvisited the list of unvisited vertices
	 * @return a lower bound of the cost of paths in <code>g</code> starting from <code>lastVisitedVertex</code>, visiting 
	 * every vertex in <code>unvisited</code> exactly once, and returning back to vertex <code>0</code>.
	 */
	private static Double bound(Intersection lastVisitedVertex, List<Intersection> visited, List<Intersection> unvisited) {
        int unvisitedSize = unvisited.size();
        
        Double borneInf;
        Double l = Double.MAX_VALUE;

        for (Intersection intersection : unvisited) {
            if (tspCost.get(lastVisitedVertex.getId()).get(intersection.getId()) < l) {
                l = tspCost.get(lastVisitedVertex.getId()).get(intersection.getId());
            }
        }
        borneInf = l;

        for (int i = 0; i < unvisitedSize; i++) {
            Double li = tspCost.get(unvisited.get(i).getId()).get(visited.get(0).getId());
            for (int j = 0; j < unvisitedSize; j++) {
                Double dist = tspCost.get(unvisited.get(i).getId()).get(unvisited.get(j).getId());
                if(i!=j &&  dist < li){
                    li = dist;
                }
            }
            borneInf += li;
        }
        return borneInf;
	}


    /**
	 * Template method of a branch and bound algorithm for solving the TSP in <code>g</code>.
	 * @param visited the sequence of vertices that have been already visited
	 * @param unvisited the set of vertex that have not yet been visited
	 * @param currentCost the tspCost of the path corresponding to <code>visited</code>
	 */	
	private static void branchAndBound(List<Intersection> visited, List<Intersection> unvisited, Double currentCost){
        int visitedSize = visited.size();
        int unvisitedSize = unvisited.size();

        if (unvisitedSize == 0){ 
            currentCost += tspCost.get(visited.get(visitedSize-1).getId()).get(visited.get(0).getId());

            if(currentCost < bestSolCost){
                bestSolCost = currentCost;
                bestSol = new ArrayList<>(visited);
            }
        } else if (currentCost+bound(visited.get(visitedSize-1), visited, unvisited) < bestSolCost){
            
            for (int i = 0; i < unvisitedSize; i++) {
                Intersection intersection = unvisited.get(i);
                visited.add(intersection);
                unvisited.remove(intersection);
                visitedSize = visited.size();
                branchAndBound(visited, unvisited, currentCost + tspCost.get(visited.get(visitedSize-2).getId()).get(visited.get(visitedSize-1).getId()));
                visited.remove(intersection);
                unvisited.add(intersection);
            }    
        }
    }

}
