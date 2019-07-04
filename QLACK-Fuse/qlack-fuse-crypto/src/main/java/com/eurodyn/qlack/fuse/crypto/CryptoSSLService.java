package com.eurodyn.qlack.fuse.crypto;

import com.eurodyn.qlack.fuse.crypto.dto.SSLSocketFactoryCertificateDTO;
import com.eurodyn.qlack.fuse.crypto.dto.SSLSocketFactoryDTO;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import javax.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.util.UUID;

/**
 * SSL utilities.
 */
@Service
@Validated
public class CryptoSSLService {

  private final CryptoCAService cryptoCAService;
  private final CryptoAsymmetricService cryptoAsymmetricService;
  public static final String CERT_TYPE = "X509";

  public CryptoSSLService(CryptoCAService cryptoCAService,
      CryptoAsymmetricService cryptoAsymmetricService) {
    this.cryptoCAService = cryptoCAService;
    this.cryptoAsymmetricService = cryptoAsymmetricService;
  }

  /**
   * Creates an SSL socket factory to be used in clients requiring certificate-based
   * authentication.
   *
   * @param sslSocketFactoryDTO The details of the SSL socket factory to create.
   */
  public SSLSocketFactory getSocketFactory(@Valid SSLSocketFactoryDTO sslSocketFactoryDTO)
  throws CertificateException, IOException, KeyStoreException, NoSuchAlgorithmException,
         UnrecoverableKeyException, KeyManagementException, InvalidKeySpecException {

    // Certificates to trust.
    KeyStore caKs = KeyStore.getInstance(KeyStore.getDefaultType());
    caKs.load(null, null);
    for (SSLSocketFactoryCertificateDTO certificate : sslSocketFactoryDTO
        .getTrustedCertificates()) {
      caKs.setCertificateEntry(certificate.getName(),
          cryptoCAService.pemToCertificate(certificate.getPemCertificate()));
    }
    TrustManagerFactory tmf = TrustManagerFactory.getInstance(CERT_TYPE);
    tmf.init(caKs);

    // Client key and certificate.
    String randomPassword = UUID.randomUUID().toString();
    KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
    ks.load(null, null);
    ks.setCertificateEntry(sslSocketFactoryDTO.getClientCertificate().getName(), cryptoCAService
        .pemToCertificate(sslSocketFactoryDTO.getClientCertificate().getPemCertificate()));
    ks.setKeyEntry(sslSocketFactoryDTO.getClientPrivateKey().getName(), cryptoAsymmetricService
            .pemToPrivateKey(sslSocketFactoryDTO.getClientPrivateKey().getPemPrivateKey(),
                sslSocketFactoryDTO.getClientPrivateKey().getAlgorithm()),
        randomPassword.toCharArray(), new java.security.cert.Certificate[]{cryptoCAService
            .pemToCertificate(sslSocketFactoryDTO.getClientCertificate().getPemCertificate())});
    KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory
        .getDefaultAlgorithm());
    kmf.init(ks, randomPassword.toCharArray());

    // Create SSL socket factory
    SSLContext context = SSLContext.getInstance(sslSocketFactoryDTO.getTlsVersion());
    context.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

    return context.getSocketFactory();
  }
}
