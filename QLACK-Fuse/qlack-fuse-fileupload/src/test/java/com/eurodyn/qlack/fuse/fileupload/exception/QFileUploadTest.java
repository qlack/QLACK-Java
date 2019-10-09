package com.eurodyn.qlack.fuse.fileupload.exception;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class QFileUploadTest {


  @Test
  public void constructorWithMessageTest() {
    String message = "exception message";
    QFileUploadException qFileUploadException = new QFileUploadException();
    qFileUploadException = new QFileUploadException(message);
    assertEquals(message, qFileUploadException.getMessage());
  }

}
