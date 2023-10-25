package com.eurodyn.qlack.fuse.cm.exception;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class QFileNotFoundExceptionTest {

  @Test
  public void constructorWithMessageTest() {
    String message = "exception message";
    QFileNotFoundException qFileNotFoundException = new QFileNotFoundException(
      message);
    assertEquals(message, qFileNotFoundException.getMessage());
  }

}
