package com.pld.agile.view;

import com.pld.agile.model.DeliveryRequest;
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

    /** Main label of the textual view */
    private JLabel viewTitle;

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

        deliveryRequestsPanel = new JPanel();
        deliveryRequestsPanel.setName("deliveryRequestsPanel");

        viewTitle = new JLabel("Deliveries");
        viewTitle.setHorizontalTextPosition(SwingConstants.CENTER);

        undo = new JButton("<");
        undo.setAlignmentX(Component.CENTER_ALIGNMENT);

        redo = new JButton(">");
        redo.setAlignmentX(Component.CENTER_ALIGNMENT);

        textViewHeadPanel.add(viewTitle);
        textViewHeadPanel.add(undo);
        textViewHeadPanel.add(redo);

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
        this.add(deliveryRequestsPanel, constraints);
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
        deliveryRequestsPanel.setLayout(new GridLayout(deliveryRequests.size(), 1));
    }

    /**
     * Updates the display of the delivery requests in the GUI.
     */
    private void paintRequests() {
        if(!deliveryRequests.isEmpty()) {
            Border deliveryPanelBorder = BorderFactory.createRaisedBevelBorder();
            int requestsCounter = 1;
            for(DeliveryRequest request : deliveryRequests) {
                JPanel requestPanel = new JPanel();
                JLabel requestTag = new JLabel();
                JLabel requestTime = new JLabel();

                requestPanel.setLayout(new BoxLayout(requestPanel, BoxLayout.PAGE_AXIS));
                requestPanel.setBorder(deliveryPanelBorder);
                requestPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

                requestTag.setText("Delivery request n°" + requestsCounter);
                requestTime.setText("Time Window: [" + request.getTimeWindow().getStart() + " - " +request.getTimeWindow().getEnd() + "]");

                //DEBUG
                /*System.out.println("Added delivery request: name:" + requestTag.getText() + " ; timeW:" + requestTime.getText());*/

                requestPanel.add(requestTag);
                requestPanel.add(requestTime);
                deliveryRequestsPanel.add(requestPanel);

                requestPanel.setPreferredSize(new Dimension(requestPanel.getParent().getWidth(), requestPanel.getParent().getHeight() / 10));
                requestPanel.revalidate();

                ++requestsCounter;
            }
            deliveryRequestsPanel.revalidate();
            deliveryRequestsPanel.repaint();
        }
    }
}
