/*
 * WaypointRenderer.java
 *
 * Created on March 30, 2006, 5:24 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.pld.agile.view.map;

import java.awt.*;
import java.awt.geom.Point2D;


import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.viewer.WaypointPainter;


/**
 * A Marker painter - defines how the markers are painted on the map
 */
public class MarkersPainter extends WaypointPainter<Marker> {

    @Override
    protected void doPaint(Graphics2D g, JXMapViewer viewer, int width, int height) {
        //if there is no marker to show on the map (meaning no map file is loaded)
        //then darken the map and display a "please load a map" message
        if(getWaypoints().isEmpty()){
            Rectangle rect = viewer.getViewportBounds();
            Color c = g.getColor();
            g.setColor(new Color(0,0,0,180));
            g.fillRect(0, 0, rect.width, rect.height);

            g.setColor(new Color(200,200,200,200));
            Font font = new Font(Font.SERIF,Font.BOLD,20);
            g.setFont(font);
            FontMetrics metrics = g.getFontMetrics(font);
            String text = "Please load a map to start adding delivery requests";
            int x = (rect.width - metrics.stringWidth(text)) / 2;
            int y =  ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
            g.drawString(text, x, y);

            g.setColor(c);
        }else{
            for (Marker w : getWaypoints()) {
                if(w == null ) continue;
                Point2D point = viewer.getTileFactory().geoToPixel(w.getPosition(), viewer.getZoom());
                int zoom = viewer.getZoom();
                int imgW = 100/(zoom+1);
                int imgH = (w.getImg().getHeight()*imgW)/w.getImg().getWidth();
                Rectangle rectangle = viewer.getViewportBounds();
                int x = (int)(point.getX() - rectangle.getX());
                int y = (int)(point.getY() - rectangle.getY());
                Image img = w.getImg().getScaledInstance(imgW,imgH,Image.SCALE_SMOOTH);
                g.drawImage(img, x -img.getWidth(null) / 2, y -img.getHeight(null), null);
            }
        }
    }

}
