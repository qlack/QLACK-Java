package com.eurodyn.qlack.fuse.audit.mapper;

import static org.junit.Assert.assertEquals;

import com.eurodyn.qlack.fuse.audit.InitTestValues;
import com.eurodyn.qlack.fuse.audit.dto.AuditDTO;
import com.eurodyn.qlack.fuse.audit.model.Audit;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * @author European Dynamics
 */

@RunWith(MockitoJUnitRunner.class)
public class AuditMapperTest {

  @InjectMocks
  private AuditMapperImpl auditMapperImpl;

  @Spy
  private AuditTraceMapperImpl auditTraceMapperImpl;

  private InitTestValues initTestValues;

  private Audit audit;
  private AuditDTO auditDTO;
  private List<Audit> audits;
  private List<AuditDTO> auditsDTO;

  @Before
  public void init() {
    initTestValues = new InitTestValues();

    audit = initTestValues.createAudit();
    auditDTO = initTestValues.createAuditDTO();
    audits = initTestValues.createAudits();
    auditsDTO = initTestValues.createAuditsDTO();
  }

  @Test
  public void testMapToDTOId() {
    auditDTO = auditMapperImpl.mapToDTO(audit);
    assertEquals(audit.getId(), auditDTO.getId());
  }

  @Test
  public void testMapToDTOCreatedOn() {
    auditDTO = auditMapperImpl.mapToDTO(audit);
    assertEquals(audit.getCreatedOn(), auditDTO.getCreatedOn());
  }

  @Test
  public void testMapToDTOPrinSession() {
    auditDTO = auditMapperImpl.mapToDTO(audit);
    assertEquals(audit.getPrinSessionId(), auditDTO.getPrinSessionId());
  }

  @Test
  public void testMapToDTOShortDescription() {
    auditDTO = auditMapperImpl.mapToDTO(audit);
    assertEquals(audit.getShortDescription(), auditDTO.getShortDescription());
  }

  @Test
  public void testMapToDTOEvent() {
    auditDTO = auditMapperImpl.mapToDTO(audit);
    assertEquals(audit.getEvent(), auditDTO.getEvent());
  }

  @Test
  public void testMapToDTOGroupName() {
    auditDTO = auditMapperImpl.mapToDTO(audit);
    assertEquals(audit.getGroupName(), auditDTO.getGroupName());
  }

  @Test
  public void testMapToDTOCorrelationId() {
    auditDTO = auditMapperImpl.mapToDTO(audit);
    assertEquals(audit.getCorrelationId(), auditDTO.getCorrelationId());
  }

  @Test
  public void testMapToDTOReferenceId() {
    auditDTO = auditMapperImpl.mapToDTO(audit);
    assertEquals(audit.getReferenceId(), auditDTO.getReferenceId());
  }

  @Test
  public void testMapToDTOpt1() {
    auditDTO = auditMapperImpl.mapToDTO(audit);
    assertEquals(audit.getOpt1(), auditDTO.getOpt1());
  }

  @Test
  public void testMapToDTOpt2() {
    auditDTO = auditMapperImpl.mapToDTO(audit);
    assertEquals(audit.getOpt2(), auditDTO.getOpt2());
  }

  @Test
  public void testMapToDTOpt3() {
    auditDTO = auditMapperImpl.mapToDTO(audit);
    assertEquals(audit.getOpt3(), auditDTO.getOpt3());
  }

  @Test
  public void testMapToDTOTrace() {
    auditDTO = auditMapperImpl.mapToDTO(audit);
    assertEquals(audit.getTrace().getTraceData(), auditDTO.getTrace().getTraceData());
  }

  @Test
  public void testMapToDTOList() {
    auditsDTO = auditMapperImpl.mapToDTO(audits);
    assertEquals(auditsDTO.size(), audits.size());
  }


  @Test
  public void testMapToEntityId() {
    audit = auditMapperImpl.mapToEntity(auditDTO);
    assertEquals(auditDTO.getId(), audit.getId());
  }

  @Test
  public void testMapToEntityCreatedOn() {
    audit = auditMapperImpl.mapToEntity(auditDTO);
    assertEquals(auditDTO.getCreatedOn(), audit.getCreatedOn());
  }

  @Test
  public void testMapToEntityPrinSession() {
    audit = auditMapperImpl.mapToEntity(auditDTO);
    assertEquals(auditDTO.getPrinSessionId(), audit.getPrinSessionId());
  }

  @Test
  public void testMapToEntityShortDescription() {
    audit = auditMapperImpl.mapToEntity(auditDTO);
    assertEquals(auditDTO.getShortDescription(), audit.getShortDescription());
  }

  @Test
  public void testMapToEntityEvent() {
    audit = auditMapperImpl.mapToEntity(auditDTO);
    assertEquals(auditDTO.getEvent(), audit.getEvent());
  }

  @Test
  public void testMapToEntityGroupName() {
    audit = auditMapperImpl.mapToEntity(auditDTO);
    assertEquals(auditDTO.getGroupName(), audit.getGroupName());
  }

  @Test
  public void testMapToEntityCorrelationId() {
    audit = auditMapperImpl.mapToEntity(auditDTO);
    assertEquals(auditDTO.getCorrelationId(), audit.getCorrelationId());
  }

  @Test
  public void testMapToEntityReferenceId() {
    audit = auditMapperImpl.mapToEntity(auditDTO);
    assertEquals(auditDTO.getReferenceId(), audit.getReferenceId());
  }

  @Test
  public void testMapToEntityOpt1() {
    audit = auditMapperImpl.mapToEntity(auditDTO);
    assertEquals(auditDTO.getOpt1(), audit.getOpt1());
  }

  @Test
  public void testMapToEntityOpt2() {
    audit = auditMapperImpl.mapToEntity(auditDTO);
    assertEquals(auditDTO.getOpt2(), audit.getOpt2());
  }

  @Test
  public void testMapToEntityOpt3() {
    audit = auditMapperImpl.mapToEntity(auditDTO);
    assertEquals(auditDTO.getOpt3(), audit.getOpt3());
  }

  @Test
  public void testMapToEntityTrace() {
    audit = auditMapperImpl.mapToEntity(auditDTO);
    assertEquals(auditDTO.getTrace().getTraceData(), audit.getTrace().getTraceData());
  }

  @Test
  public void testMapToEntityList() {
    audits = auditMapperImpl.mapToEntity(auditsDTO);
    assertEquals(audits.size(), auditsDTO.size());
  }

  @Test
  public void testMapToExistingEntity() {
    auditDTO.setEvent("New EVENT");
    auditMapperImpl.mapToExistingEntity(auditDTO, audit);
    assertEquals(auditDTO.getEvent(), audit.getEvent());
  }
}
