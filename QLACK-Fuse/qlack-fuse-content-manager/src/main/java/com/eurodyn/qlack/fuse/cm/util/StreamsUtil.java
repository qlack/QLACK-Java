package com.eurodyn.qlack.fuse.cm.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.zip.ZipOutputStream;

/**
 * A Util class for the DocumentService class operations.
 *
 * @author European Dynamics SA
 */
public class StreamsUtil {

  /**
   * Private Constructor
   */
  private StreamsUtil() {
  }

  /**
   * Creates a new instance of the ZipOutputStream class.
   *
   * @param outStream the output stream
   * @return the ZipOutputStream instance
   */
  public static ZipOutputStream createZipOutputStream(
    ByteArrayOutputStream outStream) {
    return new ZipOutputStream(outStream);
  }

  /**
   * Creates a new instance of the InputStream class.
   *
   * @param fileContent the byte content
   * @return the InputStream instance
   */
  public static InputStream createInputStream(byte[] fileContent) {
    return new ByteArrayInputStream(fileContent);
  }

}
