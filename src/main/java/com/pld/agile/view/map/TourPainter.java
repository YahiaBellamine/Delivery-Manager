
package com.pld.agile.view.map;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.painter.Painter;

import javax.swing.*;

/**
 * Paints a route
 * @author Martin Steiger
 */
public class TourPainter implements Painter<JXMapViewer>
{
    private Color color = Color.RED;
    private boolean antiAlias = true;

    private List<Route> tracks;

    /**
     * @param tracks the tracks
     */
    public TourPainter(List<Route> tracks)
    {
        // copy the list so that changes in the 
        // original list do not have an effect here
        this.tracks = tracks;
    }

    @Override
    public void paint(Graphics2D g, JXMapViewer map, int w, int h)
    {
        g = (Graphics2D) g.create();
        // convert from viewport to world bitmap
        Rectangle rect = map.getViewportBounds();
        g.translate(-rect.x, -rect.y);
        if (antiAlias)
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        // do the drawing
        g.setColor(color);
        g.setStroke(new BasicStroke(15/(map.getZoom()+1)));
        drawRoute(g, map);
        // do the drawing again
        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(15/(map.getZoom()+1)));
        //g.translate(-1,0);
        drawArrows(g, map, 40/(map.getZoom()+1));

        drawMarkers(g,map);

        g.translate(rect.x, rect.y);
        g.dispose();
    }

    /**
     * @param g the graphics object
     * @param map the map
     */
    private void drawRoute(Graphics2D g, JXMapViewer map)
    {
        for(Route r : tracks){
            int lastX = 0;
            int lastY = 0;
            boolean first = true;
            for(List<GeoPosition> segment : r.getRouteSegments()){
                for(GeoPosition gp : segment){
                    Point2D pt = map.getTileFactory().geoToPixel(gp, map.getZoom());
                    if (first)
                    {
                        first = false;
                    }
                    else
                    {
                        g.drawLine(lastX, lastY, (int) (pt.getX()), (int) (pt.getY()) );
                    }
                    lastX = (int) pt.getX();
                    lastY = (int) pt.getY();
                }
            }
        }
    }

    private void drawArrows(Graphics2D g, JXMapViewer map, int d)
    {
        int lastX = 0;
        int lastY = 0;
        for(Route r : tracks){
            boolean first = true;
            boolean jump = true;
            for(List<GeoPosition> segment : r.getRouteSegments()){
                for(GeoPosition gp : segment){
                    Point2D pt = map.getTileFactory().geoToPixel(gp, map.getZoom());
                    if (first)
                    {
                        first = false;
                    }
                    else
                    {
                        if(jump){
                            double a = Math.atan2(lastY-pt.getY(),lastX-pt.getX()) + Math.PI/4;
                            int dx = (int) (d*Math.sin(a));
                            int dy =(int) (-d*Math.cos(a));
                            a -= 2*Math.PI /4;
                            int dx2 = (int)(d*Math.sin(a));
                            int dy2 = (int) (-d*Math.cos(a));

                            int x = (5*lastX+(int)(3*pt.getX()))/8;
                            int y = (5*lastY+(int) (3*pt.getY()))/8;

                            g.drawLine(x,y, x+dx, y+dy);
                            g.drawLine(x,y, x-dx2, y-dy2);
                        }
                    }
                    jump = !jump;
                    lastX = (int) pt.getX();
                    lastY = (int) pt.getY();
                }
            }
        }
    }

    private void drawMarkers(Graphics2D g, JXMapViewer map)
    {

        for(Route r : tracks){
            for(Marker marker : r.getRouteMarkers()){
                Point2D point = map.getTileFactory().geoToPixel(marker.getPosition(), map.getZoom());
                int zoom = map.getZoom();
                int imgW = 100/(zoom+1);
                int imgH = (marker.getImg().getHeight()*imgW)/marker.getImg().getWidth();
                int x = (int)(point.getX() );
                int y = (int)(point.getY() );
                Image img = marker.getImg().getScaledInstance(imgW,imgH,Image.SCALE_SMOOTH);
                g.drawImage(img, x -img.getWidth(null) / 2, y -img.getHeight(null), null);
            }
        }
    }
}
