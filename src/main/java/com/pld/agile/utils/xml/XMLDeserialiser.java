package com.pld.agile.utils.xml;

import com.pld.agile.controller.Controller;
import com.pld.agile.model.*;
import com.pld.agile.model.enums.TimeWindow;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class XMLDeserialiser {
  public static void loadMap(String path, CityMap cityMap) throws ExceptionXML, ParserConfigurationException, IOException, SAXException {
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

      // create db, instance of DocumentBuilder
      DocumentBuilder db = dbf.newDocumentBuilder();
      // charge the required .xml
      Document document = db.parse(path);
      // verify if we found the document
      if(document==null){
        throw new ExceptionXML("Empty file");
      }
      // obtain the map
      Node map = document.getElementsByTagName("map").item(0);
      // verify if the document starts with "map"
      if(map==null){
        throw new ExceptionXML("No map");
      }

      //reinitialising the map
      cityMap.reInitializeCityMap();

      Node warehouse = document.getElementsByTagName("warehouse").item(0);
      if(warehouse==null){
        throw new ExceptionXML("No warehouse");
      }
      String warehouseAddress=warehouse.getAttributes().getNamedItem("address").getNodeValue();
      //System.out.println("The address of our warehouse: "+warehouseAddress);


      //build intersections
      NodeList intersectionsList = document.getElementsByTagName("intersection");
      for (int i = 0; i < intersectionsList.getLength(); i++) {
        Intersection intersection = createIntersection((Element) intersectionsList.item(i));
        cityMap.addIntersection(intersection);
      }

      //build RoadSegments
      NodeList RoadSegmentsList = document.getElementsByTagName("segment");
      for (int i = 0; i < RoadSegmentsList.getLength(); i++) {
        createRoadSegment((Element) RoadSegmentsList.item(i),cityMap.getIntersections());
      }

      Long warehouseId = Long.parseLong(warehouseAddress);
      cityMap.setWarehouse(cityMap.getIntersections().get(warehouseId));

  }

  private static void createRoadSegment(Element e, Map<Long, Intersection> intersections) throws ExceptionXML {
    long destinationID = Long.parseLong(e.getAttribute("destination"));
    if (destinationID < 0) {
      throw new ExceptionXML("Incorrect destination ID");
    }
    Intersection destination = intersections.get(destinationID);
    if (destination == null) {
      throw new ExceptionXML("Unknown intersection");

    }
    String name = e.getAttribute("name");
    double length = Double.parseDouble(e.getAttribute("length"));
    if (length <= 0) {
      throw new ExceptionXML("Incorrect length");
    }

    // update the outgoingSegment in Intersection
    long originID = Long.parseLong(e.getAttribute("origin"));

    if (originID < 0) {
      throw new ExceptionXML("Incorrect origin ID");
    }
//    if(originID==destinationID){
//      throw new ExceptionXML("Same origin ID and destination ID");
//    }
    Intersection origin = intersections.get(originID);
    if (origin == null) {
      throw new ExceptionXML("Unknown Intersection");
    }
    RoadSegment r= new RoadSegment(name,length,destination);
    origin.addOutgoingSegment(r);
  }

  private static Intersection createIntersection(Element e) throws ExceptionXML {
    double latitude = Double.parseDouble(e.getAttribute("latitude"));
    double longitude = Double.parseDouble(e.getAttribute("longitude"));
    long id = Long.parseLong(e.getAttribute("id"));
    if (id < 0) {
      throw new ExceptionXML("Invalid Intersection ID");
    }
    if(latitude<-90||latitude>90){
      throw new ExceptionXML(("Invalid latitude"));
    }
    if(longitude<-180||longitude>180){
      throw new ExceptionXML("Invalid longitude");
    }
    
    return new Intersection(id, latitude,longitude);
  }

  public static void loadTours(String path, List<Tour> tours,CityMap cityMap) throws ExceptionXML, ParserConfigurationException, IOException, SAXException {
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    // create db, instance of DocumentBuilder
    DocumentBuilder db = dbf.newDocumentBuilder();
    // charge the required .xml
    Document document = db.parse(path);
    // verify if we found the document
    if(document==null){
      throw new ExceptionXML("Empty file");
    }
    // obtain the tours
    NodeList toursList = document.getElementsByTagName("tour");
    // verify if the document starts with "map"
    if(tours==null){
      throw new ExceptionXML("No tours");
    }

    for (int i = 0; i < toursList.getLength(); i++) {
      Tour cityTour = new Tour();
      //List deliveryRequests=new LinkedList<>();
      //List intersections=new LinkedList<>();
      Node tour=toursList.item(i);
      if(tour.getNodeType()==Node.ELEMENT_NODE){
        String idCourierString = tour.getAttributes().getNamedItem("id_courier").getNodeValue();
        Integer idCourierInteger = Integer.parseInt(idCourierString);
        cityTour.setCourier(new Courier(idCourierInteger));
        Double duration=Double.parseDouble(tour.getAttributes().getNamedItem("duration").getNodeValue());
        cityTour.setTourDuration(duration);
        for(Node node=tour.getFirstChild();node!=null;node=node.getNextSibling()){
          Element element = null;
          if(node instanceof Element)  element=(Element)node;
          if(node.getNodeType()==Node.ELEMENT_NODE){
            if(node.getNodeName().equals("delivery_request")){
              DeliveryRequest deliveryRequest = createDeliveryRequest(element,cityMap);
              cityTour.addDeliveryRequest(deliveryRequest);
            }
            if(node.getNodeName().equals("intersection")){
              Intersection intersection = createIntersection(element);
              if(cityMap.getIntersections().containsValue(intersection)){
                cityTour.addIntersection(intersection);
              }else{
                throw new ExceptionXML("You try to restore tours saved from a different map. Please reload map and then restore your tours.");
              }
            }
          }
        }
      }
      tours.add(cityTour);
    }
    cityMap.setTourList(tours);
  }

  private static DeliveryRequest createDeliveryRequest(Element element,CityMap cityMap) throws ExceptionXML {

    String tw = element.getAttribute("time_window");
    // default value
    TimeWindow timeWindow = TimeWindow.TW_8_9;
    switch (tw.charAt(0)){
      case '8':
        // timeWindow=TimeWindow.TW_8_9;
        break;
      case '9':
        timeWindow=TimeWindow.TW_9_10;
        break;
      case '1':
        if(tw.charAt(1)=='0') timeWindow=TimeWindow.TW_10_11;
        else if(tw.charAt(1)=='1') timeWindow=TimeWindow.TW_11_12;
        break;
      default:
        timeWindow=TimeWindow.TW_8_9;
        break;
    }

    long id = Long.parseLong(element.getAttribute("id_intersection"));
    Intersection intersection=cityMap.getIntersections().get(id);

    String time=element.getAttribute("delivery_time");
    String h=time.substring(0,2);
    String min=time.substring(3,5);
    String s=time.substring(6,8);

    double arrival_time=Integer.parseInt(h)+Double.parseDouble(min)/60+Double.parseDouble(s)/3600;

    if (id < 0) {
      throw new ExceptionXML("Invalid Intersection ID");
    }

    return new DeliveryRequest(timeWindow,intersection,arrival_time);
  }

}
