package com.eurodyn.qlack.fuse.cm.exception;

import com.eurodyn.qlack.common.exception.QException;

public class QFileNotFoundException extends QException {

  private static final long serialVersionUID = 4278682901134677579L;

  public QFileNotFoundException(String message) {
    super(message);
  }

}
