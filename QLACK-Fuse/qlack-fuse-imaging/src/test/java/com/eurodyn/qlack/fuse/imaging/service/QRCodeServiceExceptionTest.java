package com.eurodyn.qlack.fuse.imaging.service;

import com.eurodyn.qlack.fuse.imaging.exception.ImagingException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.*;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class QRCodeServiceExceptionTest {

  @InjectMocks
  private QRCodeService qrCodeService;

  private MockedStatic<ImageIO> mockedStatic;

  @BeforeEach
  public void init() {
    qrCodeService = new QRCodeService();
      mockedStatic =  mockStatic(ImageIO.class);
  }

    @AfterEach
    public void close() {
        mockedStatic.close();
    }

  @Test
  public void generateQRCodeIoExceptionTest(){
      assertThrows(ImagingException.class, () -> {
          when(ImageIO.write(any(RenderedImage.class), any(String.class), any(
                  OutputStream.class))).thenThrow(new IOException());
          qrCodeService.generateQRCode("text");
      });
      }



  }
