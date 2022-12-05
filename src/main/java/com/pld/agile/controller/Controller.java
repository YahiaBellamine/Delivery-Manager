package com.pld.agile.controller;

import com.pld.agile.model.CityMap;
import com.pld.agile.model.DeliveryRequest;
import com.pld.agile.model.Intersection;
import com.pld.agile.model.enums.TimeWindow;
import com.pld.agile.utils.Algorithm;
import com.pld.agile.utils.xml.ExceptionXML;
import com.pld.agile.utils.xml.XMLDeserialiser;
import com.pld.agile.view.Window;
import com.pld.agile.view.map.Marker;
import org.jxmapviewer.viewer.GeoPosition;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.geom.Point2D;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class Controller {
  private State currentState;
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

  /**
   * Change the current state of the controller
   * @param state the new current state
   */
  protected void setCurrentState(State state){
    currentState = state;
  }

  public void addDeliveryRequest() {
    if (currentIntersectionId != null) {
      TimeWindow tm = (TimeWindow) this.window.getDeliveryRequestView().comboBoxTimeWindow.getSelectedItem();
      DeliveryRequest deliveryRequest = new DeliveryRequest(tm, this.intersections.get(currentIntersectionId));
      // Add delivery request to right panel
      this.deliveryRequests.add(deliveryRequest);
      LinkedList<Intersection> optimalTour = Algorithm.ExecuteAlgorithm(this.cityMap.getWarehouse(), deliveryRequests);
      this.window.getMapViewer().updateTour(optimalTour.stream().map(intersection -> {
        return new GeoPosition(intersection.getLatitude(), intersection.getLongitude());
      }).toList());
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

  public void resetDeliveryRequests(){

  }

  public void loadMap(/*String path*/) throws UnsupportedEncodingException {
    String path = "src/main/java/com/pld/agile/utils/maps/smallMap.xml";

    JFileChooser j = new JFileChooser("src/main/java/com/pld/agile/utils/maps");
    j.setAcceptAllFileFilterUsed(false);
    j.setDialogTitle("Select a map file (.xml)");

    FileNameExtensionFilter restrict = new FileNameExtensionFilter("Only .xml files", "xml");
    j.addChoosableFileFilter(restrict);

    // invoke the showsOpenDialog function to show the save dialog
    int r = j.showOpenDialog(null);

    // if the user selects a file
    if (r == JFileChooser.APPROVE_OPTION) {
      // set the label to the path of the selected file
      path = j.getSelectedFile().toURI().getPath();
     // path = java.net.URLDecoder.decode(path,"utf-8");
      System.out.println(path);
    }
    try {
      XMLDeserialiser.load(path, intersections, cityMap);
    } catch (ExceptionXML e) {
      throw new RuntimeException(e);
    }
    window.getMapViewer().clearAll();
    deliveryRequests.clear();
    this.window.getDeliveriesView().displayRequests(deliveryRequests);

    /*for (Intersection intersection : intersections.values()) {
    deliveryRequests.clear();
    this.window.getDeliveriesView().displayRequests(deliveryRequests);
    for (Intersection intersection : intersections.values()) {
      if(cityMap.getWarehouse().getId() == intersection.getId()) continue;
      GeoPosition geoPosition = new GeoPosition(intersection.getLatitude(), intersection.getLongitude());
      this.window.getMapViewer().addPoint(geoPosition, intersection.getId(), Marker.Type.MAP);
    }*/

    Intersection warehouse = cityMap.getWarehouse();
//    HashSet<Intersection> settledVertices = new HashSet<>();
//    HashSet<Intersection> unsettledVertices = new HashSet<>();
//
//    unsettledVertices.add(warehouse);
//
//    while(unsettledVertices.size()!=0){
//      Intersection intersection = unsettledVertices.iterator().next();
//
//      List<RoadSegment> outgoingSegments = intersection.getOutgoingSegments();
//
//      for (RoadSegment outgoingSegment:outgoingSegments) {
//        Intersection destination = outgoingSegment.getDestination();
//        if(!settledVertices.contains(destination)) {
//          if (!unsettledVertices.contains(destination)) {
//            unsettledVertices.add(destination);
//          }
//        }
//      }
//      settledVertices.add(intersection);
//      unsettledVertices.remove(intersection);
//      GeoPosition geoPosition = new GeoPosition(intersection.getLatitude(), intersection.getLongitude());
//      this.window.getMapViewer().addPoint(geoPosition, intersection.getId(), Marker.Type.MAP);
//    }

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
  public Window getWindow() {
    return window;
  }

  public void setWindow(Window window) {
    this.window = window;
  }
}
