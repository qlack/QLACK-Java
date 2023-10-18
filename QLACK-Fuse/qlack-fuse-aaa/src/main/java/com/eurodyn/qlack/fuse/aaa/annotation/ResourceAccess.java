package com.eurodyn.qlack.fuse.aaa.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Describes access rules for a REST endpoint  on a role level: through
 * roleAccess on an operation/resource level
 * annotation
 *
 * @author European Dynamics SA
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ResourceAccess {

  String[] operations() default {};
}
