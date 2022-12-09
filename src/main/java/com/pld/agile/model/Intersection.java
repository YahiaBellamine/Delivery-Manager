package com.pld.agile.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class Intersection {
    private Long id;
    private double latitude;
    private double longitude;
    private List<RoadSegment> outgoingSegments= new ArrayList<>();

    // constructor
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

    // add a new outgoing segment in the list
    public void addOutgoingSegment(RoadSegment temp)  {
        RoadSegment outgoingSegment=temp;
        outgoingSegments.add(outgoingSegment);
    }

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

    @Override
    public String toString() {
        return "Intersection: "+ id;
    }
}

