package com.eurodyn.qlack.fuse.imaging.service;

import com.eurodyn.qlack.fuse.imaging.exception.ImagingException;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import javax.imageio.ImageIO;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

/**
 * Provides QR code generation related functionality
 *
 * @author European Dynamics SA.
 */
@Service
@Validated
public class QRCodeService {

  /**
   * Default width for QR code.
   */
  public static final int DEFAULT_WIDTH = 125;

  /**
   * Default height for QR code.
   */
  public static final int DEFAULT_HEIGHT = 125;

  /**
   * Default image format for QR code.
   */
  public static final String DEFAULT_FORMAT = "PNG";

  /**
   * Default background color for QR code.
   */
  public static final Color DEFAULT_BACKGROUND = Color.WHITE;

  /**
   * Default foreground color for QR code.
   */
  public static final Color DEFAULT_FOREGROUND = Color.BLACK;

  /**
   * Generates a QR code for the given text using default values for width, height, image format,
   * background and foreground colors
   *
   * @param text the input text
   * @return a byte array representing the QR code
   */
  public byte[] generateQRCode(String text) {
    return generateQRCode(text, DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_FORMAT, DEFAULT_BACKGROUND,
        DEFAULT_FOREGROUND);
  }

  /**
   * Generates a QR code for the given text using provided values for width, height, image format,
   * background and foreground colors
   *
   * @param text the input text
   * @param width qr code width
   * @param height qr code height
   * @param imageFormat qr code image format
   * @param background qr code image background color
   * @param foreground qr code image foreground color
   * @return a byte array representing the QR code
   */
  public byte[] generateQRCode(String text, int width, int height, String imageFormat,
      Color background, Color foreground) {
    byte[] qrCode = null;

    try {
      // Prepare the QRCode writer.
      HashMap hintMap = new HashMap();
      hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
      QRCodeWriter qrCodeWriter = new QRCodeWriter();
      BitMatrix byteMatrix = qrCodeWriter
          .encode(text, BarcodeFormat.QR_CODE, width, height, hintMap);

      // Create the BufferedImage to hold the QRCode.
      int matrixWidth = byteMatrix.getWidth();
      BufferedImage image = new BufferedImage(matrixWidth, matrixWidth, BufferedImage.TYPE_INT_RGB);

      // Paint and save the image using the ByteMatrix.
      image.createGraphics();
      Graphics2D graphics = (Graphics2D) image.getGraphics();
      graphics.setColor(background);
      graphics.fillRect(0, 0, matrixWidth, matrixWidth);
      graphics.setColor(foreground);
      for (int i = 0; i < matrixWidth; i++) {
        for (int j = 0; j < matrixWidth; j++) {
          if (byteMatrix.get(i, j)) {
            graphics.fillRect(i, j, 1, 1);
          }
        }
      }

      // Get the image into a byte[].
      try (ByteArrayOutputStream dstImage = new ByteArrayOutputStream()) {
        ImageIO.write(image, imageFormat, dstImage);
        qrCode = dstImage.toByteArray();
      }
    } catch (WriterException | IOException e) {
      throw new ImagingException("Could not generate QR code.", e);
    }

    return qrCode;
  }
}
