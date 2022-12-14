package com.pld.agile.view.map;

import com.pld.agile.controller.Controller;

import org.jxmapviewer.viewer.GeoPosition;

import javax.swing.event.MouseInputListener;
import java.awt.Point;
import java.awt.event.MouseEvent;

/**
 * The mouse listener for selecting the delivery requests position on the map.
 */
public class MarkerMouseListener implements MouseInputListener {

    /** The Controller instance controlling the map */
    private final Controller controller;

    /**
     * The default constructor.
     * @param controller The Controller instance controlling the map.
     */
    public MarkerMouseListener(Controller controller){
        this.controller = controller;
    }

    /**
     * Updates the position of the delivery request marker.
     * @param e The event to be processed.
     */
    @Override public void mouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            int x = e.getX();
            int y = e.getY();
            GeoPosition pos = controller.getWindow().getMapViewer().getMapViewer().convertPointToGeoPosition(new Point(x, y));
            controller.selectDestinationPoint(pos);
        }
    }
    @Override public void mousePressed(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
    @Override public void mouseDragged(MouseEvent e) {}
    @Override public void mouseMoved(MouseEvent e) {}
}
