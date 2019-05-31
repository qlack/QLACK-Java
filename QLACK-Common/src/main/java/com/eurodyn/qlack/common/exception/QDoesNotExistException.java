package com.eurodyn.qlack.common.exception;

/**
 * A generic exception representing a "entity does not exist in the system" condition.
 *
 * @author European Dynamics SA
 */
public class QDoesNotExistException extends QException {

    public QDoesNotExistException() {
        super();
    }

    public QDoesNotExistException(String message) {
        super(message);
    }

    public QDoesNotExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public QDoesNotExistException(String message, Object... args) {
        super(message, args);
    }
}
