package com.eurodyn.qlack.fuse.cm.mappers;

import com.eurodyn.qlack.fuse.cm.dto.BinChunkDTO;
import com.eurodyn.qlack.fuse.cm.model.VersionBin;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BinChunkDTOMapper extends CMBaseMapper<VersionBin, BinChunkDTO> {

  @Override
  @Mapping(source = "version.id", target = "versionID")
  BinChunkDTO mapToDTO(VersionBin versionBin);

}
