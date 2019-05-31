package com.eurodyn.qlack.fuse.aaa.mappers;

import com.eurodyn.qlack.fuse.aaa.dto.UserAttributeDTO;
import com.eurodyn.qlack.fuse.aaa.model.UserAttribute;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserAttributeMapper extends AAAMapper<UserAttribute, UserAttributeDTO> {

  @Override
  @Mapping(source = "user.id", target = "userId")
  UserAttributeDTO mapToDTO(UserAttribute userAttribute);
}
