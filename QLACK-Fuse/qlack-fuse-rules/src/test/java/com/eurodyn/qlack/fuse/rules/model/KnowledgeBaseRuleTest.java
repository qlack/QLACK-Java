package com.eurodyn.qlack.fuse.rules.model;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class KnowledgeBaseRuleTest {

  private KnowledgeBaseRule knowledgeBaseRule;

  @Before
  public void init() {
    knowledgeBaseRule = new KnowledgeBaseRule();
  }

  @Test
  public void ruleTest() {
    String rule = "rule";
    knowledgeBaseRule.setRule(rule);
    assertEquals(rule, knowledgeBaseRule.getRule());
  }

  @Test
  public void baseRule() {
    KnowledgeBase knowledgeBase = new KnowledgeBase();
    knowledgeBaseRule.setBase(knowledgeBase);
    assertEquals(knowledgeBase, knowledgeBaseRule.getBase());
  }

}
