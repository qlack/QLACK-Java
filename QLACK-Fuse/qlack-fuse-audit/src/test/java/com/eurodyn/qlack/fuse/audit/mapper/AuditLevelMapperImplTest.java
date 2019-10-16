package com.eurodyn.qlack.fuse.audit.mapper;

import com.eurodyn.qlack.fuse.audit.InitTestValues;
import com.eurodyn.qlack.fuse.audit.dto.AuditLevelDTO;
import com.eurodyn.qlack.fuse.audit.model.AuditLevel;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class AuditLevelMapperImplTest {

  @InjectMocks
  private AuditLevelMapperImpl auditLevelMapper;

  private AuditLevel auditLevel;

  private List<AuditLevel> auditLevels;

  private AuditLevelDTO auditLevelDTO;

  private List<AuditLevelDTO> auditLevelDTOs;

  private InitTestValues initTestValues;

  @Before
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
    assertEquals(null, auditLevelDTO);
  }

  @Test
  public void mapToDTOTest(){
    auditLevelDTO = auditLevelMapper.mapToDTO(auditLevel);
    assertEquals(auditLevelDTO.getName(),auditLevel.getName());
  }


  @Test
  public void mapToDTOListNullTest() {
    auditLevelDTOs = auditLevelMapper.mapToDTO((List<AuditLevel> ) null);
    assertEquals(null, auditLevelDTOs);

  }
  @Test
  public void mapListToListDTOTest(){
    auditLevelDTOs = auditLevelMapper.mapToDTO(auditLevels);
    assertEquals(auditLevelDTOs.size(),auditLevels.size());
  }

  @Test
  public void mapToEntityNullTest() {
    auditLevel = auditLevelMapper.mapToEntity((AuditLevelDTO) null);
    assertEquals(null, auditLevel);
  }

  @Test
  public void mapToEntityListNullTest() {
    auditLevelDTOs = null ;
    auditLevels = auditLevelMapper.mapToEntity(auditLevelDTOs);
    assertEquals(null, auditLevels);
  }

  @Test
  public void mapToEntityDTONullTest() {
    auditLevel = auditLevelMapper.mapToEntity((AuditLevelDTO) null);
    assertEquals(null, auditLevel);

  }

  @Test
  public void mapToEntityDTOTest(){
    auditLevel = auditLevelMapper.mapToEntity(auditLevelDTO);
    assertEquals(auditLevelDTO.getName(),auditLevel.getName());
  }

  @Test
  public void mapToExistingEntitySetTraceNullTest(){
    auditLevelDTO.setName(null);
    assertNull(null,auditLevelDTO.getName());
    auditLevel.setName(null);
    assertNull(auditLevel.getName());
    auditLevelMapper.mapToExistingEntity(auditLevelDTO,auditLevel);
  }

  @Test
  public void mapEntityListToListDTOTest(){
    auditLevels =  auditLevelMapper.mapToEntity(auditLevelDTOs);
    assertEquals(auditLevels.size(),auditLevelDTOs.size());
  }

  @Test
  public void mapToExistingDTONullTest(){
    AuditLevelDTO auditLevelDTO = initTestValues.createAuditLevelDTO();
    auditLevelDTO.setName(null);
    auditLevelMapper.mapToExistingEntity(null,auditLevel);
    assertEquals(null,auditLevelDTO.getName());
  }

}
