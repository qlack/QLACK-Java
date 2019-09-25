package com.eurodyn.qlack.fuse.imaging.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Definition of Dots Per Inch (DPI).
 *
 * @author European Dynamics SA.
 */
@Getter
@Setter
@ToString
public class DotsPerInch {

  /**
   * Horizontal DPI.
   */
  private int horizontal;

  /**
   * Vertical DPI.
   */
  private int vertical;
}
