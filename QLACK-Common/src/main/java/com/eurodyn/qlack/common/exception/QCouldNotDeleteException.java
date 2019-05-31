package com.eurodyn.qlack.common.exception;

/**
 * A generic exception representing a "can not delete the given object" condition.
 *
 * @author European Dynamics SA
 */
public class QCouldNotDeleteException extends QException {

    public QCouldNotDeleteException() {
        super();
    }

    public QCouldNotDeleteException(String message) {
        super(message);
    }

    public QCouldNotDeleteException(String message, Throwable cause) {
        super(message, cause);
    }
}
