package com.pld.agile.controller;

import com.pld.agile.model.*;
import com.pld.agile.model.enums.TimeWindow;
import com.pld.agile.view.Window;
import org.jxmapviewer.viewer.GeoPosition;

public class Controller {
  private State currentState;
  private Window window;
  private CityMap cityMap;
  protected final InitialState initialState = new InitialState();
  protected final LoadedMapState loadedMapState = new LoadedMapState();
  protected final DestinationSelectedState destinationSelectedState = new DestinationSelectedState();
  protected final ComputedTourState computedTourState = new ComputedTourState();

  public Controller() {
    new Couriers();
    this.cityMap = new CityMap();
    this.window = new Window(cityMap, this);
    currentState = initialState;
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
  public void deleteDeliveryRequest(Courier courier, int indexDeliveryRequest){
    currentState.deleteDeliveryRequest(cityMap, courier, indexDeliveryRequest);
  }

  /**
   * Update a delivery request
   * @param
   */
  public void updateDeliveryRequest(TimeWindow newTimeWindow, Courier newCourier, Courier previousCourier, int indexDeliveryRequest){
    currentState.updateDeliveryRequest(cityMap, newTimeWindow, newCourier, previousCourier, indexDeliveryRequest);
  }

  /**
   * Restore tours from a file
   * @param
   */
  public void loadTours(){
    currentState.loadTours(cityMap, this, window);
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
   * @param pos the geo position of the intersection
   */
  public void selectDestinationPoint(GeoPosition pos) {
    currentState.selectDestinationPoint(this, window, pos, cityMap);
  }

  // TODO: remove all getters and setters (shouldn't be in the controller)
  public Window getWindow() {
    return window;
  }

  public void addCourier() {
    Couriers.addCourier();
    window.getDeliveryRequestView().updateCouriers();
    cityMap.addTour();
  }

}
