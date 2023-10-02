package com.eurodyn.qlack.fuse.fd.exception;

import com.eurodyn.qlack.common.exception.QException;
import java.io.Serial;

/**
 * An Exception class that is used to throw a message that something wrong
 * happens during Voting process
 *
 * @author European Dynamics SA
 */
public class VoteException extends QException {

  @Serial
  private static final long serialVersionUID = -7123328962302816656L;

  /**
   * An abstract method declaration that is used to throw an exception message
   * for votes
   *
   * @param msg the message that is throwing
   */
  public VoteException(String msg) {
    super(msg);
  }

}
