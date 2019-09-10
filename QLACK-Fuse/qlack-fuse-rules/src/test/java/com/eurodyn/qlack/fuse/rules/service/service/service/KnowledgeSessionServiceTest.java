package com.eurodyn.qlack.fuse.rules.service.service.service;

import com.eurodyn.qlack.fuse.rules.component.RulesComponent;
import com.eurodyn.qlack.fuse.rules.dto.ExecutionResultsDTO;
import com.eurodyn.qlack.fuse.rules.model.KnowledgeBase;
import com.eurodyn.qlack.fuse.rules.service.KnowledgeBaseService;
import com.eurodyn.qlack.fuse.rules.service.KnowledgeSessionService;
import com.eurodyn.qlack.fuse.rules.service.service.InitTestValues;
import com.eurodyn.qlack.fuse.rules.util.ClassLoaderKnowledgeBase;
import com.eurodyn.qlack.fuse.rules.util.RulesUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kie.api.runtime.KieSession;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.Map;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class KnowledgeSessionServiceTest {

  @InjectMocks private KnowledgeSessionService knowledgeSessionService;

  @Mock private KnowledgeBaseService knowledgeBaseService;
  @Mock private KnowledgeBase knowledgeBaseState;
  @Mock private RulesComponent rulesComponent;

  private List<byte[]> inputLibraries;
  private List<String> inputRules;
  private Map<String, byte[]> inputGlobals;

  @Before
  public void init(){
    knowledgeSessionService = new KnowledgeSessionService(rulesComponent, knowledgeBaseService);
    InitTestValues initTestValues = new InitTestValues();
    knowledgeBaseState = initTestValues.createKnowledgeBase();
    inputLibraries = initTestValues.createLibraries();
    inputRules = initTestValues.createRules();
    inputGlobals = initTestValues.createInputGlobals();
  }

  @Test
  public void createKnowledgeSessionTest() {
    when(knowledgeBaseService.findKnowledgeBaseStateById("knowledgeBaseId")).thenReturn(knowledgeBaseState);
    KieSession newSession =
        knowledgeSessionService.createKnowledgeSession("knowledgeBaseId");
    assertNotNull(newSession);
  }

  @Test
  public void statelessExecuteTest(){
    when(knowledgeBaseService.findKnowledgeBaseStateById("knowledgeBaseId"))
        .thenReturn(knowledgeBaseState);
    ExecutionResultsDTO executionResultsDTO = knowledgeSessionService.statelessExecute("knowledgeBaseId", inputLibraries, null,
        null, null, "ruleName");
    assertNull(executionResultsDTO);
  }

  @Test
  public void statelessExecuteNullBaseIdTest(){
    ExecutionResultsDTO executionResultsDTO = knowledgeSessionService.statelessExecute(null, inputLibraries, inputRules,
        null, null, "ruleName");
    assertNull(executionResultsDTO);
  }

  @Test
  public void statelessExecuteInputGobalsTest(){
    when(knowledgeBaseService.findKnowledgeBaseStateById("knowledgeBaseId"))
        .thenReturn(knowledgeBaseState);
    ExecutionResultsDTO executionResultsDTO = knowledgeSessionService.statelessExecute("knowledgeBaseId", inputLibraries, inputRules,
        inputGlobals, inputLibraries, "ruleName");
    assertNotNull(executionResultsDTO);
  }

  @Test
  public void fireRulesTest(){
    when(knowledgeBaseService.findKnowledgeBaseStateById("knowledgeBaseId"))
        .thenReturn(knowledgeBaseState);
    ExecutionResultsDTO executionResultsDTO = knowledgeSessionService.fireRules("knowledgeBaseId", null,
        null, null);
    assertNotNull(executionResultsDTO);
  }

  @Test
  public void fireRulesInputGlobalsFactsTest(){
    when(knowledgeBaseService.findKnowledgeBaseStateById("knowledgeBaseId"))
        .thenReturn(knowledgeBaseState);
    ExecutionResultsDTO executionResultsDTO = knowledgeSessionService.fireRules("knowledgeBaseId", inputRules,
        inputGlobals, inputLibraries);
    assertNotNull(executionResultsDTO);
  }
}
