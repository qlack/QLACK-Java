package com.eurodyn.qlack.fuse.rules.service;

import com.eurodyn.qlack.fuse.rules.component.RulesComponent;
import com.eurodyn.qlack.fuse.rules.dto.ExecutionResultsDTO;
import com.eurodyn.qlack.fuse.rules.model.KnowledgeBase;
import com.eurodyn.qlack.fuse.rules.util.RulesUtil;
import com.eurodyn.qlack.fuse.rules.util.classloader.ClassLoaderKnowledgeBase;
import com.eurodyn.qlack.fuse.rules.util.classloader.JarClassLoaderBuilder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import lombok.extern.java.Log;
import org.drools.core.base.RuleNameEqualsAgendaFilter;
import org.drools.core.command.runtime.rule.FireAllRulesCommand;
import org.kie.api.KieBase;
import org.kie.api.command.Command;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.StatelessKieSession;
import org.kie.api.runtime.rule.AgendaFilter;
import org.kie.internal.command.CommandFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * This service provides methods related to the Kie Session.
 *
 * @author European Dynamics SA
 */
@Service
@Transactional
@Log
public class KnowledgeSessionService {

  private final RulesUtil rulesUtil = new RulesUtil();

  private final RulesComponent rulesComponent;

  private final KnowledgeBaseService knowledgeBaseService;

  public KnowledgeSessionService(RulesComponent rulesComponent,
      KnowledgeBaseService knowledgeBaseService) {
    this.rulesComponent = rulesComponent;
    this.knowledgeBaseService = knowledgeBaseService;
  }

  /**
   * Creates a new stateful Knowledge Session using an existing Knowledge Base.
   *
   * @param knowledgeBaseId the id of the existing Knowledge Base
   * @return the id of the newly created Knowledge Session
   */
  public KieSession createKnowledgeSession(String knowledgeBaseId) {
    log.info("Creating new Knowledge Session for Knowledge Base with id " + knowledgeBaseId);

    KnowledgeBase knowledgeBaseState = knowledgeBaseService
        .findKnowledgeBaseStateById(knowledgeBaseId);

    KieBase kieBase = rulesUtil.createKieBaseFromBaseState(knowledgeBaseState).knowledgeBase;

    return kieBase.newKieSession();
  }

  /**
   * Execute rules directly using a stateless session. The rules and the libraries can be retrieved
   * from an existing Knowledge Base or can be given as parameter.
   *
   * @param knowledgeBaseId the id of the Knowledge Base that included the libraries and the rules
   * to be executed
   * @param inputLibraries the libraries to be used
   * @param inputRules the rules to be executed
   * @param inputGlobals the globals to be added in the session
   * @param inputFacts the objects that the rules will be executed against
   * @param ruleNameToExecute the name of a specific rule that can be executed from the Knowledge
   * Base, while the others are ignored
   * @return the results of the execution
   */
  public ExecutionResultsDTO statelessExecute(String knowledgeBaseId, List<byte[]> inputLibraries,
      List<String> inputRules,
      Map<String, byte[]> inputGlobals, List<byte[]> inputFacts, String ruleNameToExecute) {
    log.info(
        "Creating Stateless Knowledge Session using the Knowledge Base with id " + knowledgeBaseId);

    KieBase kieBase;
    ClassLoader classLoader;
    List<Command<?>> commands = new ArrayList<>();

    if (knowledgeBaseId != null) {
      KnowledgeBase knowledgeBaseState = knowledgeBaseService
          .findKnowledgeBaseStateById(knowledgeBaseId);
      ClassLoaderKnowledgeBase clkb = rulesUtil.createKieBaseFromBaseState(knowledgeBaseState);
      kieBase = clkb.knowledgeBase;

      classLoader = clkb.classLoader;
    } else {
      kieBase = rulesUtil.createKieBase(inputLibraries, inputRules);

      JarClassLoaderBuilder classLoaderBuilder = new JarClassLoaderBuilder();
      classLoader = classLoaderBuilder.buildClassLoader(null);
    }

    //set globals
    Map<String, Object> globals = new LinkedHashMap<>();
    if (inputGlobals != null) {
      for (Entry<String, byte[]> inputGlobal : inputGlobals.entrySet()) {
        String id = inputGlobal.getKey();
        Object object = rulesComponent.deserializeObject(classLoader, inputGlobal.getValue());
        globals.put(id, object);
      }
      globals.entrySet().stream()
          .forEach(g -> commands.add(CommandFactory.newSetGlobal(g.getKey(), g.getValue())));
    }

    //set facts
    List<Object> facts = new ArrayList<>();
    try {
      for (byte[] inputFact : inputFacts) {
        Object object = rulesComponent.deserializeObject(classLoader, inputFact);
        facts.add(object);
      }
      facts.stream().forEach(f -> commands.add(CommandFactory.newInsert(f)));
    } catch (NullPointerException e) {
      log.severe("Cannot execute session without facts. At least one fact must be provided.");
      return null;
    }

    //set rules
    if ((ruleNameToExecute != null) && (knowledgeBaseId != null)) {
      AgendaFilter ruleNameFilter = new RuleNameEqualsAgendaFilter(ruleNameToExecute);
      commands.add(new FireAllRulesCommand(ruleNameFilter));
    }

    //execute
    StatelessKieSession statelessKieSession = kieBase.newStatelessKieSession();
    statelessKieSession.execute(CommandFactory.newBatchExecution(commands));

    //get global results
    return getResults(globals, facts);
  }

