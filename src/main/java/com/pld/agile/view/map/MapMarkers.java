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
public class MapMarkers implements WaypointRenderer<MyWaypoint>
{

//    private static final Log log = LogFactory.getLog(MapMarkers.class);

    private final Map<Color, BufferedImage> map = new HashMap<Color, BufferedImage>();

//    private final Font font = new Font("Lucida Sans", Font.BOLD, 10);

    private BufferedImage origImage;

    /**
     * Uses a default waypoint image
     */
    public MapMarkers()
    {
//        URL resource = getClass().getResource("waypoint_white.png");
//
//        try
//        {
//            origImage = ImageIO.read(resource);
//        }
//        catch (Exception ex)
//        {
//            log.warn("couldn't read waypoint_white.png", ex);
//        }
    }

    private BufferedImage convert(BufferedImage loadImg, Color newColor)
    {
        int w = loadImg.getWidth();
        int h = loadImg.getHeight();
        BufferedImage imgOut = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        BufferedImage imgColor = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g = imgColor.createGraphics();
        g.setColor(newColor);
        g.fillRect(0, 0, w+1, h+1);
        g.dispose();

        Graphics2D graphics = imgOut.createGraphics();
        graphics.drawImage(loadImg, 0, 0, null);
        graphics.setComposite(MultiplyComposite.Default);
        graphics.drawImage(imgColor, 0, 0, null);
        graphics.dispose();

        return imgOut;
    }

    @Override
    public void paintWaypoint(Graphics2D g, JXMapViewer viewer, MyWaypoint w)
    {
        g = (Graphics2D)g.create();

        int zoom = viewer.getZoom();
        int imgW = 35/(zoom+1);
        int imgH = 35/(zoom+1);
        Point2D point = viewer.getTileFactory().geoToPixel(w.getPosition(), viewer.getZoom());


        BufferedImage myImg = new BufferedImage(imgW,imgH,BufferedImage.TYPE_INT_RGB);
        myImg.getGraphics().setColor(Color.black);
        myImg.getGraphics().drawRect(0,0, myImg.getWidth(),myImg.getHeight());

        int x = (int)point.getX();
        int y = (int)point.getY();
        g.setColor(Color.black);
        g.fillRect(x -myImg.getWidth() / 2, y -myImg.getHeight(), myImg.getWidth(),myImg.getHeight());
        g.drawImage(myImg, x -myImg.getWidth() / 2, y -myImg.getHeight(), null);
        g.dispose();
    }
}
