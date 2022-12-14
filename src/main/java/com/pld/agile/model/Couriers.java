package com.pld.agile.model;

import java.util.ArrayList;
import java.util.List;

/**
 * A list of couriers.
 * */
public class Couriers {

    /** The list of couriers */
    public static List<Courier> courierList;

    /**
     * Default constructor.
     */
    public Couriers(){
        courierList = new ArrayList<>();
        addCourier();
    }

    /**
     * Add Courier to the Couriers list
     */
    public static void addCourier() {
        courierList.add(new Courier(courierList.size()));
    }

    /**
     * Adjust the size of the Couriers list
     * @param nbCouriers- Size of the Couriers list
     */
    public static void updateCouriers(int nbCouriers) {
        courierList.clear();
        for (int i = 0; i < nbCouriers; i++) {
            addCourier();
        }
    }

}
