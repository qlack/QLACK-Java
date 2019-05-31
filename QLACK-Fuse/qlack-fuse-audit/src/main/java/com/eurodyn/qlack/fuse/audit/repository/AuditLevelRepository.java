package com.eurodyn.qlack.fuse.audit.repository;

import com.eurodyn.qlack.common.repository.QlackBaseRepository;
import com.eurodyn.qlack.fuse.audit.model.AuditLevel;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditLevelRepository extends QlackBaseRepository<AuditLevel, String> {

  AuditLevel findByName(String name);
}
