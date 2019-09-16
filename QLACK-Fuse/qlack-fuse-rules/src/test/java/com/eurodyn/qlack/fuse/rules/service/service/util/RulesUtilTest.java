package com.eurodyn.qlack.fuse.rules.service.service.util;

import com.eurodyn.qlack.fuse.rules.model.KnowledgeBase;
import com.eurodyn.qlack.fuse.rules.model.KnowledgeBaseLibrary;
import com.eurodyn.qlack.fuse.rules.service.service.InitTestValues;
import com.eurodyn.qlack.fuse.rules.util.ClassLoaderKnowledgeBase;
import com.eurodyn.qlack.fuse.rules.util.RulesUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kie.api.KieBase;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class RulesUtilTest {

  @InjectMocks private RulesUtil rulesUtil;

  @Mock private KnowledgeBase knowledgeBase;

  private List<byte[]> libraries;
  private List<String> rules;

  @Before
  public void init(){
    rulesUtil = new RulesUtil();
    InitTestValues initTestValues = new InitTestValues();
    knowledgeBase = initTestValues.createKnowledgeBase();
    libraries = initTestValues.createLibraries();
    rules = initTestValues.createRules();
  }

  @Test
  public void createKieBaseFromBaseStateTest(){
    assertNotNull(rulesUtil.createKieBaseFromBaseState(knowledgeBase));
  }

  @Test
  public void createKieBaseTest(){
    assertNotNull(rulesUtil.createKieBase(libraries, rules));
  }
}
