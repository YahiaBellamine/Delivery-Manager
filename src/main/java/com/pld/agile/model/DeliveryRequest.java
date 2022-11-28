package com.pld.agile.model;

public class DeliveryRequest {
    /** The TimeWindow of the delivery request */
    private TimeWindow timeWindow;
    /** The address of the delivery request */
    private Intersection address;

    /**
     * DeliveryRequest constructor.
     * @param deliveryTime - The valid TimeWindow for the delivery request.
     * @param deliveryAddress - The address of the delivery request.
     */
    public DeliveryRequest(TimeWindow deliveryTime, Intersection deliveryAddress) {
        timeWindow = deliveryTime;
        address = deliveryAddress;
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
}
