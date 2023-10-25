package com.eurodyn.qlack.fuse.mailing.exception;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
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
    MailingException mailingException = new MailingException(message,
      throwable);
    assertEquals(throwable, mailingException.getCause());
    assertEquals(message, mailingException.getMessage());
  }

}
