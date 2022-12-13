package com.pld.agile.view;

import com.pld.agile.model.CityMap;
import com.pld.agile.model.DeliveryRequest;
import com.pld.agile.model.Tour;
import com.pld.agile.observer.Observable;
import com.pld.agile.observer.Observer;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;
import javax.swing.border.Border;
import javax.swing.*;
import java.awt.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DeliveriesView extends JPanel implements Observer {

    /** Scrollable panel to display the delivery requests of a tour */
    private JScrollPane requestsScrollPane;

    /** Tree to display the information about all the tours and their delivery requests */
    private JTree toursTree;

    /** Panel to display details about delivery requests or tours */
    private JPanel detailsPanel;

    /** the map */
    private CityMap cityMap;

    public static final String UPDATE_DELIVERY_REQUEST = "Update a delivery request";
    public static final String DELETE_DELIVERY_REQUEST = "Delete a delivery request";

    /**
     * Constructor of the textual view.
     */
    public DeliveriesView(CityMap cityMap, Window window) {
        Border textViewBorder = BorderFactory.createLoweredBevelBorder();
        this.setLayout(new GridBagLayout());
        this.setName("textViewPanel");
        this.setBorder(textViewBorder);
        this.setMaximumSize(new Dimension(window.getMaximumSize().width / 5, window.getMaximumSize().height));

        GridBagConstraints constraints = new GridBagConstraints();

        requestsScrollPane = new JScrollPane();
        requestsScrollPane.setName("toursScrollPane");
        requestsScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        requestsScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        requestsScrollPane.setSize(this.getWidth(), this.getHeight());

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 0.3;
        constraints.weighty = 0.6;
        constraints.gridwidth = 3;
        constraints.fill = GridBagConstraints.BOTH;
        this.add(requestsScrollPane, constraints);

        //Details panel
        detailsPanel = new JPanel();
        detailsPanel.setName("detailsPane");
        detailsPanel.setMaximumSize(new Dimension(this.getMaximumSize().width / 2, this.getMaximumSize().height / 2));
        detailsPanel.setPreferredSize(new Dimension(this.getMaximumSize().width / 2, this.getMaximumSize().height / 2));
        detailsPanel.setLayout(new GridBagLayout());

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.weightx = 0.2;
        constraints.weighty = 0.4;
        constraints.gridwidth = 3;
        constraints.fill = GridBagConstraints.BOTH;
        this.add(detailsPanel, constraints);

        //Constraints to add this panel to the main view
        constraints.gridx = 4;
        constraints.gridy = 1;
        constraints.weightx = 0.3;
        constraints.weighty = 0.99;
        constraints.gridwidth = 1;
        constraints.fill = GridBagConstraints.BOTH;
        window.add(this, constraints);

        cityMap.addObserver(this);

        this.cityMap = cityMap;
    }

    /**
     * Method triggered by an update in the model class CityMap.
     * @param o
     * @param arg
     */
    public void update(Observable o, Object arg){
        repaint();
    }

    /**
     * Repaint the component.
     */
    @Override
    public void repaint() {
        updateTree();
        updateDetails(null);
    }

    /**
     * Update the tree view depending on the CityMap
     */
    private void updateTree() {
        if(cityMap != null) {
            DefaultMutableTreeNode treeRoot = new DefaultMutableTreeNode("Tours information");
            int indexCourier = 1;
            for (Tour tour : cityMap.getTourList()) {
                if(tour != null) {
                    DefaultMutableTreeNode treeTour = new DefaultMutableTreeNode("Courier n°" + indexCourier);
                    int indexDelivery = 1;
                    for (DeliveryRequest deliveryRequest : tour.getDeliveryRequests()) {
                        String deliveryString = "Delivery n°" + indexDelivery;
                        if((deliveryRequest.getArrivalTime() > deliveryRequest.getTimeWindow().getEnd()) || (deliveryRequest.getArrivalTime() < deliveryRequest.getTimeWindow().getStart())) {
                            deliveryString += "(Out of Time Window!)";
                        }
                        DefaultMutableTreeNode treeLeaf = new DefaultMutableTreeNode(deliveryString);

                        //Check if the courier has to wait
                        String previousNode = (String) treeTour.getLastLeaf().getUserObject();
                        if(previousNode.startsWith("Delivery")) {
                            int previousDeliveryNumber = treeTour.getLastLeaf().getParent().getIndex(treeTour.getLastLeaf());
                            DeliveryRequest previousDelivery = cityMap.getTourList().get(indexCourier - 1).getDeliveryRequests().get(previousDeliveryNumber);
                            //5 minutes = 0.08
                            double departureTime = previousDelivery.getArrivalTime() + 0.08;
                            double arrivalTime = deliveryRequest.getArrivalTime();
                            double waitingTime = arrivalTime - departureTime;
                            //If the courier has to wait more than 30 minutes we notify it in the tree
                            if(waitingTime > 0.5) {
                                //Format the waiting time for display
                                double temp = waitingTime;
                                int hours = (int) temp;
                                temp -= hours;
                                int minutes = (int) (temp * 60);
                                temp = temp * 60 - minutes;
                                int seconds = (int) (temp * 60);
                                DefaultMutableTreeNode waitLeaf = new DefaultMutableTreeNode("Wait for: " + hours + "h" + minutes + "min" + seconds + "s");
                                treeTour.add(waitLeaf);
                            }
                        }
                        treeTour.add(treeLeaf);
                        ++indexDelivery;
                    }
                    treeRoot.add(treeTour);
                    ++indexCourier;
                }
            }
            toursTree = new JTree(treeRoot);
            toursTree.setMaximumSize(this.getMaximumSize());
            toursTree.setMinimumSize(this.getMaximumSize());
            toursTree.setPreferredSize(this.getMaximumSize());
            toursTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
            toursTree.addTreeSelectionListener(new TreeSelectionListener() {
                public void valueChanged(TreeSelectionEvent e) {
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode)toursTree.getLastSelectedPathComponent();
                    //If nothing is selected
                    if (node == null) {
                        return;
                    }
                    updateDetails(node);
                }
            });
            toursTree.setCellRenderer(new DeliveriesTreeCellRenderer());
            //Expand the tree
            for (int i = 0; i < toursTree.getRowCount(); i++) {
                toursTree.expandRow(i);
            }
            requestsScrollPane.setViewportView(toursTree);
        }
    }

    /**
     * Update the details panel depending on the selected node in the tree.
     * @param detail The object we need the details to be displayed.
     */
    public void updateDetails(Object detail) {
        if(detailsPanel != null) {
            Font titleFont = new Font("Courier", Font.BOLD, 15);
            if (detail != null) {
                for (Component component : detailsPanel.getComponents()) {
                    detailsPanel.remove(component);
                }

                String node = (String) ((DefaultMutableTreeNode) detail).getUserObject();

                if (node.startsWith("Delivery n°")) {
                    Pattern nodeNameToNumber = Pattern.compile("n°\\d+");

                    Matcher nameMatcher = nodeNameToNumber.matcher(node);
                    int deliveryNumber = -1;
                    if(nameMatcher.find()) {
                        Pattern numberToInt = Pattern.compile("\\d+");
                        Matcher numberMatcher = numberToInt.matcher(nameMatcher.group(0));

                        if(numberMatcher.find()) {
                            deliveryNumber = Integer.parseInt(numberMatcher.group(0));
                        }
                    }
                    /*TODO - implement proper error handling*/
                    else {
                        return;
                    }
                    int tourNumber = ((DefaultMutableTreeNode) detail).getParent().getParent().getIndex(((DefaultMutableTreeNode) detail).getParent());
                    DeliveryRequest delivery = cityMap.getTourList().get(tourNumber).getDeliveryRequests().get(deliveryNumber - 1);

                    JLabel detailsTitle = new JLabel("Tour n°" + (tourNumber + 1) + " - Delivery n°" + deliveryNumber);
                    detailsTitle.setName("detailsTitle");
                    detailsTitle.setFont(titleFont);

                    JLabel deliveryArrivalTime = new JLabel(" Arrival Time: " + delivery.getFormattedArrivalTime());
                    deliveryArrivalTime.setHorizontalTextPosition(SwingConstants.LEFT);

                    JLabel deliveryTimeWindow = new JLabel(" Time Window: " + delivery.getTimeWindow().getStart() + "h - " + delivery.getTimeWindow().getEnd() + "h");
                    deliveryTimeWindow.setName("detailsTimeWindow");
                    deliveryArrivalTime.setHorizontalTextPosition(SwingConstants.LEFT);

                    JLabel deliveryCourier = new JLabel(" Courier (id): " + cityMap.getTourList().get(tourNumber).getCourier().getCourierId());
                    deliveryCourier.setName("detailsCourier");
                    deliveryArrivalTime.setHorizontalTextPosition(SwingConstants.LEFT);

                    JButton update = new JButton("Update");
                    update.setActionCommand(UPDATE_DELIVERY_REQUEST);
                    Window parent = (Window)(this.getParent().getParent().getParent().getParent());
                    update.addActionListener(parent.getButtonListener());

                    JButton delete = new JButton("Delete");
                    delete.setActionCommand(DELETE_DELIVERY_REQUEST);
                    delete.addActionListener(parent.getButtonListener());

                    GridBagConstraints constraints = new GridBagConstraints();

                    constraints.gridx = 1;
                    constraints.gridy = 0;
                    constraints.weightx = 0.2;
                    constraints.weighty = 1;
                    constraints.gridwidth = 2;
                    constraints.fill = GridBagConstraints.NONE;
                    detailsPanel.add(detailsTitle, constraints);

                    constraints.gridx = 0;
                    constraints.gridy = 1;
                    constraints.weightx = 0.2;
                    constraints.weighty = 1;
                    constraints.gridwidth = 2;
                    constraints.fill = GridBagConstraints.HORIZONTAL;
                    detailsPanel.add(deliveryArrivalTime, constraints);

                    constraints.gridx = 0;
                    constraints.gridy = 2;
                    constraints.weightx = 0.2;
                    constraints.weighty = 1;
                    constraints.gridwidth = 2;
                    constraints.fill = GridBagConstraints.HORIZONTAL;
                    detailsPanel.add(deliveryTimeWindow, constraints);

                    constraints.gridx = 0;
                    constraints.gridy = 3;
                    constraints.weightx = 0.2;
                    constraints.weighty = 1;
                    constraints.gridwidth = 2;
                    constraints.fill = GridBagConstraints.HORIZONTAL;
                    detailsPanel.add(deliveryCourier, constraints);

                    constraints.gridx = 0;
                    constraints.gridy = 5;
                    constraints.weightx = 0.2;
                    constraints.weighty = 1;
                    constraints.gridwidth = 2;
                    constraints.fill = GridBagConstraints.NONE;
                    detailsPanel.add(update, constraints);

                    constraints.gridx = 2;
                    constraints.gridy = 5;
                    constraints.weightx = 0.2;
                    constraints.weighty = 1;
                    constraints.gridwidth = 2;
                    constraints.fill = GridBagConstraints.NONE;
                    detailsPanel.add(delete, constraints);
                }

                if (node.startsWith("Courier")) {
                    int tourNumber = ((DefaultMutableTreeNode) detail).getParent().getIndex((DefaultMutableTreeNode) detail);

                    JLabel tourDuration = new JLabel(" Tour Duration: " + cityMap.getTourList().get(tourNumber).getFormattedTourDuration());
                    tourDuration.setFont(titleFont);
                    tourDuration.setHorizontalTextPosition(SwingConstants.CENTER);

                    GridBagConstraints constraints = new GridBagConstraints();

                    constraints.gridx = 0;
                    constraints.gridy = 0;
                    constraints.weightx = 1;
                    constraints.weighty = 1;
                    constraints.gridwidth = 4;
                    constraints.fill = GridBagConstraints.BOTH;
                    detailsPanel.add(tourDuration, constraints);
                }
            }
            detailsPanel.revalidate();
        }
    }
}
