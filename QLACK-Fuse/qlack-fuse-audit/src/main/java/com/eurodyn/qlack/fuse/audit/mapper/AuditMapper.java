package com.eurodyn.qlack.fuse.audit.mapper;

import com.eurodyn.qlack.fuse.audit.dto.AuditDTO;
import com.eurodyn.qlack.fuse.audit.model.Audit;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;

/**
 * Mapping interface for <tt>Audit</tt> entities and DTOs
 *
 * @author European Dynamics SA.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = AuditTraceMapper.class)
public interface AuditMapper extends AuditBaseMapper<Audit, AuditDTO> {

  default Page<AuditDTO> toAuditDTO(Page<Audit> audits) {
    return audits.map(this::mapToDTO);
  }

  @Override
  @Mapping(target = "level", source = "levelId.name")
  AuditDTO mapToDTO(Audit audit);
}
