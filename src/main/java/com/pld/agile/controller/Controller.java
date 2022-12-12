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
    currentState.deleteDeliveryRequest(cityMap, this, courier, indexDeliveryRequest);
  }

  // public void saveTour() throws ExceptionXML, ParserConfigurationException, TransformerException {
//    for(DeliveryRequest d: deliveryRequests){
//      System.out.println(d.getAddress());
//    }
     // if(deliveryRequests.size()==0) {
     //   this.window.displayMessage("Add at least one delivery request before saving the tour");
       // return;
     // }
    // TODO: when we have several delivery men, the index should be modified  and call function save in a loop
    // Tour optimalTour = CityMap.getTour(0);
    // if(optimalTour==null) {
     // this.window.displayMessage("No tour to save");
     // return;
   // }
   // XMLSerialiser.save(optimalTour);

  // }

  /**
   * Update a delivery request
   * @param
   */
  public void updateDeliveryRequest(Courier courier, int indexDeliveryRequest){
    currentState.updateDeliveryRequest(cityMap, this, window, courier, indexDeliveryRequest);
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
   * @param pos the geo position of the intersection
   */
  public void selectDestinationPoint(GeoPosition pos) {
    currentState.selectDestinationPoint(this, window, pos, cityMap);
  }

  // TODO: remove all getters and setters (shouldn't be in the controller)
  public Window getWindow() {
    return window;
  }


}
