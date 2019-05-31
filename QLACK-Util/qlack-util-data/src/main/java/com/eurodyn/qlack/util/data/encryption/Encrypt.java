package com.eurodyn.qlack.util.data.encryption;

import static java.lang.annotation.ElementType.METHOD;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation to use together with {@link EncryptDecryptAspect}. This annotation denotes a method that should be
 * scanned for fields that need to be encrypted.
 */
@Target({METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Encrypt {
}
