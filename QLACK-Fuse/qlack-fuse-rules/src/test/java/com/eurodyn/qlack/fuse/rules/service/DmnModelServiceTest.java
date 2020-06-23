package com.eurodyn.qlack.fuse.rules.service;

import com.eurodyn.qlack.common.exception.QDoesNotExistException;
import com.eurodyn.qlack.fuse.rules.dto.DmnModelDTO;
import com.eurodyn.qlack.fuse.rules.exception.QRulesException;
import com.eurodyn.qlack.fuse.rules.mapper.DmnModelMapper;
import com.eurodyn.qlack.fuse.rules.model.DmnModel;
import com.eurodyn.qlack.fuse.rules.repository.DmnModelRepository;
import lombok.extern.java.Log;
import org.camunda.bpm.model.dmn.Dmn;
import org.camunda.bpm.model.dmn.DmnModelInstance;
import org.camunda.commons.utils.IoUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@Log
public class DmnModelServiceTest {

    private static final String DECISION_ID = "decisionId";
    private static final String TO_BE_EXECUTED = "decision";
    private static final String FILE_PATHNAME = "src/test/resources/camunda.xml";
    private final List<Map<String, Object>> inputs = new ArrayList<>();

    @InjectMocks
    private DmnModelService dmnModelService;
    @Mock
    private DmnModelMapper dmnModelMapper;
    @Mock
    private DmnModelRepository dmnModelRepository;
    @Mock
    private DmnModel dmnModel;

    @Test
    public void createDmnModelByPathNameTest() {
        String id = UUID.randomUUID().toString();
        File file = new File(FILE_PATHNAME);
        DmnModelInstance modelInstance = Dmn.readModelFromFile(file);

        dmnModel = new DmnModel();
        dmnModel.setId(id);
        dmnModel.setModelInstance(Dmn.convertToString(modelInstance));

        when(dmnModelRepository.save(any(DmnModel.class))).thenReturn(dmnModel);

        String dmnModelId = dmnModelService.createDmnModel(FILE_PATHNAME);
        assertEquals(id, dmnModelId);
        verify(dmnModelRepository, times(1)).save(any(DmnModel.class));
    }

    @Test(expected = QRulesException.class)
    public void createDmnModelFileNotFoundTest() {
        String dmnModelId = dmnModelService.createDmnModel("src/" + "notExistingFile.txt");
        assertNull(dmnModelId);
    }

    @Test
    public void createDmnModelByInputStreamTest() {
        String id = UUID.randomUUID().toString();
        File file = new File(FILE_PATHNAME);

        dmnModel = new DmnModel();
        dmnModel.setId(id);
        dmnModel.setModelInstance(IoUtil.inputStreamAsString(IoUtil.fileAsStream(file)));

        when(dmnModelRepository.save(any())).thenReturn(dmnModel);

        String dmnModelId = dmnModelService.createDmnModel(IoUtil.fileAsStream(file));
        assertEquals(id, dmnModelId);
        verify(dmnModelRepository, times(1)).save(any());
    }

    @Test
    public void deleteModelIdTest() {
        String id = UUID.randomUUID().toString();
        when(dmnModelRepository.existsById(id)).thenReturn(true);
        dmnModelService.deleteDmnModel(id);
        verify(dmnModelRepository, times(1)).deleteById(id);
    }

    @Test(expected = QRulesException.class)
    public void deleteModelIdNonExistTest() {
        String id = UUID.randomUUID().toString();
        dmnModelService.deleteDmnModel(id);
        verify(dmnModelRepository, times(1)).deleteById(id);
    }

    @Test
    public void findByIdTest() {
        DmnModelDTO dmnModelDTO = new DmnModelDTO();
        dmnModelDTO.setId("testId");
        dmnModelDTO.setModelInstance("testInstance");

        when(dmnModelRepository.fetchById(DECISION_ID)).thenReturn(dmnModel);
        when(dmnModelMapper.mapToDTO(any(DmnModel.class))).thenReturn(dmnModelDTO);
        DmnModelDTO modelDTO = dmnModelService.findById(DECISION_ID);

        assertNotNull(modelDTO);
        verify(dmnModelRepository, times(1)).fetchById(DECISION_ID);
    }

    @Test(expected = QDoesNotExistException.class)
    public void findByIdNullTest() {
        DmnModelDTO modelDTO = dmnModelService.findById(DECISION_ID);
        assertNull(modelDTO);
    }

    @Test
    public void getAllTest() {
        dmnModelService.getAll();
        verify(dmnModelRepository, times(1)).findAll();
    }

    @Test
    public void executeRulesWithPathnameTest() {
        modelInstanceInit();

        List<Map<String, Object>> result =
                dmnModelService.executeRules(FILE_PATHNAME, inputs, TO_BE_EXECUTED);
        assertNotNull(result);
    }

    @Test(expected = QRulesException.class)
    public void executeRulesFileNotFoundTest() {
        modelInstanceInit();

        List<Map<String, Object>> result =
                dmnModelService.executeRules("src/" + "notExistingFile.txt", inputs, TO_BE_EXECUTED);
        assertNotNull(result);
    }

    @Test(expected = QRulesException.class)
    public void executeRulesWrongInputsTest() {
        Map<String, Object> map = new HashMap<>();
        map.put("Wrong key", "Wrong value");
        inputs.add(map);

        List<Map<String, Object>> result =
                dmnModelService.executeRules(FILE_PATHNAME, inputs, TO_BE_EXECUTED);
        assertNotNull(result);
    }


    @Test
    public void executeRulesWithStringArrayTest() {
        modelInstanceInit();
        List<String> rules = new ArrayList<>();
        try (Stream<String> lines = Files.lines(Paths.get(FILE_PATHNAME))) {
            rules = lines.collect(Collectors.toList());
        } catch (IOException e) {
            log.severe(e.getMessage());
        }

        List<Map<String, Object>> result =
                dmnModelService.executeRules(rules, inputs, TO_BE_EXECUTED);
        assertNotNull(result);
    }

    @Test
    public void executeRulesWithInputStreamTest() throws FileNotFoundException {
        modelInstanceInit();
        File file = new File(FILE_PATHNAME);
        InputStream inputStream = new FileInputStream(file);

        List<Map<String, Object>> result =
                dmnModelService.executeRules(inputStream, inputs, TO_BE_EXECUTED);
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
