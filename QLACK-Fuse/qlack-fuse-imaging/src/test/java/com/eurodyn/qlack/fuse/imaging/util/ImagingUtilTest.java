package com.eurodyn.qlack.fuse.imaging.util;


import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class ImagingUtilTest {

  private byte[] createByteImage() throws IOException {
    BufferedImage bImage = ImageIO
      .read(this.getClass().getResource("/image/tiff.png"));
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    ImageIO.write(bImage, "tiff", bos);
    return bos.toByteArray();
  }

  @Test
  public void getDpiTest() throws IOException {
    assertNotNull(ImagingUtil.getDPI(createByteImage()));
  }

}
