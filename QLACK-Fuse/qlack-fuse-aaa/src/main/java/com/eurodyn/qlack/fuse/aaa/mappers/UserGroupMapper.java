package com.eurodyn.qlack.fuse.aaa.mappers;

import com.eurodyn.qlack.fuse.aaa.dto.UserGroupDTO;
import com.eurodyn.qlack.fuse.aaa.model.UserGroup;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A Mapper Interface for UserGroup.
 * @author European Dynamics SA
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserGroupMapper {

  /**Maps UserGroup object to DTO
   * @param userGroup the userGroup object
   * @param lazyRelatives the lazyRelatives check
   * @return the relative DTO
   */
  @Mapping(source = "parent.id", target = "parentId")
  @Mapping(target = "children", qualifiedByName = "mapChildren")
  UserGroupDTO mapToDTO(UserGroup userGroup, @Context boolean lazyRelatives);

  /** Maps a list of {@link UserGroup} objects to DTO
   * @param userGroups a list of userGroup objects
   * @param lazyRelatives the lazyRelatives check
   * @return the relative DTO
   */
  List<UserGroupDTO> mapToDTO(List<UserGroup> userGroups, @Context boolean lazyRelatives);

  /** Maps a UserGroupDTO to Entity
   * @param userGroupDTO the userGroupDTO
   * @return the relative entity
   */
  @Mapping(source = "parentId", target = "parent.id")
  UserGroup mapToEntity(UserGroupDTO userGroupDTO);

  /** Maps a {@link UserGroupDTO} to Entity
   * @param userGroupsDTO a list of UserGroupDTO
   * @return the relative Entity
   */
  List<UserGroup> mapToEntity(List<UserGroupDTO> userGroupsDTO);

  /** Maps existing Entity
   * @param userGroupDTO the userGroupDTO object
   * @param userGroup the mapping target of {@link UserGroup} object
   */
  void mapToExistingEntity(UserGroupDTO userGroupDTO, @MappingTarget UserGroup userGroup);

  /** Maps a list of childrens to DTO
   * @param children the children list of {@link UserGroup} objects
   * @param lazyRelatives the lazyRelatives check
   * @return a set of {@link UserGroupDTO} objects
   */
  @Named("mapChildren")
  default Set<UserGroupDTO> mapChildren(List<UserGroup> children, @Context boolean lazyRelatives){
    if (!lazyRelatives) {
     return new HashSet<>(mapToDTO(children, true));
    }
    return new HashSet<>();
  }
}
