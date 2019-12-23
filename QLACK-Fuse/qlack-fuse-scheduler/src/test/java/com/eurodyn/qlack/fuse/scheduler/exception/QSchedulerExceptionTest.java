package com.eurodyn.qlack.fuse.scheduler.exception;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
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
