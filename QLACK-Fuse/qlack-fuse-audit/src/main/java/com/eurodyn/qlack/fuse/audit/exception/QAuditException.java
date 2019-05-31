package com.eurodyn.qlack.fuse.audit.exception;

import com.eurodyn.qlack.common.exception.QException;

/**
 * The QAuditException class is used by the qlack-fuse-audit module to throw error messages on the runtime and rollback the transactions.
 *
 * @author European Dynamics SA
 */
public class QAuditException extends QException {

  private static final long serialVersionUID = 4852993089765648460L;

  public QAuditException(String message) {
    super(message);
  }

  public QAuditException(String message, Throwable cause) {
    super(message, cause);
  }

  public QAuditException(Throwable cause) {
    super(cause);
  }

}
