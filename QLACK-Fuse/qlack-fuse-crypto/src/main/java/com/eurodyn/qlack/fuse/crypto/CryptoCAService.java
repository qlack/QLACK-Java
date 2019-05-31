package com.eurodyn.qlack.fuse.crypto;

import com.eurodyn.qlack.fuse.crypto.dto.CPPPemHolderDTO;
import com.eurodyn.qlack.fuse.crypto.dto.CreateCADTO;
import com.eurodyn.qlack.fuse.crypto.dto.SignDTO;
import com.eurodyn.qlack.fuse.crypto.dto.SignDTO.SignDTOBuilder;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.operator.OperatorCreationException;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.io.IOException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidKeySpecException;

/**
 * Certificate Authority management.
 */
@Service
@Validated
public class CryptoCAService {

  private final CryptoKeyService cryptoKeyService;
  private final CryptoSignService cryptoSignService;
  private final CryptoConversionService cryptoConversionService;

  public CryptoCAService(CryptoKeyService cryptoKeyService,
      CryptoSignService cryptoSignService,
      CryptoConversionService cryptoConversionService) {
    this.cryptoKeyService = cryptoKeyService;
    this.cryptoSignService = cryptoSignService;
    this.cryptoConversionService = cryptoConversionService;
  }

  /**
   * Create a new Certificate Authority. This method also supports creating a sub-CA by providing the issuer's
   * information.
   *
   * @param createCADTO The details of the CA to be created.
   *
   * @return CPPPemHolderDTO
   * @throws NoSuchProviderException NoSuchProviderException
   * @throws NoSuchAlgorithmException NoSuchAlgorithmException
   * @throws InvalidKeySpecException InvalidKeySpecException
   * @throws OperatorCreationException OperatorCreationException
   * @throws IOException IOException
   */
  public CPPPemHolderDTO createCA(CreateCADTO createCADTO)
      throws NoSuchProviderException, NoSuchAlgorithmException, InvalidKeySpecException, OperatorCreationException, IOException {
    // Create a keypair for this CA.
    KeyPair keyPair = cryptoKeyService.createKeyPair(createCADTO.getCreateKeyPairRequestDTO());

    // Prepare signing.
    final SignDTOBuilder signDTOBuilder = SignDTO.builder()
        .validForm(createCADTO.getValidFrom())
        .validTo(createCADTO.getValidTo())
        .locale(createCADTO.getLocale())
        .publicKey(keyPair.getPublic())
        .privateKey(keyPair.getPrivate())
        .signatureAlgorithm(createCADTO.getSignatureAlgorithm())
        .signatureProvider(createCADTO.getSignatureProvider())
        .subjectCN(createCADTO.getSubjectCN())
        .ca(true);

    // Choose which private key to use. If no parent key is found then this is a self-signed certificate and the
    // private key created for the keypair will be used.
    if (StringUtils.isNotEmpty(createCADTO.getIssuerCN()) && StringUtils
        .isNotEmpty(createCADTO.getIssuerPrivateKey())) {
      signDTOBuilder
          .issuerPrivateKey(cryptoConversionService.pemToPrivateKey(
              createCADTO.getIssuerPrivateKey(),
              createCADTO.getIssuerPrivateKeyProvider(),
              createCADTO.getIssuerPrivateKeyAlgorithm()))
          .issuerCN(createCADTO.getIssuerCN());
    } else {
      signDTOBuilder
          .issuerPrivateKey(keyPair.getPrivate())
          .issuerCN(createCADTO.getSubjectCN());
    }

    X509CertificateHolder certHolder = cryptoSignService.signKey(signDTOBuilder.build());

    // Prepare reply.
    CPPPemHolderDTO cppPemKey = new CPPPemHolderDTO();
    cppPemKey.setPublicKey(cryptoConversionService.publicKeyToPEM(keyPair));
    cppPemKey.setPrivateKey(cryptoConversionService.privateKeyToPEM(keyPair));
    cppPemKey.setCertificate(cryptoConversionService.certificateToPEM(certHolder));

    return cppPemKey;
  }
}
