package com.eurodyn.qlack.util.data.filter;

import static java.lang.annotation.ElementType.METHOD;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Method-level annotation to filter JSON replies using
 * https://github.com/bohnman/squiggly-java allowing {@link
 * org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable}
 * to not be filtered.
 */
@Target(METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ReplyPageableFilter {

  // The filter to apply.
  String value();
}
