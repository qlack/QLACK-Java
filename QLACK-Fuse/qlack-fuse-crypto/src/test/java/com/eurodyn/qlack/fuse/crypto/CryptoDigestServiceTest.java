package com.eurodyn.qlack.fuse.crypto;

import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static org.junit.Assert.assertNotNull;

public class CryptoDigestServiceTest {

  private CryptoDigestService cryptoDigestService = new CryptoDigestService();
  private String secret;
  private String message;

  private ByteArrayInputStream getByteArrayOutputStream() throws IOException {
    BufferedImage bImage = ImageIO.read(this.getClass().getResource("/file-binary.jpg"));
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    ImageIO.write(bImage, "jpg", bos );
    bos.toByteArray();
    return new ByteArrayInputStream(bos.toByteArray());
  }

  @Test
  public void hmacSha256Test() throws InvalidKeyException, NoSuchAlgorithmException {
    secret = "secret";
    message = "message";
    assertNotNull(cryptoDigestService.hmacSha256(secret, message));
  }

  @Test
  public void md5Test() {
    message = "message";
    assertNotNull(cryptoDigestService.md5(message));
  }

  @Test
  public void md5InputStreamTest() throws IOException {
    assertNotNull(cryptoDigestService.md5(getByteArrayOutputStream()));
  }

  @Test
  public void sha256Test() {
    message = "message";
    assertNotNull(cryptoDigestService.sha256(message));
  }

  @Test
  public void sha256InputStreamTest() throws IOException {
    assertNotNull(cryptoDigestService.sha256(getByteArrayOutputStream()));
  }

  @Test
  public void generateSecureRandomTest() throws NoSuchAlgorithmException {
    assertNotNull(cryptoDigestService.generateSecureRandom(200));
  }
}
