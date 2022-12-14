package com.pld.agile.model;


import com.pld.agile.observer.Observable;
import org.jxmapviewer.viewer.GeoPosition;

import java.awt.geom.Point2D;
import java.util.*;

/**
 * The main class in the model package, containing all the data.
 */
public class CityMap extends Observable {

    /** the intersection representing the warehouse */
    private Intersection warehouse;

    /** The list of Tour */
    private List<Tour> tourList;
    /** a HashMap containing all the intersections with their IDs as keys */
    private Map<Long, Intersection> intersections;

    /**
     * Default CityMap constructor.
     * The intersections list is empty.
     * The warehouse is null.
     */
    public CityMap() {
        this.warehouse = null;
        initializeTourList();
        this.intersections = new HashMap<>();
    }

    /**
     *
     * @return The list of Tour.
     */
    public List<Tour> getTourList() { return this.tourList; }

    /**
     * Set the list of Tour in the CityMap.
     * @param tourList The list of Tour.
     */
    public void setTourList(List<Tour> tourList) {
        this.tourList = new LinkedList<>(tourList);
        Couriers.updateCouriers(tourList.size());
        notifyObservers();
    }

    /**
     * Initialize the list of Tour and synchronize the number of couriers accordingly.
     */
    private void initializeTourList(){
        this.tourList = new LinkedList<>();
        tourList.add(null);
        Couriers.updateCouriers(tourList.size());
    }

    /**
     * Update a tour in the list of tours of the CityMap
     * @param tour- The tour that needs to be updated
     */
    public void updateTour(Tour tour){
        tourList.set(tour.getCourier().getCourierId(), tour);
        notifyObservers(tour);
    }

    /**
     *
     * @return The warehouse Intersection
     */
    public Intersection getWarehouse() {
        return this.warehouse;
    }

    /**
     *
     * @param warehouse The warehouse Intersection.
     */
    public void setWarehouse(Intersection warehouse) {
        this.warehouse = warehouse;
        notifyObservers(warehouse);
    }

    /**
     *
     * @return the HashMap of all the intersection in this format : {id - Intersection}
     */
    public Map<Long, Intersection> getIntersections() {
        return this.intersections;
    }

    /**
     * Returns the tour of a particular courier.
     * @param courier The courier.
     * @return The tour of the specified courier.
     */
    public Tour getTour(Courier courier){
        return tourList.get(courier.getCourierId());
    }

    /**
     * Add a tour to the list of tours of the CityMap.
     */
    public void addTour() {
        this.tourList.add(null);
    }

    /**
     * Add intersection to the list of intersections of the CityMap.
     * @param intersection The intersection that needs to be added.
     */
    public void addIntersection(Intersection intersection) {
        this.intersections.put(intersection.getId(), intersection);
    }

    /**
     * Search intersection corresponding to a GeoPosition.
     * @param position Geoposition from which we find the corresponding intersection.
     * @return The intersection corresponding to the GeoPosition.
     */
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

    /**
     * Reinitilize the attributes of the CityMap. Wareshouse is null, list of tour is empty and intersections is empty.
     */
    public void reInitializeCityMap(){
        this.warehouse = null;
        initializeTourList();
        this.intersections = new HashMap<>();
        notifyObservers();
    }

    /**
     *
     * @return "CityMap{warehouse=<i>....</i>, tourList=<i>....</i>, intersections=<i>....</i>}"
     */
    @Override
    public String toString() {
        return "CityMap{" +
                "warehouse=" + warehouse +
                ", tourList=" + tourList +
                ", intersections=" + intersections +
                '}';
    }
}
