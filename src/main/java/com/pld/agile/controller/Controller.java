package com.pld.agile.controller;

import com.pld.agile.model.CityMap;
import com.pld.agile.model.DeliveryRequest;
import com.pld.agile.model.Intersection;
import com.pld.agile.model.enums.TimeWindow;
import com.pld.agile.utils.xml.ExceptionXML;
import com.pld.agile.utils.xml.XMLDeserialiser;
import com.pld.agile.view.Window;
import com.pld.agile.view.map.Marker;
import org.jxmapviewer.viewer.GeoPosition;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Controller {

  private Window window;
  private CityMap cityMap;
  private Map<Long, Intersection> intersections;
  private Long currentIntersectionId;
  private List<DeliveryRequest> deliveryRequests;

  public Controller() {
    window = new Window(this);
    cityMap = new CityMap();
    this.intersections = new HashMap<>();
    deliveryRequests = new LinkedList<>();
    window.setVisible(true);
  }

  public void addDeliveryRequest() {
    if (currentIntersectionId != null) {
      TimeWindow tm = (TimeWindow) this.window.getDeliveryRequestView().comboBoxTimeWindow.getSelectedItem();
      DeliveryRequest deliveryRequest = new DeliveryRequest(tm, this.intersections.get(currentIntersectionId));
      // Add delivery request to right panel
      this.deliveryRequests.add(deliveryRequest);
      this.window.getDeliveriesView().displayRequests(deliveryRequests);

      /* Add pointer to the map*/
      GeoPosition geoPosition = new GeoPosition(intersections.get(currentIntersectionId).getLatitude(),
              intersections.get(currentIntersectionId).getLongitude());
      this.window.getMapViewer().addPoint(geoPosition, currentIntersectionId, Marker.Type.TOUR);
      this.window.getMapViewer().clearRequestMarker();
      currentIntersectionId = null;
      this.window.getDeliveryRequestView().setSelectDestinationPoint("  Select your destination point on the map.");

    } else {
      this.window.displayMessage("Please select an intersection on the map");
    }
  }

  public void loadMap(/*String path*/) {
    String path = "src/main/java/com/pld/agile/utils/maps/smallMap.xml";
    try {
      XMLDeserialiser.load(path, intersections, cityMap);
    } catch (ExceptionXML e) {
      throw new RuntimeException(e);
    }
    for (Intersection intersection : intersections.values()) {
      if(cityMap.getWarehouse().getId() == intersection.getId()) continue;
      GeoPosition geoPosition = new GeoPosition(intersection.getLatitude(), intersection.getLongitude());
      this.window.getMapViewer().addPoint(geoPosition, intersection.getId(), Marker.Type.MAP);
    }
//    // Define the warehouse marker on the map
    GeoPosition warehousePosition = new GeoPosition(cityMap.getWarehouse().getLatitude(),
            cityMap.getWarehouse().getLongitude());
    this.window.getMapViewer().addPoint(warehousePosition, cityMap.getWarehouse().getId(), Marker.Type.WAREHOUSE);
    this.window.getMapViewer().recenter();
  }

  public void selectIntersection(Long currentIntersectionId) {
    this.currentIntersectionId = currentIntersectionId;
    GeoPosition geoPosition = new GeoPosition(intersections.get(currentIntersectionId).getLatitude(),
            intersections.get(currentIntersectionId).getLongitude());
    this.window.getMapViewer().addPoint(geoPosition, currentIntersectionId, Marker.Type.REQUEST);
    this.window.getMapViewer().update();
    this.window.getDeliveryRequestView().setSelectDestinationPoint("Intersection " + currentIntersectionId);
  }

  public void deleteDeliveryRequest(DeliveryRequest deliveryRequest) {
    // cityMap.deleteDeliveryRequest(deliveryRequest);
  }

}
