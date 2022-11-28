package com.pld.agile.view.map;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.input.CenterMapListener;
import org.jxmapviewer.input.PanKeyListener;
import org.jxmapviewer.input.PanMouseInputListener;
import org.jxmapviewer.input.ZoomMouseWheelListenerCursor;
import org.jxmapviewer.viewer.*;

import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.*;

public class MapViewer {
    public JPanel mainPanel;
    public JPanel mapPanel;
    public JButton bottomButton;
    public JPanel topPanel;
    public JLabel topLabel;
    public JPanel centerPanel;
    public JPanel leftPanel;
    public JPanel rightPanel;
    public JPanel bottomPanel;
    public JXMapViewer mapViewer;

    private HashSet<GeoPosition> points;

    public MapViewer() {


        bottomButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                recenter();
            }
        });

        points = new HashSet<>();

        // Add interactions
        MouseInputListener mia = new PanMouseInputListener(mapViewer);
        mapViewer.addMouseListener(mia);
        mapViewer.addMouseMotionListener(mia);

        mapViewer.addMouseListener(new CenterMapListener(mapViewer));

        mapViewer.addMouseWheelListener(new ZoomMouseWheelListenerCursor(mapViewer));

        mapViewer.addKeyListener(new PanKeyListener(mapViewer));

        // Add a selection painter
        SelectionAdapter sa = new SelectionAdapter(mapViewer);
        SelectionPainter sp = new SelectionPainter(sa);
        mapViewer.addMouseListener(sa);
        mapViewer.addMouseMotionListener(sa);
        mapViewer.setOverlayPainter(sp);


    }

    private void createUIComponents() {
        mapViewer = new JXMapViewer();

        // Create a TileFactoryInfo for OpenStreetMap
        TileFactoryInfo info = new OSMTileFactoryInfo();
        DefaultTileFactory tileFactory = new DefaultTileFactory(info);
        mapViewer.setTileFactory(tileFactory);

        // Use 8 threads in parallel to load the tiles
        tileFactory.setThreadPoolSize(8);

        mapPanel = mapViewer;
    }

    public void drawPoint(double x, double y, String text){
        GeoPosition newPoint = new GeoPosition(x,y);
        points.add(newPoint);
                //Arrays.asList(new SwingWaypoint(text, newPoint)));

        HashSet<MyWaypoint> wPoints = new HashSet<>();
        for(GeoPosition gp : points){
            wPoints.add(new MyWaypoint(text, Color.black,gp));

        }

        // Set the overlay painter
        WaypointPainter<MyWaypoint> waypointPainter = new WaypointPainter<MyWaypoint>();
        waypointPainter.setWaypoints(wPoints);
        waypointPainter.setRenderer(new MapMarkers());
        mapViewer.setOverlayPainter(waypointPainter);

        // Add the JButtons to the map viewer
//        for (SwingWaypoint w : waypoints) {
//           // mapViewer.add(w.getButton());
//        }
    }

    public void recenter(){
        if(points.size() == 1){
            mapViewer.setAddressLocation(points.iterator().next());
        }else{
            mapViewer.zoomToBestFit(points,0.7);
        }
    }

}
