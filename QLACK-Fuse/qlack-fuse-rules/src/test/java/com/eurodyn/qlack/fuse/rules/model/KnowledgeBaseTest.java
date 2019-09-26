package com.eurodyn.qlack.fuse.rules.model;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class KnowledgeBaseTest {

  private KnowledgeBase knowledgeBase;

  @Before
  public void init() {
    knowledgeBase = new KnowledgeBase();
  }

  @Test
  public void stateTest() {
    byte[] state = "byte array".getBytes();
    knowledgeBase.setState(state);
    assertEquals(state, knowledgeBase.getState());
  }

  @Test
  public void librariesTest() {
    List<KnowledgeBaseLibrary> libraries = new ArrayList<>();
    knowledgeBase.setLibraries(libraries);
    assertEquals(libraries, knowledgeBase.getLibraries());
  }

  @Test
  public void rulesTest() {
    List<KnowledgeBaseRule> rules = new ArrayList<>();
    knowledgeBase.setRules(rules);
    assertEquals(rules, knowledgeBase.getRules());
  }

}
