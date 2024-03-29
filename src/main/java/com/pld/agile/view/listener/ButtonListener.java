package com.pld.agile.view.listener;

import com.pld.agile.controller.Controller;
import com.pld.agile.model.Courier;
import com.pld.agile.model.Couriers;
import com.pld.agile.model.enums.TimeWindow;
import com.pld.agile.view.TextualView;
import com.pld.agile.view.DeliveryRequestView;
import com.pld.agile.view.Window;
import com.pld.agile.view.map.MapViewer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.*;
import java.awt.*;

/**
 * the button listener for all the buttons on the GUI
 */
public class ButtonListener implements ActionListener {

  /** the Controller instance */
  private final Controller controller;

  /** the graphical view */
  private final MapViewer mapViewer;

  /** the default constructor */
  public ButtonListener(Controller controller, MapViewer mapViewer) {
    this.controller = controller;
    this.mapViewer = mapViewer;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    // Method called by the button listener each time a button is clicked
    // Forward the corresponding message to the controller
    JComponent source = (JComponent) e.getSource();

    switch (e.getActionCommand()) {
      case Window.LOAD_MAP:
        controller.loadMap();
        break;

      case DeliveryRequestView.ADD_DELIVERY_REQUEST:
        controller.addNewRequest();
        break;

      case Window.SAVE_TOURS:
        controller.saveTours();
        break;

      case Window.LOAD_TOURS:
        controller.loadTours();
        break;

      case Window.RECENTER_MAP:
        mapViewer.recenter();
        break;

      case Window.ADD_COURIER:
        controller.addCourier();
        break;

      case TextualView.UPDATE_DELIVERY_REQUEST:
        JComboBox<TimeWindow> comboBoxTimeWindow = new JComboBox<>();
        ((JLabel) comboBoxTimeWindow.getRenderer()).setHorizontalAlignment(JLabel.LEFT);
        for(TimeWindow timeWindow : TimeWindow.values()) {
          comboBoxTimeWindow.addItem(timeWindow);
        }

        JComboBox<Courier> comboBoxCourier = new JComboBox<>();
        ((JLabel) comboBoxCourier.getRenderer()).setHorizontalAlignment(JLabel.LEFT);
        for(Courier courier : Couriers.courierList) {
          comboBoxCourier.addItem(courier);
        }

        final JComponent[] inputs = new JComponent[] {
                new JLabel("New Courier:"),
                comboBoxCourier,
                new JLabel("New Time Window:"),
                comboBoxTimeWindow
        };
        int result = JOptionPane.showConfirmDialog(null, inputs, "Update delivery request", JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
          //Get delivery and previous courier
          int deliveryId = getOriginDelivery(source);
          int previousCourierId = getOriginCourier(source);
          Courier previousCourier = Couriers.courierList.get(previousCourierId);

          //Get the new courier and time window
          Courier selectedCourier = (Courier) comboBoxCourier.getSelectedItem();
          TimeWindow selectedTimeWindow = (TimeWindow) comboBoxTimeWindow.getSelectedItem();
          //Change method signature so that it takes the previous courier, the new courier, the new time window and the delivery id(position in the tour list)
          controller.updateDeliveryRequest(selectedTimeWindow, selectedCourier, previousCourier, deliveryId);
        }
        break;

      case TextualView.DELETE_DELIVERY_REQUEST:
        int userChoice = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this delivery request ?", "Confirm delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        //Validate delete
        if(userChoice == JOptionPane.YES_OPTION) {
          int deliveryNumber = getOriginDelivery(source);
          int courierId = getOriginCourier(source);
          Courier deliveryCourier = Couriers.courierList.get(courierId);

          controller.deleteDeliveryRequest(deliveryCourier, deliveryNumber);
        }
        break;
    }
  }

  /**
   * Returns the delivery linked to an event source.
   * @param source The source we want to retrieve the delivery from.
   * @return The id of the delivery.
   */
  private int getOriginDelivery(JComponent source) {
    Container sourceParent = source.getParent().getParent();
    Component[] components = sourceParent.getComponents();
    int deliveryNumber = -1;
    for(Component component : components) {
      if (Objects.equals(component.getName(), "detailsTitle")) {
        Pattern stringToNumber = Pattern.compile("Delivery n°\\d+");
        Matcher nameMatcher = stringToNumber.matcher(((JLabel) component).getText());

        if (nameMatcher.find()) {
          Pattern numberToInt = Pattern.compile("\\d+");
          Matcher deliveryMatcher = numberToInt.matcher(nameMatcher.group(0));

          if (deliveryMatcher.find()) {
            deliveryNumber = Integer.parseInt(deliveryMatcher.group(0));
          }
        }
      }
    }
    return deliveryNumber-1;
  }

  /**
   * Returns the courier linked to an event source.
   * @param source The source we want to retrieve the courier from.
   * @return The id of the courier.
   */
  private int getOriginCourier(JComponent source) {
    Container sourceParent = source.getParent().getParent();
    Component[] components = sourceParent.getComponents();
    int courierId = -1;
    for(Component component : components) {
      if(Objects.equals(component.getName(), "detailsCourier")) {
        Pattern numberToInt = Pattern.compile("\\d+");
        Matcher courierMatcher = numberToInt.matcher(((JLabel) component).getText());

        if(courierMatcher.find()) {
          courierId = Integer.parseInt(courierMatcher.group(0));
        }
      }
    }
    return courierId-1;
  }
}
