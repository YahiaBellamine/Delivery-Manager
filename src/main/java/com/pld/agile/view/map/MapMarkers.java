/*
 * WaypointRenderer.java
 *
 * Created on March 30, 2006, 5:24 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.pld.agile.view.map;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.viewer.Waypoint;
import org.jxmapviewer.viewer.WaypointPainter;
import org.jxmapviewer.viewer.WaypointRenderer;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * A fancy waypoint painter
 * @author Martin Steiger
 */
public class MapMarkers extends WaypointPainter<MyWaypoint>
{
    @Override
    protected void doPaint(Graphics2D g, JXMapViewer viewer, int width, int height) {
       // g = (Graphics2D)g.create();

        for (MyWaypoint w : getWaypoints()) {
            Point2D point = viewer.getTileFactory().geoToPixel(w.getPosition(), viewer.getZoom());
            int zoom = viewer.getZoom();
            int imgW = 35/(zoom+1);
            int imgH = 35/(zoom+1);
            Rectangle rectangle = viewer.getViewportBounds();
            int x = (int)(point.getX() - rectangle.getX());
            int y = (int)(point.getY() - rectangle.getY());
            JLabel lbl = w.getLbl();
            lbl.setBounds( x-imgW/2, y-imgH/2, imgW,imgH);
            lbl.setBackground(w.getColor());
            lbl.setOpaque(true);
            viewer.add(lbl);
        }
    }
}
