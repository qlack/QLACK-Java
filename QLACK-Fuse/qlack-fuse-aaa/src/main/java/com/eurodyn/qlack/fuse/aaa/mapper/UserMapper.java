package com.eurodyn.qlack.fuse.aaa.mapper;

import com.eurodyn.qlack.fuse.aaa.dto.UserDTO;
import com.eurodyn.qlack.fuse.aaa.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

/**
 * A Mapper interface to map the {@link User} objects
 *
 * @author European Dynamics SA
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
  uses = UserAttributeMapper.class)
public interface UserMapper extends AAAMapper<User, UserDTO> {

  /**
   * Maps existing Entity
   *
   * @param dto the source DTO
   * @param user the mapping target entity
   */
  @Mapping(target = "userAttributes", ignore = true)
  @Mapping(target = "password", ignore = true)
  void mapToExistingEntity(UserDTO dto, @MappingTarget User user);
}
