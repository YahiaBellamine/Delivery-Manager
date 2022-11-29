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

    private HashSet<Marker> map;

    private Marker warehouse;
    private HashSet<Marker> markers;
    private List<GeoPosition> tour;

    public MapViewer() {
        bottomButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                recenter();
            }
        });

        markers = new HashSet<>();
        map = new HashSet<>();
        tour = new LinkedList<>();

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

    public void addPoint(GeoPosition pos, long id, Marker.Type type){
        switch (type){
            case MAP -> map.add(new Marker(id, Color.BLACK, pos, type));
            case WAREHOUSE -> {
                map.add(new Marker(id, Color.GREEN, pos, type));
                //warehouse = new Marker(id,Color.RED, pos, Marker.Type.WAREHOUSE);
            }
            case TOUR -> markers.add(new Marker(id, Color.green, pos, type));
            case REQUEST -> markers.add(new Marker(id, Color.orange, pos, type));
        }
    }

    public void updateTour(List<GeoPosition> tourlist){
        this.tour.clear();
        this.tour.addAll(tourlist);
        update();
    }

    public List<GeoPosition> getTour(){
        return this.tour;
    }

    public void clearMarkers(){
        markers.clear();
        tour.clear();
        update();
    }

    public void clearAll(){
        clearMarkers();
        map.clear();
        update();
    }

    public void update(){
        List<Painter<JXMapViewer>> painters = new ArrayList<Painter<JXMapViewer>>();

        //the warehouse

        //the map

        MapMarkersPainter mapPainter = new MapMarkersPainter();
        mapPainter.setWaypoints(map);
        painters.add(mapPainter);

        //the markers
        if(markers.size() >0){
            MarkersPainter markersPainter = new MarkersPainter();
            markersPainter.setWaypoints(markers);
            painters.add(markersPainter);
        }

        //the tour
        if(tour.size()>1){
            TourPainter tourPainter = new TourPainter(tour);
            painters.add(tourPainter);
        }

        CompoundPainter<JXMapViewer> painter = new CompoundPainter<JXMapViewer>(painters);
        mapViewer.setOverlayPainter(painter);
    }

    public void recenter(){
        if(map.size() == 1){
            mapViewer.setAddressLocation(getPositions(map).iterator().next());
        }else{
            mapViewer.zoomToBestFit(getPositions(map),0.7);
            update();
            mapViewer.repaint();
        }
    }

    public HashSet<GeoPosition> getPositions(HashSet<Marker> ms){
        HashSet<GeoPosition> positions = new HashSet<>();
        for(Marker m : ms){
            positions.add(m.getPosition());
        }
        return positions;
    }

}
