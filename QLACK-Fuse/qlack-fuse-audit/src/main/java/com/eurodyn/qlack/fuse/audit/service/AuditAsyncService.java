package com.eurodyn.qlack.fuse.audit.service;

import com.eurodyn.qlack.fuse.audit.dto.AuditDTO;
import com.eurodyn.qlack.fuse.audit.mappers.AuditMapper;
import com.eurodyn.qlack.fuse.audit.repository.AuditLevelRepository;
import com.eurodyn.qlack.fuse.audit.repository.AuditRepository;
import com.eurodyn.qlack.fuse.audit.util.AuditProperties;
import java.text.MessageFormat;
import java.util.Date;
import javax.transaction.Transactional;
import lombok.extern.java.Log;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
@Transactional
@Log
public class AuditAsyncService extends AuditService {

  public AuditAsyncService(AuditProperties auditProperties, AuditRepository auditRepository, AuditMapper auditMapper,
    AuditLevelRepository auditLevelRepository) {
    super(auditProperties, auditRepository, auditMapper, auditLevelRepository);
  }

  @Override
  @Async
  public void audit(String level, String event, String groupName, String description, String sessionID, Object traceData) {
    log.info("Async: Adding audit from params and trace data as an object");
    super.audit(level, event, groupName, description, sessionID, traceData);
  }

  @Override
  @Async
  public String audit(String level, String event, String groupName, String description, String sessionID, Object traceData,
    String referenceId) {
    log.info(MessageFormat.format("Async: Adding audit with referenceId: {0} ", referenceId));
    return super.audit(level, event, groupName, description, sessionID, traceData, referenceId);
  }

  @Override
  @Async
  public void audit(String level, String event, String groupName, String description, String sessionID, String traceData) {
    log.info("Async: Adding audit from params and trace data as a String ");
    super.audit(level, event, groupName, description, sessionID, traceData);
  }

  @Override
  @Async
  public String audit(AuditDTO audit) {
    log.info(MessageFormat.format("Async: Adding audit ''{0}''.", audit));
    return super.audit(audit);
  }

  @Override
  @Async
  public void deleteAudit(String id) {
    log.info(MessageFormat.format("Async: Deleting audit ''{0}''.", id));
    super.deleteAudit(id);
  }

  @Override
  @Async
  public void truncateAudits() {
    log.info("Async: Clearing all audit log data.");
    super.truncateAudits();
  }

  @Override
  @Async
  public void truncateAudits(Date createdOn) {
    log.info(MessageFormat.format("Async: Clearing audit log data before {0}", createdOn));
    super.truncateAudits(createdOn);
  }

  @Override
  @Async
  public void truncateAudits(long retentionPeriod) {
    log.info(MessageFormat.format("Async: Clearing audit log data older than {0}", String.valueOf(retentionPeriod)));
    super.truncateAudits(retentionPeriod);
  }
}
