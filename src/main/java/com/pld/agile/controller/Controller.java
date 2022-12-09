package com.pld.agile.controller;

import com.pld.agile.model.CityMap;
import com.pld.agile.model.DeliveryRequest;
import com.pld.agile.model.Intersection;
import com.pld.agile.model.enums.TimeWindow;
import com.pld.agile.utils.Algorithm;
import com.pld.agile.view.Window;
import com.pld.agile.view.map.Marker;
import org.jxmapviewer.viewer.GeoPosition;

import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class Controller {
  private State currentState;
  private Window window;
  private CityMap cityMap;
  private Map<Long, Intersection> intersections;/* TODO: remove attribut (already present in cityMap)*/
  private Long currentIntersectionId;/* TODO: remove attribut (already present in DestinationSelectedState)*/
  private List<DeliveryRequest> deliveryRequests; /* TODO: remove attribut (already present in cityMap->Tour)*/

  protected final InitialState initialState = new InitialState();
  protected final LoadedMapState loadedMapState = new LoadedMapState();
  protected final DestinationSelectedState destinationSelectedState = new DestinationSelectedState();
  protected final ComputedTourState computedTourState = new ComputedTourState();

  public Controller() {
    window = new Window(this);
    cityMap = new CityMap();
    this.intersections = new HashMap<>();
    deliveryRequests = new LinkedList<>();
    window.setVisible(true);
  }

  /**
   * Change the current state of the controller
   * @param state the new current state
   */
  protected void setCurrentState(State state){
    currentState = state;
  }

  /**
   * Create a new delivery request
   * @param
   */
  public void addNewRequest() {
    currentState.addNewRequest(cityMap, this, window);
  }

  /**
   * Delete a delivery Request
   * @param
   */
  public void deleteDeliveryRequest(){
    currentState.deleteDeliveryRequest(cityMap, window);
  }

  /**
   * Update a delivery request
   * @param
   */
  public void updateDeliveryRequest(){
    currentState.updateDeliveryRequest(cityMap, window);
  }

  /**
   * Restore tours from a file
   * @param
   */
  public void restoreTours(){
    currentState.restoreTours(cityMap, this, window);
  }

  /**
   * Save tours to a file
   * @param
   */
  public void saveTours(){
    currentState.saveTours(cityMap, window);
  }

  /**
   * Load a map from a file
   * @param
   */
  public void loadMap() {
    currentState.loadMap(this, window, intersections ,cityMap);
  }

  /**
   * Select a destination point
   * @param destinationPointId the id of the destination point
   */
  public void selectDestinationPoint(Long destinationPointId) {
    currentState.selectDestinationPoint(this, destinationPointId);
  }

  public void selectIntersection(Long currentIntersectionId) {
    /* TODO: remove and replace by currentState.selectDestinationPoint*/
    this.currentIntersectionId = currentIntersectionId;
    GeoPosition geoPosition = new GeoPosition(intersections.get(currentIntersectionId).getLatitude(),
            intersections.get(currentIntersectionId).getLongitude());
    this.window.getMapViewer().addPoint(geoPosition, currentIntersectionId, Marker.Type.REQUEST);
    this.window.getMapViewer().update();
    this.window.getDeliveryRequestView().setSelectDestinationPoint("Intersection " + currentIntersectionId);
  }

  // TODO: Remove from here (need to be somewhere else)
  public void searchIntersection(double x, double y){
    double r = Double.MAX_VALUE;
    long id=-1;
    for(Intersection i : intersections.values()){
      double dist = Point2D.distance(x,y,i.getLatitude(),i.getLongitude());
      if(dist<r){
        System.out.println("dist "+dist+ " "+i.getLatitude()+" "+i.getLongitude());
        id = i.getId();
        r = dist;
      }
    }
    if(id!=-1)    selectIntersection(id);
  }

  // TODO: remove all getters and setters (shouldn't be in the controller)
  public Window getWindow() {
    return window;
  }

  public void setWindow(Window window) {
    this.window = window;
  }
}
