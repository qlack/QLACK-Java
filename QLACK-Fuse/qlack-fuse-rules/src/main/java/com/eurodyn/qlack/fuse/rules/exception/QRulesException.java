package com.eurodyn.qlack.fuse.rules.exception;

import com.eurodyn.qlack.common.exception.QException;

/**
 * The QRulesException class is used by the qlack-fuse-rules module to throw
 * error messages on the runtime and rollback the transactions.
 *
 * @author European Dynamics SA
 */
public class QRulesException extends QException {

  private static final long serialVersionUID = 4852993089765648460L;

  public QRulesException(String message) {
    super(message);
  }

  public QRulesException(String message, Throwable cause) {
    super(message, cause);
  }

  public QRulesException(Throwable cause) {
    super(cause);
  }

}
