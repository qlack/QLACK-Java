package com.eurodyn.qlack.util.av.api.exception;


import com.eurodyn.qlack.common.exception.QException;

/**
 *  Exception for the case of any error during a file scan.
 *
 * @author European Dynamics SA.
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
