package com.eurodyn.qlack.fuse.rules.util;

import com.eurodyn.qlack.fuse.rules.InitTestValues;
import com.eurodyn.qlack.fuse.rules.exception.QRulesException;
import com.eurodyn.qlack.fuse.rules.model.KnowledgeBase;
import com.eurodyn.qlack.fuse.rules.model.KnowledgeBaseLibrary;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class RulesUtilTest {

  @InjectMocks
  private RulesUtil rulesUtil;

  @Mock
  private KnowledgeBase knowledgeBase;

  private List<byte[]> libraries;
  private List<String> rules;
  private List<String> wrongRules;
  private List<KnowledgeBaseLibrary> knowledgeBaseLibraryList;
  private InitTestValues initTestValues;

  @Before
  public void init() {
    rulesUtil = new RulesUtil();
    initTestValues = new InitTestValues();
    knowledgeBase = initTestValues.createKnowledgeBase();
    libraries = initTestValues.createLibraries();
    rules = initTestValues.createRules();
    wrongRules = initTestValues.createWrongRules();
    knowledgeBaseLibraryList = new ArrayList<>();
  }

  @Test
  public void createKieBaseFromBaseStateTest() {
    knowledgeBaseLibraryList.add(initTestValues.createKnowledgeBaseLibrary());
    knowledgeBase.setLibraries(knowledgeBaseLibraryList);
    assertNotNull(rulesUtil.createKieBaseFromBaseState(knowledgeBase));
  }

  @Test
  public void createKieBaseTest() {
    assertNotNull(rulesUtil.createKieBase(null, rules));
    assertNotNull(rulesUtil.createKieBase(libraries, rules));
  }

  @Test(expected = QRulesException.class)
  public void compileRulesExceptionTest() {
    assertNotNull(rulesUtil.createKieBase(libraries, wrongRules));
  }
}

