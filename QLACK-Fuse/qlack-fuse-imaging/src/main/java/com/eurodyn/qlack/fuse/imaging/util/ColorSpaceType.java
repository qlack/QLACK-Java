package com.eurodyn.qlack.fuse.imaging.util;

/**
 * The list of detectable color-spaces.
 *
 * @author European Dynamics SA.
 */
@SuppressWarnings("squid:S00115")
public enum ColorSpaceType {
  TYPE_XYZ(0),
  TYPE_Lab(1),
  TYPE_Luv(2),
  TYPE_YCbCr(3),
  TYPE_Yxy(4),
  TYPE_RGB(5),
  TYPE_GRAY(6),
  TYPE_HSV(7),
  TYPE_HLS(8),
  TYPE_CMYK(9),
  TYPE_CMY(11),
  TYPE_2CLR(12),
  TYPE_3CLR(13),
  TYPE_4CLR(14),
  TYPE_5CLR(15),
  TYPE_6CLR(16),
  TYPE_7CLR(17),
  TYPE_8CLR(18),
  TYPE_9CLR(19),
  TYPE_ACLR(20),
  TYPE_BCLR(21),
  TYPE_CCLR(22),
  TYPE_DCLR(23),
  TYPE_ECLR(24),
  TYPE_FCLR(25),
  CS_sRGB(1000),
  CS_LINEAR_RGB(1004),
  CS_CIEXYZ(1001),
  CS_PYCC(1002),
  CS_GRAY(1003);

  /**
   * Color space type value
   */
  private final int val;

  /**
   * Copy constructor
   * @param val the color space type value
   */
  ColorSpaceType(int val) {
    this.val = val;
  }

  /**
   * Returns current color space type value
   * @return the current color space type value
   */
  public int getVal() {
    return val;
  }

  /**
   * Returns the value corresponding name
   * @param val the color space type value
   * @return the value corresponding name
   */
  public static String getReverseVal(int val) {
      for(ColorSpaceType v : values()) {
        if (v.getVal() == val) {
          return v.name();
        }
      }
      throw new IllegalArgumentException();
  }
}
