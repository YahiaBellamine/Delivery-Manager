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

    default void loadMap(Controller controller, Window window, CityMap cityMap) {
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

    default void selectDestinationPoint(Controller controller, Window window, GeoPosition pos, CityMap cityMap) {}

    default void addNewRequest(CityMap cityMap, Controller controller, Window window) {}

    default void loadTours(CityMap cityMap, Controller controller, Window window) {}

    default void saveTours(CityMap cityMap, Window w) {}

    default void deleteDeliveryRequest(CityMap cityMap, Courier courier, int indexDeliveryRequest) {}

    default void updateDeliveryRequest(CityMap cityMap, TimeWindow newTimeWindow, Courier newCourier, Courier previousCourier, int indexDeliveryRequest) {}
}


