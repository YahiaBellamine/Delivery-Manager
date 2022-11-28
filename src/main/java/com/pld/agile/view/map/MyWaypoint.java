
package com.pld.agile.view.map;

import java.awt.*;
import java.awt.event.MouseEvent;

import org.jxmapviewer.viewer.DefaultWaypoint;
import org.jxmapviewer.viewer.GeoPosition;

import javax.swing.*;
import javax.swing.event.MouseInputListener;

/**
 * A waypoint that also has a color and a label
 * @author Martin Steiger
 */
public class MyWaypoint extends DefaultWaypoint
{
    private final long id;
    private Color color;

    private JLabel lbl;

    public JLabel getLbl() {
        return lbl;
    }

    public void setLbl(JLabel lbl) {
        this.lbl = lbl;
    }

    /**
     * @param id the id
     * @param color the color
     * @param coord the coordinate
     */
    public MyWaypoint(long id, Color color, GeoPosition coord)
    {
        super(coord);
        this.id = id;
        this.color = color;
        this.lbl = new JLabel();

        lbl.addMouseListener(new MarkerMouseListener(this));
    }

    /**
     * @return the color
     */
    public Color getColor()
    {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public long getId() {
        return id;
    }
}
