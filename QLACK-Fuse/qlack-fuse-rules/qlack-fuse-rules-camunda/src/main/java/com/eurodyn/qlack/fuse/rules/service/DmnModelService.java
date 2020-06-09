package com.eurodyn.qlack.fuse.rules.service;

import com.eurodyn.qlack.common.exception.QDoesNotExistException;
import com.eurodyn.qlack.fuse.rules.dto.DmnModelDTO;
import com.eurodyn.qlack.fuse.rules.mapper.DmnModelMapper;
import com.eurodyn.qlack.fuse.rules.model.DmnModel;
import com.eurodyn.qlack.fuse.rules.repository.DmnModelRepository;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.camunda.bpm.dmn.engine.DmnDecision;
import org.camunda.bpm.dmn.engine.DmnDecisionResult;
import org.camunda.bpm.dmn.engine.DmnEngine;
import org.camunda.bpm.dmn.engine.DmnEngineConfiguration;
import org.camunda.bpm.engine.variable.VariableMap;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.List;

/**
 * This service provides methods related to the DmnModel object.
 *
 * @author European Dynamics SA
 */
@Service
@Transactional
@Log
@AllArgsConstructor
public class DmnModelService implements RuleService {

    private final DmnModelMapper dmnModelMapper;
    private final DmnModelRepository dmnModelRepository;

    /**
     * Creates a new DMN engine
     *
     * @return the newly created DMN engine
     */
    public DmnEngine createDmnEngine() {
        // create, build and return a new DMN engine with default configuration
        return DmnEngineConfiguration
                .createDefaultDmnEngineConfiguration()
                .buildEngine();
    }

    /**
     * Finds the DMN model based on its id.
     *
     * @param dmnModelId the id of the DMN model
     * @return the DMN model
     */
    public DmnModel findDmnModelById(String dmnModelId) {
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
    public List<DmnModelDTO> getAllDmnModels() {
        log.info("Retrieving all DMN models");

        List<DmnModel> dmnModelList = dmnModelRepository.findAll();
        return dmnModelMapper.mapToDTO(dmnModelList);
    }

    /**
     * Evaluates a DMN decision using a set of input variables
     *
     * @param inputStream the DMN XML file as input stream
     * @param dmnEngine   the DMN engine
     * @param variables   the input variables
     * @return the result of the evaluation
     */
    public DmnDecisionResult evaluateDecision(InputStream inputStream, DmnEngine dmnEngine, VariableMap variables) {
        log.info("Evaluating decision!");

        DmnDecision decision = dmnEngine.parseDecisions(inputStream).get(0);
        return dmnEngine.evaluateDecision(decision, variables);
    }


}
