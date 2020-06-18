package com.eurodyn.qlack.fuse.rules.service;

import com.eurodyn.qlack.common.exception.QDoesNotExistException;
import com.eurodyn.qlack.fuse.rules.dto.ExecutionResultsDTO;
import com.eurodyn.qlack.fuse.rules.exception.QRulesException;
import com.eurodyn.qlack.fuse.rules.mapper.DmnModelMapper;
import com.eurodyn.qlack.fuse.rules.model.DmnModel;
import com.eurodyn.qlack.fuse.rules.repository.DmnModelRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    List<Map<String, Object>> inputs = new ArrayList<>();

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
        Map<String, Object> inputGlobals = new HashMap<>();
        List<Map<String, Object>> facts = new ArrayList<>();

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

    @Test(expected = QRulesException.class)
    public void executeRulesFileNotFoundTest() {
        modelInstanceInit();

        List<Map<String, Object>> result =
                dmnModelService.executeRules("src/" + "notExistingFile.txt", null, inputs, "decision");
        assertNotNull(result);
    }

    @Test(expected = QRulesException.class)
    public void executeRulesWrongInputsTest() {
        Map<String, Object> map = new HashMap<>();
        map.put("Wrong key", "Wrong value");
        inputs.add(map);

        List<Map<String, Object>> result =
                dmnModelService.executeRules("src/test/resources/" + FILE_NAME, null, inputs, "decision");
        assertNotNull(result);
    }


    @Test
    public void executeRulesWithStringArrayTest() {
        modelInstanceInit();
        List<String> rules = new ArrayList<>();
        try (Stream<String> lines = Files.lines(Paths.get("src/test/resources/" + FILE_NAME))) {
            rules = lines.collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<Map<String, Object>> result =
                dmnModelService.executeRules(null, rules, inputs, "decision");
        assertNotNull(result);
    }


    private void modelInstanceInit() {
        Map<String, Object> map1 = new HashMap<>();
        Map<String, Object> map2 = new HashMap<>();

        map1.put("season", "Spring");
        map1.put("guestCount", 4);
        map2.put("season", "Fall");
        map2.put("guestCount", 2);

        inputs.add(map1);
        inputs.add(map2);
    }

}
