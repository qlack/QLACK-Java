package com.eurodyn.qlack.util.data.stream;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Utilities to be used when working with streams.
 */
public class StreamUtils {

  /**
   * An implementation of a distinct filter based on a property.
   *
   * Example:
   * <code>
   *   persons.stream().filter(distinctByKey(Person::getName))
   * </code>
   * @param keyExtractor The function to be utilised to identify uniqueness.
   */
  public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
    Set<Object> seen = ConcurrentHashMap.newKeySet();
    return t -> seen.add(keyExtractor.apply(t));
  }
}
