package com.pld.agile.view;

import com.pld.agile.model.DeliveryRequest;
import com.pld.agile.model.Tour;

import javax.swing.border.Border;
import java.util.LinkedList;
import java.util.List;
import javax.swing.*;
import java.awt.*;

public class DeliveriesView extends JPanel {
    /** Panel used to display the head infos of the textual view */
    private JPanel textViewHeadPanel;

    /** Panel used to display the delivery requests of a tour */
    private JPanel deliveryRequestsPanel;

    /** Scrollable panel to display the delivery requests of a tour */
    private JScrollPane requestsScrollPane;

    /** Main label of the textual view */
    private JLabel viewTitle;

    /** Label for displaying tour duration */
    private JLabel tourDuration;

    /** Undo button */
    private JButton undo;

    /** Redo button */
    private JButton redo;

    /** List of delivery requests to display in the GUI */
    private List<DeliveryRequest> deliveryRequests;

    /**
     * Constructor of the textual view.
     */
    public DeliveriesView() {
        super();
        deliveryRequests = new LinkedList<>();

        Border textViewBorder = BorderFactory.createLoweredBevelBorder();
        this.setLayout(new GridBagLayout());
        this.setName("textViewPanel");
        this.setBorder(textViewBorder);

        textViewHeadPanel = new JPanel();
        textViewHeadPanel.setName("textViewHeadPanel");
        textViewHeadPanel.setLayout(new FlowLayout());

        tourDuration = new JLabel("");
        tourDuration.setHorizontalTextPosition(SwingConstants.CENTER);

        deliveryRequestsPanel = new JPanel();
        deliveryRequestsPanel.setName("deliveryRequestsPanel");

        requestsScrollPane = new JScrollPane();
        requestsScrollPane.setName("deliveryRequestsScrollPane");
        requestsScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        requestsScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        requestsScrollPane.setViewportView(deliveryRequestsPanel);

        viewTitle = new JLabel("Deliveries");
        viewTitle.setHorizontalTextPosition(SwingConstants.CENTER);

        undo = new JButton("<");
        undo.setAlignmentX(Component.CENTER_ALIGNMENT);

        redo = new JButton(">");
        redo.setAlignmentX(Component.CENTER_ALIGNMENT);

        textViewHeadPanel.add(viewTitle);
        textViewHeadPanel.add(undo);
        textViewHeadPanel.add(redo);
        textViewHeadPanel.add(tourDuration);

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weighty = 0.1;
        constraints.gridwidth = 3;
        constraints.fill = GridBagConstraints.BOTH;
        this.add(textViewHeadPanel, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.weighty = 0.9;
        constraints.gridwidth = 3;
        constraints.fill = GridBagConstraints.BOTH;
        this.add(requestsScrollPane, constraints);
    }

    /**
     * Changes the tour duration
     * @param tour - The new tour
     */
    public void displayTourDuration(Tour tour) {
        tourDuration.setText("Tour duration :"+ tour.getFormattedTourDuration());
    }

    /**
     * Changes the list of delivery requests to display for a new one.
     * @param requests - The new list of delivery requests to display.
     */
    public void displayRequests(List<DeliveryRequest> requests) {
        deliveryRequests.clear();
        deliveryRequests.addAll(requests);
        clearDeliveryGUI();
        updateDeliveryPanelLayout();
        paintRequests();
    }

    /**
     * Clears the GUI part where the list of delivery requests is displayed.
     */
    private  void clearDeliveryGUI() {
        Component[] guiDeliveries = deliveryRequestsPanel.getComponents();
        for(Component component : guiDeliveries) {
            deliveryRequestsPanel.remove(component);
        }
        deliveryRequestsPanel.revalidate();
        deliveryRequestsPanel.repaint();
    }

    /**
     * Updates the layout of the panel in charge to display the delivery requests list.
     * The new layout is updated depending on the number of deliveries to display.
     */
    private void updateDeliveryPanelLayout() {
        System.out.println("New layout - number of rows: " + deliveryRequests.size());
        deliveryRequestsPanel.setLayout(new GridBagLayout());
    }

    /**
     * Updates the display of the delivery requests in the GUI.
     */
    private void paintRequests() {
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
                /*System.out.println("Added delivery request: name:" + requestTag.getText() + " ; timeW:" + requestTime.getText());*/

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
    }
}
