package com.eurodyn.qlack.common.exception;

/**
 * A generic exception representing an error while trying to mutate data.
 *
 * @author European Dynamics SA
 */
public class QMutationNotPermittedException extends QException {

  public QMutationNotPermittedException() {
    super();
  }

  public QMutationNotPermittedException(String message) {
    super(message);
  }

  public QMutationNotPermittedException(String message, Throwable cause) {
    super(message, cause);
  }
}
