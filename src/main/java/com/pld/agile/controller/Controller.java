package com.pld.agile.controller;

import com.pld.agile.model.*;
import com.pld.agile.view.Window;
import com.pld.agile.view.map.Marker;
import org.jxmapviewer.viewer.GeoPosition;

import java.awt.geom.Point2D;

public class Controller {
  private State currentState;
  private Window window;
  private CityMap cityMap;
  protected final InitialState initialState = new InitialState();
  protected final LoadedMapState loadedMapState = new LoadedMapState();
  protected final DestinationSelectedState destinationSelectedState = new DestinationSelectedState();
  protected final ComputedTourState computedTourState = new ComputedTourState();

  public Controller() {
    new Couriers(1);
    this.cityMap = new CityMap();
    this.window = new Window(cityMap, this);
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
    currentState.loadMap(this, window, cityMap);
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
    GeoPosition geoPosition = new GeoPosition(cityMap.getIntersections().get(currentIntersectionId).getLatitude(),
            cityMap.getIntersections().get(currentIntersectionId).getLongitude());
    this.window.getMapViewer().addPoint(geoPosition, currentIntersectionId, Marker.Type.REQUEST);
    this.window.getMapViewer().update();
    this.window.getDeliveryRequestView().setSelectDestinationPoint("Intersection " + currentIntersectionId);
  }

  // TODO: Remove from here (need to be somewhere else)
  public void searchIntersection(double x, double y){
    double r = Double.MAX_VALUE;
    long id=-1;
    for(Intersection i : cityMap.getIntersections().values()){
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

}
