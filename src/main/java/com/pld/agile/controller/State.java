package com.pld.agile.controller;

import com.pld.agile.model.CityMap;
import com.pld.agile.model.Courier;
import com.pld.agile.model.enums.TimeWindow;
import com.pld.agile.utils.Algorithm;
import com.pld.agile.utils.xml.ExceptionXML;
import com.pld.agile.utils.xml.XMLDeserialiser;
import com.pld.agile.view.Window;
import org.jxmapviewer.viewer.GeoPosition;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public interface State {
    /**
     * This method load a map from a XML file.
     * @param cityMap The city map.
     * @param controller The controller.
     * @param window The window.
     */
    public default void loadMap(Controller controller, Window window, CityMap cityMap) {
        JFileChooser j = new JFileChooser("src/main/java/com/pld/agile/utils/maps");
        j.setAcceptAllFileFilterUsed(false);
        j.setDialogTitle("Select a map file (.xml)");

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
                window.getMapViewer().clearMap();
                XMLDeserialiser.loadMap(path, cityMap);
                controller.setCurrentState(controller.loadedMapState);

            } catch (ExceptionXML e) {
                controller.setCurrentState(controller.initialState);
                window.displayMessage("Error while loading the map"+e);
                throw new RuntimeException(e);
            } catch (ParserConfigurationException | IOException | SAXException e) {
                window.displayMessage("System error in parse XML");
                throw new RuntimeException(e);
            } catch (NumberFormatException e){
                window.displayMessage("Number format error");
            }
        }
        Algorithm.reInitializeMapAttributes();
    }

    /**
     * This method select a destination point on the map.
     * @param controller The controller.
     * @param window The window.
     * @param pos The geo position of the selected intersection.
     * @param cityMap The city map.
     */
    public default void selectDestinationPoint(Controller controller, Window window, GeoPosition pos, CityMap cityMap) {};

    /** This method add a new delivery request.
     * @param controller The controller.
     * @param window The window.
     * @param cityMap The city map.
     */
    public default void addNewRequest(CityMap cityMap, Controller controller, Window window) {};

    /**
     * This method load tours from an XML file.
     * @param cityMap The city map.
     * @param controller The controller.
     * @param window The window.
     */
    public default void loadTours(CityMap cityMap, Controller controller, Window window) {};

    /**
     * This method save tours to an XML file.
     * @param cityMap The city map.
     * @param w The window.
     */
    public default void saveTours(CityMap cityMap, Window w) {};

    /**
     * This method deletes a delivery request.
     * @param cityMap The city map.
     * @param courier The courier of the delivery request to be deleted.
     */
    public default void deleteDeliveryRequest(CityMap cityMap, Courier courier, int indexDeliveryRequest) {};

    /**
     * This method update a delivery request.
     * @param cityMap The city map.
     * @param newTimeWindow The new time window of the delivery request.
     * @param newCourier The new courier of the delivery request (if changed).
     * @param previousCourier the previous courier of the delivery request.
     * @param indexDeliveryRequest The index of the delivery request in the list of delivery requests of the previous courier.
     */
    public default void updateDeliveryRequest(CityMap cityMap, TimeWindow newTimeWindow, Courier newCourier, Courier previousCourier, int indexDeliveryRequest) {};

}


