package com.eurodyn.qlack.fuse.crypto.service;

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
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.logging.Level;
import java.util.logging.Logger;
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

/**
 * Symmetric encryption/decryption utility methods.
 */
@Service
@Validated
public class CryptoSymmetricService {

  // JUL reference.
  private static final Logger LOGGER = Logger
      .getLogger(CryptoSymmetricService.class.getName());

  /**
   * Trim an IV to 128 bits.
   *
   * @param iv The IV to trim.
   */
  private byte[] trimIV(byte[] iv) {
    if (iv.length > 16) {
      LOGGER.log(Level.WARNING,
          "Provided IV is {0} bytes larger than 16 bytes and will be "
              + "trimmed.", iv.length - 16);
      return ArrayUtils.subarray(iv, 0, 16);
    } else {
      return iv;
    }
  }

  /**
   * Generates a symmetric key.
   *
   * @param keyLength the length of the key
   * @param algorithm the algorithm to use, e.g. AES
   * @return the generated key
   * @throws NoSuchAlgorithmException thrown when no algorithm is found for encryption
   */
  public SecretKey generateKey(final int keyLength, final String algorithm)
  throws NoSuchAlgorithmException {
    final KeyGenerator keyGen = KeyGenerator.getInstance(algorithm);
    keyGen.init(keyLength, SecureRandom.getInstanceStrong());

    return keyGen.generateKey();
  }

  /**
   * Generates a symmetric key.
   *
   * @param keyLength the length of the key
   * @param algorithm the algorithm to use, e.g. AES
   * @param provider  the provider for the algorithm, e.g. SUN
   * @return the generated key
   * @throws NoSuchAlgorithmException thrown when no algorithm is found for encryption
   */
  public SecretKey generateKey(final int keyLength, final String algorithm, final String provider)
  throws NoSuchAlgorithmException, NoSuchProviderException {
    final KeyGenerator keyGen = KeyGenerator.getInstance(algorithm, provider);
    keyGen.init(keyLength, SecureRandom.getInstanceStrong());

    return keyGen.generateKey();
  }

  /**
   * Generates a {@link SecretKey} from a Base64 encoded symmetric key.
   *
   * @param key       the Base64 encoded version of the key
   * @param algorithm the algorithm to use, e.g. AES
   * @return the generated secret key
   */
  public SecretKey keyFromString(final String key, final String algorithm) {
    return new SecretKeySpec(Base64.decodeBase64(key), algorithm);
  }

  /**
   * Generates a random IV of 16 bytes.
   *
   * @return the bytes of the generated IV
   */
  public byte[] generateIV() {
    final byte[] iv = new byte[16];
    new SecureRandom().nextBytes(iv);

    return iv;
  }

  /**
   * Generates a random IV of specific length.
   *
   * @param length the length of the IV
   * @return the bytes of the generated IV
   */
  public byte[] generateIV(int length) {
    final byte[] iv = new byte[length];
    new SecureRandom().nextBytes(iv);

    return iv;
  }


  /**
   * Generates the original IV from a Base64 encoded IV.
   *
   * @param iv the IV to decode
   * @return the bytes of the decoded IV
   */
  public byte[] ivFromString(String iv) {
    return Base64.decodeBase64(iv);
  }

