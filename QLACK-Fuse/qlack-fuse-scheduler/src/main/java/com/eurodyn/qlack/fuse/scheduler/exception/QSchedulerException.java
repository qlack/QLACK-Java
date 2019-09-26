package com.eurodyn.qlack.fuse.scheduler.exception;

import com.eurodyn.qlack.common.exception.QException;

/**
 * General exception for all scheduler related errors
 *
 * @author European Dynamics SA.
 */
public class QSchedulerException extends QException {

  public QSchedulerException(String message) {
    super(message);
  }

  public QSchedulerException(String message, Throwable cause) {
    super(message, cause);
  }

  public QSchedulerException(Throwable cause) {
    super(cause);
  }

}
