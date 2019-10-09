package com.eurodyn.qlack.fuse.cm.mapper;

import com.eurodyn.qlack.fuse.cm.dto.VersionAttributeDTO;
import com.eurodyn.qlack.fuse.cm.model.VersionAttribute;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface VersionAttributeMapper extends
    CMBaseMapper<VersionAttribute, VersionAttributeDTO> {

  @Override
  @Mapping(source = "version.id", target = "versionId")
  VersionAttributeDTO mapToDTO(VersionAttribute versionAttribute);

}
