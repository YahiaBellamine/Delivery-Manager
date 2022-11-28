package com.pld.agile.model;

import java.sql.Timestamp;

public class TimeWindow {
    /** TimeWindow starting time */
    private Timestamp startTime;
    /** TimeWindow ending time */
    private Timestamp endTime;

    /**
     * TImeWindow constructor.
     * @param start - The starting time of the TimeWindow.
     * @param end - The ending time of the TimeWindow.
     */
    public TimeWindow(Timestamp start, Timestamp end) {
        startTime = start;
        endTime = end;
    }

    /**
     *
     * @return - The starting time of the TimeWindow.
     */
    public Timestamp getStartTime() {
        return startTime;
    }

    /**
     *
     * @return - The ending time of the TimeWindow.
     */
    public Timestamp getEndTime() {
        return endTime;
    }
}
