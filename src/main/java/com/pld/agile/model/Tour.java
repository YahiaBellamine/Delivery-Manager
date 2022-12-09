package com.pld.agile.model;

import java.util.LinkedList;
import java.util.List;

public class Tour {
    /** The list of all delivery requests integrated in the tour */
    private List<DeliveryRequest> deliveryRequests;
    /** The list of all intersections that compose the tour */
    private List<Intersection> intersections;

    /**
     * Default Tour constructor.
     * The delivery requests list and the intersections list start empty.
     */
    public Tour() {
        deliveryRequests = new LinkedList<>();
        intersections = new LinkedList<>();
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
     * @return - The list of all intersections that compose the Tour.
     */
    public List<Intersection> getIntersections() {
        return intersections;
    }

    /**
     * Adds a new delivery request to the Tour.
     * @param newDeliveryRequest - The delivery request to add to the Tour.
     */
    public void addDeliveryRequest(DeliveryRequest newDeliveryRequest) {
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
