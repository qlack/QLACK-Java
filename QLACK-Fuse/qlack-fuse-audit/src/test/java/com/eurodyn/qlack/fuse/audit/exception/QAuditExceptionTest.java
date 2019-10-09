package com.eurodyn.qlack.fuse.audit.exception;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class QAuditExceptionTest {

  @Test
  public void constructorWithMessageTest() {
    String message = "exception message";
    QAuditException qAuditException = new QAuditException(message);
    assertEquals(message, qAuditException.getMessage());
  }

  @Test
  public void constructorWithCauseTest() {
    Throwable throwable = new Throwable();
    QAuditException qAuditException = new QAuditException(throwable);
    assertEquals(throwable, qAuditException.getCause());
  }

  @Test
  public void constructorWithMessageAndCauseTest() {
    String message = "exception message";
    Throwable throwable = new Throwable();
    QAuditException qAuditException = new QAuditException(message, throwable);
    assertEquals(throwable, qAuditException.getCause());
    assertEquals(message, qAuditException.getMessage());
  }

}
