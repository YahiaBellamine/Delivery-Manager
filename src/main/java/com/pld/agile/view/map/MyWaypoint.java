
package com.pld.agile.view.map;

import java.awt.Color;

import org.jxmapviewer.viewer.DefaultWaypoint;
import org.jxmapviewer.viewer.GeoPosition;

import javax.swing.*;

/**
 * A waypoint that also has a color and a label
 * @author Martin Steiger
 */
public class MyWaypoint extends DefaultWaypoint
{
    private final String label;
    private final Color color;
    private JLabel lbl;

    public JLabel getLbl() {
        return lbl;
    }

    public void setLbl(JLabel lbl) {
        this.lbl = lbl;
    }

    /**
     * @param label the text
     * @param color the color
     * @param coord the coordinate
     */
    public MyWaypoint(String label, Color color, GeoPosition coord)
    {
        super(coord);
        this.label = label;
        this.color = color;
    }

    /**
     * @return the label text
     */
    public String getLabel()
    {
        return label;
    }

    /**
     * @return the color
     */
    public Color getColor()
    {
        return color;
    }

}
