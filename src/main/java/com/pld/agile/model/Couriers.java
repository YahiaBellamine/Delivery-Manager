package com.pld.agile.model;

import java.util.ArrayList;
import java.util.List;

public class Couriers {

    public static List<Courier> courierList;

    public Couriers(){
        courierList = new ArrayList<>();
        courierList.add(new Courier(0));
    }

    public static void addCourier() {
        courierList.add(new Courier(courierList.size()));
    }
}
