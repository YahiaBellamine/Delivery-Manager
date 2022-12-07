package com.pld.agile.utils.xml;

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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;



public class XMLDeserialiser {
  public static void load(String path, Map<Long, Intersection> intersections, CityMap cityMap) throws ExceptionXML{
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    try {
      // create db, instance of DocumentBuilder
      DocumentBuilder db = dbf.newDocumentBuilder();
      // charge the required .xml
      Document document = db.parse(path);
      // verify if we found the document
      if(document==null){
        throw new ExceptionXML("Error file is null");
      }
      // obtain the map
      Node map = document.getElementsByTagName("map").item(0);
      // verify if the document starts with "map"
      if(map==null){
        throw new ExceptionXML("This is not a map");
      }
      // TODO: How can we create Map while initialising the max and min for longitude and latitude here?

      //reinitialising the map
      intersections.clear();

      Node warehouse = document.getElementsByTagName("warehouse").item(0);
      String warehouseAddress=warehouse.getAttributes().getNamedItem("address").getNodeValue();
      System.out.println("The address of our warehouse: "+warehouseAddress);

      //build intersections
      NodeList intersectionsList = document.getElementsByTagName("intersection");
      for (int i = 0; i < intersectionsList.getLength(); i++) {
        Intersection intersection = createIntersection((Element) intersectionsList.item(i));
        intersections.put(intersection.getId(),intersection);
      }

      //build RoadSegments
      NodeList RoadSegmentsList = document.getElementsByTagName("segment");
      System.out.println(RoadSegmentsList.getLength());
      for (int i = 0; i < RoadSegmentsList.getLength(); i++) {
        //TODO: QUESTION: We don't need to return the result of creation, right?
        RoadSegment RoadSegment = createRoadSegment((Element) RoadSegmentsList.item(i),intersections);
      }

      // create the CityMap
      // System.out.println(cityMap.getWarehouse().getId());
      CityMap temp=createCityMap((Element)warehouse,intersections);
      //cityMap= (CityMap) temp.clone();
      CityMap.DeepCopy(temp,cityMap);
      //System.out.println(cityMap.getWarehouse().getId());


    } catch (ParserConfigurationException e) {
      e.printStackTrace();
    }catch (SAXException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }

  }


  private static RoadSegment createRoadSegment(Element e, Map<Long, Intersection> intersections) throws ExceptionXML {
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
    Intersection origin = intersections.get(originID);
    if (origin == null) {
      throw new ExceptionXML("Unknown Intersection");
    }
    RoadSegment r= new RoadSegment(name,length,destination);
    origin.addOutgoingSegment(r);
    //System.out.println("ok");
    //System.out.println(Intersection.sizeOutgoingSegment(origin));

    return r;
  }


  private static Intersection createIntersection(Element e) throws ExceptionXML {
    double latitude = Double.parseDouble(e.getAttribute("latitude"));
    double longitude = Double.parseDouble(e.getAttribute("longitude"));
    long id = Long.parseLong(e.getAttribute("id"));
    List<RoadSegment> outgoingSegments = new ArrayList<RoadSegment>();
    if (id < 0) {
      throw new ExceptionXML("Invalid ID");
    }
    return new Intersection(id, latitude,longitude,outgoingSegments);
  }

  private static CityMap createCityMap(Element e,Map<Long, Intersection> intersections) throws ExceptionXML{
    long warehouseID = Long.parseLong(e.getAttribute("address"));
    if (warehouseID < 0) {
      throw new ExceptionXML("Incorrect warehouse ID");
    }
    Intersection warehouse=intersections.get(warehouseID);
    if (warehouse == null) {
      throw new ExceptionXML("Unknown Intersection for warehouse");
    }
    // TODO: to be MODIFIED! 0 0 0 0 will be changed to the max and min for longitude and latitude
    // Map m= new CityMap(0,0,0,0,warehouse) ;
    return  new CityMap(0,0,0,0,warehouse) ;

  }
}
