package com.pld.agile;

import com.pld.agile.model.CityMap;
import com.pld.agile.model.Intersection;
import com.pld.agile.utils.xml.ExceptionXML;
import com.pld.agile.utils.xml.XMLDeserialiser;
import com.pld.agile.view.map.MapViewer;
import com.pld.agile.view.map.Marker;
import org.jxmapviewer.viewer.GeoPosition;

import javax.swing.*;

import java.awt.*;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Main {


  public static void main(String[] args) {

    String s = "src/main/java/com/pld/agile/utils/maps/smallMap.xml";

    //example of creating a hashmap
    //and creating the graph using the XML deserialiser function
    Map<Long, Intersection> intersections = new HashMap<Long, Intersection>();
    Intersection i=new Intersection((long)1,0,0,null);
    CityMap cityMap=new CityMap(0,0,0,0,i);
    try {
      XMLDeserialiser.load(s, intersections, cityMap);
    } catch (ExceptionXML e) {
      throw new RuntimeException(e);
    }


    //example of creating the map panel
    MapViewer mapForm = new MapViewer();

    //example of traversing the map of intersections and printing information
    for(Map.Entry<Long,Intersection> entry:intersections.entrySet() ) {
      if(entry.getKey() == 25303831) continue;
      System.out.println("ID: "+entry.getKey());
      System.out.println(entry.getValue().getLatitude());
      System.out.println(entry.getValue().getLongitude());
      System.out.println(entry.getValue().getOutgoingSegments().size());
      //example of printing the outgoing segments
//           for(int j=0;j<entry.getValue().getOutgoingSegments().size();j++){
//               System.out.println(entry.getValue().getOutgoingSegments().get(j));
//           }
      System.out.println();

      //example of creating a GeoPosition objects and adding it to the map as a map marker
      GeoPosition pos = new GeoPosition(entry.getValue().getLatitude(),entry.getValue().getLongitude());
      mapForm.addPoint(pos,
              entry.getValue().getId(), Marker.Type.MAP);
    }

    //example of defining the warehouse marker on the map
    GeoPosition pos = new GeoPosition(cityMap.getWarehouse().getLatitude(),
            cityMap.getWarehouse().getLongitude());
    mapForm.addPoint(pos,cityMap.getWarehouse().getId(), Marker.Type.WAREHOUSE);

    //example of adding some Tour markers to the map
    //and creating a list of GeoPositions to draw the route
    int counter=0;
    LinkedList<GeoPosition> listPos = new LinkedList<>();
    listPos.add(pos);
    for(Intersection entry:intersections.values()){
      GeoPosition pos2 = new GeoPosition(entry.getLatitude(), entry.getLongitude());
      mapForm.addPoint(pos2, 0, Marker.Type.TOUR);
      listPos.add(pos2);
      counter++;
      if(counter>4) break;
    }
    listPos.add(pos);
    mapForm.updateTour(listPos);

    // clearing tour markers and the tour route:
   // mapForm.clearMarkers();

    //clearing all the markers (including the intersections' markers
  //  mapForm.clearAll();

    //example of adding a marker as the delivery request address
    GeoPosition pos3 = new GeoPosition(45.757072,4.8586116);
    mapForm.addPoint(pos3, 0, Marker.Type.REQUEST);


    //creating the JFrame
    JFrame frame = new JFrame("MapViewer");
    frame.setContentPane(mapForm.mainPanel);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(new Dimension(1000,500));
    frame.setVisible(true);
    //mapForm.recenter();
  }


}
//        // System.out.println("Hello world!");
//        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
//        try {
//            // create db, instance of DocumentBuilder
//            DocumentBuilder db = dbf.newDocumentBuilder();
//            // charge the required .xml
//            Document document = db.parse("smallMap.xml");
//
//            // obtain the map
//            Node map = document.getElementsByTagName("map").item(0);
//            // print the length of the list
//            //System.out.println("We have 1 map");
//
//            Node warehouse = document.getElementsByTagName("warehouse").item(0);
//            System.out.println("We have 1 warehouse");
//            String warehouseAddress=warehouse.getAttributes().getNamedItem("address").getNodeValue();
//            System.out.println("The address of our warehouse: "+warehouseAddress);
//
//            NodeList intersection = document.getElementsByTagName("intersection");
//            System.out.println("We have "+intersection.getLength()+" intersections");
//
//            NodeList segment = document.getElementsByTagName("segment");
//            System.out.println("We have "+segment.getLength()+" segments");
//
//            int lengthIntersection=intersection.getLength();
//            String idIntersection[]= new String[lengthIntersection];
//            String latitudeIntersection[]= new String[lengthIntersection];
//            String longitudeIntersection[]= new String[lengthIntersection];
//            // Traverse every node of intersection
//            for(int i=0;i<intersection.getLength();i++){
//                // System.out.println("======================The traverse of number " + (i + 1) + "element======================");
//                // get a node by using item(i)
//                Node element = intersection.item(i);
//                idIntersection[i]=element.getAttributes().getNamedItem("id").getNodeValue();
//                latitudeIntersection[i]=element.getAttributes().getNamedItem("latitude").getNodeValue();
//                longitudeIntersection[i]=element.getAttributes().getNamedItem("longitude").getNodeValue();
//                if(i==0) System.out.println(idIntersection[i]+" "+latitudeIntersection[i]+" "+longitudeIntersection[i]);
//                // System.out.println("======================End of traverse of number " + (i + 1) + " element======================");
//            }
//
//            int lSegment=segment.getLength();
//            String destinationSegment[]= new String[lSegment];
//            String lengthSegment[]= new String[lSegment];
//            String nameSegment[]= new String[lSegment];
//            String originSegment[]= new String[lSegment];
//            // Traverse every node of segment
//            for(int i=0;i<lSegment;i++){
//                // System.out.println("======================The traverse of number " + (i + 1) + "element======================");
//                // get a node by using item(i)
//                Node element = segment.item(i);
//                destinationSegment[i]=element.getAttributes().getNamedItem("destination").getNodeValue();
//                lengthSegment[i]=element.getAttributes().getNamedItem("length").getNodeValue();
//                nameSegment[i]=element.getAttributes().getNamedItem("name").getNodeValue();
//                originSegment[i]=element.getAttributes().getNamedItem("origin").getNodeValue();
//                if(i==0) System.out.println(destinationSegment[i]+" "+lengthSegment[i]+" "+nameSegment[i]+" "+originSegment[i]);
//                // System.out.println("======================End of traverse of number " + (i + 1) + " element======================");
//            }
//        } catch (ParserConfigurationException e) {
//            e.printStackTrace();
//        }catch (SAXException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }




