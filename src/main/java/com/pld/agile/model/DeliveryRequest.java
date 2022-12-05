package com.pld.agile.model;

import com.pld.agile.model.enums.TimeWindow;

public class DeliveryRequest {
    /** The TimeWindow of the delivery request */
    private TimeWindow timeWindow;
    /** The address of the delivery request */
    private Intersection address;
    /** The passing time through the delivery address */
    private Double passingTime;

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
     * @return - The time when the courier will deliver for this delivery request.
     */
    public double getPassingTime() {
        return passingTime;
    }

    public void setPassingTime(Double passingTime) { this.passingTime = passingTime; }

    public String getFormattedPassingTime() {
        double temp = this.passingTime;
        int hours = (int)temp;
        temp -= hours;
        int minutes = (int)(temp*60);
        temp = temp*60 - minutes;
        int seconds = (int)(temp*60);
        return hours+"h"+minutes+"min"+seconds+"s";
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
