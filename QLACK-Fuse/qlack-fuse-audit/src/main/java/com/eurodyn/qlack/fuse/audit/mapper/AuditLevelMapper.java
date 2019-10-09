package com.eurodyn.qlack.fuse.audit.mapper;

import com.eurodyn.qlack.fuse.audit.dto.AuditLevelDTO;
import com.eurodyn.qlack.fuse.audit.model.AuditLevel;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * Mapping interface for <tt>AuditLevel</tt> entities and DTOs
 *
 * @author European Dynamics SA.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AuditLevelMapper extends AuditBaseMapper<AuditLevel, AuditLevelDTO> {

}
