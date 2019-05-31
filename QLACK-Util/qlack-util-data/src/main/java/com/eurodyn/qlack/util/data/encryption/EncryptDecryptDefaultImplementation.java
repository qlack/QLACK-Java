package com.eurodyn.qlack.util.data.encryption;

import org.springframework.stereotype.Component;

/**
 * A dummy implementation of the {@link EncryptorDecryptor} that does nothing special but simply
 * return the provided input for both {@link #encrypt(String)} and {@link #decrypt(String)} methods.
 * Provide your own implementation on your own application marking it with {@link
 * org.springframework.context.annotation.Primary}.
 */
@Component
public class EncryptDecryptDefaultImplementation implements EncryptorDecryptor {

  /**
   * {@inheritDoc}
   */
  @Override
  public String encrypt(String message) {
    return message;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String decrypt(String ciphertext) {
    return ciphertext;
  }
}
