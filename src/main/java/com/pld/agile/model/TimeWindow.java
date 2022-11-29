package com.pld.agile.model;

public class TimeWindow {
    /** TimeWindow starting time */
    private int startTime;
    /** TimeWindow ending time */
    private int endTime;

    /**
     * TImeWindow constructor.
     * @param start - The starting time of the TimeWindow.
     * @param end - The ending time of the TimeWindow.
     */
    public TimeWindow(int start, int end) {
        startTime = start;
        endTime = end;
    }

    /**
     *
     * @return - The starting time of the TimeWindow.
     */
    public int getStartTime() {
        return startTime;
    }

    /**
     *
     * @return - The ending time of the TimeWindow.
     */
    public int getEndTime() {
        return endTime;
    }
}
