package com.pld.agile.view;

import com.pld.agile.controller.Controller;
import com.pld.agile.model.CityMap;
import com.pld.agile.model.Intersection;
import com.pld.agile.view.listener.ButtonListener;
import com.pld.agile.view.map.MapViewer;
import org.jxmapviewer.viewer.GeoPosition;

import javax.swing.*;
import java.awt.*;

public class Window extends JFrame {

  /** The map for the GUI */
  private MapViewer mapViewer;

  /** The graphical view of the GUI */
  private final DeliveryRequestView deliveryRequestView;

  /** The textual view of the GUI */
  private final DeliveriesView deliveriesView;

  /** The menu bar of the GUI */
  private final JMenuBar menuBar;

  /** The button listener of the GUI */
  private ButtonListener buttonListener;


  public final static String LOAD_MAP = "Load a Map";
  public final static String ADD_DELIVERY_REQUEST = "Add a Delivery Request";
  public final static String SAVE_TOURS= "Save the tours";
  public final static String LOAD_TOURS= "Load the tours";
  public final static String RECENTER_MAP = "Recenter the Map";
  public final static String ADD_COURIER = "Add a courier";

  public Window(CityMap cityMap, Controller controller) {
    super("Delivery Manager");

    this.mapViewer = new MapViewer(cityMap, controller);

    //Create the JFrame
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setResizable(false);
    Dimension screenDimensions = Toolkit.getDefaultToolkit().getScreenSize();
    this.setSize((int)(screenDimensions.width * 0.95), (int)(screenDimensions.height * 0.95));
    this.setLayout(null);

    int x = (int) ((screenDimensions.getWidth() - this.getWidth()) / 2);
    int y = (int) ((screenDimensions.getHeight() - this.getHeight() - 45) / 2);
    this.setLocation(x, y);

    JPanel contentPane = new JPanel(new GridBagLayout());
    this.setContentPane(contentPane);
    mapViewer.setCenter(new GeoPosition(45.7640,4.8357));
    mapViewer.recenter();
    this.getContentPane().add(mapViewer.getMapViewer());

    buttonListener = new ButtonListener(controller, mapViewer);

    //Creation of the menu bar
    menuBar = new JMenuBar();

    JMenu fileMenu = new JMenu("File");
    JMenuItem loadMap = new JMenuItem("Load Map");
    loadMap.setActionCommand(LOAD_MAP);
    loadMap.addActionListener(buttonListener);
    JMenuItem loadTours = new JMenuItem("Load Tours");
    loadTours.setActionCommand(LOAD_TOURS);
    loadTours.addActionListener(buttonListener);
    JMenuItem saveTours = new JMenuItem("Save Tours");
    saveTours.setActionCommand(SAVE_TOURS);
    saveTours.addActionListener(buttonListener);

    JMenu actionsMenu = new JMenu("Actions");
    JMenuItem recenterMap = new JMenuItem("Recenter Map");
    recenterMap.setActionCommand(RECENTER_MAP);
    recenterMap.addActionListener(buttonListener);
    JMenuItem addCourier = new JMenuItem("Add Courier");
    addCourier.setActionCommand(ADD_COURIER);
    addCourier.addActionListener(buttonListener);

    fileMenu.add(loadMap);
    fileMenu.add(loadTours);
    fileMenu.add(saveTours);

    actionsMenu.add(recenterMap);
    actionsMenu.add(addCourier);

    menuBar.add(fileMenu);
    menuBar.add(actionsMenu);

    JButton addDeliveryRequestBtn = new JButton("Add a Delivery Request");
    addDeliveryRequestBtn.setMaximumSize(new Dimension(250, 30));
    addDeliveryRequestBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
    addDeliveryRequestBtn.setActionCommand(ADD_DELIVERY_REQUEST);
    addDeliveryRequestBtn.addActionListener(buttonListener);

    deliveryRequestView = new DeliveryRequestView();

    deliveriesView = new DeliveriesView(cityMap, this);
    deliveriesView.setSize(new Dimension(this.getContentPane().getWidth() / 4, this.getContentPane().getHeight()));

    JPanel leftContainer = new JPanel();
    leftContainer.setLayout(new BoxLayout(leftContainer, BoxLayout.PAGE_AXIS));

    leftContainer.add(Box.createRigidArea(new Dimension(0,50)));
    leftContainer.add(deliveryRequestView);
    leftContainer.add(Box.createRigidArea(new Dimension(0,20)));
    leftContainer.add(addDeliveryRequestBtn);
    leftContainer.add(Box.createRigidArea(new Dimension(0,100)));

    GridBagConstraints constraints = new GridBagConstraints();

    //Menu bar
    constraints.gridx = 0;
    constraints.gridy = 0;
    constraints.weightx = 1;
    constraints.weighty = 0.01;
    constraints.gridwidth = 5;
    constraints.fill = GridBagConstraints.BOTH;
    this.add(menuBar, constraints);

    //User entry panel
    constraints.gridx = 0;
    constraints.gridy = 1;
    constraints.weightx = 0.1;
    constraints.weighty = 0.99;
    constraints.gridwidth = 1;
    constraints.fill = GridBagConstraints.BOTH;
    this.add(leftContainer, constraints);

    //Graphical view (Map) panel
    constraints.gridx = 1;
    constraints.gridy = 1;
    constraints.weightx = 1;
    constraints.weighty = 0.99;
    constraints.gridwidth = 3;
    constraints.fill = GridBagConstraints.BOTH;
    this.add(mapViewer.getMapViewer(), constraints);

    /*//Textual view panel
    constraints.gridx = 3;
    constraints.gridy = 1;
    constraints.weightx = 0.1;
    constraints.weighty = 0.99;
    constraints.gridwidth = 1;
    constraints.fill = GridBagConstraints.BOTH;
    this.add(deliveriesView, constraints);*/

    this.setVisible(true);
  }

  public MapViewer getMapViewer() {
    return mapViewer;
  }

  public DeliveryRequestView getDeliveryRequestView() {
    return deliveryRequestView;
  }

//  public DeliveriesView getDeliveriesView() {
//    return deliveriesView;
//  }

  public void displayMessage(String message){
    JOptionPane.showMessageDialog(this, message);
  }

  public void updateSelectedPoint(Intersection intersection) {
    this.mapViewer.setRequestMarker(intersection.getGeoPosition());
    this.mapViewer.updateMap();
    this.deliveryRequestView.setSelectDestinationPoint("Intersection " + intersection.getId());
  }

  public ButtonListener getButtonListener() {
    return buttonListener;
  }
}

