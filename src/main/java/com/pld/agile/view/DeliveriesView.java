package com.pld.agile.view;

import com.pld.agile.model.CityMap;
import com.pld.agile.model.DeliveryRequest;
import com.pld.agile.model.Tour;
import com.pld.agile.observer.Observable;
import com.pld.agile.observer.Observer;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.border.Border;
import javax.swing.*;
import java.awt.*;

public class DeliveriesView extends JPanel implements Observer {

    /** Scrollable panel to display the delivery requests of a tour */
    private JScrollPane requestsScrollPane;

    /** Tree to display the information about all the tours and their delivery requests */
    private JTree toursTree;

    /** the map */
    private CityMap cityMap;

    /**
     * Constructor of the textual view.
     */
    public DeliveriesView(CityMap cityMap, Window window) {
        super();

        Border textViewBorder = BorderFactory.createLoweredBevelBorder();
        this.setLayout(new GridBagLayout());
        this.setName("textViewPanel");
        this.setBorder(textViewBorder);
        this.setBackground(Color.CYAN);

        GridBagConstraints constraints = new GridBagConstraints();

        requestsScrollPane = new JScrollPane(toursTree);
        requestsScrollPane.setName("deliveryRequestsScrollPane");
        requestsScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        requestsScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        requestsScrollPane.setSize(this.getWidth(), this.getHeight());

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 1;
        constraints.weighty = 0.9;
        constraints.gridwidth = 3;
        constraints.fill = GridBagConstraints.BOTH;
        this.add(requestsScrollPane, constraints);

        // Constraints to add this panel to the main view
        constraints.gridx = 3;
        constraints.gridy = 0;
        constraints.weightx = 0.1;
        constraints.weighty = 1;
        constraints.gridwidth = 1;
        constraints.fill = GridBagConstraints.BOTH;

        this.setSize(window.getContentPane().getWidth() / 4, window.getContentPane().getHeight());
        window.getContentPane().add(this, constraints);

        cityMap.addObserver(this);

        this.cityMap = cityMap;
    }

    private void updateTree() {
        if(cityMap != null) {
            DefaultMutableTreeNode treeRoot = new DefaultMutableTreeNode("Tours information");
            int indexCourier = 1;
            for (Tour tour : cityMap.getTourList()) {
                if(tour != null) {
                    DefaultMutableTreeNode treeTour = new DefaultMutableTreeNode("Courier n°" + indexCourier);
                    int indexDelivery = 1;
                    for (DeliveryRequest deliveryRequest : tour.getDeliveryRequests()) {
                        treeTour.add(new DefaultMutableTreeNode("Delivery n°" + indexDelivery));
                        ++indexDelivery;
                    }
                    treeRoot.add(treeTour);
                    ++indexCourier;
                }
            }
            toursTree = new JTree(treeRoot);
            requestsScrollPane .setViewportView(toursTree);
            toursTree.revalidate();
            toursTree.repaint();
            requestsScrollPane.revalidate();
            requestsScrollPane.repaint();
        }
    }

    public void update(Observable o, Object arg){
        repaint();
    }

    /**
     * Changes the tour duration
     * @param //tour - The new tour
     */
    /*public void displayTourDuration(Tour tour) {
        tourDuration.setText("Tour duration :"+ tour.getFormattedTourDuration());
    }*/

    @Override
    public void repaint() {
        updateTree();
    }
    /* TODO : Use deliveryRequests as an attribute of tour to display them in the textual view  */
    /**
     * Changes the list of delivery requests to display for a new one.
     * @param requests - The new list of delivery requests to display.
     */
    /*public void displayRequests(List<DeliveryRequest> requests) {
        deliveryRequests.clear();
        deliveryRequests.addAll(requests);
        clearDeliveryGUI();
        updateDeliveryPanelLayout();
        paintRequests();
    }*/

    /**
     * Clears the GUI part where the list of delivery requests is displayed.
     */
    /*private  void clearDeliveryGUI() {
        Component[] guiDeliveries = deliveryRequestsPanel.getComponents();
        for(Component component : guiDeliveries) {
            deliveryRequestsPanel.remove(component);
        }
        deliveryRequestsPanel.revalidate();
        deliveryRequestsPanel.repaint();
    }*/

    /**
     * Updates the layout of the panel in charge to display the delivery requests list.
     * The new layout is updated depending on the number of deliveries to display.
     */
    /*private void updateDeliveryPanelLayout() {
        System.out.println("New layout - number of rows: " + deliveryRequests.size());
        deliveryRequestsPanel.setLayout(new GridBagLayout());
    }*/

    /**
     * Updates the display of the delivery requests in the GUI.
     */
   /* private void paintRequests() {
        if(!deliveryRequests.isEmpty()) {
            Border deliveryPanelBorder = BorderFactory.createRaisedBevelBorder();

            GridBagConstraints constraints = new GridBagConstraints();
            constraints.gridx = 0;
            constraints.weightx = 1;
            constraints.weighty = 1 / deliveryRequests.size();
            constraints.gridwidth = 1;
            constraints.fill = GridBagConstraints.HORIZONTAL;

            int requestsCounter = 1;
            for(DeliveryRequest request : deliveryRequests) {
                JPanel requestPanel = new JPanel();
                JLabel requestTag = new JLabel();
                JLabel requestTime = new JLabel();
                JLabel requestArrivalTime = new JLabel();

                requestPanel.setLayout(new BoxLayout(requestPanel, BoxLayout.PAGE_AXIS));
                requestPanel.setBorder(deliveryPanelBorder);
                requestPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
                requestPanel.setAlignmentY(Component.TOP_ALIGNMENT);

                requestTag.setText("Delivery request n°" + requestsCounter);
                requestTime.setText("Time Window: [" + request.getTimeWindow().getStart() + " - " +request.getTimeWindow().getEnd() + "]");
                requestArrivalTime.setText("Arrival time: " + request.getFormattedArrivalTime());

                //DEBUG
                *//*System.out.println("Added delivery request: name:" + requestTag.getText() + " ; timeW:" + requestTime.getText());*//*

                requestPanel.add(requestTag);
                requestPanel.add(requestTime);
                requestPanel.add(requestArrivalTime);

                constraints.gridy = requestsCounter - 1;
                deliveryRequestsPanel.add(requestPanel, constraints);

                requestPanel.setPreferredSize(new Dimension(requestPanel.getParent().getWidth(), requestPanel.getParent().getHeight() / 10));
                requestPanel.revalidate();

                ++requestsCounter;
            }
            requestsScrollPane.setViewportView(deliveryRequestsPanel);
            requestsScrollPane.revalidate();
            requestsScrollPane.repaint();
            deliveryRequestsPanel.revalidate();
            deliveryRequestsPanel.repaint();
        }
    }*/
}
