package com.eurodyn.qlack.fuse.rules.component;

import com.eurodyn.qlack.fuse.rules.exception.QRulesException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.util.List;

/**
 * Custom class that ensures that Look-ahead class validation is performed before the
 * de-serialization of the byte array into an object.
 *
 * @author European Dynamics SA
 */
public class LookAheadObjectInputStream extends ObjectInputStream {

  private ClassLoader classLoader;

  private List<String> whitelistedClasses;

  /**
   * The LookAheadObjectInputStream constructor
   *
   * @param classLoader the Drools Classloader
   * @param inputStream the object to be read
   * @param whitelistedClasses the packages that are allowed to be de-serialized
   * @throws IOException exception during the stream read
   */
  public LookAheadObjectInputStream(ClassLoader classLoader, InputStream inputStream,
      List<String> whitelistedClasses)
      throws IOException {
    super(inputStream);
    this.classLoader = classLoader;
    this.whitelistedClasses = whitelistedClasses;
  }

  /**
   * Finds the classname of the object.
   *
   * @param desc the object
   * @return the classname of the object
   * @throws IOException exception if the object cannot be read
   * @throws ClassNotFoundException exception if the classname does not belong on the Drools
   * Classloader
   */
  @Override
  protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException,
      ClassNotFoundException {
    String name = desc.getName();
    acceptedClassName(name);
    Class<?> clazz = classLoader.loadClass(name);
    if (clazz != null) {
      return clazz;
    } else {
      return super.resolveClass(desc);
    }
  }

  /**
   * Checks if the given class is part of the whitelist and throws an exception if it not.
   *
   * @param name the classname
   */
  private void acceptedClassName(String name) {
    for (String pattern : whitelistedClasses) {
      if (pattern.equals("*") || name.startsWith(pattern)) {
        return;
      }
    }
    throw new QRulesException(
        "The class " + name + " cannot be de-serialized for security reasons.");
  }
}
