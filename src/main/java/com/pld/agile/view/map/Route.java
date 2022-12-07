package com.pld.agile.view.map;

import org.jxmapviewer.viewer.GeoPosition;
import com.pld.agile.model.Intersection;
import com.pld.agile.model.Tour;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.List;
import java.awt.*;

public class Route {

    /** A list of geo-positions lists to define the route */
    private List<List<GeoPosition>> routeSegments;

    /** The list defining the color of each geo-positions list segments */
    private List<Color> routeColors;

    /**
     * Default Route constructor.
     * The route starts empty.
     */
    public Route() {
        routeSegments = new LinkedList<>();
        routeColors = new ArrayList<>();
    }

    /**
     * Updates the route with the route of the desired tour.
     * @param tour - The tour to update the route with.
     */
    public void updateRouteSegments(Tour tour) {
        routeSegments.clear();
        LinkedList<GeoPosition> segList = new LinkedList<>();

        int deliveryIndex = 0;

        for(Intersection intersection : tour.getIntersections()) {
            GeoPosition geoPos = new GeoPosition(intersection.getLatitude(), intersection.getLongitude());
            segList.add(geoPos);

            if((tour.getDeliveryRequests().get(deliveryIndex).getAddress().getLongitude() == intersection.getLongitude())
                    && (tour.getDeliveryRequests().get(deliveryIndex).getAddress().getLatitude() == intersection.getLatitude())) {
                        routeSegments.add(segList);
                        segList.clear();
                        segList.add(geoPos);
                        ++deliveryIndex;
            }
        }
    }

    /**
     * Changes the color of route segments to the desired color.
     * @param segments - The segments to update.
     * @param color - The color to paint the segments with.
     */
    public void updateRouteColor(List<Integer> segments, Color color) {
        if(!segments.isEmpty()) {
            for(Integer segment : segments) {
                routeColors.set(segment, color);
            }
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
}
