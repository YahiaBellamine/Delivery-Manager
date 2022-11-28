package com.pld.agile.view.map;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.input.CenterMapListener;
import org.jxmapviewer.input.PanKeyListener;
import org.jxmapviewer.input.PanMouseInputListener;
import org.jxmapviewer.input.ZoomMouseWheelListenerCursor;
import org.jxmapviewer.painter.CompoundPainter;
import org.jxmapviewer.painter.Painter;
import org.jxmapviewer.viewer.*;

import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

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
    private HashSet<MyWaypoint> wPoints;

    public MapViewer() {

        bottomButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                recenter();
            }
        });

        points = new HashSet<>();
        wPoints = new HashSet<>();

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
        mapViewer.setLayout(null);
        mapPanel = mapViewer;

    }

    public void addPoint(double x, double y, long id){
        GeoPosition newPoint = new GeoPosition(x,y);
        points.add(newPoint);
        //MyWaypoint myPoint = new MyWaypoint(text,Color.black,newPoint);
                //Arrays.asList(new SwingWaypoint(text, newPoint)));

        wPoints.add(new MyWaypoint(id, Color.black,newPoint));
    }

    public void update(){
        MapMarkers waypointPainter = new MapMarkers();
        waypointPainter.setWaypoints(wPoints);
        //mapViewer.setOverlayPainter(waypointPainter);


        RoutePainter routePainter = new RoutePainter(points.stream().toList().subList(0,10));

        List<Painter<JXMapViewer>> painters = new ArrayList<Painter<JXMapViewer>>();
        painters.add(routePainter);
        painters.add(waypointPainter);

        CompoundPainter<JXMapViewer> painter = new CompoundPainter<JXMapViewer>(painters);
        mapViewer.setOverlayPainter(painter);
    }

    public void recenter(){
        if(points.size() == 1){
            mapViewer.setAddressLocation(points.iterator().next());
        }else{
            mapViewer.zoomToBestFit(points,0.7);
            update();
            mapViewer.repaint();

        }
    }

}
