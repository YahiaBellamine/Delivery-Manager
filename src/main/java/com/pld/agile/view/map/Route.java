package com.pld.agile.view.map;

import com.pld.agile.model.DeliveryRequest;
import org.jxmapviewer.viewer.GeoPosition;
import com.pld.agile.model.Intersection;
import com.pld.agile.model.Tour;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.List;
import java.awt.*;

/**
 * the Route contains the Tour's itinerary and markers for each delivery request in the Tour
 */
public class Route {

    /** A list of geo-positions lists to define the route */
    private List<List<GeoPosition>> routeSegments;

    /** The list defining the color of each geo-positions list segments */
    private List<Color> routeColors;

    /** A list of Markers to define the delivery requests */
    private List<Marker> routeMarkers;


    /** the default color for the segments */
    private Color defaultColor;

    /**
     * Default Route constructor.
     * The route starts empty.
     * @param c - the color of the route
     */
    public Route(Color c) {
        routeSegments = new LinkedList<>();
        routeColors = new ArrayList<>();
        routeMarkers = new ArrayList<>();
        defaultColor = c;
        reinitColors();
    }

    /**
     * Updates the route with the route of the desired tour.
     * @param tour - The tour to update the route with.
     */
    public void updateRouteSegments(Tour tour) {
        routeSegments.clear();
        routeMarkers.clear();
        LinkedList<GeoPosition> segList = new LinkedList<>();

        int deliveryIndex = 0;

        for(Intersection intersection : tour.getIntersections()) {
            GeoPosition geoPos = new GeoPosition(intersection.getLatitude(), intersection.getLongitude());
            segList.add(geoPos);

            if(deliveryIndex<tour.getDeliveryRequests().size() &&
            (tour.getDeliveryRequests().get(deliveryIndex).getAddress().getId() == intersection.getId())) {
                        routeSegments.add(segList);
                        segList = new LinkedList<>();
                        ++deliveryIndex;
            }
        }
        routeSegments.add(segList);
        for(DeliveryRequest delivery : tour.getDeliveryRequests()){
            Intersection i = delivery.getAddress();
            GeoPosition gp = new GeoPosition(i.getLatitude(),i.getLongitude());
            routeMarkers.add(new Marker(gp,ImageUtil.getMarkerImage(defaultColor)));
        }
        routeColors.clear();
        for(int i = 0; i<routeSegments.size(); i++){
            routeColors.add(defaultColor);
        }
    }

    /**
     * Changes the color of route segments to the desired color.
     * @param segments - The segments to update.
     * @param color - The color to paint the segments with.
     */
    public void updateRouteColor(List<Integer> segments, Color color) {
        reinitColors();
        if(!segments.isEmpty()) {
            for(Integer segment : segments) {
                routeColors.set(segment, color);
                if(segment < routeMarkers.size()){
                    routeMarkers.get(segment).setImg(ImageUtil.getMarkerImage(color));
                    routeColors.set(segment, color);
                }
                if(segment-1 < routeMarkers.size() && segment>-1){
                    routeMarkers.get(segment-1).setImg(ImageUtil.getMarkerImage(color));
                }
//                routeMarkersColors.set(segment,color);
            }
        }
    }

    /**
     * reinitializes the route color to default
     */
    public void reinitColors(){
        for(Marker m : routeMarkers){
            m.setImg(ImageUtil.getMarkerImage(defaultColor));
        }
        routeColors.clear();
        for(int i =0; i<routeSegments.size(); i++){
            routeColors.add(defaultColor);
        }
    }

    /**
     *
     * @return The list of route segments of the Route.
     */
    public List<List<GeoPosition>> getRouteSegments() {
        return routeSegments;
    }

    /**
     *
     * @return The list of colors for each route segment.
     */
    public List<Color> getRouteColors() {
        return routeColors;
    }

    /**
     *
     * @return The list of Markers.
     */
    public List<Marker> getRouteMarkers() {
        return routeMarkers;
    }

    public Color getDefaultColor() {
        return defaultColor;
    }
}
