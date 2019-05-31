package com.eurodyn.qlack.util.data.encryption;

/**
 * An interface for beans providing encrypting/decrypting services to be used in conjunction to {@link
 * EncryptDecryptAspect}.
 */
public interface EncryptorDecryptor {

  /**
   * Encrypts a plaintext.
   *
   * @param message The plaintext to encrypt.
   * @return The encrypted version of the plaintext.
   */
  String encrypt(String message);

  /**
   * Decrypts a messages previously encrypted.
   * @param ciphertext The encrypted message to decrypt.
   * @return The original plaintext message before encryption.
   */
  String decrypt(String ciphertext);
}
