package com.pld.agile.model;

import java.util.Objects;
public class RoadSegment {

    private String name;
    private double length;
    private Intersection destination;

    // constructor
    public RoadSegment(String name, double length, Intersection destination) {
        this.name = name;
        this.length = length;
        this.destination = destination;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }


    public Intersection getDestination() {
        return destination;
    }

    public void setDestination(Intersection destination) {
        this.destination = destination;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RoadSegment)) return false;
        RoadSegment RoadSegment = (RoadSegment) o;
        return Double.compare(RoadSegment.getLength(), getLength()) == 0 && Objects.equals(getName(), RoadSegment.getName()) && Objects.equals(getDestination(), RoadSegment.getDestination());
    }

}