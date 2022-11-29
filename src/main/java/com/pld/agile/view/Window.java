package com.pld.agile.view;

import com.pld.agile.model.CityMap;

import javax.swing.*;
import java.awt.*;

public class Window extends JFrame {
  public Window() {
    super("Delivery Manager");
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setResizable(false);
    Dimension screenDimensions = Toolkit.getDefaultToolkit().getScreenSize();
    this.setSize((int)(screenDimensions.width * 0.9), (int)(screenDimensions.height * 0.9));

    int x = (int) ((screenDimensions.getWidth() - this.getWidth()) / 2);
    int y = (int) ((screenDimensions.getHeight() - this.getHeight()) / 2);
    this.setLocation(x, y);

  }
}

