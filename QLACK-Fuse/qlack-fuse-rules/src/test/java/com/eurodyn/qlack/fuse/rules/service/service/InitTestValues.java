package com.eurodyn.qlack.fuse.rules.service.service;

import com.eurodyn.qlack.fuse.rules.component.RulesComponent;
import com.eurodyn.qlack.fuse.rules.model.KnowledgeBase;
import com.eurodyn.qlack.fuse.rules.model.KnowledgeBaseLibrary;
import com.eurodyn.qlack.fuse.rules.model.KnowledgeBaseRule;
import com.eurodyn.qlack.fuse.rules.util.RulesUtil;
import org.kie.api.KieBase;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class InitTestValues {

  private RulesUtil rulesUtil = new RulesUtil();
  private List<byte[]> libraries = new ArrayList<>();
  private List<String> rules = new ArrayList<>();
  private KnowledgeBase knowledgeBase = new KnowledgeBase();
  private RulesComponent rulesComponent = new RulesComponent();

  public KnowledgeBase createKnowledgeBase(){
    knowledgeBase.setState(rulesUtil.serializeKnowledgeBase(createKieBase()));
    knowledgeBase.setLibraries(createKnowledgeBaseLibrary());
    knowledgeBase.setRules(createKnowledgeBaseRule());
    knowledgeBase.setId("knowledgeBaseId");
    return knowledgeBase;
  }

  public KieBase createKieBase(){
    return rulesUtil.createKieBase(libraries, rules);
  }

  public List<byte[]> createLibraries(){
    List<KnowledgeBaseLibrary> runtimeLibraries = createKnowledgeBase().getLibraries();
    for (KnowledgeBaseLibrary runtimeLibrary : runtimeLibraries) {
      byte[] library = runtimeLibrary.getLibrary();
      libraries.add(library);
    }
    return libraries;
  }

  public List<byte[]> createLibrariesAdd(){
    KnowledgeBaseLibrary knowledgeBaseLibrary = new KnowledgeBaseLibrary();
    knowledgeBaseLibrary.setBase(createKnowledgeBase());
    knowledgeBaseLibrary.setLibrary(new byte[1024]);
    libraries.add(knowledgeBaseLibrary.getLibrary());
    return libraries;
  }

  public List<String> createRules(){
    List<KnowledgeBaseRule> runtimeRules = knowledgeBase.getRules();
    for (KnowledgeBaseRule runtimeRule : runtimeRules) {
      String rule = runtimeRule.getRule();
      rules.add(rule);
    }
    return rules;
  }

  public List<String> createRulesAdd(){
    String rule = "rule\"Offer for Gold on Festival\"\nwhen\nthen\nend";
    KnowledgeBaseRule knowledgeBaseRule = new KnowledgeBaseRule();
    knowledgeBaseRule.setRule(rule);
    knowledgeBaseRule.setBase(knowledgeBase);
    knowledgeBaseRule.setId(knowledgeBase.getId());
    rules.add(knowledgeBaseRule.getRule());
    return rules;
  }

  public List<KnowledgeBaseLibrary> createKnowledgeBaseLibrary(){
    List<KnowledgeBaseLibrary> knowledgeBaseLibraries = new ArrayList<>();
    for (byte[] library : libraries) {
      KnowledgeBaseLibrary knowledgeBaseLibrary = new KnowledgeBaseLibrary();
      knowledgeBaseLibrary.setLibrary(library);

      knowledgeBaseLibrary.setBase(knowledgeBase);
      knowledgeBaseLibraries.add(knowledgeBaseLibrary);
    }
    return knowledgeBaseLibraries;
  }

  public List<KnowledgeBaseRule> createKnowledgeBaseRule(){
    List<KnowledgeBaseRule> knowledgeBaseRules = new ArrayList<>();
    for (String rule : rules) {
      KnowledgeBaseRule knowledgeBaseRule = new KnowledgeBaseRule();
      knowledgeBaseRule.setRule(rule);

      knowledgeBaseRule.setBase(knowledgeBase);
      knowledgeBaseRules.add(knowledgeBaseRule);
    }
    return knowledgeBaseRules;
  }

  public Map<String, byte[]> createInputGlobals(){
    Map<String, Object> globals = new LinkedHashMap<>();
    globals.put("outPut", new byte[1024]);
    Map<String, byte[]> outputGlobals = new LinkedHashMap<>();
    for (Map.Entry<String, Object> object : globals.entrySet()) {
      String id = object.getKey();
      byte[] bytes = rulesComponent.serializeObject(object.getValue());
      outputGlobals.put(id, bytes);
    }
    return outputGlobals;
  }
}