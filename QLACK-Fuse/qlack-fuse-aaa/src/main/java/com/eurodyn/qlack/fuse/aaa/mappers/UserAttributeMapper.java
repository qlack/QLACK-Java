package com.eurodyn.qlack.fuse.aaa.mappers;

import com.eurodyn.qlack.fuse.aaa.dto.UserAttributeDTO;
import com.eurodyn.qlack.fuse.aaa.model.UserAttribute;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

/**
 * A Mapper interface for {@link UserAttribute} objects.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserAttributeMapper extends AAAMapper<UserAttribute, UserAttributeDTO> {

  /** Maps a UserAttribute entity to DTO
   * @param userAttribute the userAttribute
   * @return the UserAttributeDTO object
   */
  @Override
  @Mapping(source = "user.id", target = "userId")
  UserAttributeDTO mapToDTO(UserAttribute userAttribute);
}
