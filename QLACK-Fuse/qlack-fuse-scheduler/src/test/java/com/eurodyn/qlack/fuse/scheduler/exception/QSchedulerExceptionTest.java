package com.eurodyn.qlack.fuse.scheduler.exception;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class QSchedulerExceptionTest {

  @Test
  public void constructorWithMessageTest() {
    String message = "exception message";
    QSchedulerException qSchedulerException = new QSchedulerException(message);
    assertEquals(message, qSchedulerException.getMessage());
  }

  @Test
  public void constructorWithCauseTest() {
    Throwable throwable = new Throwable();
    QSchedulerException qSchedulerException = new QSchedulerException(
      throwable);
    assertEquals(throwable, qSchedulerException.getCause());
  }

  @Test
  public void constructorWithMessageAndCauseTest() {
    String message = "exception message";
    Throwable throwable = new Throwable();
    QSchedulerException qSchedulerException = new QSchedulerException(message,
      throwable);
    assertEquals(throwable, qSchedulerException.getCause());
    assertEquals(message, qSchedulerException.getMessage());
  }

}
