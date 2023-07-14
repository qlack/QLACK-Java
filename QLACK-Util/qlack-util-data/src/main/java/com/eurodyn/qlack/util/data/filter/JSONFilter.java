package com.eurodyn.qlack.util.data.filter;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.nicklasw.squiggly.Squiggly;
import dev.nicklasw.squiggly.util.SquigglyUtils;

/**
 * A helper class providing methods to filter JSON objects. (see
 * https://github.com/bohnman/squiggly-java)
 */
public class JSONFilter {

  private static ObjectMapper defaultObjectMapper = new ObjectMapper();
  private static ObjectMapper nonEmptyObjectMapper =
    new ObjectMapper().setSerializationInclusion(Include.NON_EMPTY);
  private static ObjectMapper nonNullObjectMapper =
    new ObjectMapper().setSerializationInclusion(Include.NON_NULL);

  /**
   * Private Constructor
   */
  private JSONFilter() {
  }

  /**
   * Filter using an {@link ObjectMapper} with default initialisation
   * parameters.
   *
   * @param object The object to filter.
   * @param filter The filter to apply.
   * @return Returns the original object with the filter applied.
   */
  @SuppressWarnings("unchecked")
  public static <T> T filterDefault(T object, String filter) {
    return (T) SquigglyUtils
      .objectify(Squiggly.init(defaultObjectMapper, filter), object,
        object.getClass());
  }

  /**
   * Filter using an {@link ObjectMapper} initialised with
   * setSerializationInclusion(Include.NON_EMPTY).
   *
   * @param object The object to filter.
   * @param filter The filter to apply.
   * @return Returns the original object with the filter applied.
   */
  @SuppressWarnings("unchecked")
  public static <T> T filterNonEmpty(T object, String filter) {
    return (T) SquigglyUtils
      .objectify(Squiggly.init(nonEmptyObjectMapper, filter), object,
        object.getClass());
  }

  /**
   * Filter using an {@link ObjectMapper} initialised with
   * setSerializationInclusion(Include.NON_NULL).
   *
   * @param object The object to filter.
   * @param filter The filter to apply.
   * @return Returns the original object with the filter applied.
   */
  @SuppressWarnings("unchecked")
  public static <T> T filterNonNull(T object, String filter) {
    return (T) SquigglyUtils
      .objectify(Squiggly.init(nonNullObjectMapper, filter), object,
        object.getClass());
  }

}
