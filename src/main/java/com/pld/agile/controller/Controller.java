package com.pld.agile.controller;

import com.pld.agile.model.CityMap;
import com.pld.agile.model.DeliveryRequest;
import com.pld.agile.view.Window;

public class Controller {

  private Window window;
  private CityMap cityMap;

  public Controller(CityMap map) {
    window = new Window();
  }

  public void addDeliveryRequest(DeliveryRequest deliveryRequest) {
    // cityMap.addDeliveryRequest(deliveryRequest);
  }

  public void loadMap(String path) {
    // cityMap.loadMap(path);
  }

  public void deleteDeliveryRequest(DeliveryRequest deliveryRequest) {
    // cityMap.deleteDeliveryRequest(deliveryRequest);
  }

}
