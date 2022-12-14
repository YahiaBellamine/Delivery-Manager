package com.pld.agile.controller;

import com.pld.agile.model.*;
import com.pld.agile.model.enums.TimeWindow;
import com.pld.agile.view.DeliveryRequestView;
import com.pld.agile.view.Window;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ComputedTourStateTest {

  Controller controller = mock(Controller.class);
  CityMap cityMap;
  ComputedTourState computedTourState;
  Tour tour;
  Courier courier;
  Window window;

  @Before
  public void setUp() throws Exception {
    // Init values
    Intersection warehouse = new Intersection(0L, 0, 0);
    Intersection destinationPoint = new Intersection(1L, 1, 2);
    RoadSegment roadSegment = new RoadSegment("Rue de la Paix", 1.0, destinationPoint);
    RoadSegment roadSegment2 = new RoadSegment("Rue de la Paix", 1.0, warehouse);
    warehouse.addOutgoingSegment(roadSegment);
    destinationPoint.addOutgoingSegment(roadSegment2);

    courier = new Courier(0);
    tour = new Tour();
    tour.setCourier(courier);
    tour.addIntersection(warehouse);
    tour.addIntersection(destinationPoint);
    DeliveryRequest deliveryRequest = new DeliveryRequest(TimeWindow.TW_8_9, destinationPoint);
    DeliveryRequest deliveryRequest2 = new DeliveryRequest(TimeWindow.TW_8_9, destinationPoint);
    tour.addDeliveryRequest(deliveryRequest);
    tour.addDeliveryRequest(deliveryRequest2);

    // Mock the cityMap
    cityMap = mock(CityMap.class);
    when(cityMap.getWarehouse()).thenReturn(warehouse);
    when(cityMap.getTour(courier)).thenReturn(tour);
    when(cityMap.getTourList()).thenReturn(List.of(new Tour[]{tour}));
    doAnswer(invocation -> {
      Object[] args = invocation.getArguments();
      tour = (Tour) args[0];
      return null;
    }).when(cityMap).updateTour(any(Tour.class));

    // Mock the window
    window = mock(Window.class);
    DeliveryRequestView deliveryRequestView = mock(DeliveryRequestView.class);
    JComboBox<Courier> comboBoxCourier = mock(JComboBox.class);
    JComboBox<TimeWindow> comboBoxTimeWindow = mock(JComboBox.class);
    when(window.getDeliveryRequestView()).thenReturn(deliveryRequestView);
    deliveryRequestView.comboBoxCourier = comboBoxCourier;
    deliveryRequestView.comboBoxTimeWindow = comboBoxTimeWindow;
    when(comboBoxCourier.getSelectedItem()).thenReturn(courier);
    when(comboBoxTimeWindow.getSelectedItem()).thenReturn(TimeWindow.TW_9_10);

    computedTourState = new ComputedTourState();
  }

  @Test
  public void deleteDeliveryRequestTest() {
    assertEquals(2, tour.getDeliveryRequests().size());
    computedTourState.deleteDeliveryRequest(cityMap, courier, 0);
    assertEquals(1, tour.getDeliveryRequests().size());
    computedTourState.deleteDeliveryRequest(cityMap, courier, 0);
    assertEquals(0, tour.getDeliveryRequests().size());
  }

  @Test
  public void saveToursTest() {
    // computedTourState.saveTours(cityMap, window);
  }

  @Test
  public void loadToursTest() {
//    computedTourState.loadTours(cityMap, controller, window);
  }

  @Test
  public void updateDeliveryRequestTest() {
    // computedTourState.updateDeliveryRequest(cityMap, controller, window, courier, 0);
    // assertEquals(TimeWindow.TW_9_10, tour.getDeliveryRequests().get(0).getTimeWindow());
  }
}
