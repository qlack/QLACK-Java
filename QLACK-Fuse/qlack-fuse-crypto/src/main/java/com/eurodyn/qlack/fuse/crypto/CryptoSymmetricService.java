package com.eurodyn.qlack.fuse.crypto;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * Symmetric encryption/decryption utility methods.
 */
@Service
@Validated
public class CryptoSymmetricService {

  /**
   * Generates a symmetric key.
   *
   * @param keyLength The length of the key.
   * @param algorithm The algorithm to use.
   *
   * @return Returns a Base64 encoded key.
   * @throws NoSuchAlgorithmException NoSuchAlgorithmException
   */
  public String generateKey(final int keyLength, final String algorithm)
    throws NoSuchAlgorithmException {
    final KeyGenerator keyGen = KeyGenerator.getInstance(algorithm);
    keyGen.init(keyLength);

    return Base64.encodeBase64String(keyGen.generateKey().getEncoded());
  }

  /**
   * Generates a {@link SecretKey} from a Base64 encoded symmetric key.
   *
   * @param key The Base64 encoded version of the key.
   * @param algorithm The algorithm to use.
   *
   * @return SecretKey
   */
  public SecretKey keyFromString(final String key, final String algorithm) {
    return new SecretKeySpec(Base64.decodeBase64(key), algorithm);
  }

  /**
   * Generates a random IV.
   *
   * @return byte array
   */
  public byte[] generateIV() {
    final byte[] iv = new byte[16];
    new SecureRandom().nextBytes(iv);

    return iv;
  }

  /**
   * Generates a random IV.
   *
   * @return Returns the Base64 encoded version of the IV.
   */
  public String generateIVS() {
    return Base64.encodeBase64String(generateIV());
  }

  /**
   * Generates the original IV from a Base64 encoded IV.
   *
   * @param iv The IV to decode.
   *
   * @return byte array
   */
  public byte[] ivFromString(String iv) {
    return Base64.decodeBase64(iv);
  }

