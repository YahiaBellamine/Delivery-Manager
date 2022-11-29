package com.pld.agile;

import com.pld.agile.model.DeliveryRequest;
import com.pld.agile.model.Intersection;
import com.pld.agile.model.TimeWindow;
import com.pld.agile.view.DeliveriesView;
import com.pld.agile.view.Window;

import javax.swing.*;
import java.util.LinkedList;

public class Main {
  public static void main(String[] args) {
    LinkedList<DeliveryRequest> deliveries = new LinkedList<>();
    deliveries.add(new DeliveryRequest(new TimeWindow(7, 8), new Intersection((long)1, 53.5, 46.7, null)));
    deliveries.add(new DeliveryRequest(new TimeWindow(7, 8), new Intersection((long)2, 16.7, 22.9, null)));
    deliveries.add(new DeliveryRequest(new TimeWindow(8, 9), new Intersection((long)3, 76.8, 82.6, null)));
    deliveries.add(new DeliveryRequest(new TimeWindow(9, 10), new Intersection((long)4, 56.7, 42.4, null)));
    deliveries.add(new DeliveryRequest(new TimeWindow(10, 11), new Intersection((long)5, 64.9, 62.3, null)));

    deliveries.add(new DeliveryRequest(new TimeWindow(7, 8), new Intersection((long)6, 53.5, 46.7, null)));
    deliveries.add(new DeliveryRequest(new TimeWindow(7, 8), new Intersection((long)7, 16.7, 22.9, null)));
    deliveries.add(new DeliveryRequest(new TimeWindow(8, 9), new Intersection((long)8, 76.8, 82.6, null)));
    deliveries.add(new DeliveryRequest(new TimeWindow(9, 10), new Intersection((long)9, 56.7, 42.4, null)));
    deliveries.add(new DeliveryRequest(new TimeWindow(10, 11), new Intersection((long)10, 64.9, 62.3, null)));

    deliveries.add(new DeliveryRequest(new TimeWindow(7, 8), new Intersection((long)11, 53.5, 46.7, null)));
    deliveries.add(new DeliveryRequest(new TimeWindow(7, 8), new Intersection((long)12, 16.7, 22.9, null)));
    deliveries.add(new DeliveryRequest(new TimeWindow(8, 9), new Intersection((long)13, 76.8, 82.6, null)));
    deliveries.add(new DeliveryRequest(new TimeWindow(9, 10), new Intersection((long)14, 56.7, 42.4, null)));
    deliveries.add(new DeliveryRequest(new TimeWindow(10, 11), new Intersection((long)15, 64.9, 62.3, null)));

    deliveries.add(new DeliveryRequest(new TimeWindow(7, 8), new Intersection((long)16, 53.5, 46.7, null)));
    deliveries.add(new DeliveryRequest(new TimeWindow(7, 8), new Intersection((long)17, 16.7, 22.9, null)));
    deliveries.add(new DeliveryRequest(new TimeWindow(8, 9), new Intersection((long)18, 76.8, 82.6, null)));
    deliveries.add(new DeliveryRequest(new TimeWindow(9, 10), new Intersection((long)19, 56.7, 42.4, null)));
    deliveries.add(new DeliveryRequest(new TimeWindow(10, 11), new Intersection((long)20, 64.9, 62.3, null)));

    JFrame view = new Window();
    DeliveriesView deliveriesPanel = new DeliveriesView();
    deliveriesPanel.getTextViewPanel().setSize(view.getSize().width / 4, view.getSize().height);
    view.add(deliveriesPanel.getTextViewPanel());
    deliveriesPanel.displayRequests(deliveries);
    view.setVisible(true);
  }

}
