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
  public final static String STOCK_TOUR= "Stock the tour";

  public Window(Controller controller) {
    super("Delivery Manager");
    this.controller = controller;
    this.mapViewer = new MapViewer(controller);

    mapViewer = new MapViewer(controller);


    //Create the JFrame
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setResizable(false);
    Dimension screenDimensions = Toolkit.getDefaultToolkit().getScreenSize();
    this.setSize((int)(screenDimensions.width * 0.9), (int)(screenDimensions.height * 0.9));
    this.setLayout(null);

    int x = (int) ((screenDimensions.getWidth() - this.getWidth()) / 2);
    int y = (int) ((screenDimensions.getHeight() - this.getHeight()) / 2);
    this.setLocation(x, y);

    JPanel contentPane = new JPanel(new GridBagLayout());
    this.setContentPane(contentPane);
    mapViewer.setCenter(new GeoPosition(45.7640,4.8357));
    mapViewer.recenter();
    this.getContentPane().add(mapViewer.mainPanel);

    ButtonListener buttonListener = new ButtonListener(controller);

    JButton loadMapButton = new JButton("Load a Map");
    loadMapButton.setMaximumSize(new Dimension(250, 30));
    loadMapButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    loadMapButton.setActionCommand(LOAD_MAP);
    loadMapButton.addActionListener(buttonListener);

    JButton addDeliveryRequestBtn = new JButton("Add a Delivery Request");
    addDeliveryRequestBtn.setMaximumSize(new Dimension(250, 30));
    addDeliveryRequestBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
    addDeliveryRequestBtn.setActionCommand(ADD_DELIVERY_REQUEST);
    addDeliveryRequestBtn.addActionListener(buttonListener);

    JButton stockTourBtn = new JButton("Stock the tour");
    stockTourBtn.setMaximumSize(new Dimension(250, 30));
    stockTourBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
    stockTourBtn.setActionCommand(STOCK_TOUR);
    stockTourBtn.addActionListener(buttonListener);

    deliveryRequestView = new DeliveryRequestView();

    deliveriesView = new DeliveriesView();
    deliveriesView.setSize(contentPane.getWidth() / 4, contentPane.getHeight());

    JPanel leftContainer = new JPanel();
    leftContainer.setLayout(new BoxLayout(leftContainer, BoxLayout.PAGE_AXIS));

    leftContainer.add(Box.createRigidArea(new Dimension(0,50)));
    leftContainer.add(loadMapButton);
    leftContainer.add(Box.createRigidArea(new Dimension(0,20)));
    leftContainer.add(deliveryRequestView);
    leftContainer.add(Box.createRigidArea(new Dimension(0,20)));
    leftContainer.add(addDeliveryRequestBtn);
    leftContainer.add(Box.createRigidArea(new Dimension(0,100)));
    leftContainer.add(stockTourBtn);
    leftContainer.add(Box.createRigidArea(new Dimension(0,50)));

    GridBagConstraints constraints = new GridBagConstraints();

    //User entry panel
    constraints.gridx = 0;
    constraints.gridy = 0;
    constraints.weightx = 0.1;
    constraints.weighty = 1;
    constraints.gridwidth = 1;
    constraints.fill = GridBagConstraints.BOTH;
    this.add(leftContainer, constraints);

    //Graphical view (Map) panel
    constraints.gridx = 1;
    constraints.gridy = 0;
    constraints.weightx = 0.8;
    constraints.weighty = 1;
    constraints.gridwidth = 2;
    constraints.fill = GridBagConstraints.BOTH;
    this.add(mapViewer.mainPanel, constraints);

    //Textual view panel
    constraints.gridx = 3;
    constraints.gridy = 0;
    constraints.weightx = 0.1;
    constraints.weighty = 1;
    constraints.gridwidth = 1;
    constraints.fill = GridBagConstraints.BOTH;
    this.add(deliveriesView, constraints);

    this.setVisible(true);
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

