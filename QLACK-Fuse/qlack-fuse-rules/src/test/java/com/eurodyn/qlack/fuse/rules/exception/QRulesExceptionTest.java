package com.eurodyn.qlack.fuse.rules.exception;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class QRulesExceptionTest {

    @Test
    public void constructorWithMessageTest() {
        String message = "exception message";
        QRulesException qRulesException = new QRulesException(message);
        assertEquals(message, qRulesException.getMessage());
    }

    @Test
    public void constructorWithCauseTest() {
        Throwable throwable = new Throwable();
        QRulesException qRulesException = new QRulesException(throwable);
        assertEquals(throwable, qRulesException.getCause());
    }

    @Test
    public void constructorWithMessageAndCauseTest() {
        String message = "exception message";
        Throwable throwable = new Throwable();
        QRulesException qRulesException = new QRulesException(message, throwable);
        assertEquals(throwable, qRulesException.getCause());
        assertEquals(message, qRulesException.getMessage());
    }

}
