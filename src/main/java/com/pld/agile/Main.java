package com.pld.agile;


import com.pld.agile.controller.Controller;
import java.awt.*;



public class Main {


  public static void main(String[] args) {
    EventQueue.invokeLater(new Runnable() {
      @Override
      public void run() {
        Controller controller = new Controller();
      }
    });

  }


}