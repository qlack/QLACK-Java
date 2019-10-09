package com.eurodyn.qlack.fuse.audit.mapper;

import com.eurodyn.qlack.fuse.audit.dto.AuditTraceDTO;
import com.eurodyn.qlack.fuse.audit.model.AuditTrace;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * Mapping interface for <tt>AuditTrace</tt> entities and DTOs
 *
 * @author European Dynamics SA.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AuditTraceMapper extends AuditBaseMapper<AuditTrace, AuditTraceDTO> {

}
