package com.pld.agile.view;

import com.pld.agile.model.enums.TimeWindow;

import java.awt.*;
import javax.swing.*;

public class DeliveryRequestView extends JPanel{

  JPanel subPanelRequest;

  JLabel selectDestinationPoint;
  public JComboBox<TimeWindow> comboBoxTimeWindow;

  public DeliveryRequestView(){
    this.setLayout(new BorderLayout());

    this.subPanelRequest = new JPanel(new BorderLayout());
    this.subPanelRequest.setBackground(Color.white);

    GridLayout layout = new GridLayout(3,2);
    layout.setVgap(30);
    this.subPanelRequest.setLayout(layout);


    JLabel enterRequest = new JLabel("  Enter you new delivery request:");
    enterRequest.setBackground(Color.gray);
    this.subPanelRequest.add(enterRequest);

    JPanel emptyPanel = new JPanel(new BorderLayout());
    emptyPanel.setBackground(Color.white);
    this.subPanelRequest.add(emptyPanel);


    JComboBox<String> comboBoxCourier = new JComboBox<>();
    ((JLabel) comboBoxCourier.getRenderer()).setHorizontalAlignment(JLabel.LEFT);
    comboBoxCourier.addItem("Courier 1");

    JLabel enterCourier = new JLabel("  Select a courier:");
    enterCourier.setBackground(Color.gray);
    this.subPanelRequest.add(enterCourier);
    this.subPanelRequest.add(comboBoxCourier);


    comboBoxTimeWindow = new JComboBox<TimeWindow>();
    ((JLabel) comboBoxTimeWindow.getRenderer()).setHorizontalAlignment(JLabel.LEFT);
    comboBoxTimeWindow.addItem(TimeWindow.TW_8_9);
    comboBoxTimeWindow.addItem(TimeWindow.TW_9_10);
    comboBoxTimeWindow.addItem(TimeWindow.TW_10_11);
    comboBoxTimeWindow.addItem(TimeWindow.TW_11_12);



    JLabel enterTimeWindow = new JLabel("  Select a TimeWindow:");
    enterTimeWindow.setBackground(Color.gray);
    this.subPanelRequest.add(enterTimeWindow);
    this.subPanelRequest.add(comboBoxTimeWindow);

    selectDestinationPoint = new JLabel("  Select your destination point on the map.");

    JPanel subPanel = new JPanel(new BorderLayout());
    subPanel.add(subPanelRequest, BorderLayout.NORTH);
    subPanel.add(selectDestinationPoint, BorderLayout.CENTER);
    add(subPanel, BorderLayout.CENTER);
  }

  public void setSelectDestinationPoint(String text){
    this.selectDestinationPoint.setText(text);
  }

}