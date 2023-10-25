package com.eurodyn.qlack.fuse.audit.exception;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
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
