package com.eurodyn.qlack.fuse.crypto.service;

import static org.junit.Assert.assertNotNull;

import com.eurodyn.qlack.fuse.crypto.dto.CertificateSignDTO;
import com.eurodyn.qlack.fuse.crypto.dto.CreateCADTO;
import com.eurodyn.qlack.fuse.crypto.dto.CreateKeyPairDTO;
import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.time.Instant;
import java.util.Locale;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.operator.OperatorCreationException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CryptoCAServiceTest {

  @InjectMocks
  private CryptoCAService cryptoCAService;

  @InjectMocks
  private CryptoAsymmetricService cryptoAsymmetricService;

  @Before
  public void init() {
    cryptoAsymmetricService = new CryptoAsymmetricService();
    cryptoCAService = new CryptoCAService(cryptoAsymmetricService);
  }

  private CreateCADTO createCADTO() throws NoSuchAlgorithmException, IOException {
    CreateKeyPairDTO createKeyPairDTO = new CreateKeyPairDTO();
    createKeyPairDTO.setKeyPairGeneratorAlgorithm("RSA");
    createKeyPairDTO.setKeySize(2048);

    CreateCADTO createCADTO = new CreateCADTO();
    createCADTO.setCreateKeyPairRequestDTO(createKeyPairDTO);
    createCADTO.setSubjectCN("qlack");
    createCADTO.setSignatureAlgorithm("SHA256WithRSAEncryption");
    createCADTO.setSerial(BigInteger.valueOf(2048));
    createCADTO.setValidFrom(Instant.now());
    createCADTO.setValidTo(Instant.now().plusMillis(1000));
    createCADTO.setLocale(Locale.ENGLISH);
    createCADTO.setIssuerCN("");
    createCADTO.setIssuerPrivateKey(cryptoAsymmetricService
        .convertKeyToPEM(cryptoAsymmetricService.createKeyPair(createKeyPairDTO),
            "RSA_PRIVATE_KEY"));
    createCADTO.setIssuerPrivateKeyAlgorithm("RSA");

    return createCADTO;
  }

  @Test
  public void createCADefaultTypesTest()
      throws InvalidKeySpecException, IOException, NoSuchAlgorithmException, OperatorCreationException {
    assertNotNull(cryptoCAService.createCA(createCADTO()));
  }

  @Test
  public void createCANullKeyTest()
      throws InvalidKeySpecException, IOException, NoSuchAlgorithmException, OperatorCreationException {
    CreateCADTO createCADTO = createCADTO();
    createCADTO.setIssuerCN("qlack");

    assertNotNull(cryptoCAService.createCA(createCADTO));
  }

  @Test
  public void createCATest()
      throws InvalidKeySpecException, IOException, NoSuchAlgorithmException, OperatorCreationException {
    CreateCADTO createCADTO = createCADTO();
    createCADTO.setIssuerCN("qlack");
    createCADTO.setIssuerPrivateKey(cryptoAsymmetricService.convertKeyToPEM(
        cryptoAsymmetricService.createKeyPair(createCADTO.getCreateKeyPairRequestDTO()),
        "RSA PRIVATE KEY"));

    assertNotNull(cryptoCAService.createCA(createCADTO));
  }

  @Test
  public void pemToCertificateTest()
      throws IOException, NoSuchAlgorithmException, OperatorCreationException, CertificateException {
    CertificateSignDTO certificateSignDTO = new CertificateSignDTO();
    CreateCADTO createCADTO = createCADTO();

    KeyPair keyPair = cryptoAsymmetricService
        .createKeyPair(createCADTO.getCreateKeyPairRequestDTO());
    certificateSignDTO.setValidForm(createCADTO.getValidFrom());
    certificateSignDTO.setValidTo(createCADTO.getValidTo());
    certificateSignDTO.setLocale(createCADTO.getLocale());
    certificateSignDTO.setPublicKey(keyPair.getPublic());
    certificateSignDTO.setPrivateKey(keyPair.getPrivate());
    certificateSignDTO.setSignatureAlgorithm(createCADTO.getSignatureAlgorithm());
    certificateSignDTO.setSubjectCN(createCADTO.getSubjectCN());
    certificateSignDTO.setIssuerPrivateKey(keyPair.getPrivate());
    certificateSignDTO.setIssuerCN("qlack");
    certificateSignDTO.setCa(false);

    X509CertificateHolder certificateHolder = cryptoCAService
        .generateCertificate(certificateSignDTO);

    assertNotNull(
        cryptoCAService.pemToCertificate(cryptoCAService.certificateToPEM(certificateHolder)));
  }

}
