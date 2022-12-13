package com.pld.agile.view.map;

import com.pld.agile.controller.Controller;

import org.jxmapviewer.viewer.GeoPosition;

import javax.swing.event.MouseInputListener;
import java.awt.Point;
import java.awt.event.MouseEvent;

/**
 * a mouse listener for selecting the delivery requests position on the map
 */
public class MarkerMouseListener implements MouseInputListener {

    /** The Controller instance controlling the map */
    private Controller controller;

    /**
     * the default constructor
     * @param controller - The Controller instance controlling the map
     */
    public MarkerMouseListener(Controller controller){
        this.controller = controller;
    }

    /**
     * updates the position of the delivery request marker
     * @param e the event to be processed
     */
    @Override public void mouseClicked(MouseEvent e) {
        switch (e.getButton()) {
            case MouseEvent.BUTTON1:
                int x =e.getX();
                int y =e.getY();
                GeoPosition pos = controller.getWindow().getMapViewer().getMapViewer().convertPointToGeoPosition(new Point(x,y));
                controller.selectDestinationPoint(pos);
                break;
        }
    }
    @Override public void mousePressed(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
    @Override public void mouseDragged(MouseEvent e) {}
    @Override public void mouseMoved(MouseEvent e) {}
}
