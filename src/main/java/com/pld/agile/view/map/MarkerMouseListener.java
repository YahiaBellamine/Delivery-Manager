package com.pld.agile.view.map;

import javax.swing.event.MouseInputListener;
import java.awt.event.MouseEvent;

public class MarkerMouseListener implements MouseInputListener {

    private Marker wp;

    public MarkerMouseListener(Marker wp){
        this.wp = wp;
    }

    public Marker getWp() {
        return wp;
    }

    public void setWp(Marker wp) {
        this.wp = wp;
    }
    @Override public void mouseClicked(MouseEvent e) {

        System.out.println(wp.getId() +" "+wp.getPosition().getLatitude()+" "+wp.getPosition().getLongitude());

    }
    @Override public void mousePressed(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
    @Override public void mouseDragged(MouseEvent e) {}
    @Override public void mouseMoved(MouseEvent e) {}
}
