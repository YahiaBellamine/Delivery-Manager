package com.pld.agile.model;

import com.pld.agile.model.enums.TimeWindow;

public class DeliveryRequest {
    /** The TimeWindow of the delivery request */
    private TimeWindow timeWindow;
    /** The address of the delivery request */
    private Intersection address;
    /** The passing time through the delivery address */
    private double passingTime;

    /**
     * DeliveryRequest constructor.
     * @param deliveryTime - The valid TimeWindow for the delivery request.
     * @param deliveryAddress - The address of the delivery request.
     */
    public DeliveryRequest(TimeWindow deliveryTime, Intersection deliveryAddress) {
        timeWindow = deliveryTime;
        address = deliveryAddress;
        passingTime = 0.0;
    }

    /**
     *
     * @return - The valid TimeWindow of the delivery request.
     */
    public TimeWindow getTimeWindow() {
        return timeWindow;
    }

    /**
     *
     * @return - The address of the delivery request.
     */
    public Intersection getAddress() {
        return address;
    }

    /**
     *
     * @return - The address of the delivery request.
     */
    public double getPassingTime() {
        return passingTime;
    }

    public String getFormattedPassingTime() {
        return "";
    }

    @Override
    public String toString() {
        return "DeliveryRequest{" +
                "timeWindow=" + timeWindow +
                ", address=" + address +
                ", passingTime=" + getFormattedPassingTime() +
                '}';
    }
}
