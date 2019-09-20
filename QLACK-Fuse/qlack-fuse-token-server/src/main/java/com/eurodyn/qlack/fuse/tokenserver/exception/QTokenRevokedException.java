package com.eurodyn.qlack.fuse.tokenserver.exception;

import com.eurodyn.qlack.common.exception.QException;

public class QTokenRevokedException extends QException {

  public QTokenRevokedException(String msg) {
    super(msg);
  }
}