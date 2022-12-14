package com.pld.agile.model;

import java.util.Objects;

/**
 * The road segment connecting two intersections.
 */
public class RoadSegment {

    /** The road name */
    private String name;
    /** The length in meters */
    private double length;
    /** the intersection of destination  */
    private Intersection destination;

    /**
     * RoadSegment constructor.
     * @param name - The name of the RoadSegment
     * @param length - The length of the RoadSegment
     * @param destination - The destination of the RoadSegment
     */
    public RoadSegment(String name, double length, Intersection destination) {
        this.name = name;
        this.length = length;
        this.destination = destination;
    }

    /**
     *
     * @return The road name.
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name The road name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return The road length in meters.
     */
    public double getLength() {
        return length;
    }

    /**
     *
     * @param length The road length in meters.
     */
    public void setLength(double length) {
        this.length = length;
    }

    /**
     *
     * @return The intersection of destination.
     */
    public Intersection getDestination() {
        return destination;
    }

    /**
     *
     * @param destination The intersection of destination.
     */
    public void setDestination(Intersection destination) {
        this.destination = destination;
    }

    /**
     * Compares two RoadSegment.
     * @param o The other road segment.
     * @return true if the roads names, lengths, and destinations are the same.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RoadSegment)) return false;
        RoadSegment RoadSegment = (RoadSegment) o;
        return Double.compare(RoadSegment.getLength(), getLength()) == 0 && Objects.equals(getName(), RoadSegment.getName()) && Objects.equals(getDestination(), RoadSegment.getDestination());
    }

}