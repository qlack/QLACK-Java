package com.eurodyn.qlack.fuse.imaging.util;

/**
 * Available algorithm to compress TIFFs.
 *
 * @author European Dynamics SA.
 */
public enum TIFFCompression {
  NONE("none"),
  CCITT_RLE("CCITT RLE"),
  CCITT_T4("CCITT T.4"),
  CCITT_T6("CCITT T.6"),
  LZW("LZW"),
  JPEG("JPEG"),
  ZLIB("ZLib"),
  PACKBITS("PackBits"),
  DEFLATE("Deflate");

  /**
   * Algorithm value
   */
  private final String val;

  /**
   * Copy constructor
   *
   * @param val the current algorithm value
   */
  TIFFCompression(String val) {
    this.val = val;
  }

  /**
   * Returns current algorithm value
   *
   * @return the current algorithm value
   */
  public String getVal() {
    return val;
  }
}
