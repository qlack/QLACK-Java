package com.eurodyn.qlack.fuse.rules.component;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.eurodyn.qlack.fuse.rules.SerializableClass;
import com.eurodyn.qlack.fuse.rules.UnserializableClass;
import com.eurodyn.qlack.fuse.rules.exception.QRulesException;
import com.eurodyn.qlack.fuse.rules.util.classloader.JarClassLoaderBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

@RunWith(MockitoJUnitRunner.class)
public class RulesComponentTest {

  @InjectMocks
  private RulesComponent rulesComponent;

  private ClassLoader classLoader;

  @Before
  public void init() {
    rulesComponent = new RulesComponent();
    ReflectionTestUtils.setField(rulesComponent, "acceptedPackagesNames", "*");
    rulesComponent.init();
    JarClassLoaderBuilder classLoaderBuilder = new JarClassLoaderBuilder();
    classLoader = classLoaderBuilder.buildClassLoader(null);
  }

  @Test(expected = QRulesException.class)
  public void serializeObjectExceptionTest() {
    assertNotNull(rulesComponent.serializeObject(new UnserializableClass()));
  }

  @Test
  public void deserializeObjectTest() {
    assertNotNull(rulesComponent
      .deserializeObject(classLoader,
        rulesComponent.serializeObject(new SerializableClass())));
  }

  @Test
  public void deserializeObjectNullClassTest() throws ClassNotFoundException {

    ClassLoader classLoaderInstance = mock(ClassLoader.class);
    when(classLoaderInstance
      .loadClass("com.eurodyn.qlack.fuse.rules.SerializableClass"))
      .thenReturn(null);
    assertNotNull(rulesComponent.deserializeObject(classLoaderInstance,
      rulesComponent.serializeObject(new SerializableClass())));
  }

  @Test(expected = QRulesException.class)
  public void deserializeObjectExceptionTest() {
    assertNotNull(
      rulesComponent.deserializeObject(classLoader, "object".getBytes()));
  }

  @Test(expected = QRulesException.class)
  public void deserializeUnknownTest() {
    ReflectionTestUtils.setField(rulesComponent, "acceptedPackagesNames",
      "package.does.not.exist");
    rulesComponent.init();
    assertNotNull(rulesComponent
      .deserializeObject(classLoader,
        rulesComponent.serializeObject(new SerializableClass())));
  }

  @Test
  public void deserializeKnownObject2Test() {
    ReflectionTestUtils
      .setField(rulesComponent, "acceptedPackagesNames",
        "com.eurodyn.qlack.fuse.rules.");
    rulesComponent.init();
    assertNotNull(rulesComponent
      .deserializeObject(classLoader,
        rulesComponent.serializeObject(new SerializableClass())));
  }

}
