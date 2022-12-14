
package com.pld.agile.view.map;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.List;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.painter.Painter;

/**
 * Paints a route.
 */
public class RoutePainter implements Painter<JXMapViewer>
{

    private final List<Route> tracks;

    /**
     * @param tracks The tracks.
     */
    public RoutePainter(List<Route> tracks)
    {
        this.tracks = tracks;
    }

    @Override
    public void paint(Graphics2D g, JXMapViewer map, int w, int h)
    {
        g = (Graphics2D) g.create();
        // convert from viewport to world bitmap
        Rectangle rect = map.getViewportBounds();
        g.translate(-rect.x, -rect.y);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        // do the drawing
        //g.setColor();
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
     * Draws all the itineraries of all the routes.
     * @param g The graphics object.
     * @param map The map.
     */
    private void drawRoute(Graphics2D g, JXMapViewer map)
    {
        for(Route r : tracks){
            int lastX = 0;
            int lastY = 0;
            boolean first = true;
            for(List<GeoPosition> segment : r.getRouteSegments()){
                g.setColor(r.getDefaultColor());
                for(GeoPosition gp : segment){
                    Point2D pt = map.getTileFactory().geoToPixel(gp, map.getZoom());
                    if (first)
                    {
                        first = false;
                    }
                    else if(Point2D.distance(lastX,lastY, pt.getX(), pt.getY())>1)
                    {
                        g.drawLine(lastX, lastY, (int) (pt.getX()), (int) (pt.getY()) );
                    }
                    lastX = (int) pt.getX();
                    lastY = (int) pt.getY();
                }
            }
        }
    }

    /**
     * Draws arrows to indicate directions.
     * @param g The graphics object.
     * @param map The map.
     * @param arrowSize The size of the arrow.
     */
    private void drawArrows(Graphics2D g, JXMapViewer map, int arrowSize)
    {
        int lastX = 0;
        int lastY = 0;
        for(Route r : tracks){
            boolean first = true;
            double dist = 0;
            double lastAngle=0;
            double currentAngle=0;
            for(List<GeoPosition> segment : r.getRouteSegments()){
                for(GeoPosition gp : segment){
                    Point2D pt = map.getTileFactory().geoToPixel(gp, map.getZoom());
                    if (first)
                    {
                        first = false;
                        dist = -100;
                    }
                    else
                    {
                        currentAngle = Math.atan2(lastY-pt.getY(),lastX-pt.getX());
                        if((((Math.abs(currentAngle-lastAngle))>0.1&&dist>20) || dist>200)
                                && Point2D.distance(lastX,lastY, pt.getX(), pt.getY())>10){
                            dist=0;
                            double a = currentAngle + Math.PI/4;
                            int dx = (int) (arrowSize*Math.sin(a));
                            int dy =(int) (-arrowSize*Math.cos(a));
                            a -= 2*Math.PI /4;
                            int dx2 = (int)(arrowSize*Math.sin(a));
                            int dy2 = (int) (-arrowSize*Math.cos(a));

                            int x = (5*lastX+(int)(3*pt.getX()))/8;
                            int y = (5*lastY+(int) (3*pt.getY()))/8;

                            g.drawLine(x,y, x+dx, y+dy);
                            g.drawLine(x,y, x-dx2, y-dy2);
                        }
                    }
                    dist+= Point2D.distance(lastX,lastY,pt.getX(),pt.getY());
                    lastAngle = currentAngle;
                    lastX = (int) pt.getX();
                    lastY = (int) pt.getY();
                }
            }
        }
    }

    /**
     * Draws markers for delivery requests.
     * @param g The graphics object.
     * @param map The map.
     */
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
