package com.pld.agile.controller;

import com.pld.agile.model.CityMap;
import com.pld.agile.view.Window;

public class DestinationSelectedState implements State{

  @Override
  public void selectDestinationPoint(Controller c, Window w) {
    // TODO:
    // No change in state needed
  }

  @Override
  public void addNewRequest(CityMap cityMap, Controller c, Window w) {
    // TODO:
    c.setCurrentState(c.computedTourState);
  }

  @Override
  public void loadMap(Controller c, Window w) {
    c.setCurrentState(c.loadedMapState);
  }
}
