package com.eurodyn.qlack.util.validation;

import lombok.SneakyThrows;
import lombok.extern.java.Log;
import org.springframework.core.MethodParameter;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;
import java.util.logging.Level;

/**
 * A utility class to assist in custom validation, adding custom validation error and throwing a
 * {@link MethodArgumentNotValidException} when errors exist.
 *
 * Note that in case you are using QLACK ExceptionWrapper, you need to set its
 * `ignoreValidationException` parameter to `true`, so that it does not wrap the validation
 * exception produced here.
 */
@Log
public class QValidationUtil {

  // A reference to the caller's Errors object to interact with the list of validation errors.
  private Errors errors;
  // A reference to the caller's BindingResult object to use when throwing a validation exception.
  private BindingResult bindingResult;

  // A private no-args constructor as this utility class can not be used without
  // the Errors and BindingResult references.
  private QValidationUtil() {
  }

  /**
   * Default constructor initialising this utility class with the necessary object references to add
   * validation errors and throw validation exceptions.
   *
   * @param errors The list of validation errors.
   * @param bindingResult The result of the method binding attempt.
   */
  public QValidationUtil(Errors errors, BindingResult bindingResult) {
    this.errors = errors;
    this.bindingResult = bindingResult;
  }

  /**
   * Adds a new validation error to the list of validation errors.
   *
   * @param fieldName The name of the object field name to be associated with this validation
   * error.
   * @param errorCode A code to be associated with this validation error, useful when you need to
   * take this code and translate it somewhere else.
   * @param errorMessage A default error message to be displayed.
   */
  public void addValidationError(String fieldName, String errorCode, String errorMessage) {
    errors.rejectValue(fieldName, errorCode, errorMessage);
  }

  /**
   * Adds a new validation error and directly throws a validation exception. Useful when you only
   * need to perform a single custom validation check or when you want your user to correct
   * validation errors one by one.
   *
   * @param fieldName The name of the object field name to be associated with this validation
   * error.
   * @param errorCode A code to be associated with this validation error, useful when you need to
   * take this code and translate it somewhere else.
   * @param errorMessage A default error message to be displayed.
   */
  public void throwValidationError(String fieldName, String errorCode, String errorMessage) {
    addValidationError(fieldName, errorCode, errorMessage);
    checkForValidationErrors();
  }

  /**
   * Adds a new validation error to the list of validation errors.
   *
   * @param fieldName The name of the object field name to be associated with this validation
   * error.
   * @param errorCode A code to be associated with this validation error, useful when you need to
   * take this code and translate it somewhere else.
   */
  public void addValidationError(String fieldName, String errorCode) {
    errors.rejectValue(fieldName, errorCode);
  }

  /**
   * Adds a new validation error and directly throws a validation exception. Useful when you only
   * need to perform a single custom validation check or when you want your user to correct
   * validation errors one by one.
   *
   * @param fieldName The name of the object field name to be associated with this validation
   * error.
   * @param errorCode A code to be associated with this validation error, useful when you need to
   * take this code and translate it somewhere else.
   */
  public void throwValidationError(String fieldName, String errorCode) {
    addValidationError(fieldName, errorCode);
    checkForValidationErrors();
  }

  /**
   * Checks if validation errors exist and throws a validation exception in that case.
   */
  @SneakyThrows
  public void checkForValidationErrors() {
    if (errors.hasErrors()) {
      log.log(Level.FINEST, "Found validation errors: {0}", errors);
      // Get a reference to the method that triggered the validation check.
      final StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
      Class<?> clazz = Class.forName(stackTrace[2].getClassName());
      String methodName = stackTrace[2].getMethodName();
      final Optional<Method> targetMethod = Arrays.stream(clazz.getMethods()).filter(
          method -> method.getName().equals(methodName)).findFirst();

      // Produce the validation exception or an error if getting a reference to the caller mathod
      // was not obtained.
      if (targetMethod.isPresent()) {
        throw new MethodArgumentNotValidException(new MethodParameter(targetMethod.get(), 0),
            bindingResult);
      } else {
        log.log(Level.SEVERE,
            "Could not get a reference to the method triggering the validation exception. A RuntimeException will be thrown instead.");
        throw new RuntimeException(
            "Could not get a reference to the method triggering the validation exception.");
      }
    }
  }
}
