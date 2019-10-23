package com.eurodyn.qlack.fuse.aaa.mapper;

import com.eurodyn.qlack.fuse.aaa.InitTestValues;
import com.eurodyn.qlack.fuse.aaa.dto.OperationDTO;
import com.eurodyn.qlack.fuse.aaa.model.Operation;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class OperationImpTest {

    @InjectMocks
    private OperationMapperImpl operationMapper;

    private InitTestValues initTestValues;

    private Operation operation;

    private OperationDTO operationDTO;

    @Before
    public void init(){
        initTestValues = new InitTestValues();
        operation = initTestValues.createOperation();
        operationDTO = initTestValues.createOperationDTO();
    }

    @Test
    public void mapToDTONullTest(){
        OperationDTO operationDTO = operationMapper.mapToDTO((Operation) null);
        assertEquals(null,operationDTO);
    }

    @Test
    public void mapToDTOListNullTest() {
        List<OperationDTO> operationDTOList = operationMapper.mapToDTO((List<Operation>) null);
        assertEquals(null, operationDTOList);
    }

    @Test
    public void mapToDTOListTest() {
        List<Operation> operationList = new ArrayList<>();
        operationList.add(initTestValues.createOperation());
        List<OperationDTO> operationDTOList = operationMapper.mapToDTO(operationList);
        assertEquals(operationList.size(), operationDTOList.size());
    }

    @Test
    public void mapToEntityNullTest() {
        Operation operation = operationMapper.mapToEntity((OperationDTO) null);
        assertEquals(null, operation);
    }


    @Test
    public void mapToEntityListTest() {
        List<OperationDTO> operationDTOList = new ArrayList<>();
        operationDTOList.add(initTestValues.createOperationDTO());
        List<Operation> operationList = operationMapper.mapToEntity(operationDTOList);
        assertEquals(operationDTOList.size(), operationList.size());
    }

    @Test
    public void mapToEntityListNullTest() {
        assertEquals(null, operationMapper.mapToEntity((List<OperationDTO>) null));
        List<Operation> operationList = operationMapper.mapToEntity((List<OperationDTO>) null);
        assertEquals(null, operationList);

    }


    @Test
    public void mapToExistingEntitySetDTOTest(){
        OperationDTO operationDTO = initTestValues.createOperationDTO();
        operationDTO.setDescription(null);
        Operation operation = initTestValues.createOperation();
        operation.setDescription(null);
        operationMapper.mapToExistingEntity(operationDTO,operation);
    }

    @Test
    public void mapToExistingEntitySetDTONullTest(){
        OperationDTO operationDTO = initTestValues.createOperationDTO();
        operationDTO.setDescription(null);
        operationMapper.mapToExistingEntity(null,operation);
        assertEquals(null,operationDTO.getDescription());
    }
}
