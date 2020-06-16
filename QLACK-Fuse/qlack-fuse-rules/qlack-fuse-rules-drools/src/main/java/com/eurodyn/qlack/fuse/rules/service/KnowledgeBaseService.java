package com.eurodyn.qlack.fuse.rules.service;

import com.eurodyn.qlack.common.exception.QDoesNotExistException;
import com.eurodyn.qlack.fuse.rules.component.RulesComponent;
import com.eurodyn.qlack.fuse.rules.dto.ExecutionResultsDTO;
import com.eurodyn.qlack.fuse.rules.dto.KnowledgeBaseDTO;
import com.eurodyn.qlack.fuse.rules.mapper.KnowledgeBaseMapper;
import com.eurodyn.qlack.fuse.rules.model.KnowledgeBase;
import com.eurodyn.qlack.fuse.rules.model.KnowledgeBaseLibrary;
import com.eurodyn.qlack.fuse.rules.model.KnowledgeBaseRule;
import com.eurodyn.qlack.fuse.rules.repository.KnowledgeBaseRepository;
import com.eurodyn.qlack.fuse.rules.util.RulesUtil;
import com.eurodyn.qlack.fuse.rules.util.classloader.ClassLoaderKnowledgeBase;
import com.eurodyn.qlack.fuse.rules.util.classloader.JarClassLoaderBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.drools.core.base.RuleNameEqualsAgendaFilter;
import org.drools.core.command.runtime.rule.FireAllRulesCommand;
import org.kie.api.KieBase;
import org.kie.api.command.Command;
import org.kie.api.runtime.StatelessKieSession;
import org.kie.api.runtime.rule.AgendaFilter;
import org.kie.internal.command.CommandFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

/**
 * This service provides methods related to the KnowledgeBase,
 * KnowledgeBaseLibrary and KnowledgeBaseRule objects.
 *
 * @author European Dynamics SA
 */
@Service
@Transactional
@Log
@RequiredArgsConstructor
public class KnowledgeBaseService implements RuleService<KnowledgeBaseDTO> {

    private final RulesUtil rulesUtil = new RulesUtil();
    private final KnowledgeBaseMapper knowledgeBaseMapper;
    private final KnowledgeBaseRepository knowledgeBaseRepository;
    private final RulesComponent rulesComponent;

    /**
     * Finds the persisted Knowledge Base State based on its id.
     *
     * @param knowledgeBaseId the id of the persisted Knowledge Base
     * @return the persisted Knowledge Base
     */
    public KnowledgeBase findById(String knowledgeBaseId) {
        log.info("Retrieving Knowledge Base with id " + knowledgeBaseId);

        KnowledgeBase base = knowledgeBaseRepository.fetchById(knowledgeBaseId);

        if (base == null) {
            String errorMessage =
                    "Cannot find Knowledge Base with id " + knowledgeBaseId;

            log.severe(errorMessage);
            throw new QDoesNotExistException(errorMessage);
        }

        return base;
    }

    /**
     * Creates a new Knowledge Base and its linked Knowledge Base Libraries with
     * given libraries and rules.
     *
     * @param libraries the libraries of the base
     * @param rules     the rules of the base
     * @return the id of the newly created Knowledge Base
     */
    public String createKnowledgeBase(List<byte[]> libraries, List<String> rules) {
        log.info(
                "Creating new Knowledge Base with " + libraries.size() + " libraries and "
                        + rules.size()
                        + " rules.");

        KieBase kieBase = rulesUtil.createKieBase(libraries, rules);

        KnowledgeBase knowledgeBase = new KnowledgeBase();
        knowledgeBase.setState(rulesUtil.serializeKnowledgeBase(kieBase));

        List<KnowledgeBaseLibrary> knowledgeBaseLibraries = new ArrayList<>();
        for (byte[] library : libraries) {
            KnowledgeBaseLibrary knowledgeBaseLibrary = new KnowledgeBaseLibrary();
            knowledgeBaseLibrary.setLibrary(library);

            knowledgeBaseLibrary.setBase(knowledgeBase);
            knowledgeBaseLibraries.add(knowledgeBaseLibrary);
        }
        knowledgeBase.setLibraries(knowledgeBaseLibraries);

        List<KnowledgeBaseRule> knowledgeBaseRules = new ArrayList<>();
        for (String rule : rules) {
            KnowledgeBaseRule knowledgeBaseRule = new KnowledgeBaseRule();
            knowledgeBaseRule.setRule(rule);

            knowledgeBaseRule.setBase(knowledgeBase);
            knowledgeBaseRules.add(knowledgeBaseRule);
        }
        knowledgeBase.setRules(knowledgeBaseRules);

        knowledgeBase = knowledgeBaseRepository.save(knowledgeBase);

        return knowledgeBase.getId();
    }

