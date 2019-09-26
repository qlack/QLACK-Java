package com.eurodyn.qlack.fuse.rules.dto;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class KnowledgeBaseRuleDTOTest {

  private KnowledgeBaseRuleDTO knowledgeBaseLibraryDTO;

  @Before
  public void init() {
    knowledgeBaseLibraryDTO = new KnowledgeBaseRuleDTO();
  }

  @Test
  public void libraryTest() {
    String rule = "rule";
    knowledgeBaseLibraryDTO.setRule(rule);
    assertEquals(rule, knowledgeBaseLibraryDTO.getRule());
  }

}
