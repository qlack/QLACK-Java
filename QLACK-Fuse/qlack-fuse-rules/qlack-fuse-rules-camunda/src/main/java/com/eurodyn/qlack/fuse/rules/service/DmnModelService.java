package com.eurodyn.qlack.fuse.rules.service;

import com.eurodyn.qlack.common.exception.QDoesNotExistException;
import com.eurodyn.qlack.fuse.rules.dto.DmnModelDTO;
import com.eurodyn.qlack.fuse.rules.dto.ExecutionResultsDTO;
import com.eurodyn.qlack.fuse.rules.exception.QRulesException;
import com.eurodyn.qlack.fuse.rules.mapper.DmnModelMapper;
import com.eurodyn.qlack.fuse.rules.model.DmnModel;
import com.eurodyn.qlack.fuse.rules.repository.DmnModelRepository;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.apache.logging.log4j.util.Strings;
import org.camunda.bpm.dmn.engine.DmnDecision;
import org.camunda.bpm.dmn.engine.DmnDecisionTableResult;
import org.camunda.bpm.dmn.engine.DmnEngine;
import org.camunda.bpm.dmn.engine.DmnEngineConfiguration;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.engine.variable.Variables;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This service provides methods related to the DmnModel object.
 *
 * @author European Dynamics SA
 */
@Service
@Transactional
@Log
@AllArgsConstructor
public class DmnModelService implements RuleService<DmnModelDTO> {

    private final DmnModelMapper dmnModelMapper;
    private final DmnModelRepository dmnModelRepository;
    private final DmnEngine dmnEngine = DmnEngineConfiguration.createDefaultDmnEngineConfiguration().buildEngine();

    /**
     * Finds the DMN model based on its id.
     *
     * @param dmnModelId the id of the DMN model
     * @return the DMN model
     */
    public DmnModel findById(String dmnModelId) {
        log.info("Retrieving DMN model with ID: " + dmnModelId);

        DmnModel dmnModel = dmnModelRepository.fetchById(dmnModelId);

        if (dmnModel == null) {
            String errorMessage = "Cannot find DMN model with ID: " + dmnModelId;
            log.severe(errorMessage);
            throw new QDoesNotExistException(errorMessage);
        }

        return dmnModel;
    }

    /**
     * Retrieves all the DMN model entries.
     *
     * @return a list containing all the found DMN models
     */
    public List<DmnModelDTO> getAll() {
        log.info("Retrieving all DMN models");

        List<DmnModel> dmnModelList = dmnModelRepository.findAll();
        return dmnModelMapper.mapToDTO(dmnModelList);
    }

    /**
     * Evaluates a DMN decision using a set of input variables
     *
     * @param resourceId   the pathname of the DMN xml file
     * @param rules        the DMN xml file as a list of Strings
     * @param inputs       the map of inputs to be executed
     * @param toBeExecuted the decision Id of the DMN xml file that will be parsed
     * @return the result of the evaluation
     */
    public List<Map<String, Object>> executeRules(String resourceId, List<String> rules, List<Map<String, Object>> inputs, String toBeExecuted) {
        log.info("Evaluating decision!");

        InputStream inputStream;
        if (!Strings.isEmpty(resourceId)) {
            try {
                // create input stream from resource file
                File initialFile = new File(resourceId);
                inputStream = new FileInputStream(initialFile);
            } catch (FileNotFoundException e) {
                log.severe(e.getMessage());
                throw new QRulesException("File not found! Please make sure the name and the path are correct.");
            }
        } else {
            // create input stream from the given list of Strings
            String xmlString = String.join("", rules);
            inputStream = new ByteArrayInputStream(xmlString.getBytes());
        }

        // create a result list to be returned
        List<Map<String, Object>> resultList = new ArrayList<>();

        // create the decision to be evaluated
        DmnDecision decision = dmnEngine.parseDecision(toBeExecuted, inputStream);

        inputs.forEach(map -> {
            // create variables
            VariableMap variables = Variables.createVariables();

            for (Map.Entry<String, Object> entry : map.entrySet()) {
                // prepare variables for decision evaluation
                variables.putValue(entry.getKey(), entry.getValue());
            }

            try {
                // evaluate decision
                DmnDecisionTableResult result = dmnEngine.evaluateDecisionTable(decision, variables);
                resultList.addAll(result.getResultList());
            } catch (Exception e) {
                throw new QRulesException(e);
            }
        });

        return resultList;
    }

    /**
     * This method is only supported in the Drools implementation. To execute rules with Camunda please use the overloaded
     * method executeRules(String resourceId, String inputXml, byte[] inputs, String toBeExecuted)
     */
    public ExecutionResultsDTO executeRules(String resourceId, List<byte[]> inputLibraries,
                                            List<String> rules, Map<String, Object> inputGlobals,
                                            List<Map<String, Object>> inputs, String toBeExecuted) {
        throw new UnsupportedOperationException("This method is not supported by the Camunda implementation!" +
                " Please use the overloaded method.");
    }


}
