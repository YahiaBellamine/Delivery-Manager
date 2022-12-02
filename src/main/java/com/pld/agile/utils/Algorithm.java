package com.pld.agile.utils;

import java.util.*;

import com.pld.agile.model.DeliveryRequest;
import com.pld.agile.model.Intersection;
import com.pld.agile.model.RoadSegment;
import com.pld.agile.model.Tour;
import com.pld.agile.model.enums.TimeWindow;

public class Algorithm {

  static HashMap<Long, HashMap<Long, Double>> tspCost;
  static HashMap<Intersection, HashMap<Intersection, LinkedList<Intersection>>> shortestPathBetweenTwoDeliveryLocations;
  static LinkedList<DeliveryRequest> bestSol;
  static Double bestSolCost;

  public static Tour ExecuteAlgorithm(Intersection warehouse, LinkedList<DeliveryRequest> deliveryRequests) {

    tspCost = new HashMap<>();

    shortestPathBetweenTwoDeliveryLocations = new HashMap<>();

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

    // We sort unvisited vertices based on ascending TimeWindow
    unvisited.sort(new Comparator<DeliveryRequest>() {
      @Override
      public int compare(DeliveryRequest o1, DeliveryRequest o2) {
        return o1.getTimeWindow().isBefore(o2.getTimeWindow());
      }
    });

    branchAndBound(visited, unvisited, 0.0);

    /* Calculating the optimal tour and updating passing time for each delivery request */
    Tour optimalTour = new Tour();
    optimalTour.setDeliveryRequests(deliveryRequests);
    // We start at 8h
    Double passingTime = 8.0;
    // The previous delivery
    for (int i = 0; i < bestSol.size(); i++) {
      optimalTour.getIntersections().addAll(shortestPathBetweenTwoDeliveryLocations.get(bestSol.get(i).getAddress()).get(bestSol.get((i+1)%bestSol.size()).getAddress()));
      passingTime += tspCost.get(bestSol.get(i).getAddress().getId()).get(bestSol.get((i+1)%bestSol.size()).getAddress().getId())/(1000.0*15.0);
      bestSol.get((i+1)%bestSol.size()).setPassingTime(passingTime);
      passingTime += 5.0/60.0;
    }

    /* Calculating the duration of optimalTour */
    optimalTour.setTourDuration(bestSolCost/(1000.0*15.0) + (5.0/60.0) * deliveryRequests.size());
    System.out.println(optimalTour.getFormattedTourDuration());

    return optimalTour;
  }

  private static Intersection getLowestDistanceIntersection(HashMap<Intersection, Double> unsettledVertices) {
    Intersection res = null;
    Double lowestDistance = Double.MAX_VALUE;
    for(Map.Entry<Intersection, Double> entry: unsettledVertices.entrySet()){
      if(entry.getValue() < lowestDistance){
        lowestDistance = entry.getValue();
        res = entry.getKey();
      }
    }
    return res;
  }

