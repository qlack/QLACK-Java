package com.eurodyn.qlack.fuse.imaging.service;

import static org.mockito.ArgumentMatchers.any;
import static org.powermock.api.mockito.PowerMockito.when;

import com.eurodyn.qlack.fuse.imaging.exception.ImagingException;
import java.awt.image.RenderedImage;
import java.io.IOException;
import java.io.OutputStream;
import javax.imageio.ImageIO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ImageIO.class)
public class QRCodeServiceExceptionTest {

  @InjectMocks
  private QRCodeService qrCodeService;

  @Before
  public void init() {
    qrCodeService = new QRCodeService();
    PowerMockito.mockStatic(ImageIO.class);
  }

  @Test(expected = ImagingException.class)
  public void generateQRCodeIoExceptionTest() throws IOException {
    when(ImageIO.write(any(RenderedImage.class), any(String.class), any(
      OutputStream.class))).thenThrow(new IOException());
    qrCodeService.generateQRCode("text");
  }
}
