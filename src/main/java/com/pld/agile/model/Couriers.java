package com.pld.agile.model;

import java.util.ArrayList;
import java.util.List;

public class Couriers {

    public static List<Courier> courierList;

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
