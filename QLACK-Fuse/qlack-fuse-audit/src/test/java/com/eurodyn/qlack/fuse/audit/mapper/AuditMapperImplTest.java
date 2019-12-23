package com.eurodyn.qlack.fuse.audit.mapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.eurodyn.qlack.fuse.audit.InitTestValues;
import com.eurodyn.qlack.fuse.audit.dto.AuditDTO;
import com.eurodyn.qlack.fuse.audit.dto.AuditLevelDTO;
import com.eurodyn.qlack.fuse.audit.dto.AuditTraceDTO;
import com.eurodyn.qlack.fuse.audit.model.Audit;
import com.eurodyn.qlack.fuse.audit.model.AuditLevel;
import com.eurodyn.qlack.fuse.audit.model.AuditTrace;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
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

  @Before
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
    assertEquals(null, auditDTO);
  }

  @Test
  public void mapToDTOTest() {
    audit.getLevelId().setName(null);
    AuditDTO auditDTO = auditMapper.mapToDTO(audit);
    audit.setLevelId(null);
    assertEquals(null, auditDTO.getLevel());

  }

  @Test
  public void mapToEntityNullTest() {
    Audit audit = auditMapper.mapToEntity((AuditDTO) null);
    assertEquals(null, audit);
  }

  @Test
  public void mapToExistingEntityTest() {
    auditMapper.mapToExistingEntity(auditDTO, audit);
    auditDTO.setLevel(null);
    assertEquals(null, auditDTO.getLevel());
  }

  @Test
  public void mapToExistingEntityNullTest() {
    auditMapper.mapToExistingEntity(null, audit);
    auditDTO.setLevel(null);
    assertEquals(null, auditDTO.getLevel());
  }


  @Test
  public void mapToExistingEntitySetTraceNullTest() {
    auditDTO.setTrace(null);
    assertNull(null, auditDTO.getTrace());
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
    assertEquals(null, auditMapper.mapToEntity((List<AuditDTO>) null));
    List<Audit> audits = auditMapper.mapToEntity((List<AuditDTO>) null);
    assertEquals(null, audits);

  }


  @Test
  public void mapToDTOListNullTest() {
    List<AuditDTO> audits = auditMapper.mapToDTO((List<Audit>) null);
    assertEquals(null, audits);

  }

  @Test
  public void auditLevelIdNameTest() {

    audit.setLevelId(null);
    auditDTO = auditMapper.mapToDTO(audit);
    assertEquals(auditDTO.getLevel(), audit.getLevelId());
  }

  @Test
  public void versionAttributeNullTest() {
    auditDTO = auditMapper.mapToDTO((Audit) null);
    audit.setLevelId(null);
    assertEquals(null, audit.getLevelId());
  }

  @Test
  public void mapToExistingDTONullTest() {
    auditDTO.setLevel(null);
    auditMapper.mapToExistingEntity(null, audit);
    assertEquals(null, auditDTO.getLevel());
  }

}
