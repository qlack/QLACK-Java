package com.eurodyn.qlack.fuse.fileupload.util;

import java.io.ByteArrayOutputStream;

/**
 * A Util class for the FileUpload implementation operations.
 *
 * @author European Dynamics SA
 */
public class ByteArrayOutputStreamUtil {

  /**
   * Private Constructor
   */
  private ByteArrayOutputStreamUtil() {
  }

  /**
   * Creates a new instance of the ByteArrayOutputStream class for getting the content of a file.
   *
   * @param fileSize the expected size of the file
   * @return the ByteArrayOutputStream instance
   */
  public static ByteArrayOutputStream createByteArrayOutputStream(long fileSize) {
    return new ByteArrayOutputStream((int) fileSize);
  }

}
