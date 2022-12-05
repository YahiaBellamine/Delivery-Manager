package com.pld.agile.controller;

import com.pld.agile.model.CityMap;
import com.pld.agile.view.Window;

public interface State {
    public default void loadMap(Controller c, Window w) {};

    public default void selectDestinationPoint(Controller c, Window w) {};

    public default void addNewRequest(CityMap cityMap, Controller c, Window w) {};

    public default void restoreTours(CityMap cityMap, Controller c, Window w) {};

    public default void saveTours(CityMap cityMap, Window w) {};

    public default void deleteDeliveryRequest(CityMap cityMap, Window w) {};

    public default void updateDeliveryRequest(CityMap cityMap, Window w) {};
}


