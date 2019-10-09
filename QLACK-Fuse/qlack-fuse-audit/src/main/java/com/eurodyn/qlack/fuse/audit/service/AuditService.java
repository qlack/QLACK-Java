package com.eurodyn.qlack.fuse.audit.service;

import com.eurodyn.qlack.fuse.audit.dto.AuditDTO;
import com.eurodyn.qlack.fuse.audit.dto.AuditTraceDTO;
import com.eurodyn.qlack.fuse.audit.exception.QAuditException;
import com.eurodyn.qlack.fuse.audit.mapper.AuditMapper;
import com.eurodyn.qlack.fuse.audit.model.Audit;
import com.eurodyn.qlack.fuse.audit.repository.AuditLevelRepository;
import com.eurodyn.qlack.fuse.audit.repository.AuditRepository;
import com.eurodyn.qlack.fuse.audit.util.AuditProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.querydsl.core.types.Predicate;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;

/**
 * Provides Audit CRUD and search functionality
 *
 * @author European Dynamics SA.
 */
@Service
@Transactional
@Validated
@Slf4j
public class AuditService {

  private final ObjectMapper mapper;
  private final AuditLevelRepository auditLevelRepository;
  private final AuditMapper auditMapper;
  // Service references.
  private AuditProperties auditProperties;
  private AuditRepository auditRepository;

  @Autowired
  public AuditService(AuditProperties auditProperties,
      AuditRepository auditRepository, AuditMapper auditMapper,
      AuditLevelRepository auditLevelRepository) {
    this.auditProperties = auditProperties;
    this.auditRepository = auditRepository;
    this.auditMapper = auditMapper;
    this.auditLevelRepository = auditLevelRepository;
    this.mapper = new ObjectMapper();
  }

  private String createTraceDataStr(Object traceData) {
    try {
      return mapper.writeValueAsString(traceData);
    } catch (Exception e) {
      log.error(e.getLocalizedMessage());
      throw new QAuditException(e.getLocalizedMessage());
    }
  }

  /**
   * Adds an audit of an EVENT with minimal information.
   *
   * @param level the audit level
   * @param event the audit EVENT
   * @param description the audit description
   */
  public void audit(String level, String event, String description) {
    log.trace("Adding audit.");
    audit(level, event, null, description, null, null, null);
  }

  /**
   * Adds an audit of an EVENT with minimal information.
   *
   * @param level the audit level
   * @param event the audit EVENT
   * @param description the audit description
   * @param args the arguments to be passed on a {@link MessageFormat} for the description
   * argument.
   */
  public void audit(String level, String event, String description, Object... args) {
    log.trace("Adding audit.");
    audit(level, event, null, MessageFormat.format(description, args), null, null, null);
  }

  /**
   * Adds an audit of an EVENT that occurred in the application
   *
   * @param level the audit level
   * @param event the audit EVENT
   * @param groupName the name of the group that the audit is part of
   * @param description a description of the audit
   * @param sessionID the id of the session that the audit occurred
   * @param traceData an object containing the trace of the audit
   */
  public void audit(String level, String event, String groupName, String description,
      String sessionID, Object traceData) {
    log.trace("Adding audit from params and trace data as an object ");
    audit(level, event, groupName, description, sessionID, traceData, null);
  }

  /**
   * Creates an audit of an EVENT that occurred in the application
   *
   * @param level the audit level
   * @param event the audit EVENT
   * @param groupName the name of the group that the audit is part of
   * @param description a description of the audit
   * @param sessionID the id of the session that the audit occurred
   * @param traceData a String containing the trace of the audit
   */
  public void audit(String level, String event, String groupName, String description,
      String sessionID, String traceData) {
    log.trace("Adding audit from params and trace data as a String ");
    audit(level, event, groupName, description, sessionID, traceData, null);
  }

  /**
   * Adds an audit of an EVENT that occurred in the application
   *
   * @param level the audit level
   * @param event the audit EVENT
   * @param groupName the name of the group that the audit is part of
   * @param description a description of the audit
   * @param sessionID the id of the session that the audit occurred
   * @param traceData an object containing the trace of the audit
   * @param referenceId the reference id of the audit
   * @return the id of the created audit
   */
  public String audit(String level, String event, String groupName, String description,
      String sessionID, Object traceData,
      String referenceId) {
    AuditDTO dto = new AuditDTO(level, event, groupName, description, sessionID);
    if (referenceId != null) {
      log.trace(MessageFormat.format("Adding audit with referenceId: {0} ", referenceId));
      dto.setReferenceId(referenceId);
    }
    if (auditProperties.isTraceData() && !StringUtils.isEmpty(traceData)) {
      dto.setTrace(new AuditTraceDTO(createTraceDataStr(traceData)));
    }
    return audit(dto);
  }

