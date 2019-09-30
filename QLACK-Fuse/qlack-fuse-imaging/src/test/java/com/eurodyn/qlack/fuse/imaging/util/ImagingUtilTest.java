package com.eurodyn.qlack.fuse.imaging.util;

import static org.junit.Assert.assertNotNull;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
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
