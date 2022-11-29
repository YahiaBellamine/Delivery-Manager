package main.java.com.pld.agile;

import java.util.*;

import com.pld.agile.model.CityMap;
import com.pld.agile.model.DeliveryRequest;
import com.pld.agile.model.Intersection;
import com.pld.agile.model.RoadSegment;

import javax.lang.model.type.IntersectionType;

public class Algorithm {
    private HashMap<Long, HashMap<Long, Double>> tspCost;

    private List<Intersection> deliveryLocations;

    private List<Intersection> bestSol;
    private Double bestSolCost = Double.MAX_VALUE;

    public Algorithm() {
        tspCost = new HashMap<>();

        CityMap map = new CityMap(0, 0, 0, 0, null);
        Intersection warehouse = map.getWarehouse();

        Tour tour = new Tour();
        List<DeliveryRequest> deliveryRequests = tour.getDeliveryRequests();

        deliveryLocations = new ArrayList<>();
        for (int i = 0; i < deliveryRequests.size(); i++) {
            deliveryLocations.add(deliveryRequests.get(i).getAddress());
        }

        HashMap<Intersection, HashMap<Intersection, LinkedList<Intersection>>> shortestPathBetweenTwoIntersections = new HashMap<>();

        List<Intersection> destinations = new ArrayList<>(deliveryLocations);
        shortestPathBetweenTwoIntersections.put(warehouse, dijkstra(warehouse, destinations));
        destinations.put(warehouse);
        for (int i = 0; i < deliveryLocations.size(); i++) {
            destinations.remove(deliveryLocations.get(i));
            shortestPathBetweenTwoIntersections.put(deliveryLocations.get(i), dijkstra(deliveryLocations.get(i), destinations));
            destinations.add(deliveryLocations.get(i));
        }

        List<Intersection> visited = new ArrayList<>();
        List<Intersection> unvisited = new ArrayList<>();

        visited.add(warehouse);

        for (int i = 0; i < deliveryLocations.size(); i++) {
            unvisited.add(deliveryLocations.get(i));
        }

        bestSol = new ArrayList<>();
        branchAndBound(visited, unvisited, 0);

        LinkedList<Intersection> optimalTour = new LinkedList<>();

        for (int i = 0; i < visited.size(); i++) {
            optimalTour.addAll(shortestPathBetweenTwoIntersections.get(visited.get(i)).get(visited.get((i+1)%visited.size())));
        }
    }

    private Map.Entry<Intersection, Double> getLowestDistanceIntersection(HashMap<Intersection, Double> unsettledVertices) {
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

    private void release(Intersection vi, Intersection vj, Double distIJ, HashMap<Intersection, Double> unsettledVertices, HashMap<Intersection, Intersection> previousIntersection) {
        Double di = unsettledVertices.get(vi);
        Double dj = unsettledVertices.get(vj);
        if(dj > di + distIJ){
            dj = di + distIJ;
            unsettledVertices.put(vj, dj);
            previousIntersection.put(vj, vi);
        }
    }

    private HashMap<Intesection, LinkedList<Intersection>> dijkstra(Intersection source, List<Intersection> destinations) {
        HashMap<Intersection, Intersection> previousIntersection = new HashMap<>();

        HashMap<Intersection, Double> settledVertices = new HashMap<>();
        HashMap<Intersection, Double> unsettledVertices = new HashMap<>();

        HashMap<Long, Double> hMap = new HashMap<>();
        hMap.put(source.getId(), 0);
        tspCost.put(source.getId(), hMap);

        unsettledVertices.put(source, 0);

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

        HashMap<Intesection, LinkedList<Intersection>> shortestPathsFromSource = new HashMap<>();
        for (int i = 0; i < destinations.size(); i++) {
            Intersection destination = destinations.get(i);
            tspCost.get(source.getId()).put(destination.getId(), settledVertices.get(destination));
            LinkedList<Intersection> shortestPath = new LinkedList<>();
            shortestPath.add(destination);
            Intersection currentIntersection = destination;
            Intersection prevIntersection = null;
            while((prevIntersection = previousIntersection.get(currentIntersection))!=null){
                shortestPath.addFirst(prevIntersection);
                currentIntersection = prevIntersection;
            }
            shortestPathsFromSource.put(destination, shortestPath);
        }

        return shortestPathsFromSource;
    }

    /**
	 * Method that must be defined in TemplateTSP subclasses
	 * @param lastVisitedVertex
	 * @param unvisited
	 * @return a lower bound of the cost of paths in <code>g</code> starting from <code>lastVisitedVertex</code>, visiting 
	 * every vertex in <code>unvisited</code> exactly once, and returning back to vertex <code>0</code>.
	 */
	private Double bound(Intersection lastVisitedVertex, List<Intersection> visited, List<Intersection> unvisited) {
        int unvisitedSize = unvisited.size();
        
        Double borneInf = 0;
        Double l = Double.MAX_VALUE;

        for (int i = 0; i < unvisitedSize; i++) {
            if(tspCost.get(lastVisitedVertex.getId()).get(unvisited.get(i).getId()) < l){
                l = tspCost.get(lastVisitedVertex.getId()).get(unvisited.get(i).getId());
            }
        }
        borneInf = l;

        for (int i = 0; i < unvisitedSize; i++) {
            Double li = tspCost.get(unvisited.get(i).getId()).get(visited.get(0).getId());
            for (int j = 0; j < unvisitedSize; j++) {
                if(i!=j && tspCost.get(unvisited.get(i).getId()).get(unvisited.get(j).getId()) < li){
                    li = tspCost.get(unvisited.get(i).getId()).get(unvisited.get(j).getId());
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
	private void branchAndBound(List<Intersection> visited, List<Intersection> unvisited, Double currentCost){
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
                branchAndBound(visited, unvisited, currentCost + tspCost.get(visited.get(visitedSize-2).getId()).get(visited.get(visitedSize-1).getId()));
                visited.remove(intersection);
                unvisited.add(intersection);
            }    
        }
    }

}
