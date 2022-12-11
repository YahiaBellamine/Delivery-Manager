package com.pld.agile.controller;

import com.pld.agile.model.CityMap;
import com.pld.agile.view.Window;

public class ComputedTourState implements State{

    @Override
    public void deleteDeliveryRequest(CityMap cityMap, Window w) {
    };

    @Override
    public void saveTours(CityMap cityMap, Window w) {};

    @Override
    public void updateDeliveryRequest(CityMap cityMap, Window w) {};

    @Override
    public void selectDestinationPoint(Controller c, Long destinationPointId) {
        c.setCurrentState(c.destinationSelectedState);
        c.destinationSelectedState.setDestinationPointId(destinationPointId);
        // TODO: update the view with observer pattern
    };

}
