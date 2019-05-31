package com.eurodyn.qlack.common.exception;

/**
 * A generic exception representing a "entity is disabled" condition.
 *
 * @author European Dynamics SA
 */
public class QDisabledException extends QException {

    public QDisabledException() {
        super();
    }

    public QDisabledException(String message) {
        super(message);
    }

    public QDisabledException(String message, Throwable cause) {
        super(message, cause);
    }

    public QDisabledException(String message, Object... args) {
        super(message, args);
    }
}
