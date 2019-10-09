package com.eurodyn.qlack.fuse.cm.mapper;

import com.eurodyn.qlack.fuse.cm.dto.BinChunkDTO;
import com.eurodyn.qlack.fuse.cm.model.VersionBin;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BinChunkDTOMapper extends CMBaseMapper<VersionBin, BinChunkDTO> {

  @Override
  @Mapping(source = "version.id", target = "versionID")
  BinChunkDTO mapToDTO(VersionBin versionBin);

}
