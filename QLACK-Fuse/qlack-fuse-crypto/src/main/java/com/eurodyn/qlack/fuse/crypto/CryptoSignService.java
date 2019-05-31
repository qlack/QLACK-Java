package com.eurodyn.qlack.fuse.crypto;

import static com.eurodyn.qlack.fuse.crypto.CryptoConstants.CN;

import com.eurodyn.qlack.fuse.crypto.dto.SignDTO;
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
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.math.BigInteger;
import java.time.Instant;
import java.util.Date;

/**
 * Signing services.
 */
@Service
@Validated
public class CryptoSignService {

  /**
   * Signs a key, providing a certificate, with another key.
   *
   * @param signDTO The details of the signing to take place.
   * @return X509CertificateHolder
   * @throws OperatorCreationException OperatorCreationException
   * @throws CertIOException CertIOException
   */
  public X509CertificateHolder signKey(SignDTO signDTO) throws OperatorCreationException, CertIOException {

    // Create a generator for the certificate including all certificate details.
    X509v3CertificateBuilder certGenerator;
    synchronized (this) {
      certGenerator = new X509v3CertificateBuilder(
          new X500Name(CN + "=" + StringUtils.defaultIfBlank(signDTO.getIssuerCN(), signDTO.getSubjectCN())),
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
          .addExtension(Extension.keyUsage, true, new KeyUsage(KeyUsage.cRLSign | KeyUsage.keyCertSign));
    } else {
      certGenerator.addExtension(Extension.basicConstraints, true, new BasicConstraints(false));
    }

    // Generate the certificate.
    X509CertificateHolder certHolder;
    certHolder = certGenerator.build(
        new JcaContentSignerBuilder(signDTO.getSignatureAlgorithm())
            .setProvider(signDTO.getSignatureProvider())
            .build(signDTO.getIssuerPrivateKey()));

    return certHolder;
  }
}
