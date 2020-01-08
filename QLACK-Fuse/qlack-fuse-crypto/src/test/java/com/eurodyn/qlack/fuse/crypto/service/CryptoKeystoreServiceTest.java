package com.eurodyn.qlack.fuse.crypto.service;

import static org.junit.Assert.assertNotNull;

import com.eurodyn.qlack.fuse.crypto.dto.CertificateSignDTO;
import com.eurodyn.qlack.fuse.crypto.dto.CreateKeyPairDTO;
import com.google.common.collect.ImmutableSet;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.operator.OperatorCreationException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.security.KeyPair;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

@RunWith(MockitoJUnitRunner.class)
public class CryptoKeystoreServiceTest {

  @InjectMocks
  private CryptoKeystoreService cryptoKeystoreService;

  @InjectMocks
  private CryptoSymmetricService cryptoSymmetricService;

  @InjectMocks
  private CryptoAsymmetricService cryptoAsymmetricService;

  @InjectMocks
  private CryptoCAService cryptoCAService;

  private InputStream keystore;

  private final String execDir = Paths.get("").toAbsolutePath().toString();

  @Before
  public void init() throws FileNotFoundException {
    cryptoKeystoreService = new CryptoKeystoreService(cryptoAsymmetricService);
    File initialFile = Paths
      .get(execDir, "src", "test", "resources", "qlack-crypto-test.keystore")
      .toFile();
    keystore = new FileInputStream(initialFile);
  }

  @Test
  public void readKeyFromKeystoreTest()
    throws CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException,
    KeyStoreException, NoSuchProviderException, IOException {
    assertNotNull(cryptoKeystoreService
      .readKeyFromKeystore(keystore, "dummyPass", "qlack", "dummyPass"));
  }

  @Test
  public void readKeyFromKeystoreCustomWithProviderTest()
    throws CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException,
    KeyStoreException, NoSuchProviderException, IOException {
    assertNotNull(cryptoKeystoreService
      .readKeyFromKeystore(keystore, "dummyPass", "qlack", "dummyPass", "JKS",
        "SUN"));
  }

  @Test
  public void readKeyFromKeystoreCustomNoProviderTest()
    throws CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException,
    KeyStoreException, NoSuchProviderException, IOException {
    assertNotNull(cryptoKeystoreService
      .readKeyFromKeystore(keystore, "dummyPass", "qlack", "dummyPass", "JKS",
        ""));
  }

  @Test
  public void createKeystore()
    throws NoSuchAlgorithmException, CertificateException, NoSuchProviderException, KeyStoreException,
    IOException {
    assertNotNull(cryptoKeystoreService.createKeystore("JKS", "SUN", "pass"));
  }

  @Test
  public void saveSymmetricKey()
    throws NoSuchAlgorithmException, CertificateException, NoSuchProviderException, KeyStoreException,
    IOException {
    final String keystoreType = "PKCS12";
    final String keystoreProvider = "SunJSSE";

    final byte[] keystore = cryptoKeystoreService
      .createKeystore(keystoreType, keystoreProvider, "pass");

    final byte[] key = cryptoSymmetricService.generateKey(128, "AES").getEncoded();

    final byte[] updatedKeystore = cryptoKeystoreService
      .saveSymmetricKey(keystore, keystoreType, keystoreProvider, "pass",
        "test", key, "pass",
        "AES");
    assertNotNull(updatedKeystore);
  }

  @Test
  public void savePrivateKey()
    throws NoSuchAlgorithmException, CertificateException, NoSuchProviderException, KeyStoreException,
    IOException, OperatorCreationException, InvalidKeySpecException {
    final String keystoreType = "PKCS12";
    final String keystoreProvider = "SunJSSE";

    final byte[] keystore = cryptoKeystoreService
      .createKeystore(keystoreType, keystoreProvider, "pass");

    final KeyPair key = cryptoAsymmetricService
      .createKeyPair(CreateKeyPairDTO.builder()
        .keyPairGeneratorAlgorithm("RSA")
        .keySize(1024).build());

    final X509CertificateHolder cert = cryptoCAService
      .generateCertificate(CertificateSignDTO.builder()
        .ca(false)
        .issuerCN("test-issuer")
        .issuerPrivateKey(key.getPrivate())
        .privateKey(key.getPrivate())
        .publicKey(key.getPublic())
        .locale(Locale.ENGLISH)
        .signatureAlgorithm("SHA256WithRSAEncryption")
        .subjectCN("test-subject")
        .validForm(Instant.now())
        .validTo(Instant.now().plus(1, ChronoUnit.DAYS))
        .build());

    final byte[] updatedKeystore = cryptoKeystoreService
      .savePrivateKey(keystore, keystoreType, keystoreProvider, "pass",
        "test-private", key.getPrivate().getEncoded(), "RSA", "", "pass",
        ImmutableSet.of(cert.getEncoded()));

    assertNotNull(updatedKeystore);
  }

  @Test
  public void saveCertificate()
    throws NoSuchAlgorithmException, CertificateException, NoSuchProviderException, KeyStoreException,
    IOException, OperatorCreationException, InvalidKeySpecException {
    final String keystoreType = "PKCS12";
    final String keystoreProvider = "SunJSSE";

    final byte[] keystore = cryptoKeystoreService
      .createKeystore(keystoreType, keystoreProvider, "pass");

    final KeyPair key = cryptoAsymmetricService
      .createKeyPair(CreateKeyPairDTO.builder()
        .keyPairGeneratorAlgorithm("RSA")
        .keySize(1024).build());

    final X509CertificateHolder cert = cryptoCAService
      .generateCertificate(CertificateSignDTO.builder()
        .ca(false)
        .issuerCN("test-issuer")
        .issuerPrivateKey(key.getPrivate())
        .privateKey(key.getPrivate())
        .publicKey(key.getPublic())
        .locale(Locale.ENGLISH)
        .signatureAlgorithm("SHA256WithRSAEncryption")
        .subjectCN("test-subject")
        .validForm(Instant.now())
        .validTo(Instant.now().plus(1, ChronoUnit.DAYS))
        .build());

    final byte[] updatedKeystore = cryptoKeystoreService
      .saveCertificate(keystore, keystoreType, keystoreProvider, "pass",
        "test-cert", cert.getEncoded());

    assertNotNull(updatedKeystore);
  }

}
