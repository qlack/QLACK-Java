package com.eurodyn.qlack.fuse.cm.util;

import java.io.ByteArrayOutputStream;
import java.util.zip.ZipOutputStream;

/**
 * A Util class for the DocumentService class operations.
 *
 * @author European Dynamics SA
 */
public class ZipOutputStreamUtil {

  /**
   * Private Constructor
   */
  private ZipOutputStreamUtil() {
  }

  /**
   * Creates a new instance of the ZipOutputStream class.
   *
   * @param outStream the output stream
   * @return the ZipOutputStream instance
   */
  public static ZipOutputStream createZipOutputStream(ByteArrayOutputStream outStream) {
    return new ZipOutputStream(outStream);
  }

}
