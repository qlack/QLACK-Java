package com.eurodyn.qlack.fuse.cm.exception;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class QAncestorFolderLockExceptionTest {

  @Test
  public void constructorWithMessageTest() {
    String message = "exception message";
    QAncestorFolderLockException qAncestorFolderLockException = new QAncestorFolderLockException(
        message);
    assertEquals(message, qAncestorFolderLockException.getMessage());
  }

  @Test
  public void constructorWithMessagesTest() {
    String message = "exception message";
    String conflictNodeID = "conflictNodeID";
    String conflictNodeName = "conflictNodeName";

    QAncestorFolderLockException qAncestorFolderLockException = new QAncestorFolderLockException(
        message, conflictNodeID, conflictNodeName);
    assertEquals(message, qAncestorFolderLockException.getMessage());
    assertEquals(conflictNodeID, qAncestorFolderLockException.getConflictNodeID());
    assertEquals(conflictNodeName, qAncestorFolderLockException.getConflictNodeName());
  }

}
