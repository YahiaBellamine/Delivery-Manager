package com.pld.agile.view.map;

import com.pld.agile.controller.Controller;
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
    public JPanel centerPanel;
    public JPanel bottomPanel;
    public JXMapViewer mapViewer;

    private HashSet<Marker> map;

    private Marker warehouse;

    private Marker requestMarker;
    private HashSet<Marker> tourMarkers;
    private List<GeoPosition> tour;

    private Controller controller;

    public MapViewer(Controller controller) {
        this.controller = controller;

        tourMarkers = new HashSet<>();
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
            case MAP -> map.add(new Marker(id, Color.BLACK, pos, type, controller));
            case WAREHOUSE -> warehouse = new Marker(id,Color.RED, pos, Marker.Type.WAREHOUSE, controller);
            case TOUR -> tourMarkers.add(new Marker(id, Color.green, pos, type, controller));
            case REQUEST -> requestMarker = new Marker(id, Color.orange, pos, type, controller);
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
        tourMarkers.clear();
        tour.clear();
        requestMarker = null;
        update();
    }

    public void clearRequestMarker(){
        requestMarker = null;
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
        if(warehouse!=null){
            WarehousePainter warehousePainter = new WarehousePainter();
            HashSet<Marker> whSet = new HashSet<>();
            whSet.add(warehouse);
            warehousePainter.setWaypoints(whSet);
            painters.add(warehousePainter);
        }

        //the map
        MapMarkersPainter mapPainter = new MapMarkersPainter();
        mapPainter.setWaypoints(map);
        painters.add(mapPainter);

        //the markers
        if(tourMarkers.size() >0){
            MarkersPainter markersPainter = new MarkersPainter();
            markersPainter.setWaypoints(tourMarkers);
            painters.add(markersPainter);
            //the markers

        }

        //the request marker
        if(requestMarker!=null){
            MarkersPainter markersPainter2 = new MarkersPainter();
            HashSet<Marker> reqMarkSet = new HashSet<>();
            reqMarkSet.add(requestMarker);
            markersPainter2.setWaypoints(reqMarkSet);
            painters.add(markersPainter2);
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
        mapViewer.setAddressLocation(new GeoPosition(45.764043, 4.835659));
        mapViewer.setZoom(15);
//        if(map.size() == 1){
//            mapViewer.setAddressLocation(getPositions(map).iterator().next());
//        }else{
//            mapViewer.zoomToBestFit(getPositions(map),0.7);
//            update();
//            mapViewer.repaint();
//        }
    }

    public HashSet<GeoPosition> getPositions(HashSet<Marker> ms){
        HashSet<GeoPosition> positions = new HashSet<>();
        for(Marker m : ms){
            positions.add(m.getPosition());
        }
        return positions;
    }

}
