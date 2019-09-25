package com.eurodyn.qlack.fuse.audit.repository;

import com.eurodyn.qlack.common.repository.QlackBaseRepository;
import com.eurodyn.qlack.fuse.audit.model.AuditTrace;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for <tt>AuditTrace</tt> entities
 *
 * @author European Dynamics SA.
 */
@Repository
public interface AuditTraceRepository extends QlackBaseRepository<AuditTrace, String> {

}
