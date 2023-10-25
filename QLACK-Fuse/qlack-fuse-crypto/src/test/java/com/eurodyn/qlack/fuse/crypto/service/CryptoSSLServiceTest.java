package com.eurodyn.qlack.fuse.crypto.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CryptoSSLServiceTest {

  @InjectMocks
  private CryptoSSLService cryptoSSLService;

  @Mock
  private CryptoCAService cryptoCAService;

  @InjectMocks
  private CryptoAsymmetricService cryptoAsymmetricService;

  @BeforeEach
  public void init() {
    cryptoAsymmetricService = new CryptoAsymmetricService();
    cryptoSSLService = new CryptoSSLService(cryptoCAService,
      cryptoAsymmetricService);
  }
/*
  //TODO fix test
  @Test
  @Disabled
  public void getSocketFactoryTest()
    throws CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, IOException,
    KeyManagementException, KeyStoreException, InvalidKeySpecException,
    NoSuchProviderException {
    List<SSLSocketFactoryCertificateDTO> sslSocketFactoryCertificateDTOS = new ArrayList<>();
    SSLSocketFactoryCertificateDTO sslSocketFactoryCertificateDTO = new SSLSocketFactoryCertificateDTO();
    sslSocketFactoryCertificateDTO.setName("certificate");
    sslSocketFactoryCertificateDTO.setPemCertificate("certificate");
    sslSocketFactoryCertificateDTOS.add(sslSocketFactoryCertificateDTO);

    CreateKeyPairDTO createKeyPairDTO = new CreateKeyPairDTO();
    createKeyPairDTO.setKeyPairGeneratorAlgorithm("RSA");
    createKeyPairDTO.setKeySize(2048);

    SSLSocketFactoryPrivateKeyDTO sslSocketFactoryPrivateKeyDTO = new SSLSocketFactoryPrivateKeyDTO();
    sslSocketFactoryPrivateKeyDTO.setAlgorithm("RSA");
    sslSocketFactoryPrivateKeyDTO.setName("RSA");
    sslSocketFactoryPrivateKeyDTO.setPemPrivateKey(cryptoAsymmetricService
      .privateKeyToPEM(
        cryptoAsymmetricService.createKeyPair(createKeyPairDTO)));

    SSLSocketFactoryDTO sslSocketFactoryDTO = new SSLSocketFactoryDTO();
    sslSocketFactoryDTO.setTrustedCertificates(sslSocketFactoryCertificateDTOS);
    sslSocketFactoryDTO.setClientCertificate(sslSocketFactoryCertificateDTO);
    sslSocketFactoryDTO.setClientPrivateKey(sslSocketFactoryPrivateKeyDTO);

    assertNotNull(cryptoSSLService.getSocketFactory(sslSocketFactoryDTO));
  }
*/
}
