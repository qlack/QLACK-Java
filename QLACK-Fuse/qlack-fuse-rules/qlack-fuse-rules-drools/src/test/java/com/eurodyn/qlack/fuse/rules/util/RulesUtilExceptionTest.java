package com.eurodyn.qlack.fuse.rules.util;

import com.eurodyn.qlack.fuse.rules.InitTestValues;
import com.eurodyn.qlack.fuse.rules.exception.QRulesException;
import com.eurodyn.qlack.fuse.rules.util.classloader.JarClassLoaderBuilder;
import com.eurodyn.qlack.fuse.rules.util.classloader.MapBackedClassLoader;
import org.drools.core.impl.KnowledgeBaseFactory;
import org.drools.core.util.DroolsStreamUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kie.api.KieBase;
import org.mockito.InjectMocks;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.util.List;

import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(DroolsStreamUtils.class)
public class RulesUtilExceptionTest {

  @InjectMocks
  private RulesUtil rulesUtil;

  private List<byte[]> bytes;

  @Before
  public void init() {
    rulesUtil = new RulesUtil();
    InitTestValues initTestValues = new InitTestValues();
    bytes = initTestValues.createByteFacts();
    PowerMockito.mockStatic(DroolsStreamUtils.class);
  }

  @Test(expected = QRulesException.class)
  public void serializeKnowledgeBaseExceptionTest() throws IOException {
    KieBase kbase = KnowledgeBaseFactory
      .newKnowledgeBase("test", null);
    when(DroolsStreamUtils.streamOut(kbase)).thenThrow(new IOException());

    rulesUtil.serializeKnowledgeBase(kbase);
  }

  @Test(expected = QRulesException.class)
  public void deserializeKnowledgeBaseExceptionTest()
    throws IOException, ClassNotFoundException {

    JarClassLoaderBuilder classLoaderBuilder = new JarClassLoaderBuilder();
    MapBackedClassLoader classLoader = classLoaderBuilder
      .buildClassLoader("null");

    when(DroolsStreamUtils.streamIn(bytes.get(0), classLoader))
      .thenThrow(new IOException());

    rulesUtil.deserializeKnowledgeBase(bytes.get(0), classLoader);
  }
}

