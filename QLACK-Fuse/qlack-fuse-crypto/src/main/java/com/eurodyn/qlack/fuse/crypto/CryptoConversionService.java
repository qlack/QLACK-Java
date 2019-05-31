package com.eurodyn.qlack.fuse.crypto;

import static com.eurodyn.qlack.fuse.crypto.CryptoConstants.BC;
import static com.eurodyn.qlack.fuse.crypto.CryptoConstants.CERTIFICATE;
import static com.eurodyn.qlack.fuse.crypto.CryptoConstants.PEM_BEGIN;
import static com.eurodyn.qlack.fuse.crypto.CryptoConstants.PEM_END;
import static com.eurodyn.qlack.fuse.crypto.CryptoConstants.RSA;
import static com.eurodyn.qlack.fuse.crypto.CryptoConstants.RSA_PRIVATE_KEY;
import static com.eurodyn.qlack.fuse.crypto.CryptoConstants.RSA_PUBLIC_KEY;

import javax.crypto.spec.SecretKeySpec;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemWriter;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * Format conversion services for security elements.
 */
@Service
@Validated
public class CryptoConversionService {

  /**
   * Converts a {@link Key} to its primary encoding format as Base64 string.
   *
   * @param key The key to convert.
   *
   * @return the string value of the key
   */
  public String keyToString(Key key) {
    return Base64.getEncoder().encodeToString(key.getEncoded());
  }

  /**
   * Converts a key in Base64 format to a {@link Key}.
   *
   * @param key The key to convert.
   * @param keyAlgorithm The algorithm with which this key was created.
   *
   * @return the key
   */
  public Key stringToKey(String key, String keyAlgorithm) {
    byte[] decodedKey = Base64.getDecoder().decode(key);

    return new SecretKeySpec(decodedKey, 0, decodedKey.length, keyAlgorithm);
  }

  private String convertKeyToPEM(KeyPair keyPair, String keyType) throws IOException {
    try (StringWriter pemStrWriter = new StringWriter()) {
      try (JcaPEMWriter pemWriter = new JcaPEMWriter(pemStrWriter)) {
        if (keyType.equals(RSA_PRIVATE_KEY)) {
          pemWriter.writeObject(new PemObject(keyType, keyPair.getPrivate().getEncoded()));
        } else if (keyType.equals(RSA_PUBLIC_KEY)) {
          pemWriter.writeObject(new PemObject(keyType, keyPair.getPublic().getEncoded()));
        }
        pemWriter.flush();
        return pemStrWriter.toString();
      }
    }
  }

  /**
   * Converts a public key to string in PEM format.
   *
   * @param keyPair The keypair containing the public key to convert.
   *
   * @return the PEM representation of the
   * @throws IOException IOException
   */
  public String publicKeyToPEM(KeyPair keyPair) throws IOException {
    return convertKeyToPEM(keyPair, RSA_PUBLIC_KEY);
  }

  /**
   * Converts a private key to string in PEM format.
   *
   * @param keyPair The keypair containing the private key to convert.
   *
   * @return the PEM representation of the key
   * @throws IOException IOException
   */
  public String privateKeyToPEM(KeyPair keyPair) throws IOException {
    return convertKeyToPEM(keyPair, RSA_PRIVATE_KEY);
  }

  public String certificateToPEM(X509CertificateHolder certificateHolder) throws IOException {
    try (StringWriter pemStrWriter = new StringWriter()) {
      try (PemWriter writer = new PemWriter(pemStrWriter)) {
        //        writer.writeObject(new PemObject(CERTIFICATE, certificateHolder.toASN1Structure().getEncoded()));
        writer.writeObject(new PemObject(CERTIFICATE, certificateHolder.getEncoded()));
        writer.flush();
        return pemStrWriter.toString();
      }
    }
  }

  /**
   * Converts a text-based public key (in PEM format) to {@link PublicKey}.
   *
   * @param publicKey The public key in PEM format to convert.
   *
   * @return PublicKey
   * @throws NoSuchAlgorithmException NoSuchAlgorithmException
   * @throws NoSuchProviderException NoSuchProviderException
   * @throws InvalidKeySpecException InvalidKeySpecException
   * @throws IOException IOException
   */
  private PublicKey pemToPublicKey(String publicKey)
      throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeySpecException, IOException {
    return pemToPublicKey(publicKey, BC, RSA);
  }

  /**
   * Converts a text-based public key (in PEM format) to {@link PublicKey}.
   *
   * @param publicKey The public key in PEM format to convert.
   * @param provider The security provider with which this key was generated.
   * @param algorithm The security algorithm with which this key was generated.
   *
   * @return PublicKey
   * @throws NoSuchProviderException NoSuchProviderException
   * @throws NoSuchAlgorithmException NoSuchAlgorithmException
   * @throws InvalidKeySpecException InvalidKeySpecException
   */
  private PublicKey pemToPublicKey(String publicKey, String provider, String algorithm)
      throws NoSuchProviderException, NoSuchAlgorithmException, InvalidKeySpecException {
    PublicKey key = null;

    // Cleanup the PEM from unwanted text.
    publicKey = publicKey.replaceAll(PEM_BEGIN, "")
        .replaceAll(PEM_END, "")
        .replaceAll("[\n\r]", "")
        .trim();

    // Read the cleaned up PEM and generate the public key.
    byte[] encoded = Base64.getDecoder().decode(publicKey);
    X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encoded);
    KeyFactory factory = KeyFactory.getInstance(algorithm, provider);
    key = factory.generatePublic(keySpec);

    return key;
  }

  /**
   * Converts a text-based private key (in PEM format) to {@link PrivateKey}.
   *
   * @param privateKey The private key in PEM format to convert.
   * @param provider The security provider with which this key was generated.
   * @param algorithm The security algorithm with which this key was generated.
   *
   * @return PrivateKey
   * @throws NoSuchProviderException NoSuchProviderException
   * @throws NoSuchAlgorithmException NoSuchAlgorithmException
   * @throws InvalidKeySpecException InvalidKeySpecException
   */
  public PrivateKey pemToPrivateKey(String privateKey, String provider, String algorithm)
      throws NoSuchProviderException, NoSuchAlgorithmException, InvalidKeySpecException {
    PrivateKey key = null;

    // Cleanup the PEM from unwanted text.
    privateKey = privateKey.replaceAll(PEM_BEGIN, "")
        .replaceAll(PEM_END, "")
        .replaceAll("[\n\r]", "")
        .trim();

    // Read the cleaned up PEM and generate the public key.
    byte[] encoded = Base64.getDecoder().decode(privateKey);
    PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
    KeyFactory factory = KeyFactory.getInstance(algorithm, provider);
    key = factory.generatePrivate(keySpec);

    return key;
  }

  /**
   * Parses a certificate in PEM format encoded as X.509.
   *
   * @param cert The certificate in PEM format.
   *
   * @return X509Certificate
   * @throws CertificateException CertificateException
   */
  public X509Certificate pemToCertificate(String cert) throws CertificateException {
    CertificateFactory fact = CertificateFactory.getInstance("X.509");

    return (X509Certificate) fact.generateCertificate(new ByteArrayInputStream(cert.getBytes(StandardCharsets.UTF_8)));
  }
}
