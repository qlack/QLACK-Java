package com.eurodyn.qlack.fuse.scheduler.exception;

import com.eurodyn.qlack.common.exception.QException;

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
