package com.eurodyn.qlack.fuse.cm.exception;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class QStorageExceptionTest {

  @Test
  public void constructorWithMessageAndThrowableTest() {
    String message = "exception message";
    Throwable throwable = new Throwable();

    QStorageException qStorageException = new QStorageException(message,
      throwable);
    assertEquals(message, qStorageException.getMessage());
    assertEquals(throwable, qStorageException.getCause());
  }

}
