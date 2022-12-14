package com.pld.agile.model;


import com.pld.agile.observer.Observable;
import org.jxmapviewer.viewer.GeoPosition;

import java.awt.geom.Point2D;
import java.util.HashMap;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class CityMap extends Observable {


    private Intersection warehouse;
    private List<Tour> tourList;
    private Map<Long, Intersection> intersections;
    private Long destinationPointId;

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
        notifyObservers(warehouse);
    }

    public List<Tour> getTourList() { return this.tourList; }

    public void setTourList(List<Tour> newTourList) {
        this.tourList = newTourList;
        for(int i = 0; i < newTourList.size(); i++) {
            notifyObservers(newTourList.get(i));
        }
    }

    public Map<Long, Intersection> getIntersections() {
        return this.intersections;
    }

    private void initializeTourList(){
        for(int i = 0;  i < Couriers.courierList.size(); i++){
            tourList.add(null);
        }
    }

    private void updateSelectedPoint(Long destinationPointId) {
        this.destinationPointId = destinationPointId;
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

    public Intersection searchIntersection(GeoPosition position) {
        double x = position.getLatitude();
        double y = position.getLongitude();
        double r = Double.MAX_VALUE;
        Intersection intersection = null;
        for(Intersection i : this.intersections.values()){
            double dist = Point2D.distance(x,y,i.getLatitude(),i.getLongitude());
            if(dist<r){
                System.out.println("dist "+dist+ " "+i.getLatitude()+" "+i.getLongitude());
                intersection = i;
                r = dist;
            }
        }
        return intersection;
    }

    public void reInitializeCityMap(){
        this.warehouse = null;
        this.tourList = new LinkedList<>();
        initializeTourList();
        this.intersections = new HashMap<>();
        System.out.println("CityMap reinitialized");
        notifyObservers();
    }

    public void addTour() {
        this.tourList.add(null);
    }

    @Override
    public String toString() {
        return "CityMap{" +
                "warehouse=" + warehouse +
                ", tourList=" + tourList +
                ", intersections=" + intersections +
                ", destinationPointId=" + destinationPointId +
                '}';
    }
}
