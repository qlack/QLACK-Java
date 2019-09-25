package com.eurodyn.qlack.fuse.audit.repository;

import com.eurodyn.qlack.common.repository.QlackBaseRepository;
import com.eurodyn.qlack.fuse.audit.model.AuditLevel;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for <tt>AuditLevel</tt> entities
 *
 * @author European Dynamics SA.
 */
@Repository
public interface AuditLevelRepository extends QlackBaseRepository<AuditLevel, String> {

  AuditLevel findByName(String name);
}
