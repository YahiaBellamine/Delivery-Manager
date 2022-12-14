package com.pld.agile.model;

import org.jxmapviewer.viewer.GeoPosition;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * An intersection on the map.
 */
public class Intersection {
    /** The intersection ID */
    private Long id;
    /** The intersection latitude */
    private double latitude;
    /** The intersection longitude */
    private double longitude;
    /** All the outgoing RoadSegments from the intersection */
    private List<RoadSegment> outgoingSegments= new ArrayList<>();

    /**
     * Intersection constructor.
     * @param id - The id of the Intersection
     * @param latitude - The latitude of the RoadSegment
     * @param longitude - The longitude of the RoadSegment
     */
    public Intersection(Long id,double latitude, double longitude) {
        this.id = id;
        this.latitude=latitude;
        this.longitude=longitude;
    }

    /**
     *
     * @return The ID.
     */
    public long getId() {
        return id;
    }

    /**
     *
     * @param id The ID.
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     *
     * @return The latitude.
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     *
     * @param latitude The latitude.
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     *
     * @return The longitude.
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     *
     * @param longitude The longitude.
     */
    public void setLongitude(double longitude){
        this.longitude=longitude;
    }

    /**
     *
     * @return The list of outgoing RoadSegment.
     */
    public List<RoadSegment> getOutgoingSegments() {
        return outgoingSegments;
    }

    /**
     * Add an outgoing segment to the list of outgoing segments of the intersection
     * @param temp - The RoadSegment that will be added to the outgoing segments of the intersection
     */
    public void addOutgoingSegment(RoadSegment temp)  {
        RoadSegment outgoingSegment=temp;
        outgoingSegments.add(outgoingSegment);
    }

    /**
     * Number of outgoing segments of the intersection.
     * @param intersection - The intersection for which we want to know the number of outgoing segments
     * @return - The number of outgoing segments of the intersection
     */
    public static int sizeOutgoingSegment(Intersection intersection){
        return intersection.outgoingSegments.size();
    }

    /**
     * Compares with another Intersection.
     * @param o The other Intersection.
     * @return True if they have the same ID and the same coordinates
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Intersection)) return false;
        Intersection that = (Intersection) o;
        return getId() == that.getId() && getLatitude()==that.getLatitude() && getLongitude()==that.getLongitude();
    }

    /**
     * Create GeoPosition from latitude and longitude
     * @return - The GeoPosition corresponding to a latitude and longitude
     */
    public GeoPosition getGeoPosition(){
        return new GeoPosition(this.latitude, this.longitude);
    }

    /**
     *
     * @return "Intersection: <i>x</i>"
     */
    @Override
    public String toString() {
        return "Intersection: "+ id;
    }
}

