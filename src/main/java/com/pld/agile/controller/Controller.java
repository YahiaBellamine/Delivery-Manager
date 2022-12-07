package com.pld.agile.controller;

import com.pld.agile.model.CityMap;
import com.pld.agile.model.DeliveryRequest;
import com.pld.agile.model.Intersection;
import com.pld.agile.model.Tour;
import com.pld.agile.model.enums.TimeWindow;
import com.pld.agile.utils.Algorithm;
import com.pld.agile.utils.xml.ExceptionXML;
import com.pld.agile.utils.xml.XMLDeserialiser;
import com.pld.agile.utils.xml.XMLSerialiser;
import com.pld.agile.view.Window;
import com.pld.agile.view.map.Marker;
import com.pld.agile.view.map.Route;
import org.jxmapviewer.viewer.GeoPosition;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.geom.Point2D;
import java.io.UnsupportedEncodingException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.*;


public class Controller {

  private Window window;
  private CityMap cityMap;
  private Map<Long, Intersection> intersections;
  private Long currentIntersectionId;


  public Controller() {
    cityMap = new CityMap();
    window = new Window(this, cityMap);
    this.intersections = new HashMap<>();
    window.setVisible(true);
  }

  public void addDeliveryRequest() {
    if (currentIntersectionId != null) {
      TimeWindow tm = (TimeWindow) this.window.getDeliveryRequestView().comboBoxTimeWindow.getSelectedItem();
      DeliveryRequest deliveryRequest = new DeliveryRequest(tm, this.intersections.get(currentIntersectionId));
      // Add delivery request to right panel

      cityMap.getTourList().get(0).addDeliveryRequest(deliveryRequest);
      Tour optimalTour = Algorithm.ExecuteAlgorithm(this.cityMap.getWarehouse(),(LinkedList<DeliveryRequest>) cityMap.getTourList().get(0).getDeliveryRequests());
      cityMap.getTourList().set(0,optimalTour);
      window.getMapViewer().updateRoutes();

//      List<Intersection> optimalTourIntersections = optimalTour.getIntersections();
//      this.window.getMapViewer().updateTour(optimalTourIntersections.stream().map(intersection -> {
//        return new GeoPosition(intersection.getLatitude(), intersection.getLongitude());
//      }).toList());
//      this.window.getDeliveriesView().displayTourDuration(optimalTour);
//      this.window.getDeliveriesView().displayRequests(optimalTour.getDeliveryRequests());
//
//      /* Add pointer to the map*/
//      GeoPosition geoPosition = new GeoPosition(intersections.get(currentIntersectionId).getLatitude(),
//              intersections.get(currentIntersectionId).getLongitude());
//      this.window.getMapViewer().addPoint(geoPosition, currentIntersectionId, Marker.Type.TOUR);

      this.window.getMapViewer().clearRequestMarker();
      currentIntersectionId = null;
      this.window.getDeliveryRequestView().setSelectDestinationPoint("  Select your destination point on the map.");

    } else {
      this.window.displayMessage("Please select an intersection on the map");
    }
  }

  public void saveTour() throws ExceptionXML, ParserConfigurationException, TransformerException {
//    for(DeliveryRequest d: deliveryRequests){
//      System.out.println(d.getAddress());
////    }
//    if(deliveryRequests.size()==0) {
//      throw new ExceptionXML("Add at least one delivery request before saving the tour");
//    }
//    Tour optimalTour = Algorithm.ExecuteAlgorithm(this.cityMap.getWarehouse(), (LinkedList<DeliveryRequest>)deliveryRequests);
//    if(optimalTour==null) {
//      throw new ExceptionXML("No tour to save");
//    }
//    XMLSerialiser.save(optimalTour);
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
    cityMap.getTourList().clear();
    this.window.getDeliveriesView().displayRequests(new LinkedList<DeliveryRequest>());

    cityMap.getTourList().add(new Tour());

    //Intersection warehouse = cityMap.getWarehouse();

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
