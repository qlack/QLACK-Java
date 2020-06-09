package com.eurodyn.qlack.fuse.rules.service;

import com.eurodyn.qlack.fuse.rules.InitTestValues;
import com.eurodyn.qlack.fuse.rules.component.RulesComponent;
import com.eurodyn.qlack.fuse.rules.dto.ExecutionResultsDTO;
import com.eurodyn.qlack.fuse.rules.model.KnowledgeBase;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class KnowledgeSessionServiceTest {

  public static final String KNOWLEDGE_BASE_ID = "knowledgeBaseId";
  public static final String RULE_NAME = "ruleName";
  @InjectMocks
  private KnowledgeSessionService knowledgeSessionService;

  @Mock
  private KnowledgeBaseService knowledgeBaseService;
  @Mock
  private KnowledgeBase knowledgeBaseState;
  @Mock
  private KnowledgeBase customKnowledgeBaseState;
  @Mock
  private RulesComponent rulesComponent;

  private List<byte[]> inputLibraries;
  private List<String> inputRules;
  private Map<String, byte[]> inputGlobals;
  private List<byte[]> facts;

  @Before
  public void init() {
    knowledgeSessionService = new KnowledgeSessionService(rulesComponent,
      knowledgeBaseService);
    InitTestValues initTestValues = new InitTestValues();
    knowledgeBaseState = initTestValues.createKnowledgeBase();
    customKnowledgeBaseState = initTestValues
      .createKnowledgeBaseWithCustomRules();
    inputLibraries = initTestValues.createLibraries();
    inputRules = initTestValues.createRules();
    inputGlobals = initTestValues.createInputGlobals();
    facts = initTestValues.createFacts();
  }

  @Test
  public void statelessExecuteTest() {
    when(knowledgeBaseService.findKnowledgeBaseStateById(KNOWLEDGE_BASE_ID))
      .thenReturn(knowledgeBaseState);
    ExecutionResultsDTO executionResultsDTO = knowledgeSessionService
            .statelessExecute(KNOWLEDGE_BASE_ID, inputLibraries, null,
                    null, null, RULE_NAME);
    assertNull(executionResultsDTO);
  }

  @Test
  public void statelessExecuteNullBaseIdTest() {
    ExecutionResultsDTO executionResultsDTO = knowledgeSessionService
            .statelessExecute(null, inputLibraries, inputRules,
                    null, null, RULE_NAME);
    assertNull(executionResultsDTO);
  }

  @Test
  public void statelessExecuteInputGlobalsTest() {
    when(knowledgeBaseService.findKnowledgeBaseStateById(KNOWLEDGE_BASE_ID))
            .thenReturn(knowledgeBaseState);
    ExecutionResultsDTO executionResultsDTO = knowledgeSessionService
            .statelessExecute(KNOWLEDGE_BASE_ID, inputLibraries, inputRules,
                    inputGlobals, facts, RULE_NAME);
    TestCase.assertNotNull(executionResultsDTO);
  }

  @Test
  public void statelessExecuteInputGlobalsTestWithNullRuleNames() {
    when(knowledgeBaseService.findKnowledgeBaseStateById(KNOWLEDGE_BASE_ID))
            .thenReturn(knowledgeBaseState);
    ExecutionResultsDTO executionResultsDTO = knowledgeSessionService
            .statelessExecute(KNOWLEDGE_BASE_ID, inputLibraries, inputRules,
                    inputGlobals, facts, null);
    TestCase.assertNotNull(executionResultsDTO);
  }

  @Test
  public void statelessExecuteInputGlobalsTestWithNullKnowledgeBase() {
    ExecutionResultsDTO executionResultsDTO = knowledgeSessionService
            .statelessExecute(null, inputLibraries, inputRules,
                    inputGlobals, facts, RULE_NAME);
    TestCase.assertNotNull(executionResultsDTO);
  }

  @Test
  public void fireRulesTest() {
    when(knowledgeBaseService.findKnowledgeBaseStateById(KNOWLEDGE_BASE_ID))
            .thenReturn(knowledgeBaseState);
    ExecutionResultsDTO executionResultsDTO = knowledgeSessionService
            .fireRules(KNOWLEDGE_BASE_ID, null,
                    null, null);
    TestCase.assertNotNull(executionResultsDTO);
  }

  @Test
  public void fireRulesInputGlobalsFactsTest() {
    when(knowledgeBaseService.findKnowledgeBaseStateById(KNOWLEDGE_BASE_ID))
            .thenReturn(knowledgeBaseState);
    ExecutionResultsDTO executionResultsDTO = knowledgeSessionService
            .fireRules(KNOWLEDGE_BASE_ID, inputRules,
                    inputGlobals, facts);
    TestCase.assertNotNull(executionResultsDTO);
  }

  @Test
  public void fireRulesInputGlobalsFactsLambdaExpression() {
    when(knowledgeBaseService.findKnowledgeBaseStateById(KNOWLEDGE_BASE_ID))
            .thenReturn(customKnowledgeBaseState);
    ExecutionResultsDTO executionResultsDTO = knowledgeSessionService
            .fireRules(KNOWLEDGE_BASE_ID, inputRules,
                    inputGlobals, facts);
    TestCase.assertNotNull(executionResultsDTO);
  }
}
