package com.eurodyn.qlack.fuse.audit.service;


import com.eurodyn.qlack.common.exception.QAlreadyExistsException;
import com.eurodyn.qlack.fuse.audit.dto.AuditLevelDTO;
import com.eurodyn.qlack.fuse.audit.mappers.AuditLevelMapper;
import com.eurodyn.qlack.fuse.audit.model.AuditLevel;
import com.eurodyn.qlack.fuse.audit.repository.AuditLevelRepository;
import java.text.MessageFormat;
import java.util.List;
import javax.transaction.Transactional;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

/**
 * Provides Audit level CRUD functionality
 *
 * @author European Dynamics SA.
 */
@Transactional
@Validated
@Service
@Log
public class AuditLevelService {

  private final AuditLevelRepository auditLevelRepository;

  private final AuditLevelMapper mapper;

  public AuditLevelService(AuditLevelRepository auditLevelRepository, AuditLevelMapper mapper) {
    this.auditLevelRepository = auditLevelRepository;
    this.mapper = mapper;
  }

  /**
   * Adds a new Audit Level
   *
   * @param level DTO containing all information of the persisted level
   * @return the id of the newly created Audit level
   */
  public String addLevel(AuditLevelDTO level) {
    log.info(MessageFormat.format("Adding custom Audit level ''{0}''.", level));
    AuditLevel alLevel = mapper.mapToEntity(level);
    alLevel.setCreatedOn(System.currentTimeMillis());
    auditLevelRepository.save(alLevel);
    return alLevel.getId();
  }

  public String addLevelIfNotExists(AuditLevelDTO level) {
    if (auditLevelRepository.findByName(level.getName()) == null) {
      return addLevel(level);
    } else {
      throw new QAlreadyExistsException("Level: " + level.getName() + " already exists and will not be added.");
    }
  }

  /**
   * Deletes Audit level with specific id
   *
   * @param levelId the id of the level to delete
   */
  public void deleteLevelById(String levelId) {
    log.info(MessageFormat.format("Deleting Audit level with id ''{0}''.", levelId));
    auditLevelRepository.delete(auditLevelRepository.fetchById(levelId));
  }

  /**
   * Deletes Audit level with specific name
   *
   * @param levelName the name of the level to delete
   */
  public void deleteLevelByName(String levelName) {
    log.info(MessageFormat.format("Deleting Audit level with name ''{0}''.", levelName));
    auditLevelRepository.delete(auditLevelRepository.findByName(levelName));
  }

  /**
   * Updates level with new data
   *
   * @param level DTO containing the updated information
   */
  public void updateLevel(AuditLevelDTO level) {
    log.info(MessageFormat.format("Updating Audit level ''{0}'',", level));
    AuditLevel lev = mapper.mapToEntity(level);
    auditLevelRepository.save(lev);
    clearAuditLevelCache();
  }

  /**
   * Finds the persisted Audit level based on its name
   *
   * @param levelName the name of the persisted Audit level
   * @return the persisted Audit Level
   */
  public AuditLevelDTO getAuditLevelByName(String levelName) {
    log.info(MessageFormat.format("Searching Audit level by name ''{0}''.", levelName));
    return mapper.mapToDTO(auditLevelRepository.findByName(levelName));
  }

  /**
   * Clears the cache of the Audit level
   */
  public void clearAuditLevelCache() {
    log.info("Clearing Audit level cache");
    AuditLevel.clearCache();
  }

  /**
   * Retrieves all persisted Audit levels
   *
   * @return a list containing all existing Audit levels
   */
  public List<AuditLevelDTO> listAuditLevels() {
    log.info("Retrieving all audit levels");
    return mapper.mapToDTO(auditLevelRepository.findAll());
  }
}
