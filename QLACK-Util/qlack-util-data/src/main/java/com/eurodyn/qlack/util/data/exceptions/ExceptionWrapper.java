package com.eurodyn.qlack.util.data.exceptions;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation going together with {@link ExceptionWrapperAspect} to indicate
 * methods that need to have their exception wrapped.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ExceptionWrapper {

  // The log message to be forwarded as an error cause higher up the calling stack.
  String logMessage() default "An unexpected error occurred.";

  // Your custom exception to be used as a wrapper.
  Class<? extends Exception> wrapper();

  // A list of exception to be ignored from wrapping (i.e. they are rethrown verbatim).
  Class<? extends Throwable>[] ignore() default {};

  // A flag to indicate whether the original exception captured should be logged or not.
  boolean logOriginalException() default true;

  // Whether to ignore validation exceptions (and therefore allow them to propagate) such as
  // MethodArgumentNotValidException and ConstraintViolationException.
  boolean ignoreValidationExceptions() default false;
}