  /**
   * Adds an audit of an EVENT that occurred in the application
   *
   * @param audit a DTO containing all information of the audit to persist
   * @return the id of the created audit
   */
  public String audit(AuditDTO audit) {
    log.trace(MessageFormat.format("Adding audit ''{0}''.", audit));
    if (audit.getCreatedOn() == null) {
      audit.setCreatedOn(Calendar.getInstance().getTimeInMillis());
    }
    Audit alAudit = auditMapper.mapToEntity(audit);
    alAudit.setLevelId(auditLevelRepository.findByName(audit.getLevel()));
    auditRepository.save(alAudit);
    return alAudit.getId();
  }

  /**
   * Adds audits of multiple events that occurred in the application, and correlates them with a
   * unique id
   *
   * @param auditList a list of the audits to persist
   * @param correlationId the unique id to correlate the audits
   * @return a list containing the ids of the created audits
   */
  public List<String> audits(List<AuditDTO> auditList, String correlationId) {
    log.trace(MessageFormat.format("Adding audits ''{0}''.", auditList));

    List<String> uuids = new ArrayList<>();
    auditList.forEach(newAudit -> {
      newAudit.setCorrelationId(correlationId);
      uuids.add(audit(newAudit));
    });

    return uuids;
  }

  /**
   * Deletes an audit
   *
   * @param id the id of the audit to delete
   */
  public void deleteAudit(String id) {
    log.trace(MessageFormat.format("Deleting audit ''{0}''.", id));
    auditRepository.delete(auditRepository.fetchById(id));
  }

  /**
   * Deletes all persisted audits
   */
  public void truncateAudits() {
    log.trace("Clearing all audit log data.");
    auditRepository.deleteAll();
  }

  /**
   * Deletes all audits created before given date
   *
   * @param createdOn the date before which all audits will be deleted
   */
  public void truncateAudits(Date createdOn) {
    log.trace(MessageFormat.format("Clearing audit log data before {0}", createdOn));
    auditRepository.deleteByCreatedOnBefore(createdOn.toInstant().toEpochMilli());
  }

  /**
   * Deletes all audits created before the given period (eg. last 7 days)
   *
   * @param retentionPeriod the period that audits should be kept
   */
  public void truncateAudits(long retentionPeriod) {
    log.trace(MessageFormat.format("Clearing audit log data older than {0}",
        String.valueOf(retentionPeriod)));
    auditRepository
        .deleteByCreatedOnBefore(Calendar.getInstance().getTimeInMillis() - retentionPeriod);
  }

  /**
   * Fetches an audit by given id
   *
   * @param auditId the id of the audit
   * @return the audit that matches the specific id
   */
  public AuditDTO getAuditById(String auditId) {
    log.trace(MessageFormat.format("Fetching audit with id: {0} ", auditId));
    Audit audit = auditRepository.fetchById(auditId);
    return auditMapper.mapToDTO(audit);
  }

  /**
   * Searches for audits matching an expression
   *
   * @param pageable defines the maximum number of results to return
   * @param predicate an expression to describe which audits to return (eg.
   * qAudit.EVENT.like(expression("The EVENT"));
   * @return a page containing X number of audits matching the specific expression
   */
  public Page<AuditDTO> getAuditLogs(Pageable pageable, Predicate predicate) {
    log.trace(MessageFormat.format("Searching for audits matching the expression: {0}", predicate));
    return auditMapper.toAuditDTO(auditRepository.findAll(predicate, pageable));
  }

  /**
   * Searches distinct events for a specific reference id
   *
   * @param referenceId the reference id that each audit should have
   * @return a list of EVENT names of audits, that have the specific reference id.
   */
  public List<String> getDistinctEventsForReferenceId(String referenceId) {
    log.trace(MessageFormat.format("Fetching distinct events for id: {0}", referenceId));
    return auditRepository.findDistinctEventsByReferenceId(referenceId);
  }
}
