package com.eurodyn.qlack.common.exception;

/**
 * A generic exception superclass to facilitate marking of authorisation-related exception.
 */
public class QAuthorisationException extends QSecurityException {

  private static final long serialVersionUID = 3887709297788547031L;

  public QAuthorisationException() {
    super();
  }

  public QAuthorisationException(String msg) {
    super(msg);
  }

  public QAuthorisationException(String msg, Object... args) {
    super(msg, args);
  }
}
