package com.eurodyn.qlack.fuse.workflow.util;

import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * A Util class for the ProcessInitService class operations.
 *
 * @author European Dynamics SA
 */
public class Md5ChecksumUtil {

  /**
   * Private Constructor
   */
  private Md5ChecksumUtil() {
  }

  /**
   * Returns the md5 checksum of a given input.
   *
   * @param inputStream the input
   * @return the md5 checksum
   * @throws IOException exception occurred during the stream parsin
   */
  public static String getMd5Hex(InputStream inputStream) throws IOException {
    return DigestUtils.md5Hex(inputStream);
  }

}
