package com.eurodyn.qlack.fuse.crypto.service;

import com.eurodyn.qlack.fuse.crypto.dto.SSLSocketFactoryCertificateDTO;
import com.eurodyn.qlack.fuse.crypto.dto.SSLSocketFactoryDTO;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
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

  @Mock
  private CryptoAsymmetricService cryptoAsymmetricService;

  @Before
  public void init() {
    cryptoSSLService = new CryptoSSLService(cryptoCAService, cryptoAsymmetricService);
  }

  @Test
  public void getSocketFactoryTest(){

    List<SSLSocketFactoryCertificateDTO> sslSocketFactoryCertificateDTOS = new ArrayList<>();
    SSLSocketFactoryCertificateDTO sslSocketFactoryCertificateDTO = new SSLSocketFactoryCertificateDTO();
    sslSocketFactoryCertificateDTO.setName("");

    SSLSocketFactoryDTO sslSocketFactoryDTO = new SSLSocketFactoryDTO();

  }

}
