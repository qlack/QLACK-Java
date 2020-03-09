package com.eurodyn.qlack.fuse.aaa.mapper;

import com.eurodyn.qlack.fuse.aaa.dto.UserGroupHasOperationDTO;
import com.eurodyn.qlack.fuse.aaa.model.UserGroupHasOperation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

/**
 * A Mapper for UserHasOperation. It is used to map the object values.
 *
 * @author European Dynamics SA
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
  uses = {
    UserGroupMapper.class,
    OperationMapper.class,
    ResourceMapper.class
  })
public interface UserGroupHasOperationMapper extends
  AAAMapper<UserGroupHasOperation, UserGroupHasOperationDTO> {

  /**
   * Maps a specified Entity to according DTO
   *
   * @param userGroupHasOperation the userGroupHasOperation object
   * @return the relative DTO
   */
  @Mapping(source = "userGroup", target = "userGroupDTO")
  @Mapping(source = "operation", target = "operationDTO")
  @Mapping(source = "resource", target = "resourceDTO")
  UserGroupHasOperationDTO mapToDTO(
    UserGroupHasOperation userGroupHasOperation);
}
