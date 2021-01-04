package com.eurodyn.qlack.util.validation;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A Controller advice that converts {@link MethodArgumentNotValidException} and {@link
 * javax.validation.ConstraintViolationException} validation exceptions into a common structure
 * using {@link QValidationError}.
 */
@Log
@RestControllerAdvice
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE)
public class QValidationExceptionControllerAdvisor {

  /**
   * A handler for {@link MethodArgumentNotValidException} validation exceptions.
   *
   * @param ex The validation exception.
   * @return Returns a list of {@link QValidationError} having extracted all underlying validation
   * errors and messages.
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Object> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException ex) {
    // Convert errors to custom data structure.
    List<QValidationError> errors = new ArrayList<>(ex.getAllErrors().size());
    ex.getFieldErrors().forEach(fieldError -> {
      log.log(Level.FINEST,
          MessageFormat.format("Got a MethodArgumentNotValidException: {0}", fieldError));
      QValidationError error = new QValidationError();
      // Extract code.
      error.setCode(fieldError.getCode());
      // Extract defaultMessage.
      error.setDefaultMessage(fieldError.getDefaultMessage());
      // Extract field.
      error.setField(fieldError.getField());
      // Extract objectName.
      error.setObjectName(fieldError.getObjectName());
      // Extract rejectedValue.
      error.setRejectedValue(fieldError.getRejectedValue());
      errors.add(error);
      log.log(Level.FINEST,
          MessageFormat.format("Converted MethodArgumentNotValidException to: ", error));
    });

    // Returns converted errors.
    return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
  }

  /**
   * A handler for {@link javax.validation.ConstraintViolationException} validation exceptions.
   *
   * @param ex The validation exception.
   * @return Returns a list of {@link QValidationError} having extracted all underlying validation
   * errors and messages.
   */
  @ExceptionHandler(javax.validation.ConstraintViolationException.class)
  public ResponseEntity<Object> handleConstraintViolationException(
      javax.validation.ConstraintViolationException ex) {
    final String HIBERNATE_ERROR_PREFIX = "{org.hibernate.validator.constraints.";
    final String HIBERNATE_ERROR_SUFFIX = ".message}";

    List<QValidationError> errors = new ArrayList<>(ex.getConstraintViolations().size());
    ex.getConstraintViolations().forEach(constraintViolation -> {
      log.log(Level.FINEST,
          MessageFormat.format("Got a constraintViolation: {0}", constraintViolation));
      QValidationError error = new QValidationError();
      // Extract code.
      String messageTemplate = constraintViolation.getMessageTemplate();
      error.setCode(
          StringUtils.defaultIfBlank(
              StringUtils
                  .substringBetween(messageTemplate, HIBERNATE_ERROR_PREFIX,
                      HIBERNATE_ERROR_SUFFIX),
              messageTemplate
          ));
      // Extract defaultMessage.
      error.setDefaultMessage(constraintViolation.getMessage());
      // Extract field & objectName.
      String propertyPath = constraintViolation.getPropertyPath().toString();
      // Property path should consist of methodName.objectName.fieldName.
      if (StringUtils.countMatches(propertyPath, ".") >= 2) {
        String[] paths = propertyPath.split("\\.");
        error.setObjectName(paths[1]);
        error.setField(Stream.of(paths).skip(2).collect(Collectors.joining()));
      } else {
        // If no match could be established, set the complete propertyPath in both field and
        // objectName as a contingency, so the frontend can still perform matches when necessary.
        error.setField(propertyPath);
        error.setObjectName(propertyPath);
      }
      // Extract rejectedValue.
      error.setRejectedValue(constraintViolation.getInvalidValue());
      errors.add(error);
      log.log(Level.FINEST,
          MessageFormat.format("Converted ConstraintViolationException to: ", error));
    });

    // Returns converted errors.
    return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
  }
}
