package com.pld.agile.controller;

import com.pld.agile.model.*;
import com.pld.agile.model.enums.TimeWindow;
import com.pld.agile.view.DeliveryRequestView;
import com.pld.agile.view.Window;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


public class DestinationSelectedStateTest {

  Controller controller;
  CityMap cityMap;
  DestinationSelectedState destinationSelectedState;
  Window window;
  Tour tour;

  @Before
  public void setUp() throws Exception {
    // Init values
    Intersection warehouse = new Intersection(0L, 0, 0);
    Intersection destinationPoint = new Intersection(1L, 1, 2);
    RoadSegment roadSegment = new RoadSegment("Rue de la Paix", 1.0, destinationPoint);
    RoadSegment roadSegment2 = new RoadSegment("Rue de la Paix", 1.0, warehouse);
    warehouse.addOutgoingSegment(roadSegment);
    destinationPoint.addOutgoingSegment(roadSegment2);

    Courier courier = new Courier(0);
    tour = new Tour();
    tour.setCourier(courier);

    // Mock the cityMap
    cityMap = mock(CityMap.class);
    when(cityMap.getWarehouse()).thenReturn(warehouse);
    when(cityMap.getTour(courier)).thenReturn(tour);
    doAnswer(invocation -> {
      Object[] args = invocation.getArguments();
      tour = (Tour) args[0];
      return null;
    }).when(cityMap).updateTour(any(Tour.class));

    // Mock the Controller
    controller = mock(Controller.class);

    // Mock the window
    window = mock(Window.class);
    DeliveryRequestView deliveryRequestView = mock(DeliveryRequestView.class);
    JComboBox<Courier> comboBoxCourier = mock(JComboBox.class);
    JComboBox<TimeWindow> comboBoxTimeWindow = mock(JComboBox.class);
    when(window.getDeliveryRequestView()).thenReturn(deliveryRequestView);
    deliveryRequestView.comboBoxCourier = comboBoxCourier;
    deliveryRequestView.comboBoxTimeWindow = comboBoxTimeWindow;
    when(comboBoxCourier.getSelectedItem()).thenReturn(courier);
    when(comboBoxTimeWindow.getSelectedItem()).thenReturn(TimeWindow.TW_8_9);

    destinationSelectedState = new DestinationSelectedState();
    destinationSelectedState.setSelectedIntersection(destinationPoint);
  }

  @Test
  public void addNewRequestTest() {
    assertEquals(0, tour.getDeliveryRequests().size());
    assertEquals(0, tour.getIntersections().size());
    assertEquals((Integer)0, tour.getCourier().getCourierId());
    destinationSelectedState.addNewRequest(cityMap, controller, window);
    assertEquals(1, tour.getDeliveryRequests().size());
    assertEquals(4, tour.getIntersections().size());
    assertEquals((Integer)0, tour.getCourier().getCourierId());
  }
}
