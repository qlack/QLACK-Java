/*
 * Copyright 2014 EUROPEAN DYNAMICS SA <info@eurodyn.com>
 *
 * Licensed under the EUPL, Version 1.1 only (the "License").
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 * https://joinup.ec.europa.eu/software/page/eupl/licence-eupl
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and
 * limitations under the Licence.
 */
package com.eurodyn.qlack.fuse.aaa.exception;

import com.eurodyn.qlack.common.exception.QException;

/**
 * An Exception class definition for AAA. The creation of this Exception class
 * aims to throw an exception message if anything wrong happens to an aaa.
 *
 * @author European Dynamics SA
 */
public class AAAException extends QException {

  private static final long serialVersionUID = -153032703610522736L;

  public AAAException() {
  }

  /**
   * a constructor that is initialised the exception message
   *
   * @param message a throwing exception message
   */
  public AAAException(String message) {
    super(message);
  }

}
