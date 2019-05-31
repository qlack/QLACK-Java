package com.eurodyn.qlack.fuse.crypto;

import com.eurodyn.qlack.fuse.crypto.dto.CreateKeyPairDTO;
import javax.crypto.KeyGenerator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.io.IOException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;

/**
 * Crypto keys hanadling algorithms.
 */
@Service
@Validated
public class CryptoKeyService {

  /**
   * Generates a new key.
   *
   * @param bits The bits of the key.
   * @param provider The security provider of the key.
   * @param algorithm The security algorithm to use to generate the key.
   *
   * @return the key
   * @throws NoSuchAlgorithmException NoSuchAlgorithmException
   * @throws NoSuchProviderException NoSuchProviderException
   */
  public Key generateKey(int bits, String provider, String algorithm)
      throws NoSuchAlgorithmException, NoSuchProviderException {
    KeyGenerator generator;
    if (StringUtils.isBlank(provider)) {
      generator = KeyGenerator.getInstance(algorithm);
    } else {
      generator = KeyGenerator.getInstance(algorithm, provider);
    }
    generator.init(bits, SecureRandom.getInstanceStrong());

    return generator.generateKey();
  }

  /**
   * Generates a new key.
   *
   * @param bits The bits of the key.
   * @param algorithm The security algorithm to use. The first security provider providing this algorithm will be
   * automatically picked.
   *
   * @return the key
   * @throws IOException IOException
   * @throws NoSuchAlgorithmException NoSuchAlgorithmException
   * @throws NoSuchProviderException NoSuchProviderException
   */
  public Key generateKey(int bits, String algorithm)
      throws IOException, NoSuchAlgorithmException, NoSuchProviderException {
    return generateKey(bits, null, algorithm);
  }

  /**
   * Generates a new keypair consisting of a public key, a private key, and a certificate.
   * @param createKeyPairRequest The details of the keypair to create.
   *
   * @return the key pair
   * @throws NoSuchProviderException NoSuchProviderException
   * @throws NoSuchAlgorithmException NoSuchAlgorithmException
   */
  public KeyPair createKeyPair(CreateKeyPairDTO createKeyPairRequest)
      throws NoSuchProviderException, NoSuchAlgorithmException {
    KeyPairGenerator keyPairGenerator;

    // Set the provider and algorithm.
    if (StringUtils.isBlank(createKeyPairRequest.getGeneratorProvider())) {
      keyPairGenerator = KeyPairGenerator.getInstance(createKeyPairRequest.getGeneratorAlgorithm());
    } else {
      keyPairGenerator = KeyPairGenerator
          .getInstance(createKeyPairRequest.getGeneratorAlgorithm(),
              createKeyPairRequest.getGeneratorProvider());
    }

    // Set the secret provider and generator.
    if (StringUtils.isBlank(createKeyPairRequest.getSecretAlgorithm()) || StringUtils
        .isBlank(createKeyPairRequest.getSecretProvider())) {
      keyPairGenerator.initialize(createKeyPairRequest.getKeySize(),
          SecureRandom.getInstanceStrong());
    } else {
      keyPairGenerator.initialize(createKeyPairRequest.getKeySize(),
          SecureRandom.getInstance(createKeyPairRequest.getSecretAlgorithm(),
              createKeyPairRequest.getSecretProvider()));
    }

    return keyPairGenerator.generateKeyPair();
  }
}
