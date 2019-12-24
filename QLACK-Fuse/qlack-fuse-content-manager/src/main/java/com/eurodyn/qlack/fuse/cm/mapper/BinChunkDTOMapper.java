package com.eurodyn.qlack.fuse.cm.mapper;

import com.eurodyn.qlack.fuse.cm.dto.BinChunkDTO;
import com.eurodyn.qlack.fuse.cm.model.Version;
import com.eurodyn.qlack.fuse.cm.model.VersionBin;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BinChunkDTOMapper extends
  CMBaseMapper<VersionBin, BinChunkDTO> {

  @Override
  @Mapping(target = "versionID", source = "version", qualifiedByName = "mapVersion")
  BinChunkDTO mapToDTO(VersionBin versionBin);

  /**
   * Maps the Version value
   *
   * @param version the Version of the bin
   * @return the id of the Version
   */
  @Named("mapVersion")
  default String mapVersion(Version version) {
    if (version == null) {
      return null;
    }
    return version.getId();
  }

}
