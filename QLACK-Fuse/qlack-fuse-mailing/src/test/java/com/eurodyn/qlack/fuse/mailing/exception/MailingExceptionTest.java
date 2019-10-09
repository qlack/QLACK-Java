package com.eurodyn.qlack.fuse.mailing.exception;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MailingExceptionTest {

  @Test
  public void constructorWithMessageTest() {
    String message = "exception message";
    MailingException mailingException = new MailingException(message);
    assertEquals(message, mailingException.getMessage());
  }

  @Test
  public void constructorWithCauseTest() {
    Throwable throwable = new Throwable();
    MailingException mailingException = new MailingException(throwable);
    assertEquals(throwable, mailingException.getCause());
  }

  @Test
  public void constructorWithMessageAndCauseTest() {
    String message = "exception message";
    Throwable throwable = new Throwable();
    MailingException mailingException = new MailingException(message, throwable);
    assertEquals(throwable, mailingException.getCause());
    assertEquals(message, mailingException.getMessage());
  }

}
