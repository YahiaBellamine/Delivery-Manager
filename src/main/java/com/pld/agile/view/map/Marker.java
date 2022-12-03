
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
    public enum Type{WAREHOUSE, TOUR, REQUEST}
    private final long id;
    private Type type;
    private BufferedImage img;

    /**
     * @param id the id
     * @param img the image
     * @param position the coordinate
     */
    public Marker(long id, GeoPosition position, BufferedImage img, Type type)
    {
        super(position);
        this.id = id;
        this.img = img;
        this.type = type;
    }

    /**
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * @return the image
     */
    public BufferedImage getImg() {
        return img;
    }

    /**
     * @param img the image
     */
    public void setImg(BufferedImage img) {
        this.img = img;
    }
}
