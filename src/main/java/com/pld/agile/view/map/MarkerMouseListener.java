package com.pld.agile.view.map;

import com.pld.agile.controller.Controller;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.viewer.GeoPosition;

import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

public class MarkerMouseListener implements MouseInputListener {

    //private Marker wp;
    private Controller controller;

    public MarkerMouseListener(Controller controller){
        this.controller = controller;
    }

    @Override public void mouseClicked(MouseEvent e) {
        switch (e.getButton()) {
            case MouseEvent.BUTTON1:
                int x =e.getX();
                int y =e.getY();
                GeoPosition pos = controller.getWindow().getMapViewer().mapViewer.convertPointToGeoPosition(new Point(x,y));
                controller.searchIntersection(pos.getLatitude(),pos.getLongitude());
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
