package com.eurodyn.qlack.fuse.search.exception;

import com.eurodyn.qlack.common.exception.QException;

/**
 * A generic wrapping-exception for qlack-fuse-search module.
 *
 * @author European Dynamics SA.
 */
public class SearchException extends QException {

  /**
   * Default empty constructor.
   */
  public SearchException() {
    super();
  }

  /**
   * A constructor with a specific message.
   *
   * @param msg The message to include for this exception.
   */
  public SearchException(String msg) {
    super(msg);
  }

  /**
   * A constructor with a specific message and an underlying exception cause
   * (root exception).
   *
   * @param msg The message to include for this exception.
   * @param e The root exception for this exception.
   */
  public SearchException(String msg, Throwable e) {
    super(msg, e);
  }
}
