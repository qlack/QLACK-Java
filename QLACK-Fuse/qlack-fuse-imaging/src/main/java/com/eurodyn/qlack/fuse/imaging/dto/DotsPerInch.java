package com.eurodyn.qlack.fuse.imaging.dto;

/**
 * Definition of Dots Per Inch (DPI).
 */
public class DotsPerInch {
  // Horizontal DPI.
  private int horizontal;

  // Vertical DPI.
  private int vertical;

  public int getHorizontal() {
    return horizontal;
  }

  public void setHorizontal(int horizontal) {
    this.horizontal = horizontal;
  }

  public int getVertical() {
    return vertical;
  }

  public void setVertical(int vertical) {
    this.vertical = vertical;
  }

  @Override
  public String toString() {
    return "DotsPerInch{" +
      "horizontal=" + horizontal +
      ", vertical=" + vertical +
      '}';
  }
}
