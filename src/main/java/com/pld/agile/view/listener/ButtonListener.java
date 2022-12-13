package com.pld.agile.view.listener;

import com.pld.agile.controller.Controller;
import com.pld.agile.view.DeliveriesView;
import com.pld.agile.view.Window;
import com.pld.agile.view.map.MapViewer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.*;
import java.awt.*;

public class ButtonListener implements ActionListener {

  private final Controller controller;
  private final MapViewer mapViewer;

  public ButtonListener(Controller controller, MapViewer mapViewer) {
    this.controller = controller;
    this.mapViewer = mapViewer;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    // Method called by the button listener each time a button is clicked
    // Forward the corresponding message to the controller
    switch (e.getActionCommand()) {
      case Window.LOAD_MAP:
        controller.loadMap();
        break;

      case Window.ADD_DELIVERY_REQUEST:
        controller.addNewRequest();
        break;

      case Window.SAVE_TOURS:
        controller.saveTours()/*System.out.println("Save tours call")*/;
        break;

      case Window.LOAD_TOURS:
        controller.loadTours()/*System.out.println("Load tours call")*/;
        break;

      case Window.RECENTER_MAP:
        mapViewer.recenter();
        break;

      case Window.ADD_COURIER:
        /* controller.addCourier()*/System.out.println("Add a courier");
        break;

      case DeliveriesView.UPDATE_DELIVERY_REQUEST:
        /*controller.updateDeliveryRequest()*/
        System.out.println("Update a delivery request");
        break;

      case DeliveriesView.DELETE_DELIVERY_REQUEST:
        int result = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this delivery request ?", "Confirm delete", JOptionPane.YES_NO_OPTION);
        //Validate delete
        if(result == 0) {
          System.out.println("Delete a delivery request");

          JComponent source = (JComponent) e.getSource();
          Container sourceParent = source.getParent();
          System.out.println(sourceParent.getName());
          Component[] components = sourceParent.getComponents();
          int deliveryNumber = -1;
          int courierId = -1;
          for(Component component : components) {
            if(component.getName() == "detailsTitle") {
              Pattern stringToNumber = Pattern.compile("Delivery n°\\d+");
              Matcher nameMatcher = stringToNumber.matcher(((JLabel) component).getText());

              if(nameMatcher.find()) {
                Pattern numberToInt = Pattern.compile("\\d+");
                Matcher deliveryMatcher = numberToInt.matcher(nameMatcher.group(0));
                System.out.println(nameMatcher.group(0));

                if (deliveryMatcher.find()) {
                  deliveryNumber = Integer.parseInt(deliveryMatcher.group(0));
                }
              }
            }
            if(component.getName() == "detailsCourier") {
              Pattern stringToNumber = Pattern.compile("Courier (id): \\d+");
              Matcher nameMatcher = stringToNumber.matcher(((JLabel) component).getText());

              if(nameMatcher.find()) {
                Pattern numberToInt = Pattern.compile("\\d+");
                Matcher deliveryMatcher = numberToInt.matcher(nameMatcher.group(0));
                System.out.println(nameMatcher.group(0));

                if (deliveryMatcher.find()) {
                  courierId = Integer.parseInt(deliveryMatcher.group(0));
                }
              }
            }
          }
          //controller.deleteDeliveryRequest(courierId, deliveryNumber);
        }
      break;
    }
  }
}