  /**
   * Encrypts a plaintext.
   *
   * @param plaintext The plaintext to encrypt.
   * @param key The key to use for encryption.
   * @param iv The encryption IV.
   * @param cipherInstance The cipher instance to use, e.g. "AES/CBC/PKCS5Padding".
   * @param keyAlgorithm The algorithm for the secret key, e.g. "AES".
   * @param prefixIv Whether to prefix the IV on the return value or not.
   *
   * @return the encrypted plaintext
   * @throws NoSuchPaddingException NoSuchPaddingException
   * @throws NoSuchAlgorithmException NoSuchAlgorithmException
   * @throws InvalidAlgorithmParameterException InvalidAlgorithmParameterException
   * @throws InvalidKeyException InvalidKeyException
   * @throws BadPaddingException BadPaddingException
   * @throws IllegalBlockSizeException IllegalBlockSizeException
   */
  public byte[] encrypt(final byte[] plaintext, final SecretKey key, byte[] iv,
    final String cipherInstance, final String keyAlgorithm, final boolean prefixIv)
    throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException,
    InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
    final Cipher cipher = Cipher.getInstance(cipherInstance);
    final SecretKeySpec keySpec = new SecretKeySpec(key.getEncoded(), keyAlgorithm);
    final IvParameterSpec ivSpec = new IvParameterSpec(iv);
    cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);

    final byte[] ciphertext = cipher.doFinal(plaintext);

    if (prefixIv) {
      return ArrayUtils.addAll(iv, ciphertext);
    } else {
      return ciphertext;
    }
  }

  /**
   * Encrypts a plaintext returning a Base64 encoded value.
   *
   * @param plaintext The plaintext to encrypt.
   * @param key The key to use for encryption.
   * @param iv The encryption IV.
   * @param cipherInstance The cipher instance to use, e.g. "AES/CBC/PKCS5Padding".
   * @param keyAlgorithm The algorithm for the secret key, e.g. "AES".
   * @param prefixIv Whether to prefix the IV on the return value or not.
   *
   * @return the encrypted plaintext
   * @throws NoSuchPaddingException NoSuchPaddingException
   * @throws NoSuchAlgorithmException NoSuchAlgorithmException
   * @throws InvalidAlgorithmParameterException InvalidAlgorithmParameterException
   * @throws InvalidKeyException InvalidKeyException
   * @throws BadPaddingException BadPaddingException
   * @throws IllegalBlockSizeException IllegalBlockSizeException
   */
  public String encryptS2S(final String plaintext, final SecretKey key, byte[] iv,
    final String cipherInstance, final String keyAlgorithm, final boolean prefixIv)
    throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException,
    InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
    return Base64.encodeBase64String(encrypt(plaintext.getBytes(StandardCharsets.UTF_8), key, iv,
      cipherInstance, keyAlgorithm, prefixIv));
  }

  /**
   * Decrypts an encrypted message prefixed with an IV.
   *
   * @param ciphertext The encrypted message to decrypt.
   * @param key The key to decrypt with.
   * @param cipherInstance The cipher instance to use, e.g. "AES/CBC/PKCS5Padding".
   * @param keyAlgorithm The algorithm for the secret key, e.g. "AES".
   *
   * @return the decrypted message
   * @throws NoSuchPaddingException NoSuchPaddingException
   * @throws InvalidKeyException InvalidKeyException
   * @throws NoSuchAlgorithmException NoSuchAlgorithmException
   * @throws IllegalBlockSizeException IllegalBlockSizeException
   * @throws BadPaddingException BadPaddingException
   * @throws InvalidAlgorithmParameterException InvalidAlgorithmParameterException
   */
  public byte[] decrypt(final byte[] ciphertext, final SecretKey key,
    final String cipherInstance, final String keyAlgorithm)
    throws NoSuchPaddingException, InvalidKeyException, NoSuchAlgorithmException,
    IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
    return decrypt(ArrayUtils.subarray(ciphertext, 16, ciphertext.length), key,
      ArrayUtils.subarray(ciphertext, 0, 16), cipherInstance, keyAlgorithm);
  }

  /**
   * Decrypts an encrypted Base64 encoded {@link String} message prefixed with an IV returning a
   * String value.
   *
   * @param ciphertext The encrypted message to decrypt.
   * @param key The key to decrypt with.
   * @param cipherInstance The cipher instance to use, e.g. "AES/CBC/PKCS5Padding".
   * @param keyAlgorithm The algorithm for the secret key, e.g. "AES".
   */
  public String decryptS2S(final String ciphertext, final SecretKey key,
    final String cipherInstance, final String keyAlgorithm)
    throws NoSuchPaddingException, InvalidKeyException, NoSuchAlgorithmException,
    IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
    return new String(decrypt(Base64.decodeBase64(ciphertext), key, cipherInstance, keyAlgorithm),
      StandardCharsets.UTF_8);
  }

  /**
   * Decrypts an encrypted message.
   *
   * @param ciphertext The encrypted message to decrypt.
   * @param key The key to decrypt with.
   * @param cipherInstance The cipher instance to use, e.g. "AES/CBC/PKCS5Padding".
   * @param keyAlgorithm The algorithm for the secret key, e.g. "AES".
   * @param iv The IV to use.
   *
   * @return the decryoted message
   * @throws NoSuchPaddingException NoSuchPaddingException
   * @throws NoSuchAlgorithmException NoSuchAlgorithmException
   * @throws InvalidAlgorithmParameterException InvalidAlgorithmParameterException
   * @throws InvalidKeyException InvalidKeyException
   * @throws BadPaddingException BadPaddingException
   * @throws IllegalBlockSizeException IllegalBlockSizeException
   */
  public byte[] decrypt(final byte[] ciphertext, final SecretKey key, final byte[] iv,
    final String cipherInstance, final String keyAlgorithm)
    throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException,
    InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
    final Cipher cipher = Cipher.getInstance(cipherInstance);
    final SecretKeySpec keySpec = new SecretKeySpec(key.getEncoded(), keyAlgorithm);
    final IvParameterSpec ivSpec = new IvParameterSpec(iv);
    cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

    return cipher.doFinal(ciphertext);
  }

  /**
   * Decrypts an encrypted message returning a string value.
   *
   * @param ciphertext The encrypted message to decrypt.
   * @param key The key to decrypt with.
   * @param cipherInstance The cipher instance to use, e.g. "AES/CBC/PKCS5Padding".
   * @param keyAlgorithm The algorithm for the secret key, e.g. "AES".
   * @param iv The IV to use.
   *
   * @return the decrypted message
   * @throws NoSuchPaddingException NoSuchPaddingException
   * @throws NoSuchAlgorithmException NoSuchAlgorithmException
   * @throws InvalidAlgorithmParameterException InvalidAlgorithmParameterException
   * @throws InvalidKeyException InvalidKeyException
   * @throws BadPaddingException BadPaddingException
   * @throws IllegalBlockSizeException IllegalBlockSizeException
   */
  public String decryptS2S(final byte[] ciphertext, final SecretKey key, final byte[] iv,
    final String cipherInstance, final String keyAlgorithm)
    throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException,
    InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
    return new String(decrypt(ciphertext, key, iv, cipherInstance, keyAlgorithm),
      StandardCharsets.UTF_8);
  }

}
