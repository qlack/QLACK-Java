package com.eurodyn.qlack.common.util;

import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * An Md5 Checksum Util class.
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
   * @throws IOException exception occurred during the stream parsing
   */
  @SuppressWarnings("squid:S4790")
  public static String getMd5Hex(InputStream inputStream) throws IOException {
    return DigestUtils.md5Hex(inputStream);
  }

}
