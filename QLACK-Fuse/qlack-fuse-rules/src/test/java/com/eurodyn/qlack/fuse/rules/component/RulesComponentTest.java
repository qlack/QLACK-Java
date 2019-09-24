package com.eurodyn.qlack.fuse.rules.component;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.eurodyn.qlack.fuse.rules.SerializableClass;
import com.eurodyn.qlack.fuse.rules.UnserializableClass;
import com.eurodyn.qlack.fuse.rules.exception.QRulesException;
import com.eurodyn.qlack.fuse.rules.util.JarClassLoaderBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class RulesComponentTest {

  @InjectMocks
  private RulesComponent rulesComponent;

  private ClassLoader classLoader;

  @Before
  public void init(){
    rulesComponent = new RulesComponent();
    JarClassLoaderBuilder classLoaderBuilder = new JarClassLoaderBuilder();
    classLoader = classLoaderBuilder.buildClassLoader(null);
  }

  @Test(expected = QRulesException.class)
  public void serializeObjectExceptionTest(){
    assertNotNull(rulesComponent.serializeObject(new UnserializableClass()));
  }

  @Test
  public void deserializeObjectTest(){
    assertNotNull(rulesComponent.deserializeObject(classLoader, rulesComponent.serializeObject(new SerializableClass())));
  }

  @Test
  public void deserializeObjectNullClassTest() throws ClassNotFoundException {

    ClassLoader classLoaderInstance = mock(ClassLoader.class);
    when(classLoaderInstance.loadClass("com.eurodyn.qlack.fuse.rules.SerializableClass")).thenReturn(null);
    assertNotNull(rulesComponent.deserializeObject(classLoaderInstance, rulesComponent.serializeObject(new SerializableClass())));
  }

  @Test(expected = QRulesException.class)
  public void deserializeObjectExceptionTest(){
    assertNotNull(rulesComponent.deserializeObject(classLoader, "object".getBytes()));
  }
}
