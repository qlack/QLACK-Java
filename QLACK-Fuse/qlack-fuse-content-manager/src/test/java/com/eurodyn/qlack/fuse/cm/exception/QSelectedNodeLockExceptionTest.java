package com.eurodyn.qlack.fuse.cm.exception;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class QSelectedNodeLockExceptionTest {

  @Test
  public void constructorWithMessageTest() {
    String message = "exception message";
    QSelectedNodeLockException qSelectedNodeLockException = new QSelectedNodeLockException(
      message);
    assertEquals(message, qSelectedNodeLockException.getMessage());
  }

  @Test
  public void constructorWithMessagesTest() {
    String message = "exception message";
    String conflictNodeID = "conflictNodeID";
    String conflictNodeName = "conflictNodeName";

    QSelectedNodeLockException qSelectedNodeLockException = new QSelectedNodeLockException(
      message,
      conflictNodeID, conflictNodeName);
    assertEquals(message, qSelectedNodeLockException.getMessage());
    assertEquals(conflictNodeID,
      qSelectedNodeLockException.getConflictNodeID());
    assertEquals(conflictNodeName,
      qSelectedNodeLockException.getConflictNodeName());
  }

}
