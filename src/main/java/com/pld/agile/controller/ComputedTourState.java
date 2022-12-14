package com.pld.agile.controller;

import com.pld.agile.model.CityMap;
import com.pld.agile.model.Courier;
import com.pld.agile.model.Intersection;
import com.pld.agile.model.Tour;
import com.pld.agile.model.enums.TimeWindow;
import com.pld.agile.utils.Algorithm;

import com.pld.agile.utils.xml.ExceptionXML;
import com.pld.agile.utils.xml.XMLDeserialiser;
import com.pld.agile.utils.xml.XMLSerialiser;
import com.pld.agile.utils.InaccessibleDestinationException;
import com.pld.agile.view.Window;
import org.jxmapviewer.viewer.GeoPosition;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.sql.Time;
import java.util.LinkedList;
import java.util.List;

public class ComputedTourState implements State{

    /**
     * This method delete a delivery request from the city map
     * @param cityMap the city map
     * @param courier the courier
     * @param indexDeliveryRequest the index of the delivery request to delete
     */
    @Override
    public void deleteDeliveryRequest(CityMap cityMap, Courier courier, int indexDeliveryRequest) {
        Tour tour = cityMap.getTour(courier);
        if(tour != null && tour.getDeliveryRequests().size() > indexDeliveryRequest) {
            tour.removeDeliveryRequest(indexDeliveryRequest);
            Tour optimalTour = tour;

            if (tour.getDeliveryRequests().isEmpty()) {
                tour.clearIntersections();
            } else {
                try {
                    optimalTour = Algorithm.ExecuteAlgorithm(cityMap.getWarehouse(), tour.getDeliveryRequests());
                    optimalTour.setCourier(courier);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            cityMap.updateTour(optimalTour);
        }
    };

    /**
     * Save tours from cityMap in a xml file
     *
     * @param cityMap
     * @param w
     */
    @Override
    public void saveTours(CityMap cityMap, Window w) {
        try{
            XMLSerialiser.save(cityMap.getTourList());
        } catch (TransformerException  | ParserConfigurationException e) {
            w.displayMessage("System error in saving Tours");
            throw new RuntimeException(e);
        }
        catch (ExceptionXML e){
            w.displayMessage(e.toString());
        }
    };


    /**
     * Loads saved tours from a xml file
     *
     * @param cityMap
     * @param c
     * @param w
     */
    @Override
    public void loadTours(CityMap cityMap, Controller c,Window w) {
        JFileChooser j = new JFileChooser("src/main/java/com/pld/agile/utils/tours");
        j.setAcceptAllFileFilterUsed(false);
        j.setDialogTitle("Select a tour file (.xml)");

        FileNameExtensionFilter restrict = new FileNameExtensionFilter("Only .xml files", "xml");
        j.addChoosableFileFilter(restrict);

        // invoke the showsOpenDialog function to show the save dialog
        int r = j.showOpenDialog(null);

        // if the user selects a file
        String path;
        if (r == JFileChooser.APPROVE_OPTION) {
            // set the label to the path of the selected file
            path = j.getSelectedFile().toURI().getPath();
            try {
                List<Tour> tours = new LinkedList<>();
                XMLDeserialiser.loadTours(path, tours, cityMap);
                c.setCurrentState(c.computedTourState);
            } catch (ParserConfigurationException | IOException | SAXException e) {
                w.displayMessage("System error in loading Tours");
                throw new RuntimeException(e);
            } catch (ExceptionXML e) {
                w.displayMessage("Error while loading the map"+e);
            }
        }
    }

    @Override
    public void updateDeliveryRequest(CityMap cityMap, TimeWindow newTimeWindow, Courier newCourier, Courier previousCourier, int indexDeliveryRequest) {
        Tour tour = cityMap.getTour(previousCourier);
        if(tour != null && tour.getDeliveryRequests().size() > indexDeliveryRequest) {
            tour.updateDeliveryRequest(newTimeWindow, indexDeliveryRequest);

            if(previousCourier != newCourier) {
                Tour newTour = cityMap.getTour(newCourier);
                if (newTour == null) newTour = new Tour();
                newTour.addDeliveryRequest(tour.getDeliveryRequests().get(indexDeliveryRequest));

                try {
                    Tour newOptimalTour = Algorithm.ExecuteAlgorithm(cityMap.getWarehouse(), newTour.getDeliveryRequests());
                    newOptimalTour.setCourier(newCourier);
                    cityMap.updateTour(newOptimalTour);
                    tour.removeDeliveryRequest(indexDeliveryRequest);
                } catch (InaccessibleDestinationException e) {
                }
            }
            if (tour.getDeliveryRequests().isEmpty()) {
                tour.clearIntersections();
            } else {
                try {
                    Tour optimalTour = Algorithm.ExecuteAlgorithm(cityMap.getWarehouse(), tour.getDeliveryRequests());
                    optimalTour.setCourier(previousCourier);
                    cityMap.updateTour(optimalTour);
                } catch (InaccessibleDestinationException e) {

                }
            }
            System.out.println("------>" + tour);
        }
    }

    /**
     * This method select a destination point on the map
     * and set the current state of the controller to DestinationSeletedState
     * @param cityMap the city map
     * @param c the controller
     * @param position the geo position of the selected intersection
     * @param window the main window
     */
    @Override
    public void selectDestinationPoint(Controller c, Window window, GeoPosition position, CityMap cityMap) {
        c.setCurrentState(c.destinationSelectedState);
        Intersection selectedIntersection = cityMap.searchIntersection(position);
        c.destinationSelectedState.setSelectedIntersection(selectedIntersection);
        window.updateSelectedPoint(selectedIntersection);
    };

}
