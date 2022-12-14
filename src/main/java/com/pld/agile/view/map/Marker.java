package com.pld.agile.view.map;

import java.awt.image.BufferedImage;

import org.jxmapviewer.viewer.DefaultWaypoint;
import org.jxmapviewer.viewer.GeoPosition;


/**
 * A Marker that can be placed on a JXMapViewer,
 * it has a GeoPosition and an image.
 */
public class Marker extends DefaultWaypoint
{
    /** the marker image */
    private final BufferedImage img;

    /**
     * @param position The GeoPosition coordinates on the map.
     * @param img The marker image.
     */
    public Marker( GeoPosition position, BufferedImage img)
    {
        super(position);
        this.img = img;
    }

    /**
     * @return The marker image.
     */
    public BufferedImage getImg() {
        return img;
    }

}
