package com.pld.agile.controller;

import com.pld.agile.model.CityMap;
import com.pld.agile.model.Intersection;
import com.pld.agile.model.Tour;
import com.pld.agile.utils.xml.ExceptionXML;
import com.pld.agile.utils.xml.XMLDeserialiser;
import com.pld.agile.view.Window;
import org.jxmapviewer.viewer.GeoPosition;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * The state when a new map is loaded.
 */
public class LoadedMapState implements State{

    /**
     * This method load tours from an XML file
     * @param cityMap - The city map
     * @param c - The controller
     * @param w - The window
     */
    @Override
    public void loadTours(CityMap cityMap, Controller c,Window w) {
        String path;
        JFileChooser j = new JFileChooser("src/main/java/com/pld/agile/utils/tours");
        j.setAcceptAllFileFilterUsed(false);
        j.setDialogTitle("Select a tour file (.xml)");

        FileNameExtensionFilter restrict = new FileNameExtensionFilter("Only .xml files", "xml");
        j.addChoosableFileFilter(restrict);

        // invoke the showsOpenDialog function to show the save dialog
        int r = j.showOpenDialog(null);
        if (r == JFileChooser.APPROVE_OPTION) {
            path = j.getSelectedFile().getAbsolutePath();
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
