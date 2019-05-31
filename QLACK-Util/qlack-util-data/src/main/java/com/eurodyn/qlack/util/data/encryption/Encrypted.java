package com.eurodyn.qlack.util.data.encryption;

import static java.lang.annotation.ElementType.FIELD;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation to use together with {@link EncryptDecryptAspect}. This is an annotation to be placed in the fields of
 * your model/DTO to denote which fields should be transparently encrypted/decrypted.
 */
@Target(FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Encrypted {
}
