package com.pld.agile;


import com.pld.agile.model.DeliveryRequest;
import com.pld.agile.model.Intersection;
import com.pld.agile.model.TimeWindow;
import com.pld.agile.view.DeliveriesView;
import com.pld.agile.view.Window;
import com.pld.agile.model.CityMap;
import com.pld.agile.utils.xml.ExceptionXML;
import com.pld.agile.utils.xml.XMLDeserialiser;
import com.pld.agile.view.map.MapViewer;
import com.pld.agile.view.map.Marker;
import org.jxmapviewer.viewer.GeoPosition;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.Map;


public class Main {


  public static void main(String[] args) {
  //example of creating delivery requests
    LinkedList<DeliveryRequest> deliveries = new LinkedList<>();
    deliveries.add(new DeliveryRequest(new TimeWindow(7, 8), new Intersection((long)1, 53.5, 46.7, null)));
    deliveries.add(new DeliveryRequest(new TimeWindow(7, 8), new Intersection((long)2, 16.7, 22.9, null)));
    deliveries.add(new DeliveryRequest(new TimeWindow(8, 9), new Intersection((long)3, 76.8, 82.6, null)));
    deliveries.add(new DeliveryRequest(new TimeWindow(9, 10), new Intersection((long)4, 56.7, 42.4, null)));
    deliveries.add(new DeliveryRequest(new TimeWindow(10, 11), new Intersection((long)5, 64.9, 62.3, null)));

    deliveries.add(new DeliveryRequest(new TimeWindow(7, 8), new Intersection((long)6, 53.5, 46.7, null)));
    deliveries.add(new DeliveryRequest(new TimeWindow(7, 8), new Intersection((long)7, 16.7, 22.9, null)));
    deliveries.add(new DeliveryRequest(new TimeWindow(8, 9), new Intersection((long)8, 76.8, 82.6, null)));
    deliveries.add(new DeliveryRequest(new TimeWindow(9, 10), new Intersection((long)9, 56.7, 42.4, null)));
    deliveries.add(new DeliveryRequest(new TimeWindow(10, 11), new Intersection((long)10, 64.9, 62.3, null)));

    deliveries.add(new DeliveryRequest(new TimeWindow(7, 8), new Intersection((long)11, 53.5, 46.7, null)));
    deliveries.add(new DeliveryRequest(new TimeWindow(7, 8), new Intersection((long)12, 16.7, 22.9, null)));
    deliveries.add(new DeliveryRequest(new TimeWindow(8, 9), new Intersection((long)13, 76.8, 82.6, null)));
    deliveries.add(new DeliveryRequest(new TimeWindow(9, 10), new Intersection((long)14, 56.7, 42.4, null)));
    deliveries.add(new DeliveryRequest(new TimeWindow(10, 11), new Intersection((long)15, 64.9, 62.3, null)));

    deliveries.add(new DeliveryRequest(new TimeWindow(7, 8), new Intersection((long)16, 53.5, 46.7, null)));
    deliveries.add(new DeliveryRequest(new TimeWindow(7, 8), new Intersection((long)17, 16.7, 22.9, null)));
    deliveries.add(new DeliveryRequest(new TimeWindow(8, 9), new Intersection((long)18, 76.8, 82.6, null)));
    deliveries.add(new DeliveryRequest(new TimeWindow(9, 10), new Intersection((long)19, 56.7, 42.4, null)));
    deliveries.add(new DeliveryRequest(new TimeWindow(10, 11), new Intersection((long)20, 64.9, 62.3, null)));

    /*JFrame view = new Window();
    DeliveriesView deliveriesPanel = new DeliveriesView();
    deliveriesPanel.setSize(view.getSize().width / 4, view.getSize().height);
    view.add(deliveriesPanel);
    deliveriesPanel.displayRequests(deliveries);
    view.setVisible(true);*/

    

    //example of creating a hashmap
    //and creating the graph using the XML deserialiser function
    String s = "src/main/java/com/pld/agile/utils/maps/smallMap.xml";
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
    JFrame view = new Window();
    JPanel mainPanel = new JPanel(new GridBagLayout());
    mainPanel.setBackground(Color.RED);

    DeliveriesView deliveriesPanel = new DeliveriesView();
    deliveriesPanel.setSize(mainPanel.getWidth() / 4, mainPanel.getHeight());

    //TODO - Replace this panel with the user entry panel (@Aymane)
    DeliveriesView deliveriesPanel2 = new DeliveriesView();
    deliveriesPanel2.setSize(mainPanel.getWidth() / 4, mainPanel.getHeight());

    view.setContentPane(mainPanel);

    GridBagConstraints constraints = new GridBagConstraints();

    //User entry panel
    constraints.gridx = 0;
    constraints.gridy = 0;
    constraints.weightx = 0.1;
    constraints.weighty = 1;
    constraints.gridwidth = 1;
    constraints.fill = GridBagConstraints.BOTH;
    //TODO - Replace this panel with the user entry panel (@Aymane)
    view.add(deliveriesPanel2, constraints);

    //Graphical view (Map) panel
    constraints.gridx = 1;
    constraints.gridy = 0;
    constraints.weightx = 0.8;
    constraints.weighty = 1;
    constraints.gridwidth = 2;
    constraints.fill = GridBagConstraints.BOTH;
    view.add(mapForm.mainPanel, constraints);

    //Textual view panel
    constraints.gridx = 3;
    constraints.gridy = 0;
    constraints.weightx = 0.1;
    constraints.weighty = 1;
    constraints.gridwidth = 1;
    constraints.fill = GridBagConstraints.BOTH;
    view.add(deliveriesPanel, constraints);

    deliveriesPanel.displayRequests(deliveries);
    view.setVisible(true);
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




