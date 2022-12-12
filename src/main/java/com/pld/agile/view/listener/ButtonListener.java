package com.pld.agile.view.listener;

import com.pld.agile.controller.Controller;
import com.pld.agile.view.Window;
import com.pld.agile.view.map.MapViewer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
      case Window.LOAD_MAP -> controller.loadMap();
      case Window.ADD_DELIVERY_REQUEST -> controller.addNewRequest();
      case Window.SAVE_TOURS -> controller.saveTours()/*System.out.println("Save tours call")*/;
      case Window.LOAD_TOURS ->/* controller.loadTours()*/System.out.println("Load tours call");
      case Window.RECENTER_MAP -> mapViewer.recenter();
    }
  }
}
