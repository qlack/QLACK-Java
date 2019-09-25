package com.eurodyn.qlack.fuse.imaging.dto;

import com.eurodyn.qlack.fuse.imaging.util.ColorSpaceType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * A variety of information about an image.
 *
 * @author European Dynamics SA.
 */
@Getter
@Setter
@ToString
public class ImageInfo {

  /**
   * The total bits per pixel.
   */
  private int bitsPerPixel;

  /**
   * The format of the image. This format is what is returned by the respective ImageFormatHandler
   * class
   */
  private String format;

  /**
   * The height of the image in pixels
   */
  private int height;

  /**
   * The mime-type of the image.
   */
  private String mimeType;

  /**
   * The width of the image in pixels.
   */
  private int width;

  /**
   * The color-type found in the image as represented by ColorSpaceType class.
   */
  private ColorSpaceType colorType;

  /**
   * The DPI of the image.
   */
  private DotsPerInch dotsPerInch;

}