  private static void release(Intersection vi, Intersection vj, Double distIJ, HashMap<Intersection, Double> unsettledVertices, HashMap<Intersection, Intersection> previousIntersection) {
    Double di = unsettledVertices.get(vi);
    Double dj = unsettledVertices.get(vj);
    if(dj > di + distIJ){
      dj = di + distIJ;
      // We update the distance to vertex j
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

    while (unsettledVertices.size()!=0) {
      Intersection intersection = getLowestDistanceIntersection(unsettledVertices);

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
   * @param lastVisitedVertex the last visited vertex
   * @param unvisited the list of unvisited vertices
   * @param warehouse the warehouse vertex
   * @return a lower bound of the cost of paths in the graph of delivery locations starting from <code>lastVisitedVertex</code>, visiting
   * every vertex in <code>unvisited</code> exactly once, and returning to <code>warehouse</code>.
   */
  private static Double bound(DeliveryRequest lastVisitedVertex, List<DeliveryRequest> unvisited, DeliveryRequest warehouse) {
    Double borneInf = Double.MAX_VALUE;

    int unvisitedSize = unvisited.size();

    TimeWindow firstTW = unvisited.get(0).getTimeWindow();
    if(lastVisitedVertex.getAddress().getId() == warehouse.getAddress().getId()){ // We calculate the minimum distance between the warehouse and the delivery locations within the first time window of delivery requests
      int i = 0;
      while (i<unvisitedSize && unvisited.get(i).getTimeWindow()==firstTW){
        Double distance = tspCost.get(lastVisitedVertex.getAddress().getId()).get(unvisited.get(i).getAddress().getId());
        if (distance < borneInf) {
          borneInf = distance;
        }
        ++i;
      }
    }else{ // We calculate the minimum distance between the lastVisitedVertex and the delivery locations within the first and second time windows of delivery requests
      int i = 0;
      while (i<unvisitedSize && unvisited.get(i).getTimeWindow()==firstTW){
        ++i;
      }
      TimeWindow secondTW = (i < unvisitedSize ? unvisited.get(i).getTimeWindow() : null);
      i = 0;
      while (i<unvisitedSize && (unvisited.get(i).getTimeWindow()==firstTW || unvisited.get(i).getTimeWindow()==secondTW)){
        Double distance = tspCost.get(lastVisitedVertex.getAddress().getId()).get(unvisited.get(i).getAddress().getId());
        if (distance < borneInf) {
          borneInf = distance;
        }
        ++i;
      }

      for (i = 0; i < unvisitedSize; i++) {
        if(unvisited.get(i).getTimeWindow()==firstTW || unvisited.get(i).getTimeWindow()==secondTW){
          Double li = Double.MAX_VALUE;
          for (int j = 0; j < unvisitedSize; j++) {
            if(i != j && (unvisited.get(j).getTimeWindow()==firstTW || unvisited.get(j).getTimeWindow()==secondTW)){
              Double distance = tspCost.get(unvisited.get(i).getAddress().getId()).get(unvisited.get(j).getAddress().getId());
              if (distance < li) {
                li = distance;
              }
            }
          }
          borneInf += (li != Double.MAX_VALUE ? li : 0);
        }
      }
    }
    return borneInf;
  }


  /**
   * Template method of a branch and bound algorithm for solving the TSP in <code>g</code>.
   * @param visited the sequence of vertices that have been already visited
   * @param unvisited the set of vertex that have not yet been visited
   * @param currentCost the tspCost of the path corresponding to <code>visited</code>
   */
  private static void branchAndBound(LinkedList<DeliveryRequest> visited, LinkedList<DeliveryRequest> unvisited, Double currentCost){
    int visitedSize = visited.size();
    int unvisitedSize = unvisited.size();
    LinkedList<DeliveryRequest> unvisitedBeforePermutation = new LinkedList<>(unvisited);

    if (unvisitedSize == 0){
      Long lastVisitedIntersectionId = visited.get(visitedSize-1).getAddress().getId();
      Long warehouseIntersectionId = visited.get(0).getAddress().getId();
      currentCost += tspCost.get(lastVisitedIntersectionId).get(warehouseIntersectionId);

      if(currentCost < bestSolCost){
        bestSolCost = currentCost;
        bestSol = new LinkedList<>(visited);
      }
    } else if (currentCost + bound(visited.get(visitedSize-1), unvisitedBeforePermutation, visited.get(0)) < bestSolCost){

      for (int i = 0; i < unvisitedBeforePermutation.size(); i++) {
        DeliveryRequest vertex = unvisitedBeforePermutation.get(i);

        visited.add(vertex);
        unvisited.remove(vertex);

        visitedSize = visited.size();
        Long beforeLastVisitedIntersectionId = visited.get(visitedSize-2).getAddress().getId();
        Long lastVisitedIntersectionId = visited.get(visitedSize-1).getAddress().getId();
        branchAndBound(visited, unvisited, currentCost + tspCost.get(beforeLastVisitedIntersectionId).get(lastVisitedIntersectionId));

        visited.remove(vertex);
        unvisited.add(vertex);
      }
    }
  }

}