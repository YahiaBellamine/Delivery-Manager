
package com.pld.agile.view.map;

import java.awt.*;
import java.awt.image.BufferedImage;

import com.pld.agile.controller.Controller;
import org.jxmapviewer.viewer.DefaultWaypoint;
import org.jxmapviewer.viewer.GeoPosition;

import javax.swing.*;

/**
 * A waypoint that also has a color and a label
 * @author Martin Steiger
 */
public class Marker extends DefaultWaypoint
{
    private BufferedImage img;

    /**
     * @param img the image
     * @param position the coordinate
     */
    public Marker( GeoPosition position, BufferedImage img)
    {
        super(position);
        this.img = img;
    }

    /**
     * @return the image
     */
    public BufferedImage getImg() {
        return img;
    }

}