  /**
   * Encrypts a plaintext with a given IV.
   *
   * @param plaintext      The plaintext to encrypt
   * @param key            the key to use for encryption
   * @param iv             the encryption IV
   * @param cipherInstance the cipher instance to use, e.g. "AES/CBC/PKCS5Padding"
   * @param keyAlgorithm   the algorithm for the secret key, e.g. "AES"
   * @param prefixIv       whether to prefix the IV on the return value or not
   * @return the ciphertext optionally prefixed with the IV
   * @throws NoSuchPaddingException             thrown when the provided cipherInstance is not
   *                                            valid
   * @throws NoSuchAlgorithmException           thrown when no algorithm is found for encryption
   * @throws InvalidAlgorithmParameterException thrown when the found algorithm cannot be executed
   * @throws InvalidKeyException                thrown when the provided key is invalid
   * @throws IOException                        thrown when something unexpected happens
   */
  public byte[] encrypt(final byte[] plaintext, final SecretKey key, byte[] iv,
      final String cipherInstance, final String keyAlgorithm,
      final boolean prefixIv)
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
   * @param plaintext      the plaintext to encrypt
   * @param key            the key to use for encryption
   * @param cipherInstance the cipher instance to use, e.g. "AES/CBC/PKCS5Padding"
   * @param keyAlgorithm   the algorithm for the secret key, e.g. "AES"
   * @return the ciphertext prefixed with the IV
   * @throws NoSuchPaddingException             thrown when the provided cipherInstance is not
   *                                            valid
   * @throws NoSuchAlgorithmException           thrown when no algorithm is found for encryption
   * @throws InvalidAlgorithmParameterException thrown when the found algorithm cannot be executed
   * @throws InvalidKeyException                thrown when the provided key is invalid
   * @throws IOException                        thrown when something unexpected happens
   */
  public byte[] encrypt(final byte[] plaintext, final SecretKey key,
      final String cipherInstance,
      final String keyAlgorithm)
  throws NoSuchPaddingException, InvalidKeyException, NoSuchAlgorithmException,
         InvalidAlgorithmParameterException, IOException {
    return encrypt(plaintext, key, generateIV(), cipherInstance, keyAlgorithm,
        true);
  }

  /**
   * Encrypts a file producing an encrypted file prefixed with the internally generated IV.
   *
   * @param sourceFile     the source file to encrypt
   * @param targetFile     the target, encrypted file to produce
   * @param key            the key to use for encryption
   * @param cipherInstance the cipher instance to use, e.g. "AES/CBC/PKCS5Padding"
   * @param keyAlgorithm   the algorithm for the secret key, e.g. "AES"
   * @throws NoSuchPaddingException             thrown when the provided cipherInstance is not
   *                                            valid
   * @throws NoSuchAlgorithmException           thrown when no algorithm is found for encryption
   * @throws InvalidAlgorithmParameterException thrown when the found algorithm cannot be executed
   * @throws InvalidKeyException                thrown when the provided key is invalid
   * @throws IOException                        thrown when something unexpected happens
   */
  public void encrypt(File sourceFile, File targetFile, final SecretKey key,
      final String cipherInstance, final String keyAlgorithm)
  throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException,
         InvalidKeyException, IOException {
    encrypt(sourceFile, targetFile, key, generateIV(), cipherInstance,
        keyAlgorithm, true);
  }

  /**
   * Encrypts a file producing an encrypted file with optionally appending the IV.
   *
   * @param sourceFile     the source file to encrypt
   * @param targetFile     the target, encrypted file to produce
   * @param key            the key to use for encryption
   * @param iv             the IV to use
   * @param cipherInstance the cipher instance to use, e.g. "AES/CBC/PKCS5Padding"
   * @param keyAlgorithm   the algorithm for the secret key, e.g. "AES"
   * @param prefixIv       whether to prefix the IV on the return value or not
   * @throws NoSuchPaddingException             thrown when the provided cipherInstance is not
   *                                            valid
   * @throws NoSuchAlgorithmException           thrown when no algorithm is found for encryption
   * @throws InvalidAlgorithmParameterException thrown when the found algorithm cannot be executed
   * @throws InvalidKeyException                thrown when the provided key is invalid
   * @throws IOException                        thrown when something unexpected happens
   */
  public void encrypt(File sourceFile, File targetFile, final SecretKey key,
      byte[] iv,
      final String cipherInstance, final String keyAlgorithm,
      final boolean prefixIv)
  throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException,
         InvalidKeyException, IOException {
    try (final FileInputStream fileInputStream = new FileInputStream(
        sourceFile)) {
      try (final FileOutputStream fileOutputStream = new FileOutputStream(
          targetFile)) {
        encrypt(fileInputStream, fileOutputStream, key, iv, cipherInstance,
            keyAlgorithm, prefixIv);
      }
    }
  }

