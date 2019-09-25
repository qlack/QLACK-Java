package com.eurodyn.qlack.fuse.rules.component;

import com.eurodyn.qlack.fuse.rules.exception.QRulesException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import org.springframework.stereotype.Component;

/**
 * This component can be used in order to serialize the globals and the facts for a KieSession
 * execution and to deserialize the results.
 *
 * @author European Dynamics SA
 */
@Component
public class RulesComponent {

  public byte[] serializeObject(Object object) {
    try (ByteArrayOutputStream baos = new ByteArrayOutputStream(); ObjectOutputStream oos = new ObjectOutputStream(
        baos)) {
      oos.writeObject(object);
      return baos.toByteArray();
    } catch (IOException e) {
      throw new QRulesException(e);
    }
  }

  public Object deserializeObject(final ClassLoader classLoader, byte[] bytes) {
    try (
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        ObjectInputStream ois = new ObjectInputStream(bais) {
          @Override
          protected Class<?> resolveClass(ObjectStreamClass desc)
              throws IOException, ClassNotFoundException {
            String name = desc.getName();
            Class<?> clazz = classLoader.loadClass(name);
            if (clazz != null) {
              return clazz;
            } else {
              return super.resolveClass(desc);
            }
          }
        }) {
      return ois.readObject();
    } catch (IOException | ClassNotFoundException e) {
      throw new QRulesException(e);
    }
  }

}
