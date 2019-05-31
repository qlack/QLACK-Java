package com.eurodyn.qlack.fuse.crypto;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Symmetric encryption/decryption utility methods.
 */
@Service
@Validated
public class CryptoSymmetricService {

  // JUL reference.
  private static final Logger LOGGER = Logger.getLogger(CryptoSymmetricService.class.getName());

  /**
   * Trim an IV to 128 bits.
   *
   * @param iv The IV to trim.
   */
  private byte[] trimIV(byte[] iv) {
    if (iv.length > 16) {
      LOGGER.log(Level.WARNING, "Provided IV is {0} bytes larger than 16 bytes and will be "
        + "trimmed.", iv.length - 16);
      return ArrayUtils.subarray(iv, 0, 16);
    } else {
      return iv;
    }
  }

  /**
   * Generates a symmetric key.
   *
   * @param keyLength The length of the key.
   * @param algorithm The algorithm to use, e.g. AES.
   */
  public byte[] generateKey(final int keyLength, final String algorithm)
  throws NoSuchAlgorithmException {
    final KeyGenerator keyGen = KeyGenerator.getInstance(algorithm);
    keyGen.init(keyLength, SecureRandom.getInstanceStrong());

    return keyGen.generateKey().getEncoded();
  }

  /**
   * Generates a {@link SecretKey} from a Base64 encoded symmetric key.
   *
   * @param key The Base64 encoded version of the key.
   * @param algorithm The algorithm to use, e.g. AES.
   */
  public SecretKey keyFromString(final String key, final String algorithm) {
    return new SecretKeySpec(Base64.decodeBase64(key), algorithm);
  }

  /**
   * Generates a random IV of 16 bytes.
   */
  public byte[] generateIV() {
    final byte[] iv = new byte[16];
    new SecureRandom().nextBytes(iv);

    return iv;
  }

  /**
   * Generates a random IV of specific length.
   *
   * @param length The length of the IV.
   */
  public byte[] generateIV(int length) {
    final byte[] iv = new byte[length];
    new SecureRandom().nextBytes(iv);

    return iv;
  }


  /**
   * Generates the original IV from a Base64 encoded IV.
   *
   * @param iv The IV to decode.
   */
  public byte[] ivFromString(String iv) {
    return Base64.decodeBase64(iv);
  }

