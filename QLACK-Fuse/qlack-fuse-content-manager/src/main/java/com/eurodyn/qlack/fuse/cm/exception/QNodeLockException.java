package com.eurodyn.qlack.fuse.cm.exception;

import com.eurodyn.qlack.common.exception.QException;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QNodeLockException extends QException {

  private static final long serialVersionUID = 7102005836737307268L;
  private final String conflictNodeID;
  private final String conflictNodeName;

  public QNodeLockException(String message) {
    super(message);
    this.conflictNodeID = "";
    this.conflictNodeName = "";
  }

  public QNodeLockException(String message, String conflictNodeID,
    String conflictNodeName) {
    super(message);
    this.conflictNodeID = conflictNodeID;
    this.conflictNodeName = conflictNodeName;
  }

}