  /**
   * Encrypts a stream producing an encrypted stream with optionally appending the IV.
   *
   * @param sourceStream   the source stream to encrypt
   * @param targetStream   the target, encrypted stream to populate
   * @param key            the key to use for encryption
   * @param iv             the IV to use
   * @param cipherInstance the cipher instance to use, e.g. "AES/CBC/PKCS5Padding"
   * @param keyAlgorithm   the algorithm for the secret key, e.g. "AES"
   * @param prefixIv       whether to prefix the IV on the return value or not
   * @throws NoSuchPaddingException             thrown when the provided cipherInstance is not
   *                                            valid
   * @throws NoSuchAlgorithmException           thrown when no algorithm is found for encryption
   * @throws InvalidAlgorithmParameterException thrown when the found algorithm cannot be executed
   * @throws InvalidKeyException                thrown when the provided key is invalid
   * @throws IOException                        thrown when something unexpected happens
   */
  @SuppressWarnings("squid:S4787")
  public void encrypt(InputStream sourceStream, OutputStream targetStream, final SecretKey key,
      byte[] iv, final String cipherInstance, final String keyAlgorithm, final boolean prefixIv)
  throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException,
         InvalidKeyException, IOException {
    final Cipher cipher = Cipher.getInstance(cipherInstance);
    final SecretKeySpec keySpec = new SecretKeySpec(key.getEncoded(),
        keyAlgorithm);
    iv = trimIV(iv);
    @SuppressWarnings("squid:S3329") final IvParameterSpec ivSpec = new IvParameterSpec(iv);
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
   * @param encryptedFile  the encrypted file to decrypt
   * @param plainFile      the decrypted file to produce
   * @param key            the key to use for decryption
   * @param cipherInstance the cipher instance to use, e.g. "AES/CBC/PKCS5Padding"
   * @param keyAlgorithm   the algorithm for the secret key, e.g. "AES"
   * @throws NoSuchPaddingException             thrown when the provided cipherInstance is not
   *                                            valid
   * @throws NoSuchAlgorithmException           thrown when no algorithm is found for encryption
   * @throws InvalidAlgorithmParameterException thrown when the found algorithm cannot be executed
   * @throws InvalidKeyException                thrown when the provided key is invalid
   * @throws IOException                        thrown when something unexpected happens
   */
  public void decrypt(final File encryptedFile, final File plainFile,
      final SecretKey key,
      final String cipherInstance, final String keyAlgorithm)
  throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException,
         InvalidKeyException, IOException {
    decrypt(encryptedFile, plainFile, key, null, cipherInstance, keyAlgorithm);
  }

  /**
   * Decrypts a file, optionally decoding an appended IV.
   *
   * @param encryptedFile  the encrypted file to decrypt
   * @param plainFile      the decrypted file to produce
   * @param key            the key to use for decryption
   * @param iv             the IV with which this file was encrypted. If left null, the IV will be
   *                       decoded from the 16 first bytes of `encryptedFile`
   * @param cipherInstance the cipher instance to use, e.g. "AES/CBC/PKCS5Padding"
   * @param keyAlgorithm   the algorithm for the secret key, e.g. "AES"
   * @throws NoSuchPaddingException             thrown when the provided cipherInstance is not
   *                                            valid
   * @throws NoSuchAlgorithmException           thrown when no algorithm is found for encryption
   * @throws InvalidAlgorithmParameterException thrown when the found algorithm cannot be executed
   * @throws InvalidKeyException                thrown when the provided key is invalid
   * @throws IOException                        thrown when something unexpected happens
   */
  public void decrypt(final File encryptedFile, final File plainFile,
      final SecretKey key,
      byte[] iv, final String cipherInstance, final String keyAlgorithm)
  throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException,
         InvalidKeyException, IOException {
    try (FileInputStream fis = new FileInputStream(encryptedFile)) {
      decrypt(fis, new FileOutputStream(plainFile), key, iv, cipherInstance, keyAlgorithm);
    }
  }

  /**
   * Decrypts a file which is encrypted with a 16 byte IV prefixed.
   *
   * @param sourceStream   the encrypted stream to decrypt
   * @param targetStream   the decrypted stream to populate
   * @param key            the key to use for decryption. the 16 first bytes of `encryptedFile`
   * @param cipherInstance the cipher instance to use, e.g. "AES/CBC/PKCS5Padding"
   * @param keyAlgorithm   the algorithm for the secret key, e.g. "AES"
   * @throws NoSuchPaddingException             thrown when the provided cipherInstance is not
   *                                            valid
   * @throws NoSuchAlgorithmException           thrown when no algorithm is found for encryption
   * @throws InvalidAlgorithmParameterException thrown when the found algorithm cannot be executed
   * @throws InvalidKeyException                thrown when the provided key is invalid
   * @throws IOException                        thrown when something unexpected happens
   */
  public void decrypt(final InputStream sourceStream,
      final OutputStream targetStream,
      final SecretKey key, final String cipherInstance, final String keyAlgorithm)
  throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException,
         InvalidKeyException, IOException {
    decrypt(sourceStream, targetStream, key, null, cipherInstance,
        keyAlgorithm);
  }

  /**
   * Decrypts a file, optionally decoding an appended IV.
   *
   * @param sourceStream   the encrypted stream to decrypt
   * @param targetStream   the decrypted stream to populate
   * @param key            the key to use for decryption
   * @param iv             the IV with which this file was encrypted. If left null, the IV will be
   *                       decoded from the 16 first bytes of `encryptedFile`
   * @param cipherInstance the cipher instance to use, e.g. "AES/CBC/PKCS5Padding"
   * @param keyAlgorithm   the algorithm for the secret key, e.g. "AES"
   * @throws NoSuchPaddingException             thrown when the provided cipherInstance is not
   *                                            valid
   * @throws NoSuchAlgorithmException           thrown when no algorithm is found for encryption
   * @throws InvalidAlgorithmParameterException thrown when the found algorithm cannot be executed
   * @throws InvalidKeyException                thrown when the provided key is invalid
   * @throws IOException                        thrown when something unexpected happens
   */
  @SuppressWarnings("squid:S4787")
  public void decrypt(final InputStream sourceStream,
      final OutputStream targetStream,
      final SecretKey key, byte[] iv, final String cipherInstance,
      final String keyAlgorithm)
  throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException,
         InvalidKeyException, IOException {
    final Cipher cipher = Cipher.getInstance(cipherInstance);
    final SecretKeySpec keySpec = new SecretKeySpec(key.getEncoded(),
        keyAlgorithm);
    byte[] buffer = new byte[8192];
    int count;
    if (iv == null) {
      iv = new byte[16];
      sourceStream.read(iv, 0, 16);
    }
    final IvParameterSpec ivSpec = new IvParameterSpec(iv);
    cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
    try (final CipherOutputStream cipherOutputStream = new CipherOutputStream(
        targetStream,
        cipher)) {
      while ((count = sourceStream.read(buffer)) > 0) {
        cipherOutputStream.write(buffer, 0, count);
      }
    }
  }

  /**
   * Decrypts an encrypted message prefixed with a 16 bytes IV.
   *
   * @param ciphertext     the encrypted message to decrypt
   * @param key            the key to decrypt with
   * @param cipherInstance the cipher instance to use, e.g. "AES/CBC/PKCS5Padding"
   * @param keyAlgorithm   the algorithm for the secret key, e.g. "AES"
   * @return the bytes of the decrypted message
   * @throws NoSuchPaddingException             thrown when the provided cipherInstance is not
   *                                            valid
   * @throws NoSuchAlgorithmException           thrown when no algorithm is found for encryption
   * @throws InvalidAlgorithmParameterException thrown when the found algorithm cannot be executed
   * @throws InvalidKeyException                thrown when the provided key is invalid
   * @throws IOException                        thrown when something unexpected happens
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
   * @param ciphertext     the encrypted message to decrypt
   * @param key            the key to decrypt with
   * @param cipherInstance the cipher instance to use, e.g. "AES/CBC/PKCS5Padding"
   * @param keyAlgorithm   the algorithm for the secret key, e.g. "AES"
   * @param iv             the IV to use
   * @return the bytes of the decrypted message
   * @throws NoSuchPaddingException             thrown when the provided cipherInstance is not
   *                                            valid
   * @throws NoSuchAlgorithmException           thrown when no algorithm is found for encryption
   * @throws InvalidAlgorithmParameterException thrown when the found algorithm cannot be executed
   * @throws InvalidKeyException                thrown when the provided key is invalid
   * @throws IOException                        thrown when something unexpected happens
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
