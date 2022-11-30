package com.pld.agile.view;

import com.pld.agile.controller.Controller;
import com.pld.agile.model.CityMap;
import com.pld.agile.model.Intersection;
import com.pld.agile.view.listener.ButtonListener;
import com.pld.agile.view.map.MapViewer;
import com.pld.agile.view.map.Marker;
import org.jxmapviewer.viewer.GeoPosition;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Window extends JFrame {

  private MapViewer mapViewer;
  private final Controller controller;
  private final DeliveryRequestView deliveryRequestView;
  private final DeliveriesView deliveriesView;


  public final static String LOAD_MAP = "Load a Map";
  public final static String ADD_DELIVERY_REQUEST = "Add a Delivery Request";

  public Window(Controller controller) {
    super("Delivery Manager");
    this.controller = controller;
    this.mapViewer = new MapViewer(controller);

    mapViewer = new MapViewer(controller);
    mapViewer.mainPanel.setBounds(320, 10, 650, 650);
    mapViewer.recenter();
    this.getContentPane().add(mapViewer.mainPanel);

    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setResizable(false);
    Dimension screenDimensions = Toolkit.getDefaultToolkit().getScreenSize();
    this.setSize((int)(screenDimensions.width * 0.9), (int)(screenDimensions.height * 0.9));
    this.setLayout(null);

    int x = (int) ((screenDimensions.getWidth() - this.getWidth()) / 2);
    int y = (int) ((screenDimensions.getHeight() - this.getHeight()) / 2);
    this.setLocation(x, y);

    ButtonListener buttonListener = new ButtonListener(controller);

    JButton loadMapButton = new JButton("Load a Map");
    loadMapButton.setBounds(10, 10, 250, 30);
    loadMapButton.setActionCommand(LOAD_MAP);
    loadMapButton.addActionListener(buttonListener);

    JButton addDeliveryRequestBtn = new JButton("Add a Delivery Request");
    addDeliveryRequestBtn.setBounds(10, 300, 250, 30);
    addDeliveryRequestBtn.setActionCommand(ADD_DELIVERY_REQUEST);
    addDeliveryRequestBtn.addActionListener(buttonListener);

    deliveryRequestView = new DeliveryRequestView();
    deliveryRequestView.setBounds(10, 50, 250, 200);
    this.getContentPane().add(deliveryRequestView);

    deliveriesView = new DeliveriesView();
    deliveriesView.getTextViewPanel().setBounds(1200, 10, 300, 400);
    this.getContentPane().add(deliveriesView.getTextViewPanel());

    JPanel leftContainer = new JPanel();
    leftContainer.setBounds(0, 0, 300, 400);
    leftContainer.setLayout(null);
    leftContainer.add(loadMapButton);
    leftContainer.add(deliveryRequestView);
    leftContainer.add(addDeliveryRequestBtn);
    this.getContentPane().add(leftContainer);

  }

  public MapViewer getMapViewer() {
    return mapViewer;
  }

  public DeliveryRequestView getDeliveryRequestView() {
    return deliveryRequestView;
  }

  public DeliveriesView getDeliveriesView() {
    return deliveriesView;
  }

  public void displayMessage(String message){
    JOptionPane.showMessageDialog(this, message);
  }
}

