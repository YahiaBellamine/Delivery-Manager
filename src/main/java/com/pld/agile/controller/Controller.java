package com.pld.agile.controller;

import com.pld.agile.model.*;
import com.pld.agile.model.enums.TimeWindow;
import com.pld.agile.view.Window;
import org.jxmapviewer.viewer.GeoPosition;

/**
 * The main class controlling and coordinating all the application.
 */
public class Controller {

  /** the current state of the application */
  private State currentState;
  /** the main GUI window */
  private final Window window;
  /** the CityMap instance containing all the data*/
  private final CityMap cityMap;
  /** the initial state */
  protected final InitialState initialState = new InitialState();
  /** the loaded map state */
  protected final LoadedMapState loadedMapState = new LoadedMapState();
  /** the destination selected state */
  protected final DestinationSelectedState destinationSelectedState = new DestinationSelectedState();
  /** the computed tour state */
  protected final ComputedTourState computedTourState = new ComputedTourState();

  /**
   * Default constructor.
   */
  public Controller() {
    new Couriers();
    this.cityMap = new CityMap();
    this.window = new Window(cityMap, this);
    currentState = initialState;
    window.setVisible(true);
  }


  /**
   * Change the current state of the controller.
   * @param state The new current state.
   */
  protected void setCurrentState(State state){
    currentState = state;
  }

  /**
   * Create a new delivery request.
   */
  public void addNewRequest() {
    currentState.addNewRequest(cityMap, this, window);
  }

  /**
   * Delete a delivery Request.
   * @param courier              The courier from which the delivery request will be deleted.
   * @param indexDeliveryRequest The index of the delivery request in the delivery requests list.
   */
  public void deleteDeliveryRequest(Courier courier, int indexDeliveryRequest){
    currentState.deleteDeliveryRequest(cityMap, courier, indexDeliveryRequest);
  }

  /**
   * Updates the courier and/or the time window of the delivery request.
   * @param newTimeWindow - The new time window of the delivery request
   * @param newCourier - The new courier of the delivery request (if changed)
   * @param previousCourier - the previous courier of the delivery request
   * @param indexDeliveryRequest - The index of the delivery request in the list of delivery requests of the previous courier
   */
  public void updateDeliveryRequest(TimeWindow newTimeWindow, Courier newCourier, Courier previousCourier, int indexDeliveryRequest){
    currentState.updateDeliveryRequest(cityMap, newTimeWindow, newCourier, previousCourier, indexDeliveryRequest);
  }

  /**
   * Restore tours from a file.
   */
  public void loadTours(){
    currentState.loadTours(cityMap, this, window);
    window.getDeliveryRequestView().updateCouriers();
  }

  /**
   * Save tours to a file.
   */
  public void saveTours(){
    currentState.saveTours(cityMap, window);
  }

  /**
   * Load a map from a file.
   */
  public void loadMap() {
    currentState.loadMap(this, window, cityMap);
    window.getDeliveryRequestView().updateCouriers();
  }

  /**
   * This method select a destination point on the map
   * and set the current state of the controller to DestinationSelectedState.
   * @param pos the geo position of the selected intersection
   */
  public void selectDestinationPoint(GeoPosition pos) {
    currentState.selectDestinationPoint(this, window, pos, cityMap);
  }

  /**
   *
   * @return The main JFrame.
   */
  public Window getWindow() {
    return window;
  }

  /**
   * Adds a courier.
   */
  public void addCourier() {
    Couriers.addCourier();
    window.getDeliveryRequestView().updateCouriers();
    cityMap.addTour();
  }

}
