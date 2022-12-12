package com.pld.agile.model;

import com.pld.agile.model.enums.TimeWindow;

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
     *
     * @param deliveryTimeWindow - The valid TimeWindow for the delivery request.
     * @param deliveryAddress    - The address of the delivery request.
     */
    public DeliveryRequest(TimeWindow deliveryTimeWindow, Intersection deliveryAddress) {
        timeWindow = deliveryTimeWindow;
        address = deliveryAddress;
        arrivalTime = 0.0;
    }

    /**
     * @return - The valid TimeWindow of the delivery request.
     */
    public TimeWindow getTimeWindow() {
        return timeWindow;
    }

    /**
     * @param - The new TimeWindow of the delivery request.
     */
    public void setTimeWindow(TimeWindow newTimeWindow) {
        this.timeWindow = newTimeWindow;
    }

    /**
     * @return - The address of the delivery request.
     */
    public Intersection getAddress() {
        return address;
    }

    /**
     * @return - The time when the courier will deliver for this delivery request.
     */
    public double getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(Double arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getFormattedArrivalTime() {
        double temp = this.arrivalTime;
        int hours = (int) temp;
        temp -= hours;
        int minutes = (int) (temp * 60);
        temp = temp * 60 - minutes;
        int seconds = (int) (temp * 60);
        return hours + "h" + minutes + "min" + seconds + "s";
    }

    @Override
    public String toString() {
        return "DeliveryRequest{" +
                "timeWindow=" + timeWindow +
                ", address=" + address +
                ", arrivalTime=" + getFormattedArrivalTime() +
                '}';
    }
}
