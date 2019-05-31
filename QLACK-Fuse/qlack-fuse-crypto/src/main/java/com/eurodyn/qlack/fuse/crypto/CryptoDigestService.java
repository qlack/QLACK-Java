package com.eurodyn.qlack.fuse.crypto;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Message digest algorithms.
 */
@Service
@Validated
public class CryptoDigestService {

  private static final String HMACSHA256 = "HmacSHA256";

  /**
   * HMAC with SHA256.
   *
   * @param secret The secret to use.
   * @param message The message to hash.
   */
  public String hmacSha256(final String secret, final String message)
  throws NoSuchAlgorithmException, InvalidKeyException {
    final Mac sha256_HMAC = Mac.getInstance(HMACSHA256);
    final SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), HMACSHA256);
    sha256_HMAC.init(secret_key);

    return Hex.encodeHexString(sha256_HMAC.doFinal(message.getBytes(StandardCharsets.UTF_8)));
  }

  /**
   * Calculates the MD5 of the given string.
   *
   * @param message The message to hash.
   * @deprecated Prefer the SHA256 alternatives.
   */
  @Deprecated
  public String md5(final String message) {
    return DigestUtils.md5Hex(message);
  }

  /**
   * Calculates the MD5 of the given {@link InputStream}.
   *
   * @param inpustStream The input stream to hash.
   * @deprecated Prefer the SHA256 alternatives.
   */
  @Deprecated
  public String md5(final InputStream inpustStream) throws IOException {
    return DigestUtils.md5Hex(inpustStream);
  }

  /**
   * Calculates the SHA256 of the given string.
   *
   * @param message The message to hash.
   */
  public String sha256(final String message) {
    return DigestUtils.sha256Hex(message);
  }

  /**
   * Calculates the SHA256 of the {@link InputStream}.
   *
   * @param inputStream The input stream to hash.
   */
  public String sha256(final InputStream inputStream) throws IOException {
    return DigestUtils.sha256Hex(inputStream);
  }
}
