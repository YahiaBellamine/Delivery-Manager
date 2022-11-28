package com.pld.agile.model;

public class CityMap {
    private  double minLatitude;
    private  double maxLatitude;
    private  double minLongitude;
    private  double maxLongitude;
    private Intersection warehouse;

    public CityMap(double minLatitude, double maxLatitude, double minLongitude, double maxLongitude, Intersection warehouse) {
        this.minLatitude = minLatitude;
        this.maxLatitude = maxLatitude;
        this.minLongitude = minLongitude;
        this.maxLongitude = maxLongitude;
        this.warehouse = warehouse;
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
