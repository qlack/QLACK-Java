package com.eurodyn.qlack.fuse.cm.exception;

import com.eurodyn.qlack.common.exception.QException;

public class QAncestorFolderLockException extends QException {

  private static final long serialVersionUID = 2789638483007504036L;

  public QAncestorFolderLockException(String message) {
    super(message);
  }

  public QAncestorFolderLockException(String message, String conflictNodeID,
      String conflictNodeName) {
    super(message, conflictNodeID, conflictNodeName);
  }


}
