package com.pld.agile.controller;

import com.pld.agile.model.CityMap;
import com.pld.agile.view.Window;

public class ComputedTourState implements State{
    @Override
    public void loadMap(Controller c, Window w) {
        c.setCurrentState(c.loadedMapState);
    };

    @Override
    public void deleteDeliveryRequest(CityMap cityMap, Window w) {
    };

    @Override
    public void saveTours(CityMap cityMap, Window w) {};

    @Override
    public void updateDeliveryRequest(CityMap cityMap, Window w) {};

    @Override
    public void selectDestinationPoint(Controller c, Window w) {
        c.setCurrentState(c.destinationSelectedState);
    };


}
