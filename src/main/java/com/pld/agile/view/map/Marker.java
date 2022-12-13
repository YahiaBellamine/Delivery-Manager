package com.pld.agile.view.map;

import java.awt.image.BufferedImage;

import org.jxmapviewer.viewer.DefaultWaypoint;
import org.jxmapviewer.viewer.GeoPosition;


/**
 * A Marker that can be placed on a JXMapViewer
 * it has a GeoPosition and an image
 */
public class Marker extends DefaultWaypoint
{
    /** the marker image */
    private BufferedImage img;

    /**
     * @param position - the GeoPosition coordinates on the map
     * @param img - the marker image
     */
    public Marker( GeoPosition position, BufferedImage img)
    {
        super(position);
        this.img = img;
    }

    /**
     * @return the marker image
     */
    public BufferedImage getImg() {
        return img;
    }

}
