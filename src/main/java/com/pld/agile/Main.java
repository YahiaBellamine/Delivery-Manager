package com.pld.agile;

import com.pld.agile.model.CityMap;
import com.pld.agile.model.Intersection;
import com.pld.agile.utils.xml.ExceptionXML;
import com.pld.agile.utils.xml.XMLDeserialiser;
import com.pld.agile.view.map.MapViewer;

import javax.swing.*;

import java.awt.*;

import java.util.HashMap;
import java.util.Map;

public class Main {


  public static void main(String[] args) {

    String s = "src/main/java/com/pld/agile/utils/maps/smallMap.xml";
    Map<Long, Intersection> intersections = new HashMap<Long, Intersection>();
    Intersection i=new Intersection((long)1,0,0,null);
    CityMap cityMap=new CityMap(0,0,0,0,i);
    try {
      XMLDeserialiser.load(s, intersections, cityMap);
    } catch (ExceptionXML e) {
      throw new RuntimeException(e);
    }

    MapViewer mapForm = new MapViewer();
    // traverse the list and print information
    // System.out.println(intersections.size());
    for(Map.Entry<Long,Intersection> entry:intersections.entrySet() ) {
      System.out.println("ID: "+entry.getKey());
      System.out.println(entry.getValue().getLatitude());
      System.out.println(entry.getValue().getLongitude());
      System.out.println(entry.getValue().getOutgoingSegments().size());
//           for(int j=0;j<entry.getValue().getOutgoingSegments().size();j++){
//               System.out.println(entry.getValue().getOutgoingSegments().get(j));
//           }
      System.out.println();

      mapForm.addPoint(entry.getValue().getLatitude(),
              entry.getValue().getLongitude(),
               entry.getValue().getId());
    }


    // DeepCopy works
    System.out.println(cityMap.getWarehouse().getId());


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




