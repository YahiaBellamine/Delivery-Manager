package com.pld.agile.model;

public class Courier {

    private Integer courierId;

    public Courier(Integer courierId){
        this.courierId = courierId;
    }

    public Integer getCourierId() {
        return this.courierId;
    }

    public void setCourierId(Integer courierId) {
        this.courierId = courierId;
    }

    @Override
    public String toString() {
        return "Courier "+(this.courierId + 1);
    }
}
