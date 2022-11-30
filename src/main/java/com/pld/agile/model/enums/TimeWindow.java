package com.pld.agile.model.enums;

public enum TimeWindow {
  TW_8_9(8, 9),
  TW_9_10(9, 10),
  TW_10_11(10, 11),
  TW_11_12(11, 12);

  private int start;
  private int end;

  /**
   * TImeWindow constructor.
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
   * @return - The ending time of the TimeWindow.
   */
  public int getEnd() {
      return end;
  }

  @Override
  public String toString() {
    return this.start+"h - " + this.end + "h";
  }
}
