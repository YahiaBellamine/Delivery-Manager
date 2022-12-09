package com.pld.agile.model;

import com.pld.agile.observer.Observable;

import java.util.LinkedList;
import java.util.List;

public class CityMap extends Observable {

    private Intersection warehouse;
    private List<Tour> tourList;

    public CityMap() {
        this.tourList = new LinkedList<>();
    }

    public CityMap(Intersection warehouse) {
        this.warehouse = warehouse;
        this.tourList = new LinkedList<>();
    }

    public Intersection getWarehouse() {
        return this.warehouse;
    }

    public void setWarehouse(Intersection warehouse) {
        this.warehouse = warehouse;
    }

    public List<Tour> getTourList() { return this.tourList; }

    public void updateTourList(Tour tour){
        if(!tourList.contains(tour)){
            tourList.add(tour.getCourier().getCourierId(), tour);
        } else {
            tourList.set(tour.getCourier().getCourierId(), tour);
        }
        notifyObservers(tour);
    }

//    @Override
//    public Object clone() throws  CloneNotSupportedException{
//        CityMap cityMap=new CityMap(minLatitude,maxLatitude,minLongitude,maxLongitude,warehouse);
//        return  cityMap;
//
//
//    }


}