    /**
     * Retrieves all the Knowledge Base entries.
     *
     * @return a list containing all the found Knowledge Base
     */
    public List<KnowledgeBaseDTO> getAll() {
        log.log(Level.INFO, "Retrieving all Knowledge Base entries.");

        List<KnowledgeBase> knowledgeBases = knowledgeBaseRepository.findAll();

        return knowledgeBaseMapper.mapToDTO(knowledgeBases);
    }

    /**
     * Execute rules directly using a stateless session. The rules and the
     * libraries can be retrieved from an existing Knowledge Base or can be
     * given as parameter.
     *
     * @param resourceId     the id of the Knowledge Base
     * @param inputLibraries the libraries to be used
     * @param rules          the rules to be executed
     * @param inputGlobals   the globals to be added in the session
     * @param inputs         the objects that the rules will be executed against
     * @param toBeExecuted   the name of a specific rule that can be executed
     *                       from the Knowledge Base, while the others are ignored
     * @return the results of the execution
     */
    public ExecutionResultsDTO executeRules(String resourceId, List<byte[]> inputLibraries,
                                            List<String> rules, Map<String, byte[]> inputGlobals,
                                            List<byte[]> inputs, String toBeExecuted) {
        return fireRules(resourceId, inputLibraries, rules, inputGlobals, inputs, toBeExecuted);
    }

    /**
     * Execute rules directly using a stateless session. The rules and the
     * libraries are retrieved from an existing Knowledge Base. Neither the libraries
     * nor the input goals can be given as parameters.
     *
     * @param resourceId   the id of the Knowledge Base
     * @param rules        the rules to be executed
     * @param inputs       the objects that the rules will be executed against
     * @param toBeExecuted the name of a specific rule that can be executed
     *                     from the Knowledge Base, while the others are ignored
     * @return the results of the execution
     */
    public ExecutionResultsDTO executeRules(String resourceId, List<String> rules,
                                            List<byte[]> inputs, String toBeExecuted) {
        return fireRules(resourceId, null, rules, null, inputs, toBeExecuted);
    }

    private ExecutionResultsDTO getResults(Map<String, Object> globals, List<Object> facts) {
        //get global results
        Map<String, byte[]> outputGlobals = new LinkedHashMap<>();
        for (Map.Entry<String, Object> object : globals.entrySet()) {
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

    private ExecutionResultsDTO fireRules(String resourceId, List<byte[]> inputLibraries,
                                          List<String> inputRules, Map<String, byte[]> inputGlobals,
                                          List<byte[]> inputs, String toBeExecuted) {
        log.info("Creating Stateless Knowledge Session using the Knowledge Base with id " + resourceId);

        KieBase kieBase;
        ClassLoader classLoader;
        List<Command<?>> commands = new ArrayList<>();

        if (resourceId != null) {
            KnowledgeBase knowledgeBaseState = findById(resourceId);
            ClassLoaderKnowledgeBase clkb = rulesUtil
                    .createKieBaseFromBaseState(knowledgeBaseState);
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
            for (Map.Entry<String, byte[]> inputGlobal : inputGlobals.entrySet()) {
                String id = inputGlobal.getKey();
                Object object = rulesComponent
                        .deserializeObject(classLoader, inputGlobal.getValue());
                globals.put(id, object);
            }
            globals.forEach((key, value) -> commands
                    .add(CommandFactory.newSetGlobal(key, value)));
        }

        //set facts
        List<Object> facts = new ArrayList<>();
        try {
            for (byte[] inputFact : inputs) {
                Object object = rulesComponent
                        .deserializeObject(classLoader, inputFact);
                facts.add(object);
            }
            facts.forEach(f -> commands.add(CommandFactory.newInsert(f)));
        } catch (NullPointerException e) {
            log.severe(
                    "Cannot execute session without facts. At least one fact must be provided.");
            return null;
        }

        //set rules
        if ((toBeExecuted != null) && (resourceId != null)) {
            AgendaFilter ruleNameFilter = new RuleNameEqualsAgendaFilter(
                    toBeExecuted);
            commands.add(new FireAllRulesCommand(ruleNameFilter));
        }

        //execute
        StatelessKieSession statelessKieSession = kieBase.newStatelessKieSession();
        statelessKieSession.execute(CommandFactory.newBatchExecution(commands));

        //get global results
        return getResults(globals, facts);
    }

}
