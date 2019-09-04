package com.eurodyn.qlack.fuse.crypto;

import com.eurodyn.qlack.fuse.crypto.dto.CPPPemHolderDTO;
import com.eurodyn.qlack.fuse.crypto.dto.CertificateSignDTO;
import com.eurodyn.qlack.fuse.crypto.dto.CreateCADTO;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.CertIOException;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemWriter;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.time.Instant;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Certificate Authority management.
 */
@Service
@Validated
public class CryptoCAService {

  // JUL reference.
  private static final Logger LOGGER = Logger.getLogger(CryptoCAService.class.getName());

  private final CryptoAsymmetricService cryptoAsymmetricService;
  private static final String CN = "CN";
  private static final String CERTIFICATE = "CERTIFICATE";

  public CryptoCAService(
    CryptoAsymmetricService cryptoAsymmetricService) {
    this.cryptoAsymmetricService = cryptoAsymmetricService;
  }

  /**
   * Create a new Certificate Authority. This method also supports creating a sub-CA by providing
   * the issuer's information.
   *
   * @param createCADTO The details of the CA to be created.
   */
  public CPPPemHolderDTO createCA(final CreateCADTO createCADTO)
  throws NoSuchAlgorithmException, InvalidKeySpecException, OperatorCreationException, IOException {
    // Create a keypair for this CA.
    final KeyPair keyPair =
      cryptoAsymmetricService.createKeyPair(createCADTO.getCreateKeyPairRequestDTO());

    // Prepare signing.
    CertificateSignDTO certificateSignDTO = new CertificateSignDTO();
    certificateSignDTO.setValidForm(createCADTO.getValidFrom());
    certificateSignDTO.setValidTo(createCADTO.getValidTo());
    certificateSignDTO.setLocale(createCADTO.getLocale());
    certificateSignDTO.setPublicKey(keyPair.getPublic());
    certificateSignDTO.setPrivateKey(keyPair.getPrivate());
    certificateSignDTO.setSignatureAlgorithm(createCADTO.getSignatureAlgorithm());
    certificateSignDTO.setSubjectCN(createCADTO.getSubjectCN());
    certificateSignDTO.setCa(true);

    // Choose which private key to use. If no parent key is found then this is a self-signed certificate and the
    // private key created for the keypair will be used.
    if (StringUtils.isNotEmpty(createCADTO.getIssuerCN()) && StringUtils
      .isNotEmpty(createCADTO.getIssuerPrivateKey())) {
      certificateSignDTO.setIssuerPrivateKey(cryptoAsymmetricService.pemToPrivateKey(
        createCADTO.getIssuerPrivateKey(),
        createCADTO.getIssuerPrivateKeyAlgorithm()));
      certificateSignDTO.setIssuerCN(createCADTO.getIssuerCN());
    } else {
      certificateSignDTO.setIssuerPrivateKey(keyPair.getPrivate());
      certificateSignDTO.setIssuerCN(createCADTO.getSubjectCN());
    }

    final X509CertificateHolder certHolder = generateCertificate(certificateSignDTO);

    // Prepare reply.
    final CPPPemHolderDTO cppPemKey = new CPPPemHolderDTO();
    cppPemKey.setPublicKey(cryptoAsymmetricService.publicKeyToPEM(keyPair));
    cppPemKey.setPrivateKey(cryptoAsymmetricService.privateKeyToPEM(keyPair));
    cppPemKey.setCertificate(certificateToPEM(certHolder));

    return cppPemKey;
  }

  /**
   * Signs a key with another key providing a certificate.
   *
   * @param signDTO The details of the signing to take place.
   */
  public X509CertificateHolder generateCertificate(final CertificateSignDTO signDTO)
  throws OperatorCreationException, CertIOException {

    // Create a generator for the certificate including all certificate details.
    final X509v3CertificateBuilder certGenerator;
    // Synchronize this part, so that no two certificates can be created with the same timestamp.
    synchronized (this) {
      try {
        Thread.sleep(1);
      } catch (InterruptedException e) {
        LOGGER.log(Level.SEVERE, "Could not wait for 1 msec while generating the certificate.");
      }
      certGenerator = new X509v3CertificateBuilder(
        new X500Name(
          CN + "=" + StringUtils.defaultIfBlank(signDTO.getIssuerCN(), signDTO.getSubjectCN())),
        signDTO.isCa() ? BigInteger.ONE : BigInteger.valueOf(Instant.now().toEpochMilli()),
        new Date(signDTO.getValidForm().toEpochMilli()),
        new Date(signDTO.getValidTo().toEpochMilli()),
        signDTO.getLocale(),
        new X500Name(CN + "=" + signDTO.getSubjectCN()),
        SubjectPublicKeyInfo.getInstance(signDTO.getPublicKey().getEncoded())
      );
    }

    // Check if this is a CA certificate and in that case add the necessary key extensions.
    if (signDTO.isCa()) {
      certGenerator.addExtension(Extension.basicConstraints, true, new BasicConstraints(true));
      certGenerator
        .addExtension(Extension.keyUsage, true,
          new KeyUsage(KeyUsage.cRLSign | KeyUsage.keyCertSign));
    } else {
      certGenerator.addExtension(Extension.basicConstraints, true, new BasicConstraints(false));
    }

    // Generate the certificate.
    final X509CertificateHolder certHolder;
    certHolder = certGenerator.build(
      new JcaContentSignerBuilder(signDTO.getSignatureAlgorithm())
        .build(signDTO.getIssuerPrivateKey()));

    return certHolder;
  }

  /**
   * Converts a certificate to a PEM format encoded as X.509.
   *
   * @param certificateHolder The certificate to convert.
   */
  public String certificateToPEM(final X509CertificateHolder certificateHolder) throws IOException {
    try (StringWriter pemStrWriter = new StringWriter()) {
      try (PemWriter writer = new PemWriter(pemStrWriter)) {
        writer.writeObject(new PemObject(CERTIFICATE, certificateHolder.getEncoded()));
        writer.flush();
        return pemStrWriter.toString();
      }
    }
  }

  /**
   * Parses a certificate in PEM format encoded as X.509.
   *
   * @param cert The certificate in PEM format.
   */
  public X509Certificate pemToCertificate(final String cert) throws CertificateException {
    CertificateFactory fact = CertificateFactory.getInstance("X.509");

    return (X509Certificate) fact.generateCertificate(new ByteArrayInputStream(cert.getBytes(
      StandardCharsets.UTF_8)));
  }
}
