package com.eurodyn.qlack.fuse.rules.component;

import com.eurodyn.qlack.fuse.rules.exception.QRulesException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.util.List;

public class LookAheadObjectInputStream extends ObjectInputStream {

  private ClassLoader classLoader;

  private List<String> whitelistedClasses;

  public LookAheadObjectInputStream(ClassLoader classLoader, InputStream inputStream,
      List<String> whitelistedClasses)
      throws IOException {
    super(inputStream);
    this.classLoader = classLoader;
    this.whitelistedClasses = whitelistedClasses;
  }

  /**
   * Only deserialize instances of our expected Bicycle class
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
