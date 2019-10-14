package com.eurodyn.qlack.fuse.crypto.service;

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CryptoKeystoreServiceTest {

  @InjectMocks
  private CryptoKeystoreService cryptoKeystoreService;

  private InputStream keystore;

  private final String execDir = Paths.get("").toAbsolutePath().toString();

  @Before
  public void init() throws FileNotFoundException {
    cryptoKeystoreService = new CryptoKeystoreService();
    File initialFile = Paths.get(execDir, "src", "test", "resources", "qlack-crypto-test.keystore")
        .toFile();
    keystore = new FileInputStream(initialFile);
  }

  @Test
  public void readKeyFromKeystoreTest()
      throws CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException, NoSuchProviderException, IOException {
    assertNotNull(cryptoKeystoreService
        .readKeyFromKeystore(keystore, "dummyPass", "qlack", "dummyPass"));
  }

  @Test
  public void readKeyFromKeystoreCustomWithProviderTest()
      throws CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException, NoSuchProviderException, IOException {
    assertNotNull(cryptoKeystoreService
        .readKeyFromKeystore(keystore, "dummyPass", "qlack", "dummyPass", "JKS", "SUN"));
  }

  @Test
  public void readKeyFromKeystoreCustomNoProviderTest()
      throws CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException, NoSuchProviderException, IOException {
    assertNotNull(cryptoKeystoreService
        .readKeyFromKeystore(keystore, "dummyPass", "qlack", "dummyPass", "JKS", ""));
  }

}
