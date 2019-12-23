package com.eurodyn.qlack.fuse.cm.mapper;

import com.eurodyn.qlack.fuse.cm.dto.VersionAttributeDTO;
import com.eurodyn.qlack.fuse.cm.model.Version;
import com.eurodyn.qlack.fuse.cm.model.VersionAttribute;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface VersionAttributeMapper extends
  CMBaseMapper<VersionAttribute, VersionAttributeDTO> {

  @Override
  @Mapping(target = "versionId", source = "version", qualifiedByName = "mapVersion")
  VersionAttributeDTO mapToDTO(VersionAttribute versionAttribute);

  /**
   * Maps the Version value
   *
   * @param version the Version of the attribute
   * @return the id of the Version
   */
  @Named("mapVersion")
  default String mapVersion(Version version) {
    if (version == null) {
      return null;
    }
    String id = version.getId();
    return id;
  }
}
