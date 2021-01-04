package com.eurodyn.qlack.util.validation;

import lombok.Data;

/**
 * A custom placeholder for validation errors.
 */
@Data
public class QValidationError {
  // The code of the validation error, useful when validation messages need to be translated.
  private  String code;
  // A default message to display.
  private  String defaultMessage;
  // The object field for which this validation error is produced for.
  private  String field;
  // The name of the object on which this validation error is produced for.
  private  String objectName;
  // The value that did not pass the validation checks.
  private  Object rejectedValue;
}
