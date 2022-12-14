package com.pld.agile.model;

import com.pld.agile.model.enums.TimeWindow;

import java.util.LinkedList;
import java.util.List;

public class Tour {

    /** The courier of the tour */
    private Courier courier;
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
        courier = null;
        deliveryRequests = new LinkedList<>();
        intersections = new LinkedList<>();
        tourDuration = 0.0;
    }

    /**
     *
     * @return - The Courier of the tour
     */
    public Courier getCourier() {
        return this.courier;
    }

    /**
     *
     * @param courier
     */
    public void setCourier(Courier courier) {
        this.courier = courier;
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
    public void setDeliveryRequests(LinkedList<DeliveryRequest> deliveryRequests) {
        this.deliveryRequests.addAll(deliveryRequests);
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
     * @param tourDuration the new value of tourDuration
     * Set the tour duration
     */
    public void setTourDuration(Double tourDuration) { this.tourDuration = tourDuration; }

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
     * @param deliveryRequest - The delivery request to add to the Tour.
     */
    public void addDeliveryRequest(DeliveryRequest deliveryRequest) {
        this.deliveryRequests.add(deliveryRequest);
    }

    /**
     * Removes DeliveryRequest from the Tour.
     * @param indexDeliveryRequest - The delivery request index in the Tour List.
     */
    public void removeDeliveryRequest(int indexDeliveryRequest) {
        this.deliveryRequests.remove(indexDeliveryRequest);
    }

    /**
     * Removes DeliveryRequest from the Tour.
     * @param indexDeliveryRequest - The delivery request index in the Tour List.
     */
    public void updateDeliveryRequest(TimeWindow newTimeWindow, int indexDeliveryRequest) {
        this.deliveryRequests.get(indexDeliveryRequest).setTimeWindow(newTimeWindow);
    }

    /**
     * Adds a new intersection at the end of the Tour.
     * @param newIntersection - The intersection to add to the Tour.
     */
    public void addIntersection(Intersection newIntersection) {
        this.intersections.add(newIntersection);
    }

    @Override
    public String toString() {
        return "Tour{" +
                "courier=" + courier +
                ", deliveryRequests=" + deliveryRequests +
                ", intersections=" + intersections +
                ", tourDuration=" + tourDuration +
                '}';
    }

    public void clearIntersections() {
        this.intersections.clear();
    }
}
