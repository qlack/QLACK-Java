package com.eurodyn.qlack.fuse.aaa.mappers;

import com.eurodyn.qlack.fuse.aaa.dto.UserDetailsDTO;
import com.eurodyn.qlack.fuse.aaa.dto.UserGroupHasOperationDTO;
import com.eurodyn.qlack.fuse.aaa.model.User;
import com.eurodyn.qlack.fuse.aaa.model.UserGroup;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author EUROPEAN DYNAMICS SA
 */
@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {UserGroupHasOperationMapper.class})
public interface UserDetailsMapper {

    @Mapping(target = "userGroupHasOperations", source = "userGroups", qualifiedByName = "mapUserGroupHasOperations")
    UserDetailsDTO mapToDTO(User user, @Context UserGroupHasOperationMapper userGroupHasOperationMapper);

    @Named("mapUserGroupHasOperations")
    default List<UserGroupHasOperationDTO> mapUserGroupHasOperations(List<UserGroup> userGroups, @Context UserGroupHasOperationMapper userGroupHasOperationMapper){
        return userGroups.stream()
                .map(g -> userGroupHasOperationMapper.mapToDTO(g.getUserGroupHasOperations()))
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }
}
