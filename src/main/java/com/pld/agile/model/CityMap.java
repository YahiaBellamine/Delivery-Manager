package com.pld.agile.model;


import com.pld.agile.observer.Observable;
import org.jxmapviewer.viewer.GeoPosition;

import java.awt.geom.Point2D;
import java.util.*;

public class CityMap extends Observable {

    private Intersection warehouse;
    private List<Tour> tourList;
    private Map<Long, Intersection> intersections;
    private Long destinationPointId;

    public CityMap() {
        this.warehouse = null;
        initializeTourList();
        this.intersections = new HashMap<>();
    }

    public List<Tour> getTourList() { return this.tourList; }

    public void setTourList(List<Tour> tourList) {
        this.tourList = new LinkedList<>(tourList);
        Couriers.updateCouriers(tourList.size());
        notifyObservers();
    }

    private void initializeTourList(){
        this.tourList = new LinkedList<>();
        tourList.add(null);
        Couriers.updateCouriers(tourList.size());
    }

    public void updateTour(Tour tour){
        tourList.set(tour.getCourier().getCourierId(), tour);
        notifyObservers(tour);
    }

    public Intersection getWarehouse() {
        return this.warehouse;
    }

    public void setWarehouse(Intersection warehouse) {
        this.warehouse = warehouse;
        notifyObservers(warehouse);
    }

    public Map<Long, Intersection> getIntersections() {
        return this.intersections;
    }

    private void updateSelectedPoint(Long destinationPointId) {
        this.destinationPointId = destinationPointId;
    }

    public Tour getTour(Courier courier){
        return tourList.get(courier.getCourierId());
    }

    public void addTour() {
        this.tourList.add(null);
    }

    public void addIntersection(Intersection intersection) {
        this.intersections.put(intersection.getId(), intersection);
    }

    public Intersection searchIntersection(GeoPosition position) {
        double x = position.getLatitude();
        double y = position.getLongitude();
        double r = Double.MAX_VALUE;
        Intersection intersection = null;
        for(Intersection i : this.intersections.values()){
            double dist = Point2D.distance(x,y,i.getLatitude(),i.getLongitude());
            if(dist<r){
                intersection = i;
                r = dist;
            }
        }
        return intersection;
    }

    public void reInitializeCityMap(){
        this.warehouse = null;
        initializeTourList();
        this.intersections = new HashMap<>();
        notifyObservers();
    }
}
