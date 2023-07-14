package com.eurodyn.qlack.fuse.crypto.service;

import com.eurodyn.qlack.fuse.crypto.dto.CPPHolderDTO;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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
import java.security.spec.InvalidKeySpecException;
import java.util.Set;
import java.util.logging.Level;
import javax.crypto.spec.SecretKeySpec;
import jakarta.validation.constraints.NotNull;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.springframework.stereotype.Service;

/**
 * Utility methods to interact with keystores.
 */
@Service
@Log
public class CryptoKeystoreService {

  private final CryptoAsymmetricService cryptoAsymmetricService;

  public CryptoKeystoreService(
    CryptoAsymmetricService cryptoAsymmetricService) {
    this.cryptoAsymmetricService = cryptoAsymmetricService;
  }

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
   * @throws CertificateException thrown when the certificate cannot be
   * generated
   * @throws NoSuchAlgorithmException thrown when no algorithm is found for
   * encryption
   * @throws UnrecoverableKeyException thrown when the key os not valid
   * @throws NoSuchProviderException thrown when the provider is not valid
   */
  @SuppressWarnings("squid:MaximumInheritanceDepth")
  public CPPHolderDTO readKeyFromKeystore(final InputStream keystore,
    final String keystorePassword,
    final String keyName, final String keyPassword, final String keystoreType,
    final String keystoreProvider)
    throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException,
    UnrecoverableKeyException, NoSuchProviderException {
    final KeyStore ks;
    if (StringUtils.isBlank(keystoreType) || StringUtils
      .isBlank(keystoreProvider)) {
      ks = KeyStore.getInstance(KeyStore.getDefaultType());
    } else {
      ks = KeyStore.getInstance(keystoreType, keystoreProvider);
    }
    try (InputStream fis = new BufferedInputStream(keystore)) {
      ks.load(fis, keystorePassword.toCharArray());
      final Key key = ks.getKey(keyName, keyPassword.toCharArray());
      final Certificate certificate = ks.getCertificate(keyName);
      return new CPPHolderDTO(certificate, certificate.getPublicKey(),
        (PrivateKey) key);
    }
  }

  /**
   * Reads a key from the given keystore utilising the system's default
   * keystore type and security provider.
   *
   * @param keystore the keystore to read from
   * @param keystorePassword the keystore password
   * @param keyName the name of the key to read
   * @param keyPassword the key's password
   * @return the key information
   * @throws KeyStoreException thrown when they key is not valid
   * @throws IOException thrown when something unexpected happens
   * @throws CertificateException thrown when the certificate cannot be
   * generated
   * @throws NoSuchAlgorithmException thrown when no algorithm is found for
   * encryption
   * @throws UnrecoverableKeyException thrown when the key os not valid
   * @throws NoSuchProviderException thrown when the provider is not valid
   */
  @SuppressWarnings("squid:MaximumInheritanceDepth")
  public CPPHolderDTO readKeyFromKeystore(final InputStream keystore,
    final String keystorePassword,
    final String keyName, final String keyPassword)
    throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException,
    UnrecoverableKeyException, NoSuchProviderException {
    return readKeyFromKeystore(keystore, keystorePassword, keyName, keyPassword,
      null, null);
  }

