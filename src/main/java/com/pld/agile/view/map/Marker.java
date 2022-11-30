
package com.pld.agile.view.map;

import java.awt.*;

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
    public enum Type{WAREHOUSE, MAP, TOUR, REQUEST}
    private final long id;
    private Color color;
    private JLabel lbl;

    private Type type;

    /**
     * @param id the id
     * @param color the color
     * @param position the coordinate
     */
    public Marker(long id, Color color, GeoPosition position, Type type, Controller controller)
    {
        super(position);
        this.id = id;
        this.color = color;
        this.lbl = new JLabel();
        this.type = type;
        if(this.type == Type.MAP){
            lbl.addMouseListener(new MarkerMouseListener(this, controller));
        }
    }
    public JLabel getLbl() {
        return lbl;
    }

    public void setLbl(JLabel lbl) {
        this.lbl = lbl;
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
