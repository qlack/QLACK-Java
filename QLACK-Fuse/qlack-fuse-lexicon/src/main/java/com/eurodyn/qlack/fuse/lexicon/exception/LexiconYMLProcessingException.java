package com.eurodyn.qlack.fuse.lexicon.exception;

import com.eurodyn.qlack.common.exception.QException;

/**
 * A class that is used to throw an Exception if anything wrong happens in
 * lexicon yaml procedure
 *
 * @author European Dynamics SA
 */
public class LexiconYMLProcessingException extends QException {

  private static final long serialVersionUID = 1L;

  /**
   * An abstract method that is used to throw an exception message if anything
   * wrong happens to lexicon yaml procedure
   *
   * @param msg the message that is throwing
   */
  public LexiconYMLProcessingException(String msg) {
    super(msg);
  }

}
