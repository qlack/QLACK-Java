package com.eurodyn.qlack.fuse.aaa.mappers;

import com.eurodyn.qlack.fuse.aaa.dto.UserGroupHasOperationDTO;
import com.eurodyn.qlack.fuse.aaa.model.UserGroupHasOperation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
uses = {
    UserGroupMapper.class,
    OperationMapper.class,
    ResourceMapper.class
})
public interface UserGroupHasOperationMapper extends AAAMapper<UserGroupHasOperation, UserGroupHasOperationDTO> {

    @Mapping(source = "userGroup", target = "userGroupDTO")
    @Mapping(source = "operation", target = "operationDTO")
    UserGroupHasOperationDTO mapToDTO(UserGroupHasOperation userGroupHasOperation);
}
