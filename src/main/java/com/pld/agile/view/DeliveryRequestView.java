package com.pld.agile.view;

import com.pld.agile.model.Courier;
import com.pld.agile.model.Couriers;
import com.pld.agile.model.enums.TimeWindow;
import com.pld.agile.view.listener.ButtonListener;
import javax.swing.border.Border;
import java.awt.*;
import javax.swing.*;

public class DeliveryRequestView extends JPanel {
  
  public JComboBox<TimeWindow> comboBoxTimeWindow;
  public JComboBox<Courier> comboBoxCourier;
  public final static String ADD_DELIVERY_REQUEST = "Add a Delivery Request";

  public DeliveryRequestView(ButtonListener buttonListener) {
    Border loweredBorder = BorderFactory.createLoweredBevelBorder();
    this.setBorder(loweredBorder);
    this.setLayout(new GridBagLayout());

    Font titleFont = new Font("Courier", Font.BOLD, 25);

    JLabel title = new JLabel();
    title.setText("Add a new delivery request");
    title.setFont(titleFont);

    JLabel couriersLabel = new JLabel();
    couriersLabel.setText("for:");

    JLabel timeLabel = new JLabel();
    timeLabel.setText("in time window:");

    //Choices for courier
    comboBoxCourier = new JComboBox<>();
    ((JLabel) comboBoxCourier.getRenderer()).setHorizontalAlignment(JLabel.LEFT);
    for (Courier courier : Couriers.courierList) {
      comboBoxCourier.addItem(courier);
    }

    //Choices for time window
    comboBoxTimeWindow = new JComboBox<>();
    ((JLabel) comboBoxTimeWindow.getRenderer()).setHorizontalAlignment(JLabel.LEFT);
    for (TimeWindow timeWindow : TimeWindow.values()) {
      comboBoxTimeWindow.addItem(timeWindow);
    }

    JPanel courierPanel = new JPanel();
    courierPanel.setLayout(new FlowLayout());
    courierPanel.add(couriersLabel);
    courierPanel.add(comboBoxCourier);

    JPanel timePanel = new JPanel();
    timePanel.setLayout(new FlowLayout());
    timePanel.add(timeLabel);
    timePanel.add(comboBoxTimeWindow);

    JButton addDeliveryRequestBtn = new JButton("Add a Delivery Request");
    addDeliveryRequestBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
    addDeliveryRequestBtn.setActionCommand(ADD_DELIVERY_REQUEST);
    addDeliveryRequestBtn.addActionListener(buttonListener);

    GridBagConstraints constraints = new GridBagConstraints();

    //Add title
    constraints.gridx = 0;
    constraints.gridy = 0;
    constraints.weightx = 1;
    constraints.weighty = 1;
    constraints.gridwidth = 1;
    constraints.fill = GridBagConstraints.NONE;
    this.add(title, constraints);

    //Add courier choice
    constraints.gridx = 0;
    constraints.gridy = 1;
    constraints.weightx = 1;
    constraints.weighty = 1;
    constraints.gridwidth = 1;
    constraints.fill = GridBagConstraints.NONE;
    this.add(courierPanel, constraints);

    //Add time window choice
    constraints.gridx = 0;
    constraints.gridy = 2;
    constraints.weightx = 1;
    constraints.weighty = 1;
    constraints.gridwidth = 1;
    constraints.fill = GridBagConstraints.NONE;
    this.add(timePanel, constraints);

    //Add button
    constraints.gridx = 0;
    constraints.gridy = 3;
    constraints.weightx = 1;
    constraints.weighty = 1;
    constraints.gridwidth = 1;
    constraints.fill = GridBagConstraints.NONE;
    this.add(addDeliveryRequestBtn, constraints);
  }
}