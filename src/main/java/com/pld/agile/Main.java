package com.pld.agile;


import com.pld.agile.controller.Controller;
import com.pld.agile.model.DeliveryRequest;
import com.pld.agile.model.Intersection;
import com.pld.agile.model.CityMap;
import com.pld.agile.model.enums.TimeWindow;
import com.pld.agile.utils.xml.ExceptionXML;
import com.pld.agile.utils.xml.XMLDeserialiser;
import com.pld.agile.view.DeliveriesView;
import com.pld.agile.view.map.MapViewer;
import com.pld.agile.view.map.Marker;
import org.jxmapviewer.viewer.GeoPosition;
import com.pld.agile.view.Window;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.Map;


public class Main {


  public static void main(String[] args) {
    EventQueue.invokeLater(new Runnable() {
      @Override
      public void run() {
        Controller controller = new Controller();
      }
    });

  }


}