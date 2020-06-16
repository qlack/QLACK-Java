package com.eurodyn.qlack.fuse.rules.service;

import com.eurodyn.qlack.common.exception.QDoesNotExistException;
import com.eurodyn.qlack.fuse.rules.InitTestValues;
import com.eurodyn.qlack.fuse.rules.component.RulesComponent;
import com.eurodyn.qlack.fuse.rules.dto.ExecutionResultsDTO;
import com.eurodyn.qlack.fuse.rules.mapper.KnowledgeBaseMapper;
import com.eurodyn.qlack.fuse.rules.model.KnowledgeBase;
import com.eurodyn.qlack.fuse.rules.repository.KnowledgeBaseRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class KnowledgeBaseServiceTest {

    public static final String KNOWLEDGE_BASE_ID = "knowledgeBaseId";
    public static final String RULE_NAME = "ruleName";
    @InjectMocks
    private KnowledgeBaseService knowledgeBaseService;

    @Mock
    private KnowledgeBaseMapper knowledgeBaseMapper;
    @Mock
    private KnowledgeBaseRepository knowledgeBaseRepository;
    @Mock
    private KnowledgeBase knowledgeBase;
    @Mock
    private RulesComponent rulesComponent;
    private List<byte[]> inputLibraries;
    private List<String> inputRules;
    private Map<String, byte[]> inputGlobals;
    private List<byte[]> facts;

    @Before
    public void init() {
        InitTestValues initTestValues = new InitTestValues();
        knowledgeBaseService = new KnowledgeBaseService(knowledgeBaseMapper,
                knowledgeBaseRepository, rulesComponent);
        inputLibraries = initTestValues.createLibrariesAdd();
        inputRules = initTestValues.createRulesAdd();
        knowledgeBase = initTestValues.createKnowledgeBase();
        inputGlobals = initTestValues.createInputGlobals();
        facts = initTestValues.createFacts();
    }

    @Test(expected = QDoesNotExistException.class)
    public void findByIdNullTest() {
        KnowledgeBase base = knowledgeBaseService
                .findById(KNOWLEDGE_BASE_ID);
        assertNull(base);
    }

    @Test
    public void findByIdTest() {
        when(knowledgeBaseRepository.fetchById(KNOWLEDGE_BASE_ID))
                .thenReturn(knowledgeBase);
        KnowledgeBase base = knowledgeBaseService
                .findById(KNOWLEDGE_BASE_ID);
        assertNotNull(base);
        verify(knowledgeBaseRepository, times(1)).fetchById(KNOWLEDGE_BASE_ID);
    }

    @Test
    public void createKnowledgeBaseTest() {
        when(knowledgeBaseRepository.save(any(KnowledgeBase.class))).thenReturn(knowledgeBase);
        String base = knowledgeBaseService
                .createKnowledgeBase(inputLibraries, inputRules);
        assertEquals(knowledgeBase.getId(), base);
        verify(knowledgeBaseRepository, times(1)).save(any());
    }

    @Test
    public void getAllTest() {
        knowledgeBaseService.getAll();
        verify(knowledgeBaseRepository, times(1)).findAll();
    }

    @Test
    public void executeRulesTest() {
        when(knowledgeBaseRepository.fetchById(KNOWLEDGE_BASE_ID))
                .thenReturn(knowledgeBase);
        ExecutionResultsDTO executionResultsDTO = knowledgeBaseService
                .executeRules(KNOWLEDGE_BASE_ID, inputLibraries, null,
                        null, null, RULE_NAME);
        assertNull(executionResultsDTO);
    }

    @Test
    public void executeRulesNullTest() {
        ExecutionResultsDTO executionResultsDTO = knowledgeBaseService
                .executeRules(null, inputLibraries, inputRules,
                        null, null, RULE_NAME);
        assertNull(executionResultsDTO);
    }

    @Test
    public void executeRulesInputGlobalsTest() {
        when(knowledgeBaseRepository.fetchById(KNOWLEDGE_BASE_ID))
                .thenReturn(knowledgeBase);
        ExecutionResultsDTO executionResultsDTO = knowledgeBaseService
                .executeRules(KNOWLEDGE_BASE_ID, inputLibraries, inputRules,
                        inputGlobals, facts, RULE_NAME);
        assertNotNull(executionResultsDTO);
    }

    @Test
    public void executeRulesInputGlobalsTestWithNullRuleNames() {
        when(knowledgeBaseRepository.fetchById(KNOWLEDGE_BASE_ID))
                .thenReturn(knowledgeBase);
        ExecutionResultsDTO executionResultsDTO = knowledgeBaseService
                .executeRules(KNOWLEDGE_BASE_ID, inputLibraries, inputRules,
                        inputGlobals, facts, null);
        assertNotNull(executionResultsDTO);
    }

    @Test
    public void executeRulesInputGlobalsTestWithNullKnowledgeBase() {
        ExecutionResultsDTO executionResultsDTO = knowledgeBaseService
                .executeRules(null, inputLibraries, inputRules,
                        inputGlobals, facts, RULE_NAME);
        assertNotNull(executionResultsDTO);
    }

    @Test
    public void executeRulesExceptionTest() {
        byte[] bytes = new byte[0];
        List<byte[]> list = new ArrayList<>();
        list.add(bytes);
        ExecutionResultsDTO executionResultsDTO =
                knowledgeBaseService.executeRules(null, inputRules, facts, RULE_NAME);
        assertNotNull(executionResultsDTO);
    }
}
