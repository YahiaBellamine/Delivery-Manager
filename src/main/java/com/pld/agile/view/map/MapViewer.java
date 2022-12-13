package com.pld.agile.view.map;

import com.pld.agile.controller.Controller;
import com.pld.agile.model.CityMap;
import com.pld.agile.model.Intersection;
import com.pld.agile.model.Tour;
import com.pld.agile.observer.Observable;
import com.pld.agile.observer.Observer;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.input.CenterMapListener;
import org.jxmapviewer.input.PanMouseInputListener;
import org.jxmapviewer.input.ZoomMouseWheelListenerCursor;
import org.jxmapviewer.painter.CompoundPainter;
import org.jxmapviewer.painter.Painter;
import org.jxmapviewer.viewer.*;

import javax.swing.event.MouseInputListener;
import java.util.List;
import java.awt.*;
import java.util.*;

public class MapViewer implements Observer {
    /** The main panel that contains the map */
    private JXMapViewer mapViewer;

    /** The warehouse Marker */
    private Marker warehouse;

    /** The delivery request marker */
    private Marker requestMarker;

    /** the list of Routes to display for each tour */
    private List<Route> routes;

    /** the CityMap that contains all the model in it */
    private CityMap cityMap;

    /**
     * Initialises the map
     * @param cityMap - The CityMap instance containing all the data
     * @param controller - The Controller instance controlling the map
     * */
    public MapViewer(CityMap cityMap, Controller controller) {
        mapViewer = new JXMapViewer();

        // Create a TileFactoryInfo for OpenStreetMap
        TileFactoryInfo info = new OSMTileFactoryInfo();
        DefaultTileFactory tileFactory = new DefaultTileFactory(info);
        mapViewer.setTileFactory(tileFactory);
        // Use 8 threads in parallel to load the tiles
        tileFactory.setThreadPoolSize(8);

        //add Listeners to move/zoom on the map
        MouseInputListener mia = new PanMouseInputListener(mapViewer);
        mapViewer.addMouseListener(mia);
        mapViewer.addMouseMotionListener(mia);
        mapViewer.addMouseListener(new CenterMapListener(mapViewer));
        mapViewer.addMouseWheelListener(new ZoomMouseWheelListenerCursor(mapViewer));

        //Listener for selecting the delivery request location
        mapViewer.addMouseListener(new MarkerMouseListener(controller));

        mapViewer.setLayout(null);
        this.cityMap = cityMap;
        this.cityMap.addObserver(this);
        routes = new ArrayList<>();
        updateMap();
    }

    /**
     * sets the position of the request marker on the map
     * @param pos - the GeoPosition of the request marker on the map
     * */
    public void setRequestMarker(GeoPosition pos){
        requestMarker = new Marker(pos,ImageUtil.getMarkerImage(Color.ORANGE));
    }

    /**
     * updates the map with the changes made
     * @param o - the object being observed
     * @param arg - the object with undergoing changes
     * */
    public void update(Observable o, Object arg){
        if (arg != null) {
            if(arg instanceof Intersection){
                Intersection wh = cityMap.getWarehouse();
                System.out.println(wh);
                if(wh!=null){
                    warehouse = new Marker(wh.getGeoPosition(),ImageUtil.getWarehouseImage(Color.BLACK));
                    mapViewer.setAddressLocation(wh.getGeoPosition());
                }
            }
            else if(arg instanceof Tour tour){
                updateRoute(tour);
            }
        }
        updateMap();
    }

    /**
     * removes the request marker from the map
     * */
    public void clearRequestMarker(){
        requestMarker = null;
        updateMap();
    }

    /**
     * updates the routes to displayed on the map
     * @param tour - the Tour object to be updated
     * */
    public void updateRoute(Tour tour){
        while(routes.size()<=tour.getCourier().getCourierId()){
            routes.add(new Route(ImageUtil.getColor()));
        }
        routes.get(tour.getCourier().getCourierId()).updateRouteSegments(tour);
    }

    /**
     * repaints the map with all the objects on it
     */
    public void updateMap(){
        List<Painter<JXMapViewer>> painters = new ArrayList<>();
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
            RoutePainter routePainter = new RoutePainter(routes);
            painters.add(routePainter);
        }

        //combine all painters to one painter
        MarkersPainter markersPainter = new MarkersPainter();
        markersPainter.setWaypoints(markers);
        painters.add(markersPainter);
        CompoundPainter<JXMapViewer> painter = new CompoundPainter<>(painters);
        mapViewer.setOverlayPainter(painter);
    }

    /**
     * recenters the map and readjusts the zoom
     * */
    public void recenter(){
        mapViewer.setZoom(6);
        mapViewer.recenterToAddressLocation();
    }

    /**
     * sets the map's center
     * @param pos - the GeoPosition defining the center
     */
    public void setCenter(GeoPosition pos){
        mapViewer.setAddressLocation(pos);
    }

    /**
     *
     * @return the map object
     */
    public JXMapViewer getMapViewer() {
        return mapViewer;
    }
}
