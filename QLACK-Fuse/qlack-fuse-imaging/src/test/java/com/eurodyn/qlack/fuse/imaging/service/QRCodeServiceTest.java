package com.eurodyn.qlack.fuse.imaging.service;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class QRCodeServiceTest {

  private QRCodeService qrCodeService;

  @BeforeEach
  public void init() {
    qrCodeService = new QRCodeService();
  }

  @Test
  public void generateQRCodeTest() {
    byte[] result = qrCodeService.generateQRCode("text");
    assertTrue(result.length > 0);
  }
}
