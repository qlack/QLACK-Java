package com.eurodyn.qlack.fuse.cm.exception;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class QStorageExceptionTest {

  @Test
  public void constructorWithMessageAndThrowableTest() {
    String message = "exception message";
    Throwable throwable = new Throwable();

    QStorageException qStorageException = new QStorageException(message, throwable);
    assertEquals(message, qStorageException.getMessage());
    assertEquals(throwable, qStorageException.getCause());
  }

}
