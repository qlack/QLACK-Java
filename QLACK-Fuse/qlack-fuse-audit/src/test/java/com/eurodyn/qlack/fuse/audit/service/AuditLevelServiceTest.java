package com.eurodyn.qlack.fuse.audit.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.eurodyn.qlack.common.exception.QAlreadyExistsException;
import com.eurodyn.qlack.fuse.audit.InitTestValues;
import com.eurodyn.qlack.fuse.audit.dto.AuditLevelDTO;
import com.eurodyn.qlack.fuse.audit.mapper.AuditLevelMapper;
import com.eurodyn.qlack.fuse.audit.model.AuditLevel;
import com.eurodyn.qlack.fuse.audit.repository.AuditLevelRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * @author European Dynamics
 */

@ExtendWith(MockitoExtension.class)
public class AuditLevelServiceTest {

  @InjectMocks
  private AuditLevelService auditLevelService;

  @Spy
  private AuditLevelMapper auditLevelMapper;

  final private AuditLevelRepository auditLevelRepository = mock(
    AuditLevelRepository.class);

  private InitTestValues initTestValues;
  private AuditLevelDTO auditLevelDTO;
  private AuditLevel auditLevel;
  private List<AuditLevel> auditLevels;
  private List<AuditLevelDTO> auditLevelsDTO;

  @BeforeEach
  public void init() {
    auditLevelService = new AuditLevelService(auditLevelRepository,
      auditLevelMapper);
    initTestValues = new InitTestValues();
    auditLevelDTO = initTestValues.createAuditLevelDTO();
    auditLevel = initTestValues.createAuditLevel();
    auditLevels = initTestValues.createAuditLevels();
    auditLevelsDTO = initTestValues.createAuditLevelsDTO();
  }

  @Test
  public void testAddLevel() {
    when(auditLevelMapper.mapToEntity(auditLevelDTO)).thenReturn(auditLevel);
    String auditLevelId = auditLevelService.addLevel(auditLevelDTO);

    assertEquals(auditLevel.getId(), auditLevelId);
    verify(auditLevelRepository, times(1)).save(auditLevel);
  }

  @Test
  public void testAddNotExistingLevel() {
    when(auditLevelMapper.mapToEntity(auditLevelDTO)).thenReturn(auditLevel);
    auditLevelService.addLevelIfNotExists(auditLevelDTO);
    verify(auditLevelRepository, times(1)).save(auditLevel);
  }

  @Test
  public void testAddExistingLevel() {
    assertThrows(QAlreadyExistsException.class, () -> {
      when(auditLevelRepository.findByName(auditLevelDTO.getName()))
              .thenReturn(auditLevel);
      auditLevelService.addLevelIfNotExists(auditLevelDTO);
    });
  }

  @Test
  public void testDeleteLevelById() {
    AuditLevel auditLevel2 = initTestValues.createAuditLevel();
    when(auditLevelRepository.fetchById(auditLevel.getId()))
      .thenReturn(auditLevel2);
    auditLevelService.deleteLevelById(auditLevel.getId());

    verify(auditLevelRepository, times(1)).delete(auditLevel2);
  }

  @Test
  public void testDeleteLevelByName() {
    AuditLevel auditLevel2 = initTestValues.createAuditLevel();
    when(auditLevelRepository.findByName(auditLevel.getName()))
      .thenReturn(auditLevel2);
    auditLevelService.deleteLevelByName(auditLevel.getName());

    verify(auditLevelRepository, times(1)).delete(auditLevel2);
  }

  @Test
  public void testUpdateLevel() {
    when(auditLevelMapper.mapToEntity(auditLevelDTO)).thenReturn(auditLevel);
    auditLevelDTO.setDescription("Updated description");
    auditLevelService.updateLevel(auditLevelDTO);
    verify(auditLevelMapper, times(1)).mapToEntity(auditLevelDTO);
  }

  @Test
  public void testGetAuditLevelByName() {
    when(auditLevelRepository.findByName(auditLevel.getName()))
      .thenReturn(auditLevel);
    when(auditLevelMapper.mapToDTO(auditLevel)).thenReturn(auditLevelDTO);
    AuditLevelDTO foundAudit = auditLevelService
      .getAuditLevelByName(auditLevel.getName());

    assertEquals(auditLevelDTO, foundAudit);
  }

  @Test
  public void testListAuditLevels() {
    when(auditLevelRepository.findAll()).thenReturn(auditLevels);
    when(auditLevelMapper.mapToDTO(auditLevels)).thenReturn(auditLevelsDTO);
    List<AuditLevelDTO> allAuditLevels = auditLevelService.listAuditLevels();

    assertEquals(allAuditLevels, auditLevelsDTO);
  }
}
