package com.pld.agile.controller;

import com.pld.agile.model.*;
import com.pld.agile.model.enums.TimeWindow;
import com.pld.agile.utils.Algorithm;
import com.pld.agile.view.Window;
import com.pld.agile.view.map.Marker;
import org.jxmapviewer.viewer.GeoPosition;

import java.util.LinkedList;

public class DestinationSelectedState implements State{

  private Intersection selectedIntersection;
  @Override
  public void selectDestinationPoint(Controller controller, Window window, GeoPosition position, CityMap cityMap) {
    Intersection selectedIntersection = cityMap.searchIntersection(position);
    this.selectedIntersection = selectedIntersection;
    window.updateSelectedPoint(selectedIntersection);
  };

  @Override
  public void addNewRequest(CityMap cityMap, Controller controller, Window window) {
    TimeWindow timeWindow = (TimeWindow) window.getDeliveryRequestView().comboBoxTimeWindow.getSelectedItem();
    Courier courier = (Courier) window.getDeliveryRequestView().comboBoxCourier.getSelectedItem();

    DeliveryRequest deliveryRequest = new DeliveryRequest(timeWindow, selectedIntersection);

    Tour tour = cityMap.getTour(courier);
    if(tour == null) tour = new Tour();
    tour.addDeliveryRequest(deliveryRequest);

    Tour optimalTour = Algorithm.ExecuteAlgorithm(cityMap.getWarehouse(), tour.getDeliveryRequests());
    optimalTour.setCourier(courier);
    cityMap.updateTourList(optimalTour);

    controller.setCurrentState(controller.computedTourState);
  }

  protected void setSelectedIntersection(Intersection selectedIntersection) {
    this.selectedIntersection = selectedIntersection;
  }
}
