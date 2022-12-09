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
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;

public class MapViewer {
    public JPanel mainPanel;
    public JPanel mapPanel;
    public JButton bottomButton;
    public JPanel centerPanel;
    public JPanel bottomPanel;
    private JButton recenterButton;
    public JXMapViewer mapViewer;

    private Marker warehouse;

    private Marker requestMarker;
    private HashSet<Marker> tourMarkers;
    private List<GeoPosition> tour;

    private Controller controller;

    public MapViewer(Controller controller) {
        this.controller = controller;

        tourMarkers = new HashSet<>();
        tour = new LinkedList<>();

        // Add interactions
        MouseInputListener mia = new PanMouseInputListener(mapViewer);
        mapViewer.addMouseListener(mia);
        mapViewer.addMouseMotionListener(mia);

        mapViewer.addMouseListener(new CenterMapListener(mapViewer));

        mapViewer.addMouseWheelListener(new ZoomMouseWheelListenerCursor(mapViewer));
        mapViewer.addMouseListener(new MarkerMouseListener(controller));

        //mapViewer.addKeyListener(new PanKeyListener(mapViewer));

//        // Add a selection painter
//        SelectionAdapter sa = new SelectionAdapter(mapViewer);
//        SelectionPainter sp = new SelectionPainter(sa);
//        mapViewer.addMouseListener(sa);
//        mapViewer.addMouseMotionListener(sa);
//        mapViewer.setOverlayPainter(sp);

        //recenter();
        recenterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                recenter();
            }
        });
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
            case WAREHOUSE -> {
                warehouse = new Marker(id,pos,ImageUtil.getWarehouseImage(Color.yellow),Marker.Type.WAREHOUSE);
                setCenter(pos);
            }
            case TOUR -> tourMarkers.add(new Marker(id, pos,ImageUtil.getMarkerImage(Color.GREEN), type));
            case REQUEST -> requestMarker = new Marker(id, pos,ImageUtil.getMarkerImage(Color.ORANGE), type);
        }
    }

    public void updateTour(List<GeoPosition> tourList){
        this.tour.clear();
        this.tour.addAll(tourList);
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
        mapViewer.removeAll();
        update();
    }

    public void update(){
        List<Painter<JXMapViewer>> painters = new ArrayList<Painter<JXMapViewer>>();

        HashSet<Marker> markers = new HashSet<>();

        //the warehouse
        if(warehouse!=null){
            markers.add(warehouse);
        }
        //the markers
        if(tourMarkers.size() >0){
            markers.addAll(tourMarkers);
        }

        //the request marker
        if(requestMarker!=null){
            markers.add(requestMarker);
        }

        //the tour
        if(tour.size()>1){
            TourPainter tourPainter = new TourPainter(tour);
            painters.add(tourPainter);
        }

        MarkersPainter markersPainter = new MarkersPainter();
        markersPainter.setWaypoints(markers);
        painters.add(markersPainter);
        CompoundPainter<JXMapViewer> painter = new CompoundPainter<JXMapViewer>(painters);
        mapViewer.setOverlayPainter(painter);
    }

    public void recenter(){
//        mapViewer.setAddressLocation(new GeoPosition(45.764043, 4.835659));
//        mapViewer.setZoom(15);
//        if(map.size() == 1){
//            mapViewer.setAddressLocation(getPositions(map).iterator().next());
//        }else if(map.size()>1){
//            mapViewer.zoomToBestFit(getPositions(map),0.7);
//            update();
//            mapViewer.repaint();
//        }else{
//            GeoPosition lyon = new GeoPosition(45.7640, 4.8357);
//            mapViewer.setAddressLocation(lyon);
//            mapViewer.setZoom(6);
//        }

        mapViewer.setZoom(6);
        mapViewer.recenterToAddressLocation();
        update();
    }

    public void setCenter(GeoPosition pos){
        mapViewer.setAddressLocation(pos);
        recenter();
    }

    public HashSet<GeoPosition> getPositions(HashSet<Marker> ms){
        HashSet<GeoPosition> positions = new HashSet<>();
        for(Marker m : ms){
            positions.add(m.getPosition());
        }
        return positions;
    }

}
