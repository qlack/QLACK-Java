package com.eurodyn.qlack.util.data.optional;

import com.eurodyn.qlack.common.exception.QDoesNotExistException;
import com.google.common.collect.Iterables;
import java.text.MessageFormat;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;

/**
 * A convenience class to return the wrapped optional value throwing an
 * exception when such value does not exist.
 */
public class ReturnOptional {

  /**
   * Private Constructor
   */
  private ReturnOptional() {
  }

  /**
   * Returns an optional paremeter or a message with optional arguments.
   *
   * @param arg The optional argument to evaluate.
   * @param objectIdentifier A identifier for the optional to be displayed in
   * error messages.
   */
  private static <T> T rMsg(Optional<T> arg, String objectIdentifier) {
    if (arg.isPresent()) {
      return arg.get();
    } else {
      if (StringUtils.isNotBlank(objectIdentifier)) {
        throw new QDoesNotExistException(
          MessageFormat.format("Did not find object with "
            + "parameter {0}.", objectIdentifier));
      } else {
        throw new QDoesNotExistException("Did not find object.");
      }
    }
  }

  /**
   * Returns the wrapped value or throws an exception if such value does not
   * exist.
   *
   * @param arg The optional value.
   * @param objectIdentifier A identifier for the optional to be displayed in
   * error messages. exception log.
   */
  public static <T> T r(Optional<T> arg, String objectIdentifier) {
    return rMsg(arg, objectIdentifier);
  }


  /**
   * Returns the wrapped value or throws an exception if such value does not
   * exist.
   *
   * @param arg The optional value.
   * @param params The list of parameters used when fetching this optional to
   * provide a better exception log. These parameters will be joined into a
   * single String value.
   */
  public static <T> T r(Optional<T> arg, Iterable<Object> params) {
    return rMsg(arg, Iterables.toString(params));
  }

  /**
   * Returns the wrapped value or throws an exception if such value does not
   * exist. Usage of this method is discouraged over the alternative versions
   * requesting an object identifier (this is to avoid generic error messages
   * which complicate debugging).
   *
   * @param arg The optional value.
   */
  public static <T> T r(Optional<T> arg) {
    return rMsg(arg, null);
  }
}
