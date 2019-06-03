package com.eurodyn.qlack.util.av.api.exception;


import com.eurodyn.qlack.common.exception.QException;

/**
 * @author European Dynamics
 */
public class VirusScanException extends QException {

  public VirusScanException() {
    super();
  }

  public VirusScanException(String message) {
    super(message);
  }

  public VirusScanException(Throwable cause) {
    super(cause);
  }

  public VirusScanException(String message, Throwable cause) {
    super(message, cause);
  }
}
