package com.pld.agile.view.listener;

import com.pld.agile.controller.Controller;
import com.pld.agile.view.Window;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonListener implements ActionListener {

  private Controller controller;

  public ButtonListener(Controller controller){
    this.controller = controller;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    // Method called by the button listener each time a button is clicked
    // Forward the corresponding message to the controller
    switch (e.getActionCommand()){
      case Window.LOAD_MAP:
        controller.loadMap();
        break;
      case Window.ADD_DELIVERY_REQUEST:
        controller.addDeliveryRequest();
        break;
    }
  }
}
