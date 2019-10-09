package com.eurodyn.qlack.fuse.audit.mapper;

import static org.junit.Assert.assertEquals;

import com.eurodyn.qlack.fuse.audit.InitTestValues;
import com.eurodyn.qlack.fuse.audit.dto.AuditLevelDTO;
import com.eurodyn.qlack.fuse.audit.model.AuditLevel;
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
public class AuditLevelMapperTest {

  @InjectMocks
  private AuditLevelMapperImpl auditLevelMapperImpl;

  private InitTestValues initTestValues;
  private AuditLevel auditLevel;
  private AuditLevelDTO auditLevelDTO;
  private List<AuditLevel> auditLevels;
  private List<AuditLevelDTO> auditLevelsDTO;

  @Before
  public void init() {
    initTestValues = new InitTestValues();

    auditLevel = initTestValues.createAuditLevel();
    auditLevelDTO = initTestValues.createAuditLevelDTO();
    auditLevels = initTestValues.createAuditLevels();
    auditLevelsDTO = initTestValues.createAuditLevelsDTO();
  }

  @Test
  public void testMapToDTOId() {
    auditLevelDTO = auditLevelMapperImpl.mapToDTO(auditLevel);
    assertEquals(auditLevel.getId(), auditLevelDTO.getId());
  }

  @Test
  public void testMapToDTOName() {
    auditLevelDTO = auditLevelMapperImpl.mapToDTO(auditLevel);
    assertEquals(auditLevel.getName(), auditLevelDTO.getName());
  }

  @Test
  public void testMapToDTODescription() {
    auditLevelDTO = auditLevelMapperImpl.mapToDTO(auditLevel);
    assertEquals(auditLevel.getDescription(), auditLevelDTO.getDescription());
  }

  @Test
  public void testMapToDTOPrinSessionId() {
    auditLevelDTO = auditLevelMapperImpl.mapToDTO(auditLevel);
    assertEquals(auditLevel.getPrinSessionId(), auditLevelDTO.getPrinSessionId());
  }

  @Test
  public void testMapToDTOCreatedOn() {
    auditLevelDTO = auditLevelMapperImpl.mapToDTO(auditLevel);
    assertEquals(auditLevel.getCreatedOn(), auditLevelDTO.getCreatedOn());
  }

  @Test
  public void testMapToDTOList() {
    auditLevelsDTO = auditLevelMapperImpl.mapToDTO(auditLevels);
    assertEquals(auditLevelsDTO.size(), auditLevels.size());
  }

  @Test
  public void testMapToEntityId() {
    auditLevelMapperImpl.mapToEntity(auditLevelDTO);
    assertEquals(auditLevelDTO.getId(), auditLevel.getId());
  }

  @Test
  public void testMapToEntityName() {
    auditLevel = auditLevelMapperImpl.mapToEntity(auditLevelDTO);
    assertEquals(auditLevelDTO.getName(), auditLevel.getName());
  }

  @Test
  public void testMapToEntityDescription() {
    auditLevel = auditLevelMapperImpl.mapToEntity(auditLevelDTO);
    assertEquals(auditLevelDTO.getDescription(), auditLevel.getDescription());
  }

  @Test
  public void testMapToEntityPrinSessionId() {
    auditLevel = auditLevelMapperImpl.mapToEntity(auditLevelDTO);
    assertEquals(auditLevelDTO.getPrinSessionId(), auditLevel.getPrinSessionId());
  }

  @Test
  public void testMapToEntityCreatedOn() {
    auditLevel = auditLevelMapperImpl.mapToEntity(auditLevelDTO);
    assertEquals(auditLevelDTO.getCreatedOn(), auditLevel.getCreatedOn());
  }

  @Test
  public void testMapToEntityList() {
    auditLevels = auditLevelMapperImpl.mapToEntity(auditLevelsDTO);
    assertEquals(auditLevels.size(), auditLevelsDTO.size());
  }

  @Test
  public void testMapToExistingEntity() {
    auditLevelDTO.setName("New name");
    auditLevelMapperImpl.mapToExistingEntity(auditLevelDTO, auditLevel);
    assertEquals(auditLevelDTO.getName(), auditLevel.getName());
  }
}
