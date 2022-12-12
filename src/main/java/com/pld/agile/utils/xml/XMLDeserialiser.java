package com.pld.agile.utils.xml;

import com.pld.agile.controller.Controller;
import com.pld.agile.model.CityMap;
import com.pld.agile.model.Intersection;
import com.pld.agile.model.RoadSegment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import java.io.IOException;
import java.util.Map;

public class XMLDeserialiser {
  public static void load(String path, CityMap cityMap) throws ExceptionXML{
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
      // TODO: How can we create Map while initialising the max and min for longitude and latitude here?

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
      System.out.println(RoadSegmentsList.getLength());
      for (int i = 0; i < RoadSegmentsList.getLength(); i++) {
        createRoadSegment((Element) RoadSegmentsList.item(i),cityMap.getIntersections());
      }

      Long warehouseId = Long.parseLong(warehouseAddress);
      cityMap.setWarehouse(cityMap.getIntersections().get(warehouseId));

    } catch (ParserConfigurationException | SAXException | IOException e) {
      e.printStackTrace();
    }

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
    if(originID==destinationID){
      throw new ExceptionXML("Same origin ID and destination ID");
    }
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
    List<RoadSegment> outgoingSegments = new ArrayList<RoadSegment>();
    System.out.println(id);
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
}
