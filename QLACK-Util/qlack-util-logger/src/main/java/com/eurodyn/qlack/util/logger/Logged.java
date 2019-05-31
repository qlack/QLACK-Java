package com.eurodyn.qlack.util.logger;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Enables logging for debugging purposes on a method level.
 * If applied on a class level it logs every method execution.
 * If applied on a method level it logs the particular method execution.
 * If applied both on a class and method level, method takes precedence.
 *
 * @author European Dynamics
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {METHOD, TYPE})
public @interface Logged {
  boolean performance() default true;

  boolean paramValues() default false;
}

