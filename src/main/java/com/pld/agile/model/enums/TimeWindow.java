package com.pld.agile.model.enums;

/**
 * The time window of a delivery request.
 */
public enum TimeWindow {
  TW_8_9(8, 9),
  TW_9_10(9, 10),
  TW_10_11(10, 11),
  TW_11_12(11, 12);

  /** the start hour of the time window */
  private final int start;
  /** the end hour of the time window */
  private final int end;

  /**
   * TimeWindow constructor.
   * @param start - The starting time of the TimeWindow.
   * @param end - The ending time of the TimeWindow.
   */
  TimeWindow(int start, int end) {
      this.start = start;
      this.end = end;
  }

  /**
   *
   * @return - The starting time of the TimeWindow.
   */
  public int getStart() {
      return start;
  }

  /**
   *
   * @return The ending time of the TimeWindow.
   */
  public int getEnd() {
      return end;
  }

  /**
   * Compares with another TimeWindow.
   * @param timeWindow The other TimeWindow.
   * @return -1 if this time window is before the other time window, 0 if they start at the same time, 1 if the time window is after the other time window
   */
  public int isBefore(TimeWindow timeWindow){
    if(this.start < timeWindow.start){
      return -1;
    } else if (this.start > timeWindow.start) {
      return 1;
    }
    return 0;
  }

  /**
   *
   * @return The start and the end of the time window in this format " <i>x</i> h - <i>x</i> h"
   */
  @Override
  public String toString() {
    return this.start+"h - " + this.end + "h";
  }
}
