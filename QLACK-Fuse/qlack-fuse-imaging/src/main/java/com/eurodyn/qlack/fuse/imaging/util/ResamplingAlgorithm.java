package com.eurodyn.qlack.fuse.imaging.util;

/**
 * A list of resampling algorithms to use while resampling an image. Be aware that not just any
 * resampling algorithm is appropriate for just any type of resampling. You need to understand what
 * type of resampling you are about to perform (and some times even the type of image you are
 * working with) to be able to choose the most appropriate algorithm.
 *
 * @author European Dynamics SA.
 */
public enum ResamplingAlgorithm {
  FILTER_UNDEFINED(0),
  FILTER_POINT(1),
  FILTER_BOX(2),
  FILTER_TRIANGLE(3),
  FILTER_HERMITE(4),
  FILTER_HANNING(5),
  FILTER_HAMMING(6),
  FILTER_BLACKMAN(7),
  FILTER_GAUSSIAN(8),
  FILTER_QUADRATIC(9),
  FILTER_CUBIC(10),
  FILTER_CATROM(11),
  FILTER_MITCHELL(12),
  FILTER_LANCZOS(13),
  FILTER_BLACKMAN_BESSEL(14),
  FILTER_BLACKMAN_SINC(15);

  /**
   * Filter value
   */
  private final int val;

  /**
   * Copy constructor
   * @param val filter value
   */
  ResamplingAlgorithm(int val) {
    this.val = val;
  }

  /**
   * Returns current filter value
   * @return the current filter value
   */
  public int getVal() {
    return val;
  }
}
