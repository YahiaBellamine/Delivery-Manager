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
import java.util.LinkedList;
import java.util.List;

public class ComputedTourState implements State{

    @Override
    public void deleteDeliveryRequest(CityMap cityMap, Controller controller, Courier courier, int indexDeliveryRequest) {
        Tour tour = cityMap.getTour(courier);
        if(tour != null && tour.getDeliveryRequests().size() > indexDeliveryRequest) {
            tour.removeDeliveryRequest(indexDeliveryRequest);

            try {
                Tour optimalTour = Algorithm.ExecuteAlgorithm(cityMap.getWarehouse(), tour.getDeliveryRequests());
                optimalTour.setCourier(courier);
                cityMap.updateTourList(optimalTour);
                controller.setCurrentState(controller.computedTourState);
            } catch (InaccessibleDestinationException e){

            }
        }
    };

    /**
     * Save tours from cityMap in a xml file
     *
     * @param cityMap
     * @param c
     * @param w
     */
    @Override
    public void saveTours(CityMap cityMap, Controller c, Window w) {
        try{
            XMLSerialiser.save(cityMap.getTourList());
            c.setCurrentState(c.computedTourState);
        } catch (TransformerException | ExceptionXML | ParserConfigurationException e) {
            w.displayMessage("System error in saving Tours");
            throw new RuntimeException(e);
        }
    };

    /**
     * Loads saved tours from a xml file
     *
     * @param cityMap
     * @param c
     * @param w
     */
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
                //TODO: the list tours should be global instead of local?
                List<Tour> tours = new LinkedList<>();
                XMLDeserialiser.loadTours(path, tours, cityMap);
//                System.out.println(tours.size());
//                System.out.println(tours.get(0).getDeliveryRequests().size());
//                System.out.println(tours.get(0).getIntersections().size());
//
//                for(int i=0;i<6;i++)                { // i<6 for test2
//                    System.out.println(tours.get(0).getDeliveryRequests().get(i).getTimeWindow());
//                    System.out.println(tours.get(0).getDeliveryRequests().get(i).getArrivalTime());
//                }

            } catch (ExceptionXML | ParserConfigurationException | IOException | SAXException e) {

            }
        }
    }

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

                try {
                    Tour newOptimalTour = Algorithm.ExecuteAlgorithm(cityMap.getWarehouse(), newTour.getDeliveryRequests());
                    newOptimalTour.setCourier(newCourier);
                    cityMap.updateTourList(newOptimalTour);
                    tour.removeDeliveryRequest(indexDeliveryRequest);
                    controller.setCurrentState(controller.computedTourState);
                } catch (InaccessibleDestinationException e) {

                }
            }

            try {
                Tour optimalTour = Algorithm.ExecuteAlgorithm(cityMap.getWarehouse(), tour.getDeliveryRequests());
                optimalTour.setCourier(previousCourier);
                cityMap.updateTourList(optimalTour);
                controller.setCurrentState(controller.computedTourState);
            } catch (InaccessibleDestinationException e) {

            }
        }
    }

    @Override
    public void selectDestinationPoint(Controller c, Window window, GeoPosition position, CityMap cityMap) {
        c.setCurrentState(c.destinationSelectedState);
        Intersection selectedIntersection = cityMap.searchIntersection(position);
        c.destinationSelectedState.setSelectedIntersection(selectedIntersection);
        window.updateSelectedPoint(selectedIntersection);
    };

}
