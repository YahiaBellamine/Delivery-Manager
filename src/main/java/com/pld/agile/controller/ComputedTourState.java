package com.pld.agile.controller;

import com.pld.agile.model.CityMap;
import com.pld.agile.model.Courier;
import com.pld.agile.model.Intersection;
import com.pld.agile.model.Tour;
import com.pld.agile.model.enums.TimeWindow;
import com.pld.agile.utils.Algorithm;
import com.pld.agile.view.Window;
import org.jxmapviewer.viewer.GeoPosition;

public class ComputedTourState implements State{

    @Override
    public void deleteDeliveryRequest(CityMap cityMap, Controller controller, Courier courier, int indexDeliveryRequest) {
        Tour tour = cityMap.getTour(courier);
        if(tour != null && tour.getDeliveryRequests().size() > indexDeliveryRequest) {
            tour.removeDeliveryRequest(indexDeliveryRequest);

            Tour optimalTour = Algorithm.ExecuteAlgorithm(cityMap.getWarehouse(), tour.getDeliveryRequests());
            optimalTour.setCourier(courier);
            cityMap.updateTourList(optimalTour);
        }

        controller.setCurrentState(controller.computedTourState);
    };

    @Override
    public void saveTours(CityMap cityMap, Window w) {};

    @Override
    public void updateDeliveryRequest(CityMap cityMap, Controller controller, Window window, Courier previousCourier, int indexDeliveryRequest) {
        TimeWindow newTimeWindow = (TimeWindow) window.getDeliveryRequestView().comboBoxTimeWindow.getSelectedItem();
        Courier newCourier = (Courier) window.getDeliveryRequestView().comboBoxCourier.getSelectedItem();

        Tour tour = cityMap.getTour(previousCourier);

        if(tour != null && tour.getDeliveryRequests().size() > indexDeliveryRequest) {
            tour.updateDeliveryRequest(newTimeWindow, indexDeliveryRequest);

            if(previousCourier != newCourier) {
                Tour newTour = cityMap.getTour(newCourier);
                newTour.addDeliveryRequest(tour.getDeliveryRequests().get(indexDeliveryRequest));

                Tour newOptimalTour = Algorithm.ExecuteAlgorithm(cityMap.getWarehouse(), newTour.getDeliveryRequests());
                newOptimalTour.setCourier(newCourier);
                cityMap.updateTourList(newOptimalTour);

                tour.removeDeliveryRequest(indexDeliveryRequest);
            }

            Tour optimalTour = Algorithm.ExecuteAlgorithm(cityMap.getWarehouse(), tour.getDeliveryRequests());
            optimalTour.setCourier(previousCourier);
            cityMap.updateTourList(optimalTour);
        }

        controller.setCurrentState(controller.computedTourState);
    };

    @Override
    public void selectDestinationPoint(Controller c, Window window, GeoPosition position, CityMap cityMap) {
        c.setCurrentState(c.destinationSelectedState);
        Intersection selectedIntersection = cityMap.searchIntersection(position);
        c.destinationSelectedState.setSelectedIntersection(selectedIntersection);
        window.updateSelectedPoint(selectedIntersection);
    };

}
