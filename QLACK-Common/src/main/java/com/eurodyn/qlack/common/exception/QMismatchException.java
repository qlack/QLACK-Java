package com.eurodyn.qlack.common.exception;

/**
 * A generic exception representing an "mismatch" condition.
 *
 * @author European Dynamics SA
 */
public class QMismatchException extends QException {

  public QMismatchException() {
    super();
  }

  public QMismatchException(String message) {
    super(message);
  }

  public QMismatchException(String message, Throwable cause) {
    super(message, cause);
  }

  public QMismatchException(String message, Object... args) {
    super(message, args);
  }
}
