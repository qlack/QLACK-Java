package com.eurodyn.qlack.fuse.cm.exception;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class QDescendantNodeLockExceptionTest {

  @Test
  public void constructorWithMessageTest() {
    String message = "exception message";
    QDescendantNodeLockException qDescendantNodeLockException = new QDescendantNodeLockException(
      message);
    assertEquals(message, qDescendantNodeLockException.getMessage());
  }

  @Test
  public void constructorWithMessagesTest() {
    String message = "exception message";
    String conflictNodeID = "conflictNodeID";
    String conflictNodeName = "conflictNodeName";

    QDescendantNodeLockException qDescendantNodeLockException = new QDescendantNodeLockException(
      message, conflictNodeID, conflictNodeName);
    assertEquals(message, qDescendantNodeLockException.getMessage());
    assertEquals(conflictNodeID,
      qDescendantNodeLockException.getConflictNodeID());
    assertEquals(conflictNodeName,
      qDescendantNodeLockException.getConflictNodeName());
  }

}
