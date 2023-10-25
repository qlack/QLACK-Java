package com.eurodyn.qlack.fuse.fileupload.exception;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class QFileUploadTest {


  @Test
  public void constructorWithMessageTest() {
    String message = "exception message";
    QFileUploadException qFileUploadException;
    qFileUploadException = new QFileUploadException(message);
    assertEquals(message, qFileUploadException.getMessage());
  }

}
