package com.eurodyn.qlack.fuse.rules.service;

import com.eurodyn.qlack.common.exception.QDoesNotExistException;
import com.eurodyn.qlack.fuse.rules.dto.DmnModelDTO;
import com.eurodyn.qlack.fuse.rules.exception.QRulesException;
import com.eurodyn.qlack.fuse.rules.mapper.DmnModelMapper;
import com.eurodyn.qlack.fuse.rules.model.DmnModel;
import com.eurodyn.qlack.fuse.rules.repository.DmnModelRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.camunda.bpm.dmn.engine.DmnDecision;
import org.camunda.bpm.dmn.engine.DmnDecisionTableResult;
import org.camunda.bpm.dmn.engine.DmnEngine;
import org.camunda.bpm.dmn.engine.DmnEngineConfiguration;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.model.dmn.Dmn;
import org.camunda.bpm.model.dmn.DmnModelInstance;
import org.camunda.commons.utils.IoUtil;
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
@RequiredArgsConstructor
public class DmnModelService {

    public static final String FILE_NOT_FOUND = "File not found! Please make sure the name and the path are correct.";
    private final DmnModelMapper dmnModelMapper;
    private final DmnModelRepository dmnModelRepository;
    private final DmnEngine dmnEngine = DmnEngineConfiguration.createDefaultDmnEngineConfiguration().buildEngine();

    /**
     * Creates a new DMN model
     *
     * @param modelPath the pathname of the DMN xml file
     * @return the id of the newly created DMN model
     */
    public String createDmnModel(String modelPath) {
        DmnModel dmnModel = new DmnModel();
        File file = new File(modelPath);

        if (file.exists() && !file.isDirectory()) {
            DmnModelInstance dmnModelInstance = Dmn.readModelFromFile(file);
            dmnModel.setModelInstance(Dmn.convertToString(dmnModelInstance));
            dmnModel = dmnModelRepository.save(dmnModel);
            return dmnModel.getId();
        } else {
            throw new QRulesException(FILE_NOT_FOUND);
        }
    }

    /**
     * Creates a new DMN model
     *
     * @param inputStream the input stream containing the DMN xml file
     * @return the id of the newly created DMN model
     */
    public String createDmnModel(InputStream inputStream) {
        DmnModel dmnModel = new DmnModel();

        dmnModel.setModelInstance(IoUtil.inputStreamAsString(inputStream));
        dmnModel = dmnModelRepository.save(dmnModel);
        return dmnModel.getId();
    }

    /**
     * Deletes a DMN model based on its Id
     *
     * @param modelId the Id of the DMN model
     */
    public void deleteDmnModel(String modelId) {
        if (dmnModelRepository.existsById(modelId)) {
            dmnModelRepository.deleteById(modelId);
        } else {
            throw new QRulesException("DMN model not found");
        }
    }

    /**
     * Finds the DMN model based on its id.
     *
     * @param dmnModelId the id of the DMN model
     * @return the DMN model
     */
    public DmnModelDTO findById(String dmnModelId) {
        log.info("Retrieving DMN model with ID: " + dmnModelId);

        DmnModel dmnModel = dmnModelRepository.fetchById(dmnModelId);

        if (dmnModel == null) {
            String errorMessage = "Cannot find DMN model with ID: " + dmnModelId;
            log.severe(errorMessage);
            throw new QDoesNotExistException(errorMessage);
        }

        return dmnModelMapper.mapToDTO(dmnModel);
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
     * Evaluates a DMN decision using a set of input variables. The DMN xml file is passed as a list of Strings.
     *
     * @param dmnFile      the DMN xml file as a list of Strings
     * @param inputs       the map of inputs to be executed
     * @param toBeExecuted the decision Id of the DMN xml file that will be parsed
     * @return the result of the evaluation
     */
    public List<Map<String, Object>> executeRules(List<String> dmnFile, List<Map<String, Object>> inputs, String toBeExecuted) {
        // create input stream from the given list of Strings
        String xmlString = String.join("", dmnFile);
        InputStream inputStream = new ByteArrayInputStream(xmlString.getBytes());

        return fireRules(inputStream, inputs, toBeExecuted);
    }

    /**
     * Evaluates a DMN decision using a set of input variables. The DMN xml file is passed as an InputStream.
     *
     * @param dmnFile      the DMN xml file as an InputStream
     * @param inputs       the map of inputs to be executed
     * @param toBeExecuted the decision Id of the DMN xml file that will be parsed
     * @return the result of the evaluation
     */
    public List<Map<String, Object>> executeRules(InputStream dmnFile, List<Map<String, Object>> inputs, String toBeExecuted) {
        return fireRules(dmnFile, inputs, toBeExecuted);
    }

    /**
     * Evaluates a DMN decision using a set of input variables.
     *
     * @param modelPath    the pathname of the DMN xml file
     * @param inputs       the map of inputs to be executed
     * @param toBeExecuted the decision Id of the DMN xml file that will be parsed
     * @return the result of the evaluation
     */
    public List<Map<String, Object>> executeRules(String modelPath, List<Map<String, Object>> inputs, String toBeExecuted) {
        // create input stream from resource file
        File dmnFile = new File(modelPath);
        try {
            return fireRules(new FileInputStream(dmnFile), inputs, toBeExecuted);
        } catch (FileNotFoundException e) {
            log.severe(e.getMessage());
            throw new QRulesException(FILE_NOT_FOUND);
        }
    }

    private List<Map<String, Object>> fireRules(InputStream inputStream, List<Map<String, Object>> inputs, String toBeExecuted) {
        log.info("Evaluating decision!");

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
}
