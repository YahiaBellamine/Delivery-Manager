package com.pld.agile.controller;

import com.pld.agile.model.CityMap;
import com.pld.agile.view.Window;

public class LoadedMapState implements State{

    @Override
    public void restoreTours(CityMap cityMap, Controller c, Window w) {
        // Appel d'une fonction restore dans XMLDeserializer
        // try {
        //    XMLDeserialiser.load(path, intersections, cityMap);
        //} catch (ExceptionXML e) {
        //    throw new RuntimeException(e);
        //}

        c.setCurrentState(c.computedTourState);
    };

    @Override
    public void selectDestinationPoint(Controller c, Long destinationPointId) {
        c.setCurrentState(c.destinationSelectedState);
        c.destinationSelectedState.setDestinationPointId(destinationPointId);
    };

}
