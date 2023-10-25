package com.eurodyn.qlack.fuse.imaging.service;

import com.eurodyn.qlack.fuse.imaging.exception.ImagingException;
import com.eurodyn.qlack.fuse.imaging.util.ICCProfile;
import com.eurodyn.qlack.fuse.imaging.util.ImagingUtil;
import com.eurodyn.qlack.fuse.imaging.util.ResamplingAlgorithm;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ImagingServiceExceptionTest {

  @InjectMocks
  private ImagingService imagingService;

    private MockedStatic<ImageIO> mockedStatic;

  private byte[] createByteImage() throws IOException {
    BufferedImage bImage = ImageIO
      .read(this.getClass().getResource("/image/file-binary.jpg"));
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    ImageIO.write(bImage, "jpg", bos);
    return bos.toByteArray();
  }

  @BeforeEach
  public void init() throws IOException {
    imagingService = new ImagingService();
    imagingService.init();
      mockedStatic = mockStatic(ImageIO.class);
  }

    @AfterEach
    public void close() {
        mockedStatic.close();
    }

  @Test
  public void getInfoIoExceptionTest(){
      assertThrows(ImagingException.class, () -> {
          when(ImageIO.read(any(ByteArrayInputStream.class)))
                  .thenThrow(new IOException());
          imagingService.getInfo(createByteImage());
      });
  }

  @Test
  public void removeAlphaChannelIoExceptionTest(){
      assertThrows(ImagingException.class, () -> {
          when(ImageIO.write(any(RenderedImage.class), any(String.class), any(
                  OutputStream.class))).thenThrow(new IOException());
          imagingService.removeAlphaChannel(createByteImage());
      });
  }

  @Test
  public void convertDstColorspaceIoExceptionTest(){
      assertThrows(ImagingException.class, () -> {
          when(ImageIO.read(any(ByteArrayInputStream.class)))
                  .thenThrow(new IOException());
          imagingService.convert(createByteImage(), "icc", ICCProfile.CGATS21_CRPC1);
      });
  }

  @Test
  public void resampleByPercentIoExceptionTest(){
      assertThrows(ImagingException.class, () -> {
          when(ImageIO.read(any(ByteArrayInputStream.class)))
                  .thenThrow(new IOException());
          imagingService.resampleByPercent(createByteImage(), 100,
                  ResamplingAlgorithm.FILTER_BLACKMAN);
      });
      }


  @Test
  public void resampleByFactorIoExceptionTest(){
      assertThrows(ImagingException.class, () -> {
          when(ImageIO.read(any(ByteArrayInputStream.class)))
                  .thenThrow(new IOException());
          imagingService.resampleByFactor(createByteImage(), 5,
                  ResamplingAlgorithm.FILTER_BLACKMAN);
      });
      }


  @Test
  public void resampleByWidthIoExceptionTest(){
      assertThrows(ImagingException.class, () -> {
          when(ImageIO.read(any(ByteArrayInputStream.class)))
                  .thenThrow(new IOException());
          imagingService.resampleByWidth(createByteImage(), 5,
                  ResamplingAlgorithm.FILTER_BLACKMAN);
      });
      }


  @Test
  public void resampleByHeightIoExceptionTest(){
      assertThrows(ImagingException.class, () -> {
          when(ImageIO.read(any(ByteArrayInputStream.class)))
                  .thenThrow(new IOException());
          imagingService.resampleByHeight(createByteImage(), 5,
                  ResamplingAlgorithm.FILTER_BLACKMAN);
      });
  }

  @Test
  public void resampleIoExceptionTest(){
      assertThrows(ImagingException.class, () -> {
          when(ImageIO.read(any(ByteArrayInputStream.class)))
                  .thenThrow(new IOException());
          imagingService
                  .resample(createByteImage(), 5, 5, ResamplingAlgorithm.FILTER_BLACKMAN);
      });
  }

  @Test
  public void getDPINullReaderTest(){
      assertThrows(ImagingException.class, () -> {
          when(ImageIO.getImageReaders(null))
                  .thenReturn(Collections.emptyIterator());
          ImagingUtil.getDPI(createByteImage());
      });
  }

  @Test
  public void getTypeNullReaderTest() throws IOException {
    when(ImageIO.getImageReaders(null))
      .thenReturn(Collections.emptyIterator());
    assertNull(ImagingUtil.getType(createByteImage()));
  }

}
