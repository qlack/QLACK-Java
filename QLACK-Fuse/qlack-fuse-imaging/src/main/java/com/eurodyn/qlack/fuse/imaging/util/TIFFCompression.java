package com.eurodyn.qlack.fuse.imaging.util;

/**
 * Available algorithm to compress TIFFs.
 */
public enum TIFFCompression {
  None("None"),
  CCITT_RLE("CCITT RLE"),
  CCITT_T4("CCITT T.4"),
  CCITT_T6("CCITT T.6"),
  LZW("LZW"),
  JPEG("JPEG"),
  ZLib("ZLib"),
  PackBits("PackBits"),
  Deflate("Deflate");

  private final String val;
  TIFFCompression(String val) {
    this.val = val;
  }

  public String getVal() {
    return val;
  }
}
