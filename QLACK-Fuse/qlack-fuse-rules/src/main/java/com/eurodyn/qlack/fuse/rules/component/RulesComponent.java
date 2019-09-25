package com.eurodyn.qlack.fuse.rules.component;

import com.eurodyn.qlack.fuse.rules.exception.QRulesException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.util.Arrays;
import java.util.List;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * This component can be used in order to serialize the globals and the facts for a KieSession
 * execution and to deserialize the results.
 *
 * @author European Dynamics SA
 */
@Component
public class RulesComponent {

  private List<String> acceptedPackages;

  @Value("${qlack.fuse.rules.accepted.classes:*}")
  private String acceptedPackagesNames;

  @PostConstruct
  public void init() {
    acceptedPackages = Arrays.asList(acceptedPackagesNames.split(","));
  }

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
    try {
      ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
      ObjectInputStream ois = new LookAheadObjectInputStream(classLoader, bais, acceptedPackages);
      return ois.readObject();
    } catch (IOException | ClassNotFoundException e) {
      throw new QRulesException(e);
    }
  }
}
