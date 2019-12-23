package com.eurodyn.qlack.util.data.filter;

import static java.lang.annotation.ElementType.METHOD;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Method-level annotation to filter JSON replies using
 * https://github.com/bohnman/squiggly-java.
 */
@Target(METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ReplyFilter {

  // The filter to apply.
  String value();
}