  /**
   * Encrypts a plaintext with a given IV.
   *
   * @param plaintext The plaintext to encrypt.
   * @param key The key to use for encryption.
   * @param iv The encryption IV.
   * @param cipherInstance The cipher instance to use, e.g. "AES/CBC/PKCS5Padding".
   * @param keyAlgorithm The algorithm for the secret key, e.g. "AES".
   * @param prefixIv Whether to prefix the IV on the return value or not.
   * @return The ciphertext optionally prefixed with the IV.
   */
  public byte[] encrypt(final byte[] plaintext, final SecretKey key, byte[] iv,
    final String cipherInstance, final String keyAlgorithm, final boolean prefixIv)
  throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException,
         InvalidKeyException, IOException {
    try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
      encrypt(new BufferedInputStream(new ByteArrayInputStream(plaintext)),
        baos,
        key, iv,
        cipherInstance,
        keyAlgorithm,
        prefixIv);
      return baos.toByteArray();
    }
  }

  /**
   * Encrypts a plaintext with an internally generated IV.
   *
   * @param plaintext The plaintext to encrypt.
   * @param key The key to use for encryption.
   * @param cipherInstance The cipher instance to use, e.g. "AES/CBC/PKCS5Padding".
   * @param keyAlgorithm The algorithm for the secret key, e.g. "AES".
   * @return The ciphertext prefixed with the IV.
   */
  public byte[] encrypt(final byte[] plaintext, final SecretKey key, final String cipherInstance,
    final String keyAlgorithm)
  throws NoSuchPaddingException, InvalidKeyException, NoSuchAlgorithmException,
         InvalidAlgorithmParameterException, IOException {
    return encrypt(plaintext, key, generateIV(), cipherInstance, keyAlgorithm, true);
  }

  /**
   * Encrypts a file producing an encrypted file prefixed with the internally generated IV.
   *
   * @param sourceFile The source file to encrypt.
   * @param targetFile The target, encrypted file to produce.
   * @param key The key to use for encryption.
   * @param cipherInstance The cipher instance to use, e.g. "AES/CBC/PKCS5Padding".
   * @param keyAlgorithm The algorithm for the secret key, e.g. "AES".
   */
  public void encrypt(File sourceFile, File targetFile, final SecretKey key,
    final String cipherInstance, final String keyAlgorithm)
  throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException,
         InvalidKeyException, IOException {
    encrypt(sourceFile, targetFile, key, generateIV(), cipherInstance, keyAlgorithm, true);
  }

  /**
   * Encrypts a file producing an encrypted file with optionally appending the IV.
   *
   * @param sourceFile The source file to encrypt.
   * @param targetFile The target, encrypted file to produce.
   * @param key The key to use for encryption.
   * @param iv The IV to use.
   * @param cipherInstance The cipher instance to use, e.g. "AES/CBC/PKCS5Padding".
   * @param keyAlgorithm The algorithm for the secret key, e.g. "AES".
   * @param prefixIv Whether to prefix the IV on the return value or not.
   */
  public void encrypt(File sourceFile, File targetFile, final SecretKey key, byte[] iv,
    final String cipherInstance, final String keyAlgorithm, final boolean prefixIv)
  throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException,
         InvalidKeyException, IOException {
    try (final FileInputStream fileInputStream = new FileInputStream(sourceFile)) {
      try (final FileOutputStream fileOutputStream = new FileOutputStream(targetFile)) {
        encrypt(fileInputStream, fileOutputStream, key, iv, cipherInstance, keyAlgorithm, prefixIv);
      }
    }
  }

  /**
   * Encrypts a stream producing an encrypted stream with optionally appending the IV.
   *
   * @param sourceStream The source stream to encrypt.
   * @param targetStream The target, encrypted stream to populate.
   * @param key The key to use for encryption.
   * @param iv The IV to use.
   * @param cipherInstance The cipher instance to use, e.g. "AES/CBC/PKCS5Padding".
   * @param keyAlgorithm The algorithm for the secret key, e.g. "AES".
   * @param prefixIv Whether to prefix the IV on the return value or not.
   */
  public void encrypt(InputStream sourceStream, OutputStream targetStream, final SecretKey key,
    byte[] iv, final String cipherInstance, final String keyAlgorithm, final boolean prefixIv)
  throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException,
         InvalidKeyException, IOException {
    final Cipher cipher = Cipher.getInstance(cipherInstance);
    final SecretKeySpec keySpec = new SecretKeySpec(key.getEncoded(), keyAlgorithm);
    iv = trimIV(iv);
    final IvParameterSpec ivSpec = new IvParameterSpec(iv);
    cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);

    byte[] buffer = new byte[8192];
    int count;
    if (prefixIv) {
      targetStream.write(iv);
    }
    try (final CipherOutputStream cipherOutputStream = new CipherOutputStream(targetStream,
      cipher)) {
      while ((count = sourceStream.read(buffer)) > 0) {
        cipherOutputStream.write(buffer, 0, count);
      }
    }
  }

  /**
   * Decrypts a file also decoding an appended IV.
   *
   * @param encryptedFile The encrypted file to decrypt.
   * @param plainFile The decrypted file to produce.
   * @param key The key to use for decryption.
   * @param cipherInstance The cipher instance to use, e.g. "AES/CBC/PKCS5Padding".
   * @param keyAlgorithm The algorithm for the secret key, e.g. "AES".
   */
  public void decrypt(final File encryptedFile, final File plainFile, final SecretKey key,
    final String cipherInstance, final String keyAlgorithm)
  throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException,
         InvalidKeyException, IOException {
    decrypt(encryptedFile, plainFile, key, null, cipherInstance, keyAlgorithm);
  }

  /**
   * Decrypts a file, optionally decoding an appended IV.
   *
   * @param encryptedFile The encrypted file to decrypt.
   * @param plainFile The decrypted file to produce.
   * @param key The key to use for decryption.
   * @param iv The IV with which this file was encrypted. If left null, the IV will be decoded from
   * the 16 first bytes of `encryptedFile`.
   * @param cipherInstance The cipher instance to use, e.g. "AES/CBC/PKCS5Padding".
   * @param keyAlgorithm The algorithm for the secret key, e.g. "AES".
   */
  public void decrypt(final File encryptedFile, final File plainFile, final SecretKey key,
    byte[] iv, final String cipherInstance, final String keyAlgorithm)
  throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException,
         InvalidKeyException, IOException {
    decrypt(new FileInputStream(encryptedFile), new FileOutputStream(plainFile), key, iv,
      cipherInstance, keyAlgorithm);
  }

  /**
   * Decrypts a file which is encrypted with a 16 byte IV prefixed.
   *
   * @param sourceStream The encrypted stream to decrypt.
   * @param targetStream The decrypted stream to populate.
   * @param key The key to use for decryption.
   * the 16 first bytes of `encryptedFile`.
   * @param cipherInstance The cipher instance to use, e.g. "AES/CBC/PKCS5Padding".
   * @param keyAlgorithm The algorithm for the secret key, e.g. "AES".
   */
  public void decrypt(final InputStream sourceStream, final OutputStream targetStream,
    final SecretKey key, final String cipherInstance, final String keyAlgorithm)
  throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException,
         InvalidKeyException, IOException {
    decrypt(sourceStream, targetStream, key, null, cipherInstance, keyAlgorithm);
  }

  /**
   * Decrypts a file, optionally decoding an appended IV.
   *
   * @param sourceStream The encrypted stream to decrypt.
   * @param targetStream The decrypted stream to populate.
   * @param key The key to use for decryption.
   * @param iv The IV with which this file was encrypted. If left null, the IV will be decoded from
   * the 16 first bytes of `encryptedFile`.
   * @param cipherInstance The cipher instance to use, e.g. "AES/CBC/PKCS5Padding".
   * @param keyAlgorithm The algorithm for the secret key, e.g. "AES".
   */
  public void decrypt(final InputStream sourceStream, final OutputStream targetStream,
    final SecretKey key, byte[] iv, final String cipherInstance, final String keyAlgorithm)
  throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException,
         InvalidKeyException, IOException {
    final Cipher cipher = Cipher.getInstance(cipherInstance);
    final SecretKeySpec keySpec = new SecretKeySpec(key.getEncoded(), keyAlgorithm);
    byte[] buffer = new byte[8192];
    int count;
    if (iv == null) {
      iv = new byte[16];
      sourceStream.read(iv, 0, 16);
    }
    final IvParameterSpec ivSpec = new IvParameterSpec(iv);
    cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
    try (final CipherOutputStream cipherOutputStream = new CipherOutputStream(targetStream,
      cipher)) {
      while ((count = sourceStream.read(buffer)) > 0) {
        cipherOutputStream.write(buffer, 0, count);
      }
    }
  }

  /**
   * Decrypts an encrypted message prefixed with a 16 bytes IV.
   *
   * @param ciphertext The encrypted message to decrypt.
   * @param key The key to decrypt with.
   * @param cipherInstance The cipher instance to use, e.g. "AES/CBC/PKCS5Padding".
   * @param keyAlgorithm The algorithm for the secret key, e.g. "AES".
   */
  public byte[] decrypt(final byte[] ciphertext, final SecretKey key,
    final String cipherInstance, final String keyAlgorithm)
  throws NoSuchPaddingException, InvalidKeyException, NoSuchAlgorithmException,
         InvalidAlgorithmParameterException, IOException {
    return decrypt(ArrayUtils.subarray(ciphertext, 16, ciphertext.length), key,
      ArrayUtils.subarray(ciphertext, 0, 16), cipherInstance, keyAlgorithm);
  }

  /**
   * Decrypts an encrypted message.
   *
   * @param ciphertext The encrypted message to decrypt.
   * @param key The key to decrypt with.
   * @param cipherInstance The cipher instance to use, e.g. "AES/CBC/PKCS5Padding".
   * @param keyAlgorithm The algorithm for the secret key, e.g. "AES".
   * @param iv The IV to use.
   */
  public byte[] decrypt(final byte[] ciphertext, final SecretKey key, byte[] iv,
    final String cipherInstance, final String keyAlgorithm)
  throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException,
         InvalidKeyException, IOException {
    try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
      decrypt(new BufferedInputStream(new ByteArrayInputStream(ciphertext)),
        baos,
        key, iv,
        cipherInstance,
        keyAlgorithm);
      return baos.toByteArray();
    }
  }
}
