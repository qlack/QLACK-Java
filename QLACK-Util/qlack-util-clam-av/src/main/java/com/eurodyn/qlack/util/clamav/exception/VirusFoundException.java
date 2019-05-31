package com.eurodyn.qlack.util.clamav.exception;

import com.eurodyn.qlack.common.exception.QException;

/**
 * @author European Dynamics
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
