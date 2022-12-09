package com.pld.agile.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class CityMap {

    private Intersection warehouse;
    private Map<Long, Intersection> intersections = new HashMap<>();
    private List<Tour> tourList = new LinkedList<Tour>();

    public CityMap() {}

    public Intersection getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Intersection warehouse) {
        this.warehouse = warehouse;
    }

    public Map<Long, Intersection> getIntersections() {
        return intersections;
    }

    public void addIntersection(Intersection intersection) {
        this.intersections.put(intersection.getId(), intersection);
    }

    public void addDeliveryRequest(DeliveryRequest deliveryRequest) {
        this.tourList.get(0).addDeliveryRequest(deliveryRequest);
        // Use later
        /* this.tourList.stream().map((tour, index) -> {
            if (index == tour.courierId) {
                tour.addDeliveryRequest(deliveryRequest);
            }
            return tour;
        });*/
    }

}
