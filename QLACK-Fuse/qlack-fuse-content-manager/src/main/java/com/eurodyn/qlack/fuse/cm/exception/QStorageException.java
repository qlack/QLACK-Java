package com.eurodyn.qlack.fuse.cm.exception;

import com.eurodyn.qlack.common.exception.QException;

public class QStorageException extends QException {

  private static final long serialVersionUID = 1L;

  public QStorageException(String message, Throwable cause) {
    super(message, cause);
  }
}
