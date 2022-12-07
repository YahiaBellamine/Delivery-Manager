package com.pld.agile.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class CityMap {
    private double minLatitude;
    private double maxLatitude;
    private double minLongitude;
    private double maxLongitude;

    private Intersection warehouse;
    private static List<Tour> tourList= new ArrayList(10);

    public CityMap() {
        for (int i = 0; i < 10; i++) {
            Tour t=new Tour();
            tourList.add(t);
        }
    }

    public CityMap(double minLatitude, double maxLatitude, double minLongitude, double maxLongitude, Intersection warehouse) {
        this.minLatitude = minLatitude;
        this.maxLatitude = maxLatitude;
        this.minLongitude = minLongitude;
        this.maxLongitude = maxLongitude;
        this.warehouse = warehouse;
        for (int i = 0; i < 10; i++) {
            Tour t=new Tour();
            tourList.add(t);
        }
        //tourList = new ArrayList<>();
    }

    public double getMinLatitude() {
        return minLatitude;
    }

    public void setMinLatitude(double minLatitude) {
        this.minLatitude = minLatitude;
    }

    public double getMaxLatitude() {
        return maxLatitude;
    }

    public void setMaxLatitude(double maxLatitude) {
        this.maxLatitude = maxLatitude;
    }

    public double getMinLongitude() {
        return minLongitude;
    }

    public void setMinLongitude(double minLongitude) {
        this.minLongitude = minLongitude;
    }

    public double getMaxLongitude() {
        return maxLongitude;
    }

    public void setMaxLongitude(double maxLongitude) {
        this.maxLongitude = maxLongitude;
    }

    public static void setTour(int index, Tour tour) {
        CityMap.tourList.set(index, tour);
    }
    public static Tour getTour(int index){
        return CityMap.tourList.get(index);
    }

    public static List<Tour> getTourList() {
        return tourList;
    }

    public static void setTourList(List<Tour> tourList) {
        CityMap.tourList = tourList;
    }

    public Intersection getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Intersection warehouse) {
        this.warehouse = warehouse;
    }

    public static void DeepCopy(CityMap temp,CityMap cityMap){
        cityMap.warehouse=temp.warehouse;
        cityMap.maxLatitude= temp.maxLatitude;
        cityMap.minLatitude= temp.minLatitude;
        cityMap.maxLongitude=temp.maxLongitude;
        cityMap.minLongitude=temp.minLongitude;
    }
//    @Override
//    public Object clone() throws  CloneNotSupportedException{
//        CityMap cityMap=new CityMap(minLatitude,maxLatitude,minLongitude,maxLongitude,warehouse);
//        return  cityMap;
//
//
//    }


}
