package com.pld.agile.controller;

import com.pld.agile.model.CityMap;
import com.pld.agile.model.Intersection;
import com.pld.agile.view.Window;
import org.jxmapviewer.viewer.GeoPosition;

public class LoadedMapState implements State{

    @Override
    public void loadTours(CityMap cityMap, Controller c, Window w) {
        // Appel d'une fonction restore dans XMLDeserializer
        // try {
        //    XMLDeserialiser.load(path, intersections, cityMap);
        //} catch (ExceptionXML e) {
        //    throw new RuntimeException(e);
        //}

        c.setCurrentState(c.computedTourState);
    };

    @Override
    public void selectDestinationPoint(Controller c, Window window, GeoPosition position, CityMap cityMap) {
        c.setCurrentState(c.destinationSelectedState);
        Intersection selectedIntersection = cityMap.searchIntersection(position);
        c.destinationSelectedState.setSelectedIntersection(selectedIntersection);
        window.updateSelectedPoint(selectedIntersection);
    };

}
