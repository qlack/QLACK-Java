package com.eurodyn.qlack.fuse.audit.mapper;

import static org.junit.jupiter.api.Assertions.*;

import com.eurodyn.qlack.fuse.audit.InitTestValues;
import com.eurodyn.qlack.fuse.audit.dto.AuditTraceDTO;
import com.eurodyn.qlack.fuse.audit.model.AuditTrace;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AuditTraceMapperImplTest {

  @InjectMocks
  private AuditTraceMapperImpl auditTraceMapper;

  private AuditTrace auditTrace;

  private AuditTraceDTO auditTraceDTO;

  private List<AuditTrace> auditTraces;

  private List<AuditTraceDTO> auditTraceDTOs;


  private InitTestValues initTestValues;

  @BeforeEach
  public void init() {
    initTestValues = new InitTestValues();
    auditTrace = initTestValues.createAuditTrace();
    auditTraces = initTestValues.createAuditTraces();
    auditTraceDTO = initTestValues.createAuditTraceDTO();
    auditTraceDTOs = initTestValues.createAuditTracesDTO();


  }

  @Test
  public void mapToDTONullTest() {
    auditTraceDTO = auditTraceMapper.mapToDTO((AuditTrace) null);
    assertNull(auditTraceDTO);
  }

  @Test
  public void mapToDTOTest() {
    auditTraceDTO = auditTraceMapper.mapToDTO(auditTrace);
    assertEquals(auditTraceDTO.getTraceData(), auditTrace.getTraceData());
  }


  @Test
  public void mapToDTOListNullTest() {
    auditTraceDTOs = auditTraceMapper.mapToDTO((List<AuditTrace>) null);
    assertNull(auditTraceDTOs);

  }

  @Test
  public void mapListToListDTOTest() {
    auditTraceDTOs = auditTraceMapper.mapToDTO(auditTraces);
    assertEquals(auditTraceDTOs.size(), auditTraces.size());
  }

  @Test
  public void mapToEntityNullTest() {
    auditTrace = auditTraceMapper.mapToEntity((AuditTraceDTO) null);
    assertNull(auditTrace);
  }

  @Test
  public void mapToEntityListNullTest() {
    auditTraceDTOs = null;
    auditTraces = auditTraceMapper.mapToEntity(auditTraceDTOs);
    assertNull(auditTraces);
  }

  @Test
  public void mapToEntityDTONullTest() {
    auditTrace = auditTraceMapper.mapToEntity((AuditTraceDTO) null);
    assertNull(auditTrace);

  }

  @Test
  public void mapToEntityDTOTest() {
    auditTrace = auditTraceMapper.mapToEntity(auditTraceDTO);
    assertEquals(auditTraceDTO.getTraceData(), auditTrace.getTraceData());
  }

  @Test
  public void mapToExistingEntityTest() {
    auditTraceMapper.mapToExistingEntity(auditTraceDTO, auditTrace);
    auditTraceDTO.setTraceData(null);
    assertNull(auditTraceDTO.getTraceData());
  }

  @Test
  public void mapToExistingEntitySetTraceNullTest() {
    auditTraceDTO.setTraceData(null);
    assertNull(null, auditTraceDTO.getTraceData());
    auditTrace.setTraceData(null);
    assertNull(auditTrace.getTraceData());
    auditTraceMapper.mapToExistingEntity(auditTraceDTO, auditTrace);
  }

  @Test
  public void mapEntityListToListDTOTest() {
    auditTraces = auditTraceMapper.mapToEntity(auditTraceDTOs);
    assertEquals(auditTraces.size(), auditTraceDTOs.size());
  }


  @Test
  public void mapToExistingAuditTraceDTONullTest() {
    AuditTraceDTO auditTraceDTO = initTestValues.createAuditTraceDTO();
    auditTraceDTO.setTraceData(null);
    auditTraceMapper.mapToExistingEntity(null, auditTrace);
    assertNull(auditTraceDTO.getTraceData());
  }

}
