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

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserGroupMapper {

  @Mapping(source = "parent.id", target = "parentId")
  @Mapping(target = "children", qualifiedByName = "mapChildren")
  UserGroupDTO mapToDTO(UserGroup userGroup, @Context boolean lazyRelatives);

  List<UserGroupDTO> mapToDTO(List<UserGroup> userGroups, @Context boolean lazyRelatives);

  @Mapping(source = "parentId", target = "parent.id")
  UserGroup mapToEntity(UserGroupDTO userGroupDTO);

  List<UserGroup> mapToEntity(List<UserGroupDTO> userGroupsDTO);

  void mapToExistingEntity(UserGroupDTO userGroupDTO, @MappingTarget UserGroup userGroup);

  @Named("mapChildren")
  default Set<UserGroupDTO> mapChildren(List<UserGroup> children, @Context boolean lazyRelatives){
    if (!lazyRelatives) {
     return new HashSet<>(mapToDTO(children, true));
    }
    return new HashSet<>();
  }
}
