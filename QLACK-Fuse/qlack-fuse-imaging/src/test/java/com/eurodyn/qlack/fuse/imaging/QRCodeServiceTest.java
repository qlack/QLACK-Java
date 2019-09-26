package com.eurodyn.qlack.fuse.imaging;

import com.eurodyn.qlack.fuse.imaging.service.QRCodeService;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.QRCodeWriter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.awt.*;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class QRCodeServiceTest {

  private QRCodeService qrCodeService;

  @Before
  public void init(){
    qrCodeService = new QRCodeService();
  }

  @Test
  public void generateQRCodeTest(){
    byte[] result = qrCodeService.generateQRCode("text");
    assertTrue(result.length > 0);
  }
}
