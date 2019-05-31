package com.eurodyn.qlack.util.data.exceptions;

import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The implementation logic for the {@link ExceptionWrapper} annotation. This aspect wraps any
 * exception being thrown with a custom, user-provided exception. It can be used to isolate
 * different layers of your application from one another, or protect your REST endpoints from
 * leaking exception to your front-end.
 */
@Aspect
@Component
public class ExceptionWrapperAspect {

  // JUL reference.
  private static final Logger LOGGER = Logger
    .getLogger(ExceptionWrapperAspect.class.getName());

  @SuppressWarnings("RedundantThrows")
  @AfterThrowing(value = "@annotation(exceptionWrapper)", throwing = "originalException")
  public void protect(Throwable originalException, ExceptionWrapper exceptionWrapper)
    throws Exception {
    // Check if this exception should be ignored.
    for (Class<? extends Throwable> c : exceptionWrapper.ignore()) {
      if (originalException.getClass().isAssignableFrom(c)) {
        return;
      }
    }

    // Log the default exception message.
    if (exceptionWrapper.logOriginalException()) {
      LOGGER.log(Level.SEVERE, originalException.getLocalizedMessage(), originalException);
    }

    // Prepare a wrapped exception for higher up the stack.
    try {
      throw exceptionWrapper.wrapper().getConstructor(String.class)
        .newInstance(exceptionWrapper.logMessage());
    } catch (NoSuchMethodException | IllegalAccessException | InstantiationException
      | InvocationTargetException ie) {
      LOGGER
        .log(Level.SEVERE,
          MessageFormat.format("Could not wrap exception {0}.", originalException.getMessage()),
          ie);
    }
  }
}
