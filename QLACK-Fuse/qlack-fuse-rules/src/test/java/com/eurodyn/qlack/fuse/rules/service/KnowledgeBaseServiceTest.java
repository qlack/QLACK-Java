package com.eurodyn.qlack.fuse.rules.service;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.eurodyn.qlack.common.exception.QDoesNotExistException;
import com.eurodyn.qlack.fuse.rules.InitTestValues;
import com.eurodyn.qlack.fuse.rules.mapper.KnowledgeBaseMapper;
import com.eurodyn.qlack.fuse.rules.model.KnowledgeBase;
import com.eurodyn.qlack.fuse.rules.repository.KnowledgeBaseRepository;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class KnowledgeBaseServiceTest {

  @InjectMocks
  private KnowledgeBaseService knowledgeBaseService;

  @Mock
  private KnowledgeBaseMapper knowledgeBaseMapper;
  @Mock
  private KnowledgeBaseRepository knowledgeBaseRepository;
  @Mock
  private KnowledgeBase knowledgeBase;
  private List<byte[]> inputLibraries;
  private List<String> inputRules;

  @Before
  public void init() {
    InitTestValues initTestValues = new InitTestValues();
    knowledgeBaseService = new KnowledgeBaseService(knowledgeBaseMapper,
      knowledgeBaseRepository);
    inputLibraries = initTestValues.createLibrariesAdd();
    inputRules = initTestValues.createRulesAdd();
  }

  @Test(expected = QDoesNotExistException.class)
  public void findKnowledgeBaseStateByIdNullTest() {
    KnowledgeBase base = knowledgeBaseService
      .findKnowledgeBaseStateById("knowledgeBaseId");
    assertNull(base);
  }

  @Test
  public void findKnowledgeBaseStateByIdTest() {
    when(knowledgeBaseRepository.fetchById("knowledgeBaseId"))
      .thenReturn(knowledgeBase);
    KnowledgeBase base = knowledgeBaseService
      .findKnowledgeBaseStateById("knowledgeBaseId");
    assertNotNull(base);
    verify(knowledgeBaseRepository, times(1)).fetchById("knowledgeBaseId");
  }

  @Test
  public void createKnowledgeBaseTest() {
    String base = knowledgeBaseService
      .createKnowledgeBase(inputLibraries, inputRules);
    assertEquals(knowledgeBase.getId(), base);
    verify(knowledgeBaseRepository, times(1)).save(any());
  }

  @Test
  public void getAllKnowledgeBasesTest() {
    knowledgeBaseService.getAllKnowledgeBases();
    verify(knowledgeBaseRepository, times(1)).findAll();
  }
}
