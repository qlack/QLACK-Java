package com.eurodyn.qlack.util.querydsl;

import static java.lang.annotation.ElementType.METHOD;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation to bind {@link EmptyPredicateCheckAspect}.
 */
@Target(METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EmptyPredicateCheck {

}
