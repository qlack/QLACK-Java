package com.eurodyn.qlack.fuse.rules.service;

import com.eurodyn.qlack.common.exception.QDoesNotExistException;
import com.eurodyn.qlack.fuse.rules.mapper.DmnModelMapper;
import com.eurodyn.qlack.fuse.rules.model.DmnModel;
import com.eurodyn.qlack.fuse.rules.repository.DmnModelRepository;
import org.camunda.bpm.dmn.engine.DmnDecisionResult;
import org.camunda.bpm.dmn.engine.DmnEngine;
import org.camunda.bpm.dmn.engine.DmnEngineConfiguration;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.engine.variable.Variables;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.InputStream;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DmnModelServiceTest {

    public static final String DECISION_ID = "decisionId";
    @InjectMocks
    private DmnModelService dmnModelService;

    @Mock
    private DmnModelMapper dmnModelMapper;
    @Mock
    private DmnModelRepository dmnModelRepository;
    @Mock
    private DmnModel dmnModel;
    private DmnEngine dmnEngine;
    private InputStream inputStream;
    private VariableMap variables;

    @Test
    public void findDmnModelByIdTest() {
        when(dmnModelRepository.fetchById(DECISION_ID)).thenReturn(dmnModel);
        DmnModel model = dmnModelService.findDmnModelById(DECISION_ID);

        assertNotNull(model);
        verify(dmnModelRepository, times(1)).fetchById(DECISION_ID);
    }

    @Test(expected = QDoesNotExistException.class)
    public void findDmnModelByIdNullTest() {
        DmnModel model = dmnModelService.findDmnModelById(DECISION_ID);
        assertNull(model);
    }

    @Test
    public void getAllDmnModelsTest() {
        dmnModelService.getAllDmnModels();
        verify(dmnModelRepository, times(1)).findAll();
    }

    @Test
    public void createDmnEngineTest() {
        DmnEngine engine = dmnModelService.createDmnEngine();
        assertNotNull(engine);
    }

    @Test
    public void evaluateDecisionTest() {
        modelInstanceInit();
        DmnDecisionResult result =
                dmnModelService.evaluateDecision(inputStream, dmnEngine, variables);
        assertNotNull(result);
    }

    private void modelInstanceInit() {
        DmnEngineConfiguration dmnEngineConfiguration =
                DmnEngineConfiguration.createDefaultDmnEngineConfiguration();
        dmnEngine = dmnEngineConfiguration.buildEngine();

        String fileName = "dish-decision.dmn11.xml";
        inputStream = this.getClass().getClassLoader().getResourceAsStream(fileName);

        variables = Variables.createVariables()
                .putValue("season", "Spring")
                .putValue("guestCount", 14);
    }

}
