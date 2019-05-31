package com.eurodyn.qlack.fuse.aaa.util;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Provides the legacy MD5 password encoder for AAA.
 */
public class Md5PasswordEncoder implements PasswordEncoder {

  @Override
  public String encode(CharSequence rawPassword) {
    return DigestUtils.md5Hex(rawPassword.toString());
  }

  /**
   * Constant time comparison to prevent against timing attacks.
   */
  @Override
  public boolean matches(CharSequence rawPassword, String encodedPassword) {
    String rawHash = DigestUtils.md5Hex(rawPassword.toString());
    byte[] expected = Hex.decode(rawHash);
    byte[] actual = Hex.decode(encodedPassword);

    if (expected.length != actual.length) {
      return false;
    }

    int result = 0;

    for (int i = 0; i < expected.length; i++) {
      result |= expected[i] ^ actual[i];
    }

    return result == 0;
  }
}
