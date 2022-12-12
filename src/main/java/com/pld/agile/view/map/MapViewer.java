package com.pld.agile.view.map;

import com.pld.agile.controller.Controller;
import com.pld.agile.model.CityMap;
import com.pld.agile.model.Intersection;
import com.pld.agile.model.Tour;
import com.pld.agile.observer.Observable;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.input.CenterMapListener;
import org.jxmapviewer.input.PanKeyListener;
import org.jxmapviewer.input.PanMouseInputListener;
import org.jxmapviewer.input.ZoomMouseWheelListenerCursor;
import org.jxmapviewer.painter.CompoundPainter;
import org.jxmapviewer.painter.Painter;
import javax.swing.event.MouseInputListener;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import org.jxmapviewer.viewer.*;
import javax.swing.*;
import java.awt.*;
import java.util.*;


import com.pld.agile.observer.Observer;

public class MapViewer implements Observer {
    public JPanel mainPanel;
    public JPanel mapPanel;
    public JPanel centerPanel;
    public JXMapViewer mapViewer;

    private Marker warehouse;

    private Marker requestMarker;

    private List<Route> routes;
    private CityMap cityMap;

    public MapViewer(CityMap cityMap, Controller controller) {
        super();
        this.cityMap = cityMap;
        this.cityMap.addObserver(this);
        routes = new ArrayList<>();

        // Add interactions
        MouseInputListener mia = new PanMouseInputListener(mapViewer);
        mapViewer.addMouseListener(mia);
        mapViewer.addMouseMotionListener(mia);

        mapViewer.addMouseListener(new CenterMapListener(mapViewer));

        mapViewer.addMouseWheelListener(new ZoomMouseWheelListenerCursor(mapViewer));
        mapViewer.addMouseListener(new MarkerMouseListener(controller));

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

    public void setRequestMarker(GeoPosition pos){
        requestMarker = new Marker(pos,ImageUtil.getMarkerImage(Color.ORANGE));
    }

    public void update(Observable o, Object arg){
        /* TODO : Display tour list in the graphical view  */
        if (arg != null) {
            if(arg instanceof Intersection){
                Intersection wh = cityMap.getWarehouse();
                System.out.println(wh);
                if(wh!=null){
                    warehouse = new Marker(wh.getGeoPosition(),ImageUtil.getWarehouseImage(Color.BLACK));
                    mapViewer.setAddressLocation(wh.getGeoPosition());
                }
            }
            else if(arg instanceof Tour){
                Tour tour = (Tour) arg;
                if(tour!=null){
                    updateRoute(tour);
                }
            }
        }
        update();
    }

//    public void updateTour(List<GeoPosition> tourList){
//        this.tour.clear();
//        this.tour.addAll(tourList);
//        update();
//    }
//
//    public List<GeoPosition> getTour(){
//        return this.tour;
//    }
//
//    public void clearMarkers(){
//        tourMarkers.clear();
//        tour.clear();
//        requestMarker = null;
//        update();
//    }

    public void clearRequestMarker(){
        requestMarker = null;
        update();
    }

    public void clearAll(){
        warehouse = null;
        requestMarker = null;
        routes.clear();
        mapViewer.removeAll();
        update();
    }

    public void updateRoute(Tour tour){
        while(routes.size()<=tour.getCourier().getCourierId()){
            routes.add(new Route(ImageUtil.getColor()));
        }
        routes.get(tour.getCourier().getCourierId()).updateRouteSegments(tour);
    }
    public void update(){
        List<Painter<JXMapViewer>> painters = new ArrayList<Painter<JXMapViewer>>();

        HashSet<Marker> markers = new HashSet<>();

        //the warehouse
        if(warehouse!=null){
            markers.add(warehouse);
        }

        //the request marker
        if(requestMarker!=null){
            markers.add(requestMarker);
        }

        //the tour
        if(routes.size()>0){
            TourPainter tourPainter = new TourPainter(routes);
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

    public List<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(List<Route> routes) {
        this.routes = routes;
    }
}