  /**
   * Execute the rules of an existing Knowledge Base.
   *
   * @param knowledgeBaseId the id of the Knowledge Base
   * @param rules a list containing the names of the rules that can be executed from the Knowledge
   * Base, while the others are ignored
   * @param inputGlobals the globals to be added in the session
   * @param inputFacts the objects that the rules will be executed against
   * @return the results of the execution
   */
  public ExecutionResultsDTO fireRules(String knowledgeBaseId, List<String> rules,
      Map<String, byte[]> inputGlobals,
      List<byte[]> inputFacts) {
    log.info("Firing rules of the Knowledge Base with id " + knowledgeBaseId);

    KnowledgeBase knowledgeBaseState = knowledgeBaseService
        .findKnowledgeBaseStateById(knowledgeBaseId);

    ClassLoaderKnowledgeBase clkb = rulesUtil.createKieBaseFromBaseState(knowledgeBaseState);

    KieSession kieSession = clkb.knowledgeBase.newKieSession();
    ClassLoader classLoader = clkb.classLoader;

    //set globals
    Map<String, Object> globals = new LinkedHashMap<>();
    if (inputGlobals != null) {
      for (Entry<String, byte[]> inputGlobal : inputGlobals.entrySet()) {
        globals.put(inputGlobal.getKey(),
            rulesComponent.deserializeObject(classLoader, inputGlobal.getValue()));
      }
      globals.entrySet().stream().forEach(g -> kieSession.setGlobal(g.getKey(), g.getValue()));
    }

    //set facts
    List<Object> facts = new ArrayList<>();
    if (inputFacts != null) {
      for (byte[] inputFact : inputFacts) {
        facts.add(rulesComponent.deserializeObject(classLoader, inputFact));
      }
      facts.stream().forEach(f -> kieSession.insert(f));
    }

    //set rules
    if (rules == null) {
      kieSession.fireAllRules();
    } else {
      AgendaFilter filter = match -> {
        String matchedRule = match.getRule().getName();
        return rules.contains(matchedRule);
      };
      kieSession.fireAllRules(filter);
    }

    kieSession.dispose();

    return getResults(globals, facts);
  }

  private ExecutionResultsDTO getResults(Map<String, Object> globals, List<Object> facts) {
    //get global results
    Map<String, byte[]> outputGlobals = new LinkedHashMap<>();
    for (Entry<String, Object> object : globals.entrySet()) {
      String id = object.getKey();
      byte[] bytes = rulesComponent.serializeObject(object.getValue());
      outputGlobals.put(id, bytes);
    }

    //get facts results
    List<byte[]> outputFacts = new ArrayList<>();
    for (Object object : facts) {
      byte[] bytes = rulesComponent.serializeObject(object);
      outputFacts.add(bytes);
    }

    return new ExecutionResultsDTO(outputGlobals, outputFacts);
  }
}
