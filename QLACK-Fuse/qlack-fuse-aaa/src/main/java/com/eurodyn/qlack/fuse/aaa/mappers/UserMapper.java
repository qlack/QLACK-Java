package com.eurodyn.qlack.fuse.aaa.mappers;

import com.eurodyn.qlack.fuse.aaa.dto.UserDTO;
import com.eurodyn.qlack.fuse.aaa.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
uses = UserAttributeMapper.class)
public interface UserMapper extends AAAMapper<User, UserDTO> {

  @Mapping(target = "userAttributes", ignore = true)
  @Mapping(target = "password", ignore = true)
  void mapToExistingEntity(UserDTO dto,@MappingTarget User user);
}
