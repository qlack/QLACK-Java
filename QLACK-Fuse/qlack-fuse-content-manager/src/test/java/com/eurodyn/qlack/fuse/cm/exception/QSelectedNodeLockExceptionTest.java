package com.eurodyn.qlack.fuse.cm.exception;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class QSelectedNodeLockExceptionTest {

  @Test
  public void constructorWithMessageTest() {
    String message = "exception message";
    QSelectedNodeLockException qSelectedNodeLockException = new QSelectedNodeLockException(message);
    assertEquals(message, qSelectedNodeLockException.getMessage());
  }

  @Test
  public void constructorWithMessagesTest() {
    String message = "exception message";
    String conflictNodeID = "conflictNodeID";
    String conflictNodeName = "conflictNodeName";

    QSelectedNodeLockException qSelectedNodeLockException = new QSelectedNodeLockException(message,
        conflictNodeID, conflictNodeName);
    assertEquals(message, qSelectedNodeLockException.getMessage());
    assertEquals(conflictNodeID, qSelectedNodeLockException.getConflictNodeID());
    assertEquals(conflictNodeName, qSelectedNodeLockException.getConflictNodeName());
  }

}
