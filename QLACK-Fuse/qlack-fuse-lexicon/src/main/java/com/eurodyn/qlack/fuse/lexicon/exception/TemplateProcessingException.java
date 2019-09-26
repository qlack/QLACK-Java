package com.eurodyn.qlack.fuse.lexicon.exception;

import com.eurodyn.qlack.common.exception.QException;

/**
 *  An Exception class that is used to throw a message that
 *  something wrong happens to template procedure
 *
 * @author European Dynamics SA
 */
public class TemplateProcessingException extends QException {

  private static final long serialVersionUID = -7123328962302816656L;

  /** An abstract method declaration that is used to
   * throw an exception message for template procedure
   *
   * @param msg the message that is throwing
   */
  public TemplateProcessingException(String msg) {
    super(msg);
  }

}
