package com.eurodyn.qlack.fuse.cm.exception;

import com.eurodyn.qlack.common.exception.QException;

/**
 * @author European Dynamics
 */
public class QVersionNotFoundException extends QException {

  private static final long serialVersionUID = 4278682901134677579L;

  public QVersionNotFoundException(String message) {
    super(message);
  }

}
