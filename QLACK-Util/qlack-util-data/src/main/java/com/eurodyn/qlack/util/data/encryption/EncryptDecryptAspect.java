package com.eurodyn.qlack.util.data.encryption;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * An aspect handling encryption/decryption annotations. To properly use this aspect you need to: 1/ Annotate your
 * model/DTO fields with {@link Encrypted}. 2/ In your service methods, annotate your "getter" methods with {@link
 * Decrypt} and your "setter/save" methods with {@link Encrypt}.
 *
 * This aspect works with method signatures directly exposing a class annotated as in (2) above as well as Spring's
 * {@link org.springframework.data.domain.Pageable} wrapper.
 *
 * The actual encryption/decryption algorithm to be used is left to the user of this aspect by providing a bean
 * implementing the {@link EncryptorDecryptor} interface.
 */
@Aspect
@Component
public class EncryptDecryptAspect {
  // JUL reference.
  private static final Logger LOGGER = Logger.getLogger(EncryptDecryptAspect.class.getName());

  // Encryption/Decryption mode.
  private enum MODE {
    ENCRYPT, DECRYPT
  }

  @Autowired(required = false)
  private EncryptorDecryptor encryptorDecryptor;

  /**
   * Processes an object by either encrypting or decrypting the content of its annotated fields.
   *
   * @param o The object to process.
   * @param mode The encryption/decryption mode to work under.
   */
  private Object process(Object o, MODE mode)
      throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
    // Get fields with `Encrypted` annotation.
    final List<Field> fieldsListWithAnnotation = FieldUtils.getFieldsListWithAnnotation(o.getClass(), Encrypted.class);
    // For each field found, replace its value with an encrypted version.
    for (Field field : fieldsListWithAnnotation) {
      String fieldName = StringUtils.capitalize(field.getName());
      String fieldValue = (String) MethodUtils.invokeExactMethod(o, "get" + fieldName);
      if (StringUtils.isNotBlank(fieldValue)) {
        switch (mode) {
          case ENCRYPT:
            MethodUtils.invokeExactMethod(o, "set" + fieldName, encryptorDecryptor.encrypt(fieldValue));
            break;
          case DECRYPT:
            MethodUtils.invokeExactMethod(o, "set" + fieldName, encryptorDecryptor.decrypt(fieldValue));
            break;
        }
      }
    }

    return o;
  }

  /**
   * Encryption aspect handler.
   *
   * @param pjp A proceeding joint point wrapping your method.
   * @param encrypt The encryption annotation.
   */
  @Around("@annotation(encrypt)")
  public Object encrypt(ProceedingJoinPoint pjp, Encrypt encrypt) throws Throwable {
    // Get original arguments.
    final Object[] args = pjp.getArgs();

    // Iterate over arguments to find the ones with objects having a field with `Encrypted` annotation.
    for (int i = 0; i < args.length; i++) {
      args[i] = process(args[i], MODE.ENCRYPT);
    }

    return pjp.proceed(args);
  }

  /**
   * Decryption aspect handler.
   *
   * @param pjp A proceeding joint point wrapping your method.
   * @param decrypt The decryption annotation.
   */
  @Around("@annotation(decrypt)")
  public Object decrypt(ProceedingJoinPoint pjp, Decrypt decrypt) throws Throwable {
    Object results = pjp.proceed();
    if (results instanceof Page) {
      ((Page) results).map(o -> {
        try {
          return process(o, MODE.DECRYPT);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
          LOGGER.log(Level.SEVERE, MessageFormat.format("Could not decrypt data: {0}.", o), e);
          return o;
        }
      });
    } else {
      results = process(results, MODE.DECRYPT);
    }

    return results;
  }

}
