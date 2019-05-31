package com.eurodyn.qlack.fuse.crypto;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class CryptoSymmetricServiceTest {

  private CryptoSymmetricService cryptoSymmetricService = new CryptoSymmetricService();

  @Test
  public void generateKey() throws NoSuchAlgorithmException {
    final String key = Base64.encodeBase64String(cryptoSymmetricService.generateKey(128, "AES"));
    assertNotNull(key);
  }

  @Test
  public void keyFromString() throws NoSuchAlgorithmException {
    final String key = Base64.encodeBase64String(cryptoSymmetricService.generateKey(128, "AES"));
    final SecretKey secretKey = cryptoSymmetricService.keyFromString(key, "AES");
    assertNotNull(key);
    assertNotNull(secretKey);
    assertEquals(key, Base64.encodeBase64String(secretKey.getEncoded()));
  }

  @Test
  public void generateIV() {
    final byte[] iv = cryptoSymmetricService.generateIV();
    assertNotNull(iv);
    assertEquals(iv.length, 16);
  }

  @Test
  public void ivFromString() {
    final byte[] iv = cryptoSymmetricService.generateIV();
    String ivStr = Base64.encodeBase64String(iv);
    assertEquals(ivStr, Base64.encodeBase64String(cryptoSymmetricService.ivFromString(ivStr)));
  }

  @Test
  public void encryptDecrypt()
  throws NoSuchAlgorithmException, InvalidKeyException, InvalidAlgorithmParameterException,
         NoSuchPaddingException, IOException {
    String plaintext = "Hello world!";
    final String aes = Base64.encodeBase64String(cryptoSymmetricService.generateKey(128, "AES"));
    final SecretKey aesKey = cryptoSymmetricService.keyFromString(aes, "AES");
    final byte[] iv = cryptoSymmetricService.generateIV();

    // No IV-append test.
    byte[] ciphertext = cryptoSymmetricService
      .encrypt(plaintext.getBytes(StandardCharsets.UTF_8), aesKey, iv, "AES/CBC/PKCS5Padding",
        "AES", false);
    assertNotNull(ciphertext);
    byte[] plaintextDecrypted = cryptoSymmetricService
      .decrypt(ciphertext, aesKey, iv, "AES/CBC/PKCS5Padding", "AES");
    assertEquals(plaintext, new String(plaintextDecrypted, StandardCharsets.UTF_8));


    // IV-append test.
    ciphertext = cryptoSymmetricService
      .encrypt(plaintext.getBytes(StandardCharsets.UTF_8), aesKey, iv, "AES/CBC/PKCS5Padding",
        "AES", true);
    assertNotNull(ciphertext);
    plaintextDecrypted = cryptoSymmetricService
      .decrypt(ciphertext, aesKey, "AES/CBC/PKCS5Padding", "AES");
    assertEquals(plaintext, new String(plaintextDecrypted, StandardCharsets.UTF_8));
  }

  @Test
  public void encryptDecryptFile()
  throws IOException, InvalidAlgorithmParameterException, NoSuchAlgorithmException,
         InvalidKeyException, NoSuchPaddingException {
    String execDir = Paths.get("").toAbsolutePath().toString();
    final String aes = Base64.encodeBase64String(cryptoSymmetricService.generateKey(128, "AES"));
    final SecretKey aesKey = cryptoSymmetricService.keyFromString(aes, "AES");

    // No IV-append test.
    File plainFile = Paths.get(execDir, "src", "test", "resources", "file-binary.jpg").toFile();
    File encryptedFile = File.createTempFile("encrypted", ".bin");
    System.out.println("Temporary encrypted file: " + encryptedFile.toString());
    File decryptedFile = File.createTempFile("decrypted", ".jpg");
    System.out.println("Temporary decrypted file: " + decryptedFile.toString());
    final byte[] iv = cryptoSymmetricService.generateIV();
    cryptoSymmetricService.encrypt(plainFile, encryptedFile, aesKey, iv, "AES/CBC/PKCS5Padding",
      "AES", false);
    cryptoSymmetricService.decrypt(encryptedFile, decryptedFile, aesKey, iv, "AES/CBC/PKCS5Padding",
      "AES");
    assertTrue(FileUtils.contentEquals(plainFile, decryptedFile));

    // IV-append test.
    encryptedFile = File.createTempFile("encrypted", ".bin");
    System.out.println("Temporary encrypted file: " + encryptedFile.toString());
    decryptedFile = File.createTempFile("decrypted", ".jpg");
    System.out.println("Temporary decrypted file: " + decryptedFile.toString());
    cryptoSymmetricService.encrypt(plainFile, encryptedFile, aesKey, "AES/CBC/PKCS5Padding",
      "AES");
    cryptoSymmetricService.decrypt(encryptedFile, decryptedFile, aesKey, "AES/CBC/PKCS5Padding",
      "AES");
    assertTrue(FileUtils.contentEquals(plainFile, decryptedFile));
  }
}
