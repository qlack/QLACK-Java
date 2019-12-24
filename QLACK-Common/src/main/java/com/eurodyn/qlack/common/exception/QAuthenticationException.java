package com.eurodyn.qlack.common.exception;

/**
 * A generic exception superclass to facilitate marking of
 * authentication-related exception.
 */
@SuppressWarnings("squid:MaximumInheritanceDepth")
public class QAuthenticationException extends QSecurityException {

  private static final long serialVersionUID = -7341692118839270522L;

  public QAuthenticationException() {
    super();
  }

  public QAuthenticationException(String msg) {
    super(msg);
  }

  public QAuthenticationException(String msg, Object... args) {
    super(msg, args);
  }
}
