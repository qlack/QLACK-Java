package com.eurodyn.qlack.fuse.crypto;

import com.eurodyn.qlack.fuse.crypto.dto.CPPHolderDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

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

/**
 * Utility methods to interact with keystores.
 */
@Service
public class CryptoKeystoreService {

  /**
   * Reads a key from the given keystore.
   *
   * @param keystore The keystore to read from.
   * @param keystorePassword The keystore password.
   * @param keyName The name of the key to read.
   * @param keyPassword The key's password.
   * @param keystoreType The type of the keystore.
   * @param keystoreProvider The security provider generated the keystore.
   *
   * @return CPPHolderDTO
   * @throws KeyStoreException KeyStoreException
   * @throws IOException IOException
   * @throws CertificateException CertificateException
   * @throws NoSuchAlgorithmException NoSuchAlgorithmException
   * @throws UnrecoverableKeyException UnrecoverableKeyException
   * @throws NoSuchProviderException NoSuchProviderException
   */
  public CPPHolderDTO readKeyFromKeystore(InputStream keystore, String keystorePassword, String keyName,
      String keyPassword, String keystoreType, String keystoreProvider)
      throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException,
      UnrecoverableKeyException, NoSuchProviderException {
    KeyStore ks;
    if (StringUtils.isBlank(keystoreType) || StringUtils.isBlank(keystoreType)) {
      ks = KeyStore.getInstance(KeyStore.getDefaultType());
    } else {
      ks = KeyStore.getInstance(keystoreType, keystoreProvider);
    }
    try (InputStream fis = new BufferedInputStream(keystore)) {
      ks.load(fis, keystorePassword.toCharArray());
      Key key = ks.getKey(keyName, keyPassword.toCharArray());
      Certificate certificate = ks.getCertificate(keyName);
      return new CPPHolderDTO(certificate, certificate.getPublicKey(), (PrivateKey) key);
    }
  }

  /**
   * Reads a key from the given keystore utilising the system's default keystore type and security provider.
   *
   * @param keystore The keystore to read from.
   * @param keystorePassword The keystore password.
   * @param keyName The name of the key to read.
   * @param keyPassword The key's password.
   * @return CPPHolderDTO
   * @throws KeyStoreException KeyStoreException
   * @throws IOException IOException
   * @throws CertificateException CertificateException
   * @throws NoSuchAlgorithmException NoSuchAlgorithmException
   * @throws UnrecoverableKeyException UnrecoverableKeyException
   * @throws NoSuchProviderException NoSuchProviderException
   */
  public CPPHolderDTO readKeyFromKeystore(InputStream keystore, String keystorePassword, String keyName,
      String keyPassword)
      throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException,
      UnrecoverableKeyException, NoSuchProviderException {
    return readKeyFromKeystore(keystore, keystorePassword, keyName, keyPassword, null, null);
  }

}
