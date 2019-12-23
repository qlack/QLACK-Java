package com.eurodyn.qlack.fuse.cm.exception;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class QAncestorFolderLockException extends QNodeLockException {

  private static final long serialVersionUID = 2789638483007504036L;

  public QAncestorFolderLockException(String message) {
    super(message);
  }

  public QAncestorFolderLockException(String message, String conflictNodeID,
    String conflictNodeName) {
    super(message, conflictNodeID, conflictNodeName);
  }

}
