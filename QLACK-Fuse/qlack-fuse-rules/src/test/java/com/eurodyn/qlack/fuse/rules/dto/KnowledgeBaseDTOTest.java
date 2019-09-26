package com.eurodyn.qlack.fuse.rules.dto;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class KnowledgeBaseDTOTest {

  private KnowledgeBaseDTO knowledgeBaseDTO;

  @Before
  public void init() {
    knowledgeBaseDTO = new KnowledgeBaseDTO();
  }

  @Test
  public void idTest() {
    String id = "id";
    knowledgeBaseDTO.setId(id);
    assertEquals(id, knowledgeBaseDTO.getId());
  }

  @Test
  public void stateTest() {
    byte[] state = "byte array".getBytes();
    knowledgeBaseDTO.setState(state);
    assertEquals(state, knowledgeBaseDTO.getState());
  }

  @Test
  public void librariesTest() {
    List<KnowledgeBaseLibraryDTO> libraries = new ArrayList<>();
    knowledgeBaseDTO.setLibraries(libraries);
    assertEquals(libraries, knowledgeBaseDTO.getLibraries());
  }

  @Test
  public void rulesTest() {
    List<KnowledgeBaseRuleDTO> rules = new ArrayList<>();
    knowledgeBaseDTO.setRules(rules);
    assertEquals(rules, knowledgeBaseDTO.getRules());
  }
}
