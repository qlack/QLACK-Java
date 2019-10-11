package com.eurodyn.qlack.fuse.cm.exception;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class QFileNotFoundExceptionTest {

  @Test
  public void constructorWithMessageTest() {
    String message = "exception message";
    QFileNotFoundException qFileNotFoundException = new QFileNotFoundException(message);
    assertEquals(message, qFileNotFoundException.getMessage());
  }

}
