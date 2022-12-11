package com.pld.agile.controller;

import com.pld.agile.model.CityMap;
import com.pld.agile.model.Intersection;
import com.pld.agile.view.Window;
import org.jxmapviewer.viewer.GeoPosition;

public class ComputedTourState implements State{

    @Override
    public void deleteDeliveryRequest(CityMap cityMap, Window w) {
    };

    @Override
    public void saveTours(CityMap cityMap, Window w) {};

    @Override
    public void updateDeliveryRequest(CityMap cityMap, Window w) {};

    @Override
    public void selectDestinationPoint(Controller c, Window window, GeoPosition position, CityMap cityMap) {
        c.setCurrentState(c.destinationSelectedState);
        Intersection selectedIntersection = cityMap.searchIntersection(position);
        c.destinationSelectedState.setSelectedIntersection(selectedIntersection);
        window.updateSelectedPoint(selectedIntersection);
    };

}
