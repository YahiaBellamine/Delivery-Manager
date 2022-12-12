package com.pld.agile.controller;

import com.pld.agile.model.CityMap;
import com.pld.agile.model.Courier;
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
                XMLDeserialiser.load(path, cityMap);
                controller.setCurrentState(controller.loadedMapState);

            } catch (ExceptionXML e) {
                controller.setCurrentState(controller.initialState);
                window.displayMessage("Error while loading the map"+e);
                throw new RuntimeException(e);
            } catch (ParserConfigurationException e) {
                window.displayMessage("System error in parse XML");
                throw new RuntimeException(e);
            } catch (IOException e) {
                window.displayMessage("System error in parse XML");
                throw new RuntimeException(e);
            } catch (SAXException e) {
                window.displayMessage("System error in parse XML");
                throw new RuntimeException(e);
            } catch (NumberFormatException e){
                window.displayMessage("Number format error");
            }
        }
        // previous version for updating the view
        // will be implemented using Observer pattern
        /*window.getMapViewer().clearAll();
        deliveryRequests.clear();
        this.window.getDeliveriesView().displayRequests(deliveryRequests);
        // Define the warehouse marker on the map
        GeoPosition warehousePosition = new GeoPosition(cityMap.getWarehouse().getLatitude(),
                cityMap.getWarehouse().getLongitude());
        this.window.getMapViewer().addPoint(warehousePosition, cityMap.getWarehouse().getId(), Marker.Type.WAREHOUSE);
        this.window.getMapViewer().recenter();*/

    }

    public default void selectDestinationPoint(Controller controller, Window window, GeoPosition pos, CityMap cityMap) {};

    public default void addNewRequest(CityMap cityMap, Controller controller, Window window) {};

    public default void restoreTours(CityMap cityMap, Controller controller, Window window) {};

    public default void saveTours(CityMap cityMap, Window window) {};

    public default void deleteDeliveryRequest(CityMap cityMap, Controller c, Courier courier, int indexDeliveryRequest) {};

    public default void updateDeliveryRequest(CityMap cityMap, Controller controller, Window w, Courier courier, int indexDeliveryRequest) {};
}


