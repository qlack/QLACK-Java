package com.eurodyn.qlack.fuse.imaging.service;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class QRCodeServiceTest {

  private QRCodeService qrCodeService;

  @Before
  public void init() {
    qrCodeService = new QRCodeService();
  }

  @Test
  public void generateQRCodeTest() {
    byte[] result = qrCodeService.generateQRCode("text");
    assertTrue(result.length > 0);
  }
}
