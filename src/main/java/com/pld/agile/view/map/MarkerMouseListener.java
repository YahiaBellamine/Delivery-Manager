package com.pld.agile.view.map;

import com.pld.agile.controller.Controller;

import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.MouseEvent;

public class MarkerMouseListener implements MouseInputListener {

    private Marker wp;
    private Controller controller;

    public MarkerMouseListener(Marker wp, Controller controller){
        this.wp = wp;
        this.controller = controller;
    }

    public Marker getWp() {
        return wp;
    }

    public void setWp(Marker wp) {
        this.wp = wp;
    }
    @Override public void mouseClicked(MouseEvent e) {
        switch (e.getButton()) {
            case MouseEvent.BUTTON1:
                controller.selectIntersection(wp.getId());
                break;
        }
    }
    @Override public void mousePressed(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
    @Override public void mouseDragged(MouseEvent e) {}
    @Override public void mouseMoved(MouseEvent e) {}
}
