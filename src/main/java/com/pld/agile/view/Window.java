package com.pld.agile.view;

import com.pld.agile.model.CityMap;

import javax.swing.*;

public class Window extends JFrame {
  public Window() {
    super("PLD Agile");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(800, 600);
    setLayout(null);

    JPanel panel = new JPanel();
    panel.setBounds(0, 0, 100, 100);
    panel.setBackground(java.awt.Color.RED);
    this.add(panel);
    this.setVisible(true);
  }
}

