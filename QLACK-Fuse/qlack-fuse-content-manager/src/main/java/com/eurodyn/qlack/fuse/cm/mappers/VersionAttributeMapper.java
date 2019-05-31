package com.eurodyn.qlack.fuse.cm.mappers;

import com.eurodyn.qlack.fuse.cm.dto.VersionAttributeDTO;
import com.eurodyn.qlack.fuse.cm.model.VersionAttribute;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface VersionAttributeMapper extends CMBaseMapper<VersionAttribute, VersionAttributeDTO> {

  @Override
  @Mapping(source = "version.id", target = "versionId")
  VersionAttributeDTO mapToDTO(VersionAttribute versionAttribute);

}
