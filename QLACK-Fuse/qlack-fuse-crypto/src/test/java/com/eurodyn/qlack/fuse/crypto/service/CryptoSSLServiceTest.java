package com.eurodyn.qlack.fuse.crypto.service;

import static org.junit.Assert.assertNotNull;

import com.eurodyn.qlack.fuse.crypto.dto.CreateKeyPairDTO;
import com.eurodyn.qlack.fuse.crypto.dto.SSLSocketFactoryCertificateDTO;
import com.eurodyn.qlack.fuse.crypto.dto.SSLSocketFactoryDTO;
import com.eurodyn.qlack.fuse.crypto.dto.SSLSocketFactoryPrivateKeyDTO;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CryptoSSLServiceTest {

  @InjectMocks
  private CryptoSSLService cryptoSSLService;

  @Mock
  private CryptoCAService cryptoCAService;

  @InjectMocks
  private CryptoAsymmetricService cryptoAsymmetricService;

  @Before
  public void init() {
    cryptoAsymmetricService = new CryptoAsymmetricService();
    cryptoSSLService = new CryptoSSLService(cryptoCAService,
      cryptoAsymmetricService);
  }

  //TODO fix test
  @Test
  @Ignore
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

}
