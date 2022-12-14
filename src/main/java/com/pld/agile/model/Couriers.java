package com.pld.agile.model;

import java.util.ArrayList;
import java.util.List;

public class Couriers {

    public static List<Courier> courierList;

    public Couriers(){
        courierList = new ArrayList<>();
        addCourier();
    }

    public static void addCourier() {
        courierList.add(new Courier(courierList.size()));
    }

    public static void updateCouriers(int nbCouriers) {
        courierList.clear();
        for (int i = 0; i < nbCouriers; i++) {
            addCourier();
        }
    }

}
