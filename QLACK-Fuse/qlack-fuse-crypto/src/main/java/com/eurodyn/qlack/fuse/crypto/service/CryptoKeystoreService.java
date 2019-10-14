package com.eurodyn.qlack.fuse.crypto.service;

import com.eurodyn.qlack.fuse.crypto.dto.CPPHolderDTO;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * Utility methods to interact with keystores.
 */
@Service
public class CryptoKeystoreService {

  /**
   * Reads a key from the given keystore.
   *
   * @param keystore the keystore to read from
   * @param keystorePassword the keystore password
   * @param keyName the name of the key to read
   * @param keyPassword the key password of the key
   * @param keystoreType the type of the keystore
   * @param keystoreProvider the security provider generated the keystore
   * @return the key information
   * @throws KeyStoreException thrown when they key is not valid
   * @throws IOException thrown when something unexpected happens
   * @throws CertificateException thrown when the certificate cannot be generated
   * @throws NoSuchAlgorithmException thrown when no algorithm is found for encryption
   * @throws UnrecoverableKeyException thrown when the key os not valid
   * @throws NoSuchProviderException thrown when the provider is not valid
   */
  public CPPHolderDTO readKeyFromKeystore(final InputStream keystore, final String keystorePassword,
      final String keyName, final String keyPassword, final String keystoreType,
      final String keystoreProvider)
      throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException,
      UnrecoverableKeyException, NoSuchProviderException {
    final KeyStore ks;
    if (StringUtils.isBlank(keystoreType) || StringUtils.isBlank(keystoreProvider)) {
      ks = KeyStore.getInstance(KeyStore.getDefaultType());
    } else {
      ks = KeyStore.getInstance(keystoreType, keystoreProvider);
    }
    try (InputStream fis = new BufferedInputStream(keystore)) {
      ks.load(fis, keystorePassword.toCharArray());
      final Key key = ks.getKey(keyName, keyPassword.toCharArray());
      final Certificate certificate = ks.getCertificate(keyName);
      return new CPPHolderDTO(certificate, certificate.getPublicKey(), (PrivateKey) key);
    }
  }

  /**
   * Reads a key from the given keystore utilising the system's default keystore type and security
   * provider.
   *
   * @param keystore the keystore to read from
   * @param keystorePassword the keystore password
   * @param keyName the name of the key to read
   * @param keyPassword the key's password
   * @return the key information
   * @throws KeyStoreException thrown when they key is not valid
   * @throws IOException thrown when something unexpected happens
   * @throws CertificateException thrown when the certificate cannot be generated
   * @throws NoSuchAlgorithmException thrown when no algorithm is found for encryption
   * @throws UnrecoverableKeyException thrown when the key os not valid
   * @throws NoSuchProviderException thrown when the provider is not valid
   */
  public CPPHolderDTO readKeyFromKeystore(final InputStream keystore, final String keystorePassword,
      final String keyName, final String keyPassword)
      throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException,
      UnrecoverableKeyException, NoSuchProviderException {
    return readKeyFromKeystore(keystore, keystorePassword, keyName, keyPassword, null, null);
  }

}
