package com.eurodyn.qlack.fuse.cm.mappers;

import com.eurodyn.qlack.fuse.cm.dto.VersionDTO;
import com.eurodyn.qlack.fuse.cm.model.Version;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VersionMapper extends CMBaseMapper<Version, VersionDTO> {

}
