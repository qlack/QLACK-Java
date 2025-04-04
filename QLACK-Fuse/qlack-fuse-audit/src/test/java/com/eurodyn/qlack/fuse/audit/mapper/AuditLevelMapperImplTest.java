package com.eurodyn.qlack.fuse.audit.mapper;

import static org.junit.jupiter.api.Assertions.*;

import com.eurodyn.qlack.fuse.audit.InitTestValues;
import com.eurodyn.qlack.fuse.audit.dto.AuditLevelDTO;
import com.eurodyn.qlack.fuse.audit.model.AuditLevel;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AuditLevelMapperImplTest {

  @InjectMocks
  private AuditLevelMapperImpl auditLevelMapper;

  private AuditLevel auditLevel;

  private List<AuditLevel> auditLevels;

  private AuditLevelDTO auditLevelDTO;

  private List<AuditLevelDTO> auditLevelDTOs;

  private InitTestValues initTestValues;

  @BeforeEach
  public void init() {
    initTestValues = new InitTestValues();
    auditLevel = initTestValues.createAuditLevel();
    auditLevels = initTestValues.createAuditLevels();
    auditLevelDTO = initTestValues.createAuditLevelDTO();
    auditLevelDTOs = initTestValues.createAuditLevelsDTO();

  }

  @Test
  public void mapToDTONullTest() {
    auditLevelDTO = auditLevelMapper.mapToDTO((AuditLevel) null);
    assertNull(auditLevelDTO);
  }

  @Test
  public void mapToDTOTest() {
    auditLevelDTO = auditLevelMapper.mapToDTO(auditLevel);
    assertEquals(auditLevelDTO.getName(), auditLevel.getName());
  }


  @Test
  public void mapToDTOListNullTest() {
    auditLevelDTOs = auditLevelMapper.mapToDTO((List<AuditLevel>) null);
    assertNull(auditLevelDTOs);

  }

  @Test
  public void mapListToListDTOTest() {
    auditLevelDTOs = auditLevelMapper.mapToDTO(auditLevels);
    assertEquals(auditLevelDTOs.size(), auditLevels.size());
  }

  @Test
  public void mapToEntityNullTest() {
    auditLevel = auditLevelMapper.mapToEntity((AuditLevelDTO) null);
    assertNull(auditLevel);
  }

  @Test
  public void mapToEntityListNullTest() {
    auditLevelDTOs = null;
    auditLevels = auditLevelMapper.mapToEntity(auditLevelDTOs);
    assertNull(auditLevels);
  }

  @Test
  public void mapToEntityDTONullTest() {
    auditLevel = auditLevelMapper.mapToEntity((AuditLevelDTO) null);
    assertNull(auditLevel);

  }

  @Test
  public void mapToEntityDTOTest() {
    auditLevel = auditLevelMapper.mapToEntity(auditLevelDTO);
    assertEquals(auditLevelDTO.getName(), auditLevel.getName());
  }

  @Test
  public void mapToExistingEntitySetTraceNullTest() {
    auditLevelDTO.setName(null);
    assertNull(auditLevelDTO.getName());
    auditLevel.setName(null);
    assertNull(auditLevel.getName());
    auditLevelMapper.mapToExistingEntity(auditLevelDTO, auditLevel);
  }

  @Test
  public void mapEntityListToListDTOTest() {
    auditLevels = auditLevelMapper.mapToEntity(auditLevelDTOs);
    assertEquals(auditLevels.size(), auditLevelDTOs.size());
  }

  @Test
  public void mapToExistingDTONullTest() {
    AuditLevelDTO auditLevelDTO = initTestValues.createAuditLevelDTO();
    auditLevelDTO.setName(null);
    auditLevelMapper.mapToExistingEntity(null, auditLevel);
    assertNull(auditLevelDTO.getName());
  }

}
