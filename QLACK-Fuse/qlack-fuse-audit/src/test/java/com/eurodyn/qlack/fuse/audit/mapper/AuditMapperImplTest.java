package com.eurodyn.qlack.fuse.audit.mapper;

import static org.junit.jupiter.api.Assertions.*;

import com.eurodyn.qlack.fuse.audit.InitTestValues;
import com.eurodyn.qlack.fuse.audit.dto.AuditDTO;
import com.eurodyn.qlack.fuse.audit.dto.AuditLevelDTO;
import com.eurodyn.qlack.fuse.audit.dto.AuditTraceDTO;
import com.eurodyn.qlack.fuse.audit.model.Audit;
import com.eurodyn.qlack.fuse.audit.model.AuditLevel;
import com.eurodyn.qlack.fuse.audit.model.AuditTrace;
import java.util.List;
import java.util.function.Supplier;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AuditMapperImplTest {

  @InjectMocks
  private AuditMapperImpl auditMapper;

  @Mock
  private AuditTraceMapperImpl auditTraceMapper;

  private Audit audit;

  private AuditDTO auditDTO;

  private AuditLevel auditLevel;

  private AuditLevelDTO auditLevelDTO;

  private AuditTrace auditTrace;

  private AuditTraceDTO auditTraceDTO;

  private InitTestValues initTestValues;

  @BeforeEach
  public void init() {
    initTestValues = new InitTestValues();
    audit = initTestValues.createAudit();
    auditLevel = initTestValues.createAuditLevel();
    auditLevelDTO = initTestValues.createAuditLevelDTO();
    auditTrace = initTestValues.createAuditTrace();
    auditLevel = initTestValues.createAuditLevel();
    auditDTO = initTestValues.createAuditDTO();
    auditTraceDTO = initTestValues.createAuditTraceDTO();
  }

  @Test
  public void mapToDTONullTest() {
    AuditDTO auditDTO = auditMapper.mapToDTO((Audit) null);
    assertNull(auditDTO);
  }

  @Test
  public void mapToDTOTest() {
    audit.getLevelId().setName(null);
    AuditDTO auditDTO = auditMapper.mapToDTO(audit);
    audit.setLevelId(null);
    assertNull(auditDTO.getLevel());

  }

  @Test
  public void mapToEntityNullTest() {
    Audit audit = auditMapper.mapToEntity((AuditDTO) null);
    assertNull(audit);
  }

  @Test
  public void mapToExistingEntityTest() {
    auditMapper.mapToExistingEntity(auditDTO, audit);
    auditDTO.setLevel(null);
    assertNull(auditDTO.getLevel());
  }

  @Test
  public void mapToExistingEntityNullTest() {
    auditMapper.mapToExistingEntity(null, audit);
    auditDTO.setLevel(null);
    assertNull(auditDTO.getLevel());
  }


  @Test
  public void mapToExistingEntitySetTraceNullTest() {
    auditDTO.setTrace(null);
    assertNull((Supplier<String>) auditDTO.getTrace());
    audit.setTrace(null);
    assertNull(audit.getTrace());
    auditMapper.mapToExistingEntity(auditDTO, audit);
  }


  @Test
  public void mapToExistingEntityNewObjectTest() {
    audit.setTrace(null);
    auditMapper.mapToExistingEntity(auditDTO, audit);
    assertNotNull(audit.getTrace());

  }


  @Test
  public void mapToEntityListNullTest() {
    assertNull(auditMapper.mapToEntity((List<AuditDTO>) null));
    List<Audit> audits = auditMapper.mapToEntity((List<AuditDTO>) null);
    assertNull(audits);

  }


  @Test
  public void mapToDTOListNullTest() {
    List<AuditDTO> audits = auditMapper.mapToDTO((List<Audit>) null);
    assertNull(audits);

  }

  @Test
  public void auditLevelIdNameTest() {

    audit.setLevelId(null);
    auditDTO = auditMapper.mapToDTO(audit);
    assertNull(auditDTO.getLevel());
  }

  @Test
  public void versionAttributeNullTest() {
    auditDTO = auditMapper.mapToDTO((Audit) null);
    audit.setLevelId(null);
    assertNull(audit.getLevelId());
  }

  @Test
  public void mapToExistingDTONullTest() {
    auditDTO.setLevel(null);
    auditMapper.mapToExistingEntity(null, audit);
    assertNull(auditDTO.getLevel());
  }

}
