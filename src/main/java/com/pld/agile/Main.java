package com.pld.agile;

import com.pld.agile.view.DeliveriesView;
import com.pld.agile.view.Window;

import javax.swing.*;

public class Main {
  public static void main(String[] args) {
    JFrame view = new Window();
    DeliveriesView deliveriesPanel = new DeliveriesView();

    view.add(deliveriesPanel.getPanel1());
    view.setVisible(true);
  }

}
