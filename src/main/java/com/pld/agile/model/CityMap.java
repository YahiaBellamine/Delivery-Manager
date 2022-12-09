package com.pld.agile.model;

import com.pld.agile.observer.Observable;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class CityMap extends Observable {

    private Intersection warehouse;
    private List<Tour> tourList;
    private Map<Long, Intersection> intersections;

    public CityMap() {
        this.warehouse = null;
        this.tourList = new LinkedList<>();
        initializeTourList();
        this.intersections = new HashMap<>();
    }

    public Intersection getWarehouse() {
        return this.warehouse;
    }

    public void setWarehouse(Intersection warehouse) {
        this.warehouse = warehouse;
    }

    public List<Tour> getTourList() { return this.tourList; }

    public Map<Long, Intersection> getIntersections() {
        return this.intersections;
    }

    private void initializeTourList(){
        for(int i = 0;  i < Couriers.courierList.size(); i++){
            tourList.add(null);
        }
    }

    public void updateTourList(Tour tour){
        tourList.set(tour.getCourier().getCourierId(), tour);
        notifyObservers(tour);
    }

    public Tour getTour(Courier courier){
        return tourList.get(courier.getCourierId());
    }

    public void addIntersection(Intersection intersection) {
        this.intersections.put(intersection.getId(), intersection);
    }
}
