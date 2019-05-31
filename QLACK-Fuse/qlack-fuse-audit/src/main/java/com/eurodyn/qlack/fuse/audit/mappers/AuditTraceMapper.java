package com.eurodyn.qlack.fuse.audit.mappers;

import com.eurodyn.qlack.fuse.audit.dto.AuditTraceDTO;
import com.eurodyn.qlack.fuse.audit.model.AuditTrace;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AuditTraceMapper extends AuditBaseMapper<AuditTrace, AuditTraceDTO> {

}
