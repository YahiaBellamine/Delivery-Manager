package com.pld.agile.model;

import org.jxmapviewer.viewer.GeoPosition;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class Intersection {
    private Long id;
    private double latitude;
    private double longitude;
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude){
        this.longitude=longitude;
    }

    public List<RoadSegment> getOutgoingSegments() {
        return outgoingSegments;
    }

    public void setOutgoingSegments(List<RoadSegment> outgoingSegments) {
        this.outgoingSegments = outgoingSegments;
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

    @Override
    public String toString() {
        return "Intersection: "+ id;
    }
}

