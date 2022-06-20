package com.eurodyn.qlack.fuse.imaging.service;

import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.powermock.api.mockito.PowerMockito.when;

import com.eurodyn.qlack.fuse.imaging.exception.ImagingException;
import com.eurodyn.qlack.fuse.imaging.util.ICCProfile;
import com.eurodyn.qlack.fuse.imaging.util.ImagingUtil;
import com.eurodyn.qlack.fuse.imaging.util.ResamplingAlgorithm;
import javax.imageio.ImageIO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;

//@RunWith(PowerMockRunner.class)
//@PrepareForTest(ImageIO.class)
public class ImagingServiceExceptionTest {
/*
  @InjectMocks
  private ImagingService imagingService;

  private byte[] createByteImage() throws IOException {
    BufferedImage bImage = ImageIO
      .read(this.getClass().getResource("/image/file-binary.jpg"));
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    ImageIO.write(bImage, "jpg", bos);
    return bos.toByteArray();
  }

  @Before
  public void init() throws IOException {
    imagingService = new ImagingService();
    imagingService.init();
    PowerMockito.mockStatic(ImageIO.class);
  }

  @Test(expected = ImagingException.class)
  public void getInfoIoExceptionTest() throws IOException {
    when(ImageIO.read(any(ByteArrayInputStream.class)))
      .thenThrow(new IOException());
    imagingService.getInfo(createByteImage());
  }

  @Test(expected = ImagingException.class)
  public void removeAlphaChannelIoExceptionTest() throws IOException {
    when(ImageIO.write(any(RenderedImage.class), any(String.class), any(
      OutputStream.class))).thenThrow(new IOException());
    imagingService.removeAlphaChannel(createByteImage());
  }

  @Test(expected = ImagingException.class)
  public void convertDstColorspaceIoExceptionTest() throws IOException {
    when(ImageIO.read(any(ByteArrayInputStream.class)))
      .thenThrow(new IOException());
    imagingService.convert(createByteImage(), "icc", ICCProfile.CGATS21_CRPC1);
  }

  @Test(expected = ImagingException.class)
  public void resampleByPercentIoExceptionTest() throws IOException {
    when(ImageIO.read(any(ByteArrayInputStream.class)))
      .thenThrow(new IOException());
    imagingService.resampleByPercent(createByteImage(), 100,
      ResamplingAlgorithm.FILTER_BLACKMAN);
  }

  @Test(expected = ImagingException.class)
  public void resampleByFactorIoExceptionTest() throws IOException {
    when(ImageIO.read(any(ByteArrayInputStream.class)))
      .thenThrow(new IOException());
    imagingService.resampleByFactor(createByteImage(), 5,
      ResamplingAlgorithm.FILTER_BLACKMAN);
  }

  @Test(expected = ImagingException.class)
  public void resampleByWidthIoExceptionTest() throws IOException {
    when(ImageIO.read(any(ByteArrayInputStream.class)))
      .thenThrow(new IOException());
    imagingService.resampleByWidth(createByteImage(), 5,
      ResamplingAlgorithm.FILTER_BLACKMAN);
  }

  @Test(expected = ImagingException.class)
  public void resampleByHeightIoExceptionTest() throws IOException {
    when(ImageIO.read(any(ByteArrayInputStream.class)))
      .thenThrow(new IOException());
    imagingService.resampleByHeight(createByteImage(), 5,
      ResamplingAlgorithm.FILTER_BLACKMAN);
  }

  @Test(expected = ImagingException.class)
  public void resampleIoExceptionTest() throws IOException {
    when(ImageIO.read(any(ByteArrayInputStream.class)))
      .thenThrow(new IOException());
    imagingService
      .resample(createByteImage(), 5, 5, ResamplingAlgorithm.FILTER_BLACKMAN);
  }

  @Test(expected = ImagingException.class)
  public void getDPINullReaderTest() throws IOException {
    when(ImageIO.getImageReaders(null))
      .thenReturn(Collections.emptyIterator());
    ImagingUtil.getDPI(createByteImage());
  }

  @Test
  public void getTypeNullReaderTest() throws IOException {
    when(ImageIO.getImageReaders(null))
      .thenReturn(Collections.emptyIterator());
    assertNull(ImagingUtil.getType(createByteImage()));
  }
*/
}
