package com.pld.agile.controller;

import com.pld.agile.model.CityMap;
import com.pld.agile.model.DeliveryRequest;
import com.pld.agile.model.Intersection;
import com.pld.agile.model.enums.TimeWindow;
import com.pld.agile.utils.Algorithm;
import com.pld.agile.view.Window;
import com.pld.agile.view.map.Marker;
import org.jxmapviewer.viewer.GeoPosition;

import java.util.LinkedList;

public class DestinationSelectedState implements State{

  private Long destinationPointId;
  @Override
  public void selectDestinationPoint(Controller c,Long destinationPointId) {
    this.setDestinationPointId(destinationPointId);
  };

  @Override
  public void addNewRequest(CityMap cityMap, Controller c, Window w) {
    TimeWindow tm = (TimeWindow) w.getDeliveryRequestView().comboBoxTimeWindow.getSelectedItem();
    // TODO: Retrieve courier id from the view (number)
    DeliveryRequest deliveryRequest = new DeliveryRequest(tm, cityMap.getIntersections().get(this.destinationPointId));
    cityMap.addDeliveryRequest(deliveryRequest);
    // TODO: Calculate optimal tour
    // TODO: Update view with observer pattern (mapViewer + deliveriesView + deliveryRequestView)
    c.setCurrentState(c.computedTourState);
  }

  protected void setDestinationPointId(Long destinationPointId) {
    this.destinationPointId = destinationPointId;
  }

  protected Long getDestinationPointId() {
    return destinationPointId;
  }
}
