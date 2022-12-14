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
  public final static String SAVE_TOURS= "Save the tours";
  public final static String LOAD_TOURS= "Load the tours";
  public final static String RECENTER_MAP = "Recenter the Map";
  public final static String ADD_COURIER = "Add a courier";

  public Window(CityMap cityMap, Controller controller) {
    super("Delivery Manager");

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

    // Map view
    this.mapViewer = new MapViewer(cityMap, controller, this);

    // Button listener
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

    deliveryRequestView = new DeliveryRequestView(buttonListener);
    deliveriesView = new DeliveriesView(cityMap, this);

    JPanel gui = new JPanel();
    gui.setLayout(new GridLayout(1, 2));

    JPanel textualView = new JPanel();
    textualView.setLayout(new GridLayout(2, 1));

    textualView.add(deliveryRequestView);
    textualView.add(deliveriesView);
    gui.add(mapViewer.mainPanel);
    gui.add(textualView);

    // Deliveries view
    this.deliveriesView = new DeliveriesView(cityMap, this);
    this.deliveriesView.setSize(new Dimension(this.getContentPane().getWidth() / 4, this.getContentPane().getHeight()));

    // Constraints
    GridBagConstraints constraints = new GridBagConstraints();

    // Menu bar
    constraints.gridx = 0;
    constraints.gridy = 0;
    constraints.weightx = 1;
    constraints.weighty = 0.01;
    constraints.gridwidth = 1;
    constraints.fill = GridBagConstraints.BOTH;
    this.add(menuBar, constraints);

    //View
    constraints.gridx = 0;
    constraints.gridy = 1;
    constraints.weightx = 1;
    constraints.weighty = 0.99;
    constraints.gridwidth = 1;
    constraints.fill = GridBagConstraints.BOTH;
    this.add(gui, constraints);

    this.setExtendedState(JFrame.MAXIMIZED_BOTH);
    this.setVisible(true);
  }

  public MapViewer getMapViewer() {
    return mapViewer;
  }

  public DeliveryRequestView getDeliveryRequestView() {
    return deliveryRequestView;
  }

  public void displayMessage(String message){
    JOptionPane.showMessageDialog(this, message);
  }

  public void updateSelectedPoint(Intersection intersection) {
    this.mapViewer.setRequestMarker(intersection.getGeoPosition());
    this.mapViewer.update();
  }

  public ButtonListener getButtonListener() {
    return buttonListener;
  }
}

