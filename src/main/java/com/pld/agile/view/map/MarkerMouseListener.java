package com.pld.agile.view.map;

import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.MouseEvent;

public class MarkerMouseListener implements MouseInputListener {

    private MyWaypoint wp;

    public MarkerMouseListener(MyWaypoint wp){
        this.wp = wp;
    }

    public MyWaypoint getWp() {
        return wp;
    }

    public void setWp(MyWaypoint wp) {
        this.wp = wp;
    }
    @Override public void mouseClicked(MouseEvent e) {
        System.out.println(wp.getId());
    }
    @Override public void mousePressed(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
    @Override public void mouseDragged(MouseEvent e) {}
    @Override public void mouseMoved(MouseEvent e) {}
}
