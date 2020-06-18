package com.eurodyn.qlack.fuse.rules;

import com.eurodyn.qlack.fuse.rules.dto.KnowledgeBaseDTO;
import com.eurodyn.qlack.fuse.rules.dto.KnowledgeBaseLibraryDTO;
import com.eurodyn.qlack.fuse.rules.dto.KnowledgeBaseRuleDTO;
import com.eurodyn.qlack.fuse.rules.model.KnowledgeBase;
import com.eurodyn.qlack.fuse.rules.model.KnowledgeBaseLibrary;
import com.eurodyn.qlack.fuse.rules.model.KnowledgeBaseRule;
import com.eurodyn.qlack.fuse.rules.util.RulesUtil;
import org.kie.api.KieBase;

import java.util.ArrayList;
import java.util.List;

public class InitTestValues {

  private final RulesUtil rulesUtil = new RulesUtil();
  private final List<byte[]> libraries = new ArrayList<>();
  private final List<String> rules = new ArrayList<>();
  private final KnowledgeBase knowledgeBase = new KnowledgeBase();

  public KnowledgeBase createKnowledgeBase() {
    knowledgeBase.setState(rulesUtil.serializeKnowledgeBase(createKieBase()));
    knowledgeBase.setLibraries(createKnowledgeBaseLibraryList());
    knowledgeBase.setRules(createKnowledgeBaseRule());
    knowledgeBase.setId("knowledgeBaseId");
    return knowledgeBase;
  }

  public KieBase createKieBase() {
    return rulesUtil.createKieBase(libraries, rules);
  }

  public List<KnowledgeBaseLibrary> createKnowledgeBaseLibraryList() {
    List<KnowledgeBaseLibrary> knowledgeBaseLibraries = new ArrayList<>();
    for (byte[] library : libraries) {
      KnowledgeBaseLibrary knowledgeBaseLibrary = new KnowledgeBaseLibrary();
      knowledgeBaseLibrary.setLibrary(library);

      knowledgeBaseLibrary.setBase(knowledgeBase);
      knowledgeBaseLibraries.add(knowledgeBaseLibrary);
    }
    return knowledgeBaseLibraries;
  }

  public List<KnowledgeBaseRule> createKnowledgeBaseRule() {
    List<KnowledgeBaseRule> knowledgeBaseRules = new ArrayList<>();
    for (String rule : rules) {
      KnowledgeBaseRule knowledgeBaseRule = new KnowledgeBaseRule();
      knowledgeBaseRule.setRule(rule);

      knowledgeBaseRule.setBase(knowledgeBase);
      knowledgeBaseRules.add(knowledgeBaseRule);
    }
    return knowledgeBaseRules;
  }

  public KnowledgeBaseLibrary createKnowledgeBaseLibrary() {
    KnowledgeBaseLibrary knowledgeBaseLibrary = new KnowledgeBaseLibrary();
    knowledgeBaseLibrary.setBase(createKnowledgeBase());
    knowledgeBaseLibrary.setLibrary(new byte[1024]);
    return knowledgeBaseLibrary;
  }

  public KnowledgeBase createFullKnowledgeBase() {
    KnowledgeBase base = new KnowledgeBase();
    base.setId("id");
    base.setState("state".getBytes());
    base.setRules(new ArrayList<>());
    base.setLibraries(new ArrayList<>());

    return base;
  }

  public KnowledgeBaseDTO createKnowledgeBaseDTO() {
    KnowledgeBaseDTO knowledgeBaseDTO = new KnowledgeBaseDTO();
    knowledgeBaseDTO.setId("id");
    knowledgeBaseDTO.setState("state".getBytes());
    knowledgeBaseDTO.setRules(new ArrayList<>());
    knowledgeBaseDTO.setLibraries(new ArrayList<>());

    return knowledgeBaseDTO;
  }

  public KnowledgeBaseRule createFullKnowledgeBaseRule() {
    KnowledgeBaseRule knowledgeBaseRule = new KnowledgeBaseRule();
    knowledgeBaseRule.setRule("rule");

    return knowledgeBaseRule;
  }

  public KnowledgeBaseRuleDTO createKnowledgeBaseRuleDTO() {
    KnowledgeBaseRuleDTO knowledgeBaseRuleDTO = new KnowledgeBaseRuleDTO();
    knowledgeBaseRuleDTO.setRule("rule");

    return knowledgeBaseRuleDTO;
  }

  public KnowledgeBaseLibrary createFullKnowledgeBaseLibrary() {
    KnowledgeBaseLibrary knowledgeBaseLibrary = new KnowledgeBaseLibrary();
    knowledgeBaseLibrary.setLibrary("library".getBytes());

    return knowledgeBaseLibrary;
  }

  public KnowledgeBaseLibraryDTO createKnowledgeBaseLibraryDTO() {
    KnowledgeBaseLibraryDTO knowledgeBaseLibraryDTO = new KnowledgeBaseLibraryDTO();
    knowledgeBaseLibraryDTO.setLibrary("library".getBytes());

    return knowledgeBaseLibraryDTO;
  }

}
