package com.eurodyn.qlack.fuse.cm.exception;

import com.eurodyn.qlack.common.exception.QException;

public class QDescendantNodeLockException extends QException {

  private static final long serialVersionUID = 9055719925828944313L;

  public QDescendantNodeLockException(String message) {
    super(message);
  }

  public QDescendantNodeLockException(String message, String conflictNodeID,
      String conflictNodeName) {
    super(message, conflictNodeID, conflictNodeName);
  }
}
