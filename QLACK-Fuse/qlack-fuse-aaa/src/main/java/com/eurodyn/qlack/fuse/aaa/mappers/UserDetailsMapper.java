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
 * A Mapper interface for UserDetails.It is used to map UserDetails objects.
 * @author European Dynamics SA
 */
@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {UserGroupHasOperationMapper.class})
public interface UserDetailsMapper {

    /** Maps the specified Entity to DTO
     * @param user the user
     * @param userGroupHasOperationMapper the mapper
     * @return the relative DTO object
     */
    @Mapping(target = "userGroupHasOperations", source = "userGroups", qualifiedByName = "mapUserGroupHasOperations")
    UserDetailsDTO mapToDTO(User user, @Context UserGroupHasOperationMapper userGroupHasOperationMapper);

    /** A mapping for UserGroupHasOperations
     * @param userGroups the userGroups
     * @param userGroupHasOperationMapper the mapper
     * @return a list of  {@link UserGroupHasOperationDTO} object
     */
    @Named("mapUserGroupHasOperations")
    default List<UserGroupHasOperationDTO> mapUserGroupHasOperations(List<UserGroup> userGroups, @Context UserGroupHasOperationMapper userGroupHasOperationMapper){
        return userGroups.stream()
                .map(g -> userGroupHasOperationMapper.mapToDTO(g.getUserGroupHasOperations()))
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }
}
