package com.eurodyn.qlack.fuse.audit.mappers;

import com.eurodyn.qlack.fuse.audit.dto.AuditLevelDTO;
import com.eurodyn.qlack.fuse.audit.model.AuditLevel;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AuditLevelMapper extends AuditBaseMapper<AuditLevel, AuditLevelDTO> {

}