  /**
   * Converts a {@link KeyStore} to a byte array.
   *
   * @param keystore The keystore to convert.
   * @param keystorePassword The password of the keystore.
   */
  public byte[] keystoreToByteArray(@NotNull final KeyStore keystore,
    @NotNull final String keystorePassword)
    throws IOException, CertificateException, NoSuchAlgorithmException, KeyStoreException {
    try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
      BufferedOutputStream bos = new BufferedOutputStream(baos)) {
      keystore.store(bos, keystorePassword.toCharArray());
      return baos.toByteArray();
    }
  }

  /**
   * Converts a byte array representing a {@link KeyStore} to a KeyStore.
   *
   * @param keystore The keystore representation as a byte array.
   * @param keystoreType The type of the keystore, e.g. PKCS12
   * @param keystorePassword The password of the keystore.
   * @param keystoreProvider A provider for the specific keystore type.
   */
  public KeyStore keystoreFromByteArray(@NotNull final byte[] keystore,
    final String keystoreType, @NotNull final String keystorePassword,
    final String keystoreProvider)
    throws KeyStoreException, NoSuchProviderException, IOException, CertificateException,
    NoSuchAlgorithmException {
    final KeyStore ks;

    if (StringUtils.isBlank(keystoreType) || StringUtils
      .isBlank(keystoreProvider)) {
      ks = KeyStore.getInstance(KeyStore.getDefaultType());
    } else {
      ks = KeyStore.getInstance(keystoreType, keystoreProvider);
    }
    try (BufferedInputStream bis = new BufferedInputStream(
      new ByteArrayInputStream(keystore))) {
      if (StringUtils.isNotBlank(keystorePassword)) {
        ks.load(bis, keystorePassword.toCharArray());
      } else {
        ks.load(bis, null);
      }
    }

    return ks;
  }

  /**
   * Creates an empty keystore. This keystore can later on be used to add keys
   * and certificates into it.
   *
   * @param keystoreType The type of the keystore to create.
   * @param keystoreProvider The provider for the specific keystore type.
   * @param keystorePassword The password of the keystore.
   */
  public byte[] createKeystore(final String keystoreType,
    final String keystoreProvider,
    @NotNull final String keystorePassword)
    throws KeyStoreException, NoSuchProviderException, CertificateException, NoSuchAlgorithmException,
    IOException {
    // Create a new keystore.
    KeyStore ks;
    if (StringUtils.isBlank(keystoreType) || StringUtils
      .isBlank(keystoreProvider)) {
      ks = KeyStore.getInstance(KeyStore.getDefaultType());
    } else {
      ks = KeyStore.getInstance(keystoreType, keystoreProvider);
    }

    // Initialise the new keystore with user-provided password.
    if (StringUtils.isNotBlank(keystorePassword)) {
      ks.load(null, keystorePassword.toCharArray());
    } else {
      ks.load(null, null);
    }

    return keystoreToByteArray(ks, keystorePassword);
  }

  /**
   * Saves a symmetric key to the keystore. If the key identified by the alias
   * of the key already exists it gets overwritten.
   *
   * @param keystore The keystore to save the symmetric key into.
   * @param keystoreType The type of the keystore.
   * @param keystoreProvider The provider for the specific type of keystore.
   * @param keystorePassword The password of the keystore.
   * @param keyAlias The alias under which the key will be saved.
   * @param key The key to save.
   * @param keyPassword The password of the key.
   * @param keyAlgorithm The algorithm with which the key was generated.
   */
  @SuppressWarnings("squid:S00107")
  public byte[] saveSymmetricKey(@NotNull final byte[] keystore,
    final String keystoreType,
    final String keystoreProvider, @NotNull final String keystorePassword,
    @NotNull final String keyAlias, @NotNull final byte[] key,
    @NotNull final String keyPassword,
    @NotNull final String keyAlgorithm)
    throws KeyStoreException, NoSuchProviderException, CertificateException, NoSuchAlgorithmException,
    IOException {
    // Load the keystore.
    KeyStore ks = keystoreFromByteArray(keystore, keystoreType,
      keystorePassword, keystoreProvider);

    // Add the key.
    ks.setEntry(keyAlias,
      new KeyStore.SecretKeyEntry(
        new SecretKeySpec(key, 0, key.length, keyAlgorithm)),
      new KeyStore.PasswordProtection(keyPassword.toCharArray()));

    return keystoreToByteArray(ks, keystorePassword);
  }

  /**
   * Saves a private (asymmetric) key to the keystore. If the key identified
   * by the alias of the key already exists it gets overwritten.
   *
   * @param keystore The keystore to save the symmetric key into.
   * @param keystoreType The type of the keystore.
   * @param keystoreProvider The provider for the specific type of keystore.
   * @param keystorePassword The password of the keystore.
   * @param keyAlias The alias under which the key will be saved.
   * @param key The key to save in DER format.
   * @param keyAlgorithm The algorithm the key was generated with.
   * @param keyProvider The provider for the specific key algorithm.
   * @param keyPassword The password of the key.
   * @param certificates The certificate chain for the key.
   */
  @SuppressWarnings("squid:S00107")
  public byte[] savePrivateKey(@NotNull final byte[] keystore,
    final String keystoreType,
    final String keystoreProvider, final String keystorePassword,
    final String keyAlias,
    final byte[] key, final String keyAlgorithm, final String keyProvider,
    final String keyPassword, final Set<byte[]> certificates)
    throws NoSuchAlgorithmException, CertificateException, NoSuchProviderException, KeyStoreException,
    IOException, InvalidKeySpecException {
    // Load the keystore.
    KeyStore ks = keystoreFromByteArray(keystore, keystoreType,
      keystorePassword, keystoreProvider);

    Certificate[] certs = certificates.stream().map(cert -> {
      try {
        return new JcaX509CertificateConverter()
          .getCertificate(new X509CertificateHolder(cert));
      } catch (CertificateException | IOException e) {
        log.log(Level.SEVERE, "Could not read certificate.", e);
        return null;
      }
    }).toArray(Certificate[]::new);

    // Add the key.
    final Key privateKey = cryptoAsymmetricService
      .privateKeyFromByteArray(key, keyAlgorithm, keyProvider);
    ks.setKeyEntry(keyAlias, privateKey,
      keyPassword != null ? keyPassword.toCharArray() : "".toCharArray(),
      certs);

    return keystoreToByteArray(ks, keystorePassword);
  }

  /**
   * Saves a certificate to the keystore. If the certificate identified by the
   * alias already exists it gets overwritten.
   *
   * @param keystore The keystore to save the symmetric key into.
   * @param keystoreType The type of the keystore.
   * @param keystoreProvider The provider for the specific type of keystore.
   * @param keystorePassword The password of the keystore.
   * @param certificate The certificate to save.
   * @param certificateAlias The alias under which the certificate is saved.
   */
  public byte[] saveCertificate(@NotNull final byte[] keystore,
    final String keystoreType,
    final String keystoreProvider, final String keystorePassword,
    final String certificateAlias,
    final byte[] certificate)
    throws NoSuchAlgorithmException, CertificateException, NoSuchProviderException, KeyStoreException,
    IOException {
    // Load the keystore.
    KeyStore ks = keystoreFromByteArray(keystore, keystoreType,
      keystorePassword, keystoreProvider);

    // Add the certificate.
    ks.setCertificateEntry(certificateAlias,
      new JcaX509CertificateConverter()
        .getCertificate(new X509CertificateHolder(certificate)));

    return keystoreToByteArray(ks, keystorePassword);
  }
}
