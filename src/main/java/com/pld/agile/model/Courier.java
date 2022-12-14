package com.pld.agile.model;

/**
 * The courier.
 */
public class Courier {

    /** The courier ID. */
    private final Integer courierId;

    /**
     * Default constructor.
     * */
    public Courier(Integer courierId){
        this.courierId = courierId;
    }

    /**
     *
     * @return The courier ID.
     */
    public Integer getCourierId() {
        return this.courierId;
    }

    /**
     *
     * @return "Courier <i>x</i>"
     */
    @Override
    public String toString() {
        return "Courier "+(this.courierId + 1);
    }
}
