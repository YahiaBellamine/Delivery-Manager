package com.pld.agile.controller;

import com.pld.agile.model.*;
import com.pld.agile.model.enums.TimeWindow;
import com.pld.agile.utils.Algorithm;
import com.pld.agile.utils.InaccessibleDestinationException;
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

public class DestinationSelectedState implements State {

  private Intersection selectedIntersection;

  /**
   * This method update the selected destination point on the map
   * @param cityMap the city map
   * @param controller the controller
   * @param position the geo position of the selected intersection
   * @param window the main window
   */
  @Override
  public void selectDestinationPoint(Controller controller, Window window, GeoPosition position, CityMap cityMap) {
    Intersection selectedIntersection = cityMap.searchIntersection(position);
    this.selectedIntersection = selectedIntersection;
    window.updateSelectedPoint(selectedIntersection);
  }


  /**
   * This method add a new request with the selected destination point
   * @param cityMap the city map
   * @param controller the controller
   * @param window the main window
   */
  @Override
  public void addNewRequest(CityMap cityMap, Controller controller, Window window) {
    TimeWindow timeWindow = (TimeWindow) window.getDeliveryRequestView().comboBoxTimeWindow.getSelectedItem();
    Courier courier = (Courier) window.getDeliveryRequestView().comboBoxCourier.getSelectedItem();

    DeliveryRequest deliveryRequest = new DeliveryRequest(timeWindow, selectedIntersection);

    Tour tour = cityMap.getTour(courier);
    if (tour == null) tour = new Tour();
    tour.addDeliveryRequest(deliveryRequest);

    try {
      Tour optimalTour = Algorithm.ExecuteAlgorithm(cityMap.getWarehouse(), tour.getDeliveryRequests());
      optimalTour.setCourier(courier);
      cityMap.updateTour(optimalTour);
      controller.setCurrentState(controller.computedTourState);
    } catch (InaccessibleDestinationException e) {
      window.displayMessage(e.getMessage());
    }
  }

  /**
   * Load tours from a xml file
   *
   * @param cityMap
   * @param w
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

  protected void setSelectedIntersection(Intersection selectedIntersection) {
    this.selectedIntersection = selectedIntersection;
  }
}
