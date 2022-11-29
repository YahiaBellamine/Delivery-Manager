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
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.viewer.WaypointPainter;
import org.jxmapviewer.viewer.WaypointRenderer;

import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * A fancy waypoint painter
 * @author Martin Steiger
 */
public class MarkersPainter extends WaypointPainter<Marker>
{

    private final Map<Color, BufferedImage> map = new HashMap<Color, BufferedImage>();

//    private final Font font = new Font("Lucida Sans", Font.BOLD, 10);

    private BufferedImage origImage;

    /**
     * Uses a default waypoint image
     */
    public MarkersPainter()
    {
        try
        {
            File resource = new File( "src/main/java/com/pld/agile/view/map/waypoint_white.png");
            origImage = ImageIO.read(resource);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
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
    protected void doPaint(Graphics2D g, JXMapViewer viewer, int width, int height) {
        // g = (Graphics2D)g.create();

        for (Marker w : getWaypoints()) {
            if (origImage == null)
                return;

            BufferedImage myImg = map.get(w.getColor());

            if (myImg == null)
            {
                myImg = convert(origImage, w.getColor());
                map.put(w.getColor(), myImg);
            }
            Point2D point = viewer.getTileFactory().geoToPixel(w.getPosition(), viewer.getZoom());
            int zoom = viewer.getZoom();
//            int imgW = 35/(zoom+1);
//            int imgH = 35/(zoom+1);
            Rectangle rectangle = viewer.getViewportBounds();
            int x = (int)(point.getX() - rectangle.getX());
            int y = (int)(point.getY() - rectangle.getY());
            g.drawImage(myImg, x -myImg.getWidth() / 2, y -myImg.getHeight(), null);
//            JLabel lbl = w.getLbl();
//            lbl.setBounds( x-imgW/2, y-imgH/2, imgW,imgH);
//            lbl.setBackground(w.getColor());
//            lbl.setOpaque(true);
//            viewer.add(lbl);
        }
    }

}
