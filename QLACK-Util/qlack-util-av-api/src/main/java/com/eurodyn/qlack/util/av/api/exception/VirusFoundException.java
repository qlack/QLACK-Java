package com.eurodyn.qlack.util.av.api.exception;

import com.eurodyn.qlack.common.exception.QException;

/**
 * Exception for the case a virus was found on the scanned file.
 *
 * @author European Dynamics SA.
 */
public class VirusFoundException extends QException {

  public VirusFoundException() {
    super();
  }

  public VirusFoundException(String message) {
    super(message);
  }

  public VirusFoundException(Throwable cause) {
    super(cause);
  }

  public VirusFoundException(String message, Throwable cause) {
    super(message, cause);
  }
}
