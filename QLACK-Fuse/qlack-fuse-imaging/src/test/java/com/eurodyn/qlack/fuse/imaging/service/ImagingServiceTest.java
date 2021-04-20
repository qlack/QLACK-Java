package com.eurodyn.qlack.fuse.imaging;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import com.eurodyn.qlack.fuse.imaging.dto.ImageFormatHandler;
import com.eurodyn.qlack.fuse.imaging.dto.ImageInfo;
import com.eurodyn.qlack.fuse.imaging.exception.ImagingException;
import com.eurodyn.qlack.fuse.imaging.service.ImagingService;
import com.eurodyn.qlack.fuse.imaging.util.ICCProfile;
import com.eurodyn.qlack.fuse.imaging.util.ResamplingAlgorithm;
import com.eurodyn.qlack.fuse.imaging.util.TIFFCompression;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import javax.imageio.ImageIO;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ImagingServiceTest {

  private ImagingService imagingService;

  private byte[] createByteImage() throws IOException {
    BufferedImage bImage = ImageIO
      .read(this.getClass().getResource("/image/file-binary.jpg"));
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    ImageIO.write(bImage, "jpg", bos);
    return bos.toByteArray();
  }

  @Before
  public void init() {
    imagingService = new ImagingService();
    imagingService.init();
  }

  @Test
  public void getSupportedReadFormatsTest() {
    List<ImageFormatHandler> result =
      imagingService.getSupportedReadFormats();
    assertEquals(Arrays.asList(ImageIO.getReaderFormatNames()).size(),
      result.size());
  }

  @Test
  public void getSupportedWriteFormatsTest() {
    List<ImageFormatHandler> result =
      imagingService.getSupportedWriteFormats();
    assertEquals(Arrays.asList(ImageIO.getWriterFormatNames()).size(),
      result.size());
  }

  @Test
  public void isFormatSupportedForReadTest() {
    String supportedFormat = "bmp";
    String notSupportedFormat = "mp3";
    String randomCharacters = null;
    assertTrue(imagingService.isFormatSupportedForRead(supportedFormat));
    assertFalse(imagingService.isFormatSupportedForRead(notSupportedFormat));
    assertFalse(imagingService.isFormatSupportedForRead(randomCharacters));
  }

  @Test
  public void isFormatSupportedForWriteTest() {
    String supportedFormat = "bmp";
    String notSupportedFormat = "mp3";
    String randomCharacters = "BmP";
    assertTrue(imagingService.isFormatSupportedForWrite(supportedFormat));
    assertFalse(imagingService.isFormatSupportedForWrite(notSupportedFormat));
    assertFalse(imagingService.isFormatSupportedForWrite(randomCharacters));
  }

  @Test
  public void getInfoTest() throws IOException {
    BufferedImage bImage = ImageIO
      .read(this.getClass().getResource("/image/file-binary.jpg"));
    ImageInfo result = imagingService.getInfo(createByteImage());
    assertEquals(bImage.getHeight(), result.getHeight());
  }

  @Test
  public void removeAlphaChannelTest() throws IOException {
    byte[] result = imagingService.removeAlphaChannel(createByteImage());

    InputStream originalImageInputStream = new ByteArrayInputStream(result);
    BufferedImage temp = ImageIO.read(originalImageInputStream);
    assertFalse(temp.getColorModel().hasAlpha());
  }

  @Test
  public void convertTest() throws IOException {
    byte[] result = imagingService.convert(createByteImage(), "jpg");
    assertNotEquals(createByteImage().length, result.length);
  }

  @Test(expected = ImagingException.class)
  public void convertDstColorspaceTest() throws IOException {
    imagingService.convert(createByteImage(), "icc", ICCProfile.CGATS21_CRPC1);
  }

  @Test
  public void convertToTIFFTest() throws IOException {
    byte[] result = imagingService
      .convertToTIFF(createByteImage(), TIFFCompression.JPEG);
    assertNotEquals(createByteImage().length, result.length);
  }

  @Test
  @Ignore
  //TODO Fix the Test
  public void convertToTIFFDstColorspaceTest() throws IOException {
    imagingService.convertToTIFF(createByteImage(), ICCProfile.CoatedFOGRA27,
      TIFFCompression.JPEG);
  }

  @Test
  public void resampleByPercentTest() throws IOException {
    byte[] result = imagingService
      .resampleByPercent(createByteImage(), 100,
        ResamplingAlgorithm.FILTER_BLACKMAN);
    assertTrue(createByteImage().length > result.length);

    result = imagingService
      .resampleByPercent(createByteImage(), 50,
        ResamplingAlgorithm.FILTER_BLACKMAN);
    assertNotEquals(createByteImage().length, result.length);
  }

  @Test
  public void resampleByFactorTest() throws IOException {
    byte[] result = imagingService
      .resampleByFactor(createByteImage(), 5,
        ResamplingAlgorithm.FILTER_BLACKMAN);
    assertTrue(createByteImage().length < result.length);
  }

  @Test
  public void resampleByWidthTest() throws IOException {
    byte[] result = imagingService
      .resampleByWidth(createByteImage(), 5,
        ResamplingAlgorithm.FILTER_BLACKMAN);
    assertTrue(createByteImage().length > result.length);
  }

  @Test
  public void resampleByHeightTest() throws IOException {
    InputStream originalImageInputStream = new ByteArrayInputStream(
      createByteImage());
    BufferedImage bImage = ImageIO.read(originalImageInputStream);
    byte[] result = imagingService
      .resampleByHeight(createByteImage(), 5,
        ResamplingAlgorithm.FILTER_BLACKMAN);

    InputStream resampleImageInputStream = new ByteArrayInputStream(result);
    BufferedImage originalBufferedImage = ImageIO
      .read(resampleImageInputStream);
    assertTrue(bImage.getHeight() > originalBufferedImage.getHeight());
  }

  @Test
  public void resampleTest() throws IOException {
    InputStream originalImageInputStream = new ByteArrayInputStream(
      createByteImage());
    BufferedImage bImage = ImageIO.read(originalImageInputStream);
    byte[] result =
      imagingService
        .resample(createByteImage(), 5, 5, ResamplingAlgorithm.FILTER_BLACKMAN);

    InputStream resampleImageInputStream = new ByteArrayInputStream(result);
    BufferedImage originalBufferedImage = ImageIO
      .read(resampleImageInputStream);
    assertTrue(bImage.getHeight() > originalBufferedImage.getHeight());
  }
}
