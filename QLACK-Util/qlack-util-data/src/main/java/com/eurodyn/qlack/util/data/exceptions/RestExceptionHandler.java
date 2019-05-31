package com.eurodyn.qlack.util.data.exceptions;

import com.eurodyn.qlack.common.exception.QExceptionWrapper;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * A generic {@link ControllerAdvice} to prevent low-level error messages leaking to the callers of
 * your REST API.
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler
  protected ResponseEntity<Object> handle(RuntimeException ex, WebRequest request) {
    String errorMessage;

    // If this is a known wrapped exception use the message it already carries, otherwise use a
    // generic message.
    if (ex instanceof QExceptionWrapper) {
      errorMessage = ex.getMessage();
    } else {
      errorMessage = "There was a problem with this request, please try again later.";
    }

    // Return the wrapped exception and custom HTTP status code.
    return handleExceptionInternal(ex, errorMessage, new HttpHeaders(), HttpStatus.BAD_REQUEST,
      request);
  }

}
