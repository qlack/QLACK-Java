package com.eurodyn.qlack.fuse.aaa.annotations.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Describes access rules for a REST endpoint
 * on an operation/resource level
 * @author European Dynamics
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ResourceOperation {
    String operation();
    String resourceIdField() default "";
    String resourceIdParameter() default "";
}