package com.eurodyn.qlack.fuse.crypto.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

@RunWith(MockitoJUnitRunner.class)
public class CryptoInfoServiceTest {

  @InjectMocks
  private CryptoInfoService cryptoInfoService;

  @Before
  public void init() {
    cryptoInfoService = new CryptoInfoService();
  }

  @Test
  public void getSecurityProvidersTest() {
    assertNotNull(cryptoInfoService.getSecurityProviders());
  }

  @Test
  public void getSecurityServicesTest() {
    assertNotEquals(Collections.emptyList(), cryptoInfoService.getSecurityServices("SUN"));
    assertEquals(Collections.emptyList(),
        cryptoInfoService.getSecurityServices("UnexistedProvider"));
  }

  @Test
  public void getSecurityServicesForAlgorithmTypeTest() {
    assertNotEquals(Collections.emptyList(),
        cryptoInfoService.getSecurityServicesForAlgorithmType("Cipher"));
    assertEquals(Collections.emptyList(),
        cryptoInfoService.getSecurityServicesForAlgorithmType("UnexistedAlgorithm"));
  }

  @Test
  public void getAlgorithmTypesTest() {
    assertNotNull(cryptoInfoService.getAlgorithmTypes());
  }

  @Test
  public void prettyPrintTest() {
    assertNotEquals("",
        cryptoInfoService.prettyPrint(cryptoInfoService.getSecurityServices("SUN")));
  }

  @Test
  public void isUnlimitedStrengthActiveTest() throws NoSuchAlgorithmException {
    ReflectionTestUtils.setField(cryptoInfoService, "AES_WEAK_LENGTH", 128);
    assertTrue(cryptoInfoService.isUnlimitedStrengthActive());
  }

  @Test
  public void isUnlimitedStrengthActiveFalseTest() throws NoSuchAlgorithmException {
    ReflectionTestUtils.setField(cryptoInfoService, "AES_WEAK_LENGTH", 2147483647);
    assertFalse(cryptoInfoService.isUnlimitedStrengthActive());
  }

}
