package com.eurodyn.qlack.fuse.audit.mapper;

import static org.junit.Assert.assertEquals;

import com.eurodyn.qlack.fuse.audit.InitTestValues;
import com.eurodyn.qlack.fuse.audit.dto.AuditTraceDTO;
import com.eurodyn.qlack.fuse.audit.model.AuditTrace;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * @author European Dynamics
 */

@RunWith(MockitoJUnitRunner.class)
public class AuditTraceMapperTest {

  @InjectMocks
  private AuditTraceMapperImpl auditTraceMapperImpl;

  private InitTestValues initTestValues;
  private AuditTrace auditTrace;
  private AuditTraceDTO auditTraceDTO;
  private List<AuditTrace> auditTraces;
  private List<AuditTraceDTO> auditTracesDTO;

  @Before
  public void init() {
    initTestValues = new InitTestValues();

    auditTrace = initTestValues.createAuditTrace();
    auditTraceDTO = initTestValues.createAuditTraceDTO();
    auditTraces = initTestValues.createAuditTraces();
    auditTracesDTO = initTestValues.createAuditTracesDTO();
  }

  @Test
  public void testMapToDTOId() {
    auditTraceDTO = auditTraceMapperImpl.mapToDTO(auditTrace);
    assertEquals(auditTraceDTO.getId(), auditTrace.getId());
  }

  @Test
  public void testMapToDTOTraceData() {
    auditTraceDTO = auditTraceMapperImpl.mapToDTO(auditTrace);
    assertEquals(auditTraceDTO.getTraceData(), auditTrace.getTraceData());
  }

  @Test
  public void testMapToEntityId() {
    auditTrace = auditTraceMapperImpl.mapToEntity(auditTraceDTO);
    assertEquals(auditTrace.getId(), auditTraceDTO.getId());
  }

  @Test
  public void testMapToDTOList() {
    auditTracesDTO = auditTraceMapperImpl.mapToDTO(auditTraces);
    assertEquals(auditTraces.size(), auditTracesDTO.size());
  }

  @Test
  public void testMapToEntityTraceData() {
    auditTrace = auditTraceMapperImpl.mapToEntity(auditTraceDTO);
    assertEquals(auditTrace.getTraceData(), auditTraceDTO.getTraceData());
  }

  @Test
  public void testMapToEntityList() {
    auditTraces = auditTraceMapperImpl.mapToEntity(auditTracesDTO);
    assertEquals(auditTracesDTO.size(), auditTracesDTO.size());
  }

  @Test
  public void testMapToExistingEntity() {
    auditTraceDTO.setTraceData("New mock data");
    auditTraceMapperImpl.mapToExistingEntity(auditTraceDTO, auditTrace);
    assertEquals(auditTraceDTO.getTraceData(), auditTrace.getTraceData());
  }
}