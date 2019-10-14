package com.eurodyn.qlack.fuse.crypto.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

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
   * @param secret the secret to use
   * @param message the message to hash
   * @return the generated encoding
   * @throws NoSuchAlgorithmException thrown when no algorithm is found for encryption
   * @throws InvalidKeyException thrown when the provided key is invalid
   */
  public String hmacSha256(final String secret, final String message)
      throws NoSuchAlgorithmException, InvalidKeyException {
    final Mac sha256Hmac = Mac.getInstance(HMACSHA256);
    final SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(), HMACSHA256);
    sha256Hmac.init(secretKey);

    return Hex.encodeHexString(sha256Hmac.doFinal(message.getBytes(StandardCharsets.UTF_8)));
  }

  /**
   * Calculates the SHA256 of the given string.
   *
   * @param message the message to hash
   * @return the SHA256 values
   */
  public String sha256(final String message) {
    return DigestUtils.sha256Hex(message);
  }

  /**
   * Calculates the SHA256 of the {@link InputStream}.
   *
   * @param inputStream the input stream to hash
   * @return the SHA256 value
   * @throws IOException thrown when something unexpected happens
   */
  public String sha256(final InputStream inputStream) throws IOException {
    return DigestUtils.sha256Hex(inputStream);
  }

  /**
   * Generates a random seed.
   *
   * @param length The number of bytes of the seed. Note that this is *not* the length of the return
   * value as the return value is Base64 encoded.
   * @return A Base64 encoded version of the generated seed.
   */
  /**
   * Generates a random seed.
   *
   * @param length the number of bytes of the seed. Note that this is *not* the length of the return
   * value as the return value is Base64 encoded
   * @return a Base64 encoded version of the generated seed
   * @throws NoSuchAlgorithmException thrown when no algorithm is found for encryption
   */
  public String generateSecureRandom(int length) throws NoSuchAlgorithmException {
    return Base64.encodeBase64String(SecureRandom.getInstanceStrong().generateSeed(length));
  }
}
