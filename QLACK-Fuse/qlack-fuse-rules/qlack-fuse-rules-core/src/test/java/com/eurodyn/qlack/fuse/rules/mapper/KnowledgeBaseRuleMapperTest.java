package com.eurodyn.qlack.fuse.rules.mapper;

import com.eurodyn.qlack.fuse.rules.InitTestValues;
import com.eurodyn.qlack.fuse.rules.dto.KnowledgeBaseRuleDTO;
import com.eurodyn.qlack.fuse.rules.model.KnowledgeBaseRule;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(MockitoJUnitRunner.class)
public class KnowledgeBaseRuleMapperTest {

  @InjectMocks
  private KnowledgeBaseRuleMapperImpl knowledgeBaseRuleMapper;

  private InitTestValues initTestValues;

  @Before
  public void init() {
    initTestValues = new InitTestValues();
  }

  @Test
  public void mapToDTOTest() {
    KnowledgeBaseRule knowledgeBaseRule = initTestValues
      .createFullKnowledgeBaseRule();
    KnowledgeBaseRuleDTO knowledgeBaseRuleDTO = knowledgeBaseRuleMapper
      .mapToDTO(knowledgeBaseRule);

    assertEquals(knowledgeBaseRule.getRule(), knowledgeBaseRuleDTO.getRule());
  }

  @Test
  public void mapToDTOListTest() {
    List<KnowledgeBaseRule> knowledgeBaseRules = new ArrayList<>();
    knowledgeBaseRules.add(initTestValues.createFullKnowledgeBaseRule());
    List<KnowledgeBaseRuleDTO> knowledgeBaseRuleDTOS = knowledgeBaseRuleMapper
      .mapToDTO(knowledgeBaseRules);

    assertEquals(knowledgeBaseRules.size(), knowledgeBaseRuleDTOS.size());
  }

  @Test
  public void mapToDTONullTest() {
    assertNull(knowledgeBaseRuleMapper.mapToDTO((KnowledgeBaseRule) null));

    List<KnowledgeBaseRuleDTO> knowledgeBaseRuleDTOS = knowledgeBaseRuleMapper
      .mapToDTO(
        (List<KnowledgeBaseRule>) null);
    assertNull(knowledgeBaseRuleDTOS);
  }

  @Test
  public void mapToEntityTest() {
    KnowledgeBaseRuleDTO knowledgeBaseRuleDTO = initTestValues
      .createKnowledgeBaseRuleDTO();
    KnowledgeBaseRule knowledgeBaseRule = knowledgeBaseRuleMapper
      .mapToEntity(knowledgeBaseRuleDTO);

    assertEquals(knowledgeBaseRuleDTO.getRule(), knowledgeBaseRule.getRule());
  }

  @Test
  public void mapToEntityListTest() {
    List<KnowledgeBaseRuleDTO> knowledgeBaseRuleDTOS = new ArrayList<>();
    knowledgeBaseRuleDTOS.add(initTestValues.createKnowledgeBaseRuleDTO());
    List<KnowledgeBaseRule> knowledgeBaseRules = knowledgeBaseRuleMapper
      .mapToEntity(knowledgeBaseRuleDTOS);

    assertEquals(knowledgeBaseRuleDTOS.size(), knowledgeBaseRules.size());
  }

  @Test
  public void mapToEntityNullTest() {
    assertNull(knowledgeBaseRuleMapper.mapToEntity((KnowledgeBaseRuleDTO) null));

    List<KnowledgeBaseRule> knowledgeBaseRules = knowledgeBaseRuleMapper
      .mapToEntity(
        (List<KnowledgeBaseRuleDTO>) null);
    assertNull(knowledgeBaseRules);
  }

}
