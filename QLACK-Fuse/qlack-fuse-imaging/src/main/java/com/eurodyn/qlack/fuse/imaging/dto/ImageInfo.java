package com.eurodyn.qlack.fuse.imaging.dto;

import com.eurodyn.qlack.fuse.imaging.util.ColorSpaceType;

/**
 * A variety of information about an image.
 */
public class ImageInfo {
  // The total bits per pixel.
  private int bitsPerPixel;

  // The format of the image. This format is what is returned by the respective ImageFormatHandler
  // class.
  private String format;

  // The height of the image in pixels.
  private int height;

  // The mime-type of the image.
  private String mimeType;

  // The width of the image in pixels.
  private int width;

  // The color-type found in the image as represented by ColorSpaceType class.
  private ColorSpaceType colorType;

  // The DPI of the aimge.
  private DotsPerInch dotsPerInch;

  public int getBitsPerPixel() {
    return bitsPerPixel;
  }

  public void setBitsPerPixel(int bitsPerPixel) {
    this.bitsPerPixel = bitsPerPixel;
  }

  public String getFormat() {
    return format;
  }

  public void setFormat(String format) {
    this.format = format;
  }

  public int getHeight() {
    return height;
  }

  public void setHeight(int height) {
    this.height = height;
  }

  public String getMimeType() {
    return mimeType;
  }

  public void setMimeType(String mimeType) {
    this.mimeType = mimeType;
  }

  public int getWidth() {
    return width;
  }

  public void setWidth(int width) {
    this.width = width;
  }

  public ColorSpaceType getColorType() {
    return colorType;
  }

  public void setColorType(ColorSpaceType colorType) {
    this.colorType = colorType;
  }

  public DotsPerInch getDotsPerInch() {
    return dotsPerInch;
  }

  public void setDotsPerInch(DotsPerInch dotsPerInch) {
    this.dotsPerInch = dotsPerInch;
  }

  @Override
  public String toString() {
    return "ImageInfo{" +
      "bitsPerPixel=" + bitsPerPixel +
      ", format='" + format + '\'' +
      ", height=" + height +
      ", mimeType='" + mimeType + '\'' +
      ", width=" + width +
      ", colorType='" + colorType + '\'' +
      ", dotsPerInch=" + dotsPerInch +
      '}';
  }
}
