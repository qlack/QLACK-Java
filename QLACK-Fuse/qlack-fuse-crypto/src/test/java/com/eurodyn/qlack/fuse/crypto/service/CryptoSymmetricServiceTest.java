package com.eurodyn.qlack.fuse.crypto.service;

import static org.junit.jupiter.api.Assertions.*;

import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@ExtendWith(MockitoExtension.class)
public class CryptoSymmetricServiceTest {

  @InjectMocks
  private CryptoSymmetricService cryptoSymmetricService;

  @BeforeEach
  public void init() {
    cryptoSymmetricService = new CryptoSymmetricService();
  }

  @Test
  public void generateKey() throws NoSuchAlgorithmException {
    final String key = Base64
        .encodeBase64String(cryptoSymmetricService.generateKey(128, "AES").getEncoded());
    assertNotNull(key);
  }

  @Test
  public void keyFromString() throws NoSuchAlgorithmException {
    final String key = Base64
        .encodeBase64String(cryptoSymmetricService.generateKey(128, "AES").getEncoded());
    final SecretKey secretKey = cryptoSymmetricService.keyFromString(key, "AES");
    assertNotNull(key);
    assertNotNull(secretKey);
    assertEquals(key, Base64.encodeBase64String(secretKey.getEncoded()));
  }

  @Test
  public void generateIV() {
    final byte[] iv = cryptoSymmetricService.generateIV();
    assertNotNull(iv);
    assertEquals(16, iv.length);
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
    final String aes = Base64
        .encodeBase64String(cryptoSymmetricService.generateKey(128, "AES").getEncoded());
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
    final String aes = Base64.encodeBase64String(cryptoSymmetricService.generateKey(128, "AES").getEncoded());
    final SecretKey aesKey = cryptoSymmetricService.keyFromString(aes, "AES");

    // No IV-append test.
    File plainFile = Paths.get(execDir, "src", "test", "resources", "file-binary.jpg").toFile();
    File encryptedFile = File.createTempFile("encrypted", ".bin");
    System.out.println("Temporary encrypted file: " + encryptedFile);
    File decryptedFile = File.createTempFile("decrypted", ".jpg");
    System.out.println("Temporary decrypted file: " + decryptedFile);
    final byte[] iv = cryptoSymmetricService.generateIV();
    cryptoSymmetricService.encrypt(plainFile, encryptedFile, aesKey, iv, "AES/CBC/PKCS5Padding",
        "AES", false);
    cryptoSymmetricService.decrypt(encryptedFile, decryptedFile, aesKey, iv, "AES/CBC/PKCS5Padding",
        "AES");
    assertTrue(FileUtils.contentEquals(plainFile, decryptedFile));

    // IV-append test.
    encryptedFile = File.createTempFile("encrypted", ".bin");
    System.out.println("Temporary encrypted file: " + encryptedFile);
    decryptedFile = File.createTempFile("decrypted", ".jpg");
    System.out.println("Temporary decrypted file: " + decryptedFile);
    cryptoSymmetricService.encrypt(plainFile, encryptedFile, aesKey, "AES/CBC/PKCS5Padding",
        "AES");
    cryptoSymmetricService.decrypt(encryptedFile, decryptedFile, aesKey, "AES/CBC/PKCS5Padding",
        "AES");
    assertTrue(FileUtils.contentEquals(plainFile, decryptedFile));
  }

  @Test
  public void encryptIvLongTest()
  throws NoSuchAlgorithmException, InvalidKeyException, InvalidAlgorithmParameterException,
         NoSuchPaddingException, IOException {
    String plaintext = "Hello world!";
    final String aes = Base64.encodeBase64String(cryptoSymmetricService.generateKey(128, "AES").getEncoded());
    final SecretKey aesKey = cryptoSymmetricService.keyFromString(aes, "AES");
    final byte[] iv = cryptoSymmetricService.generateIV(30);

    // No IV-append test.
    byte[] ciphertext = cryptoSymmetricService
        .encrypt(plaintext.getBytes(StandardCharsets.UTF_8), aesKey, iv, "AES/CBC/PKCS5Padding",
            "AES", false);
    assertNotNull(ciphertext);
  }

  @Test
  public void encryptDecryptTest()
  throws NoSuchAlgorithmException, InvalidKeyException, InvalidAlgorithmParameterException,
         NoSuchPaddingException, IOException {
    assertNotNull(cryptoSymmetricService.encrypt("plaintext".getBytes(StandardCharsets.UTF_8),
        cryptoSymmetricService.keyFromString(
            Base64.encodeBase64String(cryptoSymmetricService.generateKey(128, "AES").getEncoded()), "AES"),
        "AES/CBC/PKCS5Padding", "AES"));
  }

  @Test
  @SuppressWarnings("squid:S2699")
  public void decryptTest()
  throws IOException, InvalidAlgorithmParameterException, NoSuchAlgorithmException,
         InvalidKeyException, NoSuchPaddingException {
    final String aes = Base64.encodeBase64String(cryptoSymmetricService.generateKey(128, "AES").getEncoded());
    final SecretKey aesKey = cryptoSymmetricService.keyFromString(aes, "AES");

    InputStream encryptedFile = new FileInputStream(File.createTempFile("encrypted", ".bin"));
    OutputStream decryptedFile = new FileOutputStream(File.createTempFile("decrypted", ".jpg"));
    cryptoSymmetricService.decrypt(encryptedFile, decryptedFile, aesKey, "AES/CBC/PKCS5Padding",
        "AES");

  }
}
