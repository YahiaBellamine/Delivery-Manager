package com.pld.agile.model;

import com.pld.agile.model.enums.TimeWindow;

/**
 * A delivery request.
 */
public class DeliveryRequest {
    /**
     * The TimeWindow of the delivery request
     */
    private TimeWindow timeWindow;
    /**
     * The address of the delivery request
     */
    private final Intersection address;
    /**
     * The arrival time through the delivery address
     */
    private Double arrivalTime;

    /**
     * DeliveryRequest constructor.
     * @param deliveryTimeWindow The valid TimeWindow for the delivery request.
     * @param deliveryAddress    The address of the delivery request.
     */
    public DeliveryRequest(TimeWindow deliveryTimeWindow, Intersection deliveryAddress) {
        timeWindow = deliveryTimeWindow;
        address = deliveryAddress;
        arrivalTime = 0.0;
    }
    /**
     * DeliveryRequest constructor.
     * @param deliveryTimeWindow  The valid TimeWindow for the delivery request.
     * @param deliveryAddress     The address of the delivery request.
     * @param deliveryArrivalTime The arrival time of the delivery request.
     */
    public DeliveryRequest(TimeWindow deliveryTimeWindow, Intersection deliveryAddress, Double deliveryArrivalTime) {
        timeWindow = deliveryTimeWindow;
        address = deliveryAddress;
        arrivalTime = deliveryArrivalTime;
    }

    /**
     * @return The valid TimeWindow of the delivery request.
     */
    public TimeWindow getTimeWindow() {
        return timeWindow;
    }

    /**
     * TimeWindow setter
     * @param newTimeWindow The new TimeWindow of the delivery request.
     */
    public void setTimeWindow(TimeWindow newTimeWindow) {
        this.timeWindow = newTimeWindow;
    }

    /**
     * Address getter
     * @return The address of the delivery request.
     */
    public Intersection getAddress() {
        return address;
    }

    /**
     * ArrivalTime getter
     * @return The time when the courier will deliver for this delivery request.
     */
    public double getArrivalTime() {
        return arrivalTime;
    }

    /**
     * ArrivalTime setter
     * @param arrivalTime The arrival time of the delivery request
     */
    public void setArrivalTime(Double arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    /**
     * Returns The arrival time.
     * @return The arrival time formatted : "<i>x</i>h<i>y</i>min<i>z</i>s"
     */
    public String getFormattedArrivalTime() {
        double temp = this.arrivalTime;
        int hours = (int) temp;
        temp -= hours;
        int minutes = (int) (temp * 60);
        temp = temp * 60 - minutes;
        int seconds = (int) (temp * 60);
        return hours + "h" + minutes + "min" + seconds + "s";
    }

    /**
     *
     * @return String describing the delivery request.
     */
    @Override
    public String toString() {
        return "DeliveryRequest{" +
                "timeWindow=" + timeWindow +
                ", address=" + address +
                ", arrivalTime=" + getFormattedArrivalTime() +
                '}';
    }
}
