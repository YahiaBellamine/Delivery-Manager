package com.pld.agile.model;

import java.util.LinkedList;
import java.util.List;

public class Tour {
    /** The list of all delivery requests integrated in the tour */
    private List<DeliveryRequest> deliveryRequests;
    /** The list of all intersections that compose the tour */
    private List<Intersection> intersections;
    /** The duration of the tour */
    private Double tourDuration;

    /**
     * Default Tour constructor.
     * The delivery requests list and the intersections list start empty.
     */
    public Tour() {
        deliveryRequests = new LinkedList<>();
        intersections = new LinkedList<>();
        tourDuration = 0.0;
    }

    /**
     *
     * @return - The list of all delivery requests in the Tour.
     */
    public List<DeliveryRequest> getDeliveryRequests() {
        return deliveryRequests;
    }

    /**
     *
     * @return - The list of all delivery requests in the Tour.
     */
    public void setDeliveryRequests(LinkedList<DeliveryRequest> dR) {
        this.deliveryRequests.addAll(dR);
    }

    /**
     *
     * @return - The list of all intersections that compose the Tour.
     */
    public List<Intersection> getIntersections() {
        return intersections;
    }

    /**
     *
     * @return - The duration of this tour.
     */
    public Double getTourDuration() {
        return tourDuration;
    }

    /**
     * @param tD the new value of tourDuration
     * Set the tour duration
     */
    public void setTourDuration(Double tD) { this.tourDuration = tD; }

    public String getFormattedTourDuration() {
        double temp = this.tourDuration;
        int hours = (int)temp;
        temp -= hours;
        int minutes = (int)(temp*60);
        temp = temp*60 - minutes;
        int seconds = (int)(temp*60);
        return hours+"h"+minutes+"min"+seconds+"s";
    }

    /**
     * Adds a new delivery request to the Tour.
     * @param newDeliveryRequest - The delivery request to add to the Tour.
     */
    public void addDeliveryRequests(DeliveryRequest newDeliveryRequest) {
        this.deliveryRequests.add(newDeliveryRequest);
    }

    /**
     * Adds a new intersection at the end of the Tour.
     * @param newIntersection - The intersection to add to the Tour.
     */
    public void addIntersection(Intersection newIntersection) {
        this.intersections.add(newIntersection);
    }
}
