package com.eurodyn.qlack.fuse.aaa.mapper;

import static org.junit.jupiter.api.Assertions.*;

import com.eurodyn.qlack.fuse.aaa.InitTestValues;
import com.eurodyn.qlack.fuse.aaa.dto.OperationDTO;
import com.eurodyn.qlack.fuse.aaa.model.Operation;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class OperationImpTest {

  @InjectMocks
  private OperationMapperImpl operationMapper;

  private InitTestValues initTestValues;

  private Operation operation;

  private OperationDTO operationDTO;

  @BeforeEach
  public void init() {
    initTestValues = new InitTestValues();
    operation = initTestValues.createOperation();
    operationDTO = initTestValues.createOperationDTO();
  }

  @Test
  public void mapToDTONullTest() {
    OperationDTO operationDTO = operationMapper.mapToDTO((Operation) null);
    assertNull(operationDTO);
  }

  @Test
  public void mapToDTOListNullTest() {
    List<OperationDTO> operationDTOList = operationMapper
      .mapToDTO((List<Operation>) null);
    assertNull(operationDTOList);
  }

  @Test
  public void mapToDTOListTest() {
    List<Operation> operationList = new ArrayList<>();
    operationList.add(initTestValues.createOperation());
    List<OperationDTO> operationDTOList = operationMapper
      .mapToDTO(operationList);
    assertEquals(operationList.size(), operationDTOList.size());
  }

  @Test
  public void mapToEntityNullTest() {
    Operation operation = operationMapper.mapToEntity((OperationDTO) null);
    assertNull(operation);
  }


  @Test
  public void mapToEntityListTest() {
    List<OperationDTO> operationDTOList = new ArrayList<>();
    operationDTOList.add(initTestValues.createOperationDTO());
    List<Operation> operationList = operationMapper
      .mapToEntity(operationDTOList);
    assertEquals(operationDTOList.size(), operationList.size());
  }

  @Test
  public void mapToEntityListNullTest() {
    assertNull(operationMapper.mapToEntity((List<OperationDTO>) null));
    List<Operation> operationList = operationMapper
      .mapToEntity((List<OperationDTO>) null);
    assertNull(operationList);

  }


  @Test
  public void mapToExistingEntitySetDTOTest() {
    OperationDTO operationDTO = initTestValues.createOperationDTO();
    operationDTO.setDescription(null);
    Operation operation = initTestValues.createOperation();
    operation.setDescription(null);
    operationMapper.mapToExistingEntity(operationDTO, operation);
    assertNull(operationDTO.getDescription());
  }

  @Test
  public void mapToExistingEntitySetDTONullTest() {
    OperationDTO operationDTO = initTestValues.createOperationDTO();
    operationDTO.setDescription(null);
    operationMapper.mapToExistingEntity(null, operation);
    assertNull(operationDTO.getDescription());
  }
}
