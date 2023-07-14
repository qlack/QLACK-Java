package com.eurodyn.qlack.fuse.audit.service;

import com.eurodyn.qlack.fuse.audit.dto.AuditDTO;
import com.eurodyn.qlack.fuse.audit.mapper.AuditMapper;
import com.eurodyn.qlack.fuse.audit.repository.AuditLevelRepository;
import com.eurodyn.qlack.fuse.audit.repository.AuditRepository;
import com.eurodyn.qlack.fuse.audit.util.AuditProperties;
import jakarta.transaction.Transactional;
import lombok.extern.java.Log;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Date;

/**
 * Provides Audit CRUD functionality. {@link Async} ensures that each method
 * will be executed at a separate thread
 *
 * @author European Dynamics SA.
 */
@Log
@Service
@Validated
@Transactional
public class AuditAsyncService extends AuditService {

  public AuditAsyncService(AuditProperties auditProperties,
    AuditRepository auditRepository,
    AuditMapper auditMapper,
    AuditLevelRepository auditLevelRepository) {
    super(auditProperties, auditRepository, auditMapper, auditLevelRepository);
  }

  @Async
  @Override
  public void audit(String level, String event, String description) {
    super.audit(level, event, description);
  }

  @Async
  @Override
  public void audit(String level, String event, String description,
    Object... args) {
    super.audit(level, event, description, args);
  }

  @Async
  @Override
  public void audit(String level, String event, String groupName,
    String description,
    String sessionID, Object traceData) {
    super.audit(level, event, groupName, description, sessionID, traceData);
  }

  @Async
  @Override
  public String audit(String level, String event, String groupName,
    String description,
    String sessionID, Object traceData,
    String referenceId) {

    return super
      .audit(level, event, groupName, description, sessionID, traceData,
        referenceId);
  }

  @Async
  @Override
  public void audit(String level, String event, String groupName,
    String description,
    String sessionID, String traceData) {
    super.audit(level, event, groupName, description, sessionID, traceData);
  }

  @Async
  @Override
  public String audit(AuditDTO audit) {
    return super.audit(audit);
  }

  @Async
  @Override
  public void deleteAudit(String id) {
    super.deleteAudit(id);
  }

  @Async
  @Override
  public void truncateAudits() {
    super.truncateAudits();
  }

  @Async
  @Override
  public void truncateAudits(Date createdOn) {
    super.truncateAudits(createdOn);
  }

  @Async
  @Override
  public void truncateAudits(long retentionPeriod) {
    super.truncateAudits(retentionPeriod);
  }
}
