package com.pld.agile.model;

import java.util.ArrayList;
import java.util.List;

public class Couriers {

    public static List<Courier> courierList;

    public Couriers(int nbCouriers){
        this.courierList = new ArrayList<>();
        addCouriers(nbCouriers);
    }

    public void addCouriers(int nbCouriers) {
        int i = 0;
        while (i<nbCouriers){
            this.courierList.add(new Courier(i));
            ++i;
        }
    }
}
