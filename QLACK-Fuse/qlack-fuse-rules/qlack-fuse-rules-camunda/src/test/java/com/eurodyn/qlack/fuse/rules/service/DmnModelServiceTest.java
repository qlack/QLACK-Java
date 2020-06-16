package com.eurodyn.qlack.fuse.rules.service;

import com.eurodyn.qlack.common.exception.QDoesNotExistException;
import com.eurodyn.qlack.fuse.rules.component.CamundaComponent;
import com.eurodyn.qlack.fuse.rules.dto.ExecutionResultsDTO;
import com.eurodyn.qlack.fuse.rules.mapper.DmnModelMapper;
import com.eurodyn.qlack.fuse.rules.model.DmnModel;
import com.eurodyn.qlack.fuse.rules.repository.DmnModelRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private static final String FILE_NAME = "camunda.xml";
    List<Map<String, Object>> mapList = new ArrayList<>();
    private List<byte[]> inputs;

    @Test
    public void findByIdTest() {
        when(dmnModelRepository.fetchById(DECISION_ID)).thenReturn(dmnModel);
        DmnModel model = dmnModelService.findById(DECISION_ID);

        assertNotNull(model);
        verify(dmnModelRepository, times(1)).fetchById(DECISION_ID);
    }

    @Test(expected = QDoesNotExistException.class)
    public void findByIdNullTest() {
        DmnModel model = dmnModelService.findById(DECISION_ID);
        assertNull(model);
    }

    @Test
    public void getAllTest() {
        dmnModelService.getAll();
        verify(dmnModelRepository, times(1)).findAll();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void executeRulesExceptionTest() {
        List<byte[]> inputLibraries = new ArrayList<>();
        List<String> inputRules = new ArrayList<>();
        Map<String, byte[]> inputGlobals = new HashMap<>();
        List<byte[]> facts = new ArrayList<>();

        ExecutionResultsDTO executionResultsDTO =
                dmnModelService.executeRules("", inputLibraries, inputRules, inputGlobals, facts, "");
        assertNull(executionResultsDTO);
    }

    @Test
    public void executeRulesTest() {
        modelInstanceInit();

        List<Map<String, Object>> result =
                dmnModelService.executeRules("src/test/resources/" + FILE_NAME, null, inputs, "decision");
        assertNotNull(result);
    }

    private void modelInstanceInit() {
        Map<String, Object> map1 = new HashMap<>();
        Map<String, Object> map2 = new HashMap<>();
        map1.put("season", "Spring");
        map1.put("guestCount", 4);
        map2.put("season", "Fall");
        map2.put("guestCount", 2);
        mapList.add(map1);
        mapList.add(map2);

        inputs = CamundaComponent.serializeMap(mapList);
    }

}
