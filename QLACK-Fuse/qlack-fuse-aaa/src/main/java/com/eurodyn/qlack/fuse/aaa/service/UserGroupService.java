package com.eurodyn.qlack.fuse.aaa.service;

import com.eurodyn.qlack.fuse.aaa.dto.UserGroupDTO;
import com.eurodyn.qlack.fuse.aaa.exception.InvalidGroupHierarchyException;
import com.eurodyn.qlack.fuse.aaa.mappers.UserGroupMapper;
import com.eurodyn.qlack.fuse.aaa.model.QUserGroup;
import com.eurodyn.qlack.fuse.aaa.model.UserGroup;
import com.eurodyn.qlack.fuse.aaa.model.User;
import com.eurodyn.qlack.fuse.aaa.repository.UserGroupRepository;
import com.eurodyn.qlack.fuse.aaa.repository.UserRepository;
import com.querydsl.core.types.Predicate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

/**
 * @author European Dynamics SA
 */
@Service
@Validated
@Transactional
public class UserGroupService {

  // QueryDSL helpers.
  private static QUserGroup qUserGroup = QUserGroup.userGroup;
  // Repositories
  private final UserGroupRepository userGroupRepository;
  private final UserRepository userRepository;
  // Mappers
  private final UserGroupMapper userGroupMapper;

  public UserGroupService(UserGroupRepository userGroupRepository,
                          UserRepository userRepository, UserGroupMapper userGroupMapper) {
    this.userGroupRepository = userGroupRepository;
    this.userRepository = userRepository;
    this.userGroupMapper = userGroupMapper;
  }

  public String createGroup(UserGroupDTO userGroupDTO) {
    UserGroup userGroup = userGroupMapper.mapToEntity(userGroupDTO);
    if (userGroupDTO.getParentId() != null) {
      userGroup.setParent(userGroupRepository.fetchById(userGroupDTO.getParentId()));
    }
    userGroupRepository.save(userGroup);

    return userGroup.getId();
  }

  public void updateGroup(UserGroupDTO userGroupDTO) {
    UserGroup userGroup = userGroupRepository.fetchById(userGroupDTO.getId());
    userGroupMapper.mapToExistingEntity(userGroupDTO, userGroup);
  }

  public void deleteGroup(String groupID) {
    userGroupRepository.delete(userGroupRepository.fetchById(groupID));
  }

  public void moveGroup(String groupID, String newParentId) {
    UserGroup userGroup = userGroupRepository.fetchById(groupID);
    UserGroup newParent = userGroupRepository.fetchById(newParentId);

    // Check the moving the userGroup under the new parent will not
    // create a cyclic dependency.
    UserGroup checkedUserGroup = newParent;
    while (checkedUserGroup != null) {
      if (checkedUserGroup.getId().equals(userGroup.getId())) {
        throw new InvalidGroupHierarchyException("Cannot move userGroup with ID " + groupID
            + " under userGroup with ID " + newParentId
            + " since this will create a cyclic dependency between userGroups.");
      }
      checkedUserGroup = checkedUserGroup.getParent();
    }

    userGroup.setParent(newParent);
  }

  public UserGroupDTO getGroupByID(String groupID, boolean lazyRelatives) {
    return userGroupMapper.mapToDTO(userGroupRepository.fetchById(groupID), lazyRelatives);
  }

  public List<UserGroupDTO> getGroupsByID(Collection<String> groupIds, boolean lazyRelatives) {
    Predicate predicate = qUserGroup.id.in(groupIds);

    return userGroupMapper.mapToDTO(
        userGroupRepository.findAll(predicate, Sort.by("name").ascending()), lazyRelatives);

  }

  public UserGroupDTO getGroupByName(String groupName, boolean lazyRelatives) {
    return userGroupMapper.mapToDTO(
        userGroupRepository.findByName(groupName), lazyRelatives);
  }

  public List<UserGroupDTO> getGroupByNames(List<String> groupNames, boolean lazyRelatives) {
    Predicate predicate = qUserGroup.name.in(groupNames);

    return userGroupMapper.mapToDTO(userGroupRepository.findAll(predicate), lazyRelatives);
  }

  public UserGroupDTO getGroupByObjectId(String objectId, boolean lazyRelatives) {
    return userGroupMapper.mapToDTO(
        userGroupRepository.findByObjectId(objectId), lazyRelatives);
  }

  public List<UserGroupDTO> listGroups() {
    return userGroupMapper.mapToDTO(
        userGroupRepository.findAll(Sort.by("name").ascending()), false);
  }

  public List<UserGroupDTO> listGroupsAsTree() {
    Predicate predicate = qUserGroup.parent.isNull();

    return userGroupMapper.mapToDTO(userGroupRepository.findAll(
        predicate, Sort.by("name").ascending()), false);
  }

  public UserGroupDTO getGroupParent(String groupID) {
    UserGroup userGroup = userGroupRepository.fetchById(groupID);
    return userGroupMapper.mapToDTO(userGroup.getParent(), false);
  }

  public List<UserGroupDTO> getGroupChildren(String groupID) {
    Predicate predicate;
    if (groupID == null) {
      predicate = qUserGroup.parent.isNull();
    } else {
      predicate = qUserGroup.parent.id.eq(groupID);
    }
    return userGroupMapper.mapToDTO(userGroupRepository.findAll(
        predicate, Sort.by("name").ascending()), false);

  }

  /**
   * Returns the users belonging to a given userGroup and (optionally) its hierarchy
   *
   * @param userGroup The userGroup the users of which to retrieve
   * @param includeAncestors true if users belonging to ancestors of this userGroup
   * (the userGroup's parent and its parent's parent, etc.) should be retrieved
   * @param includeDescendants true if users belonging to descendants of this
   * userGroup (the userGroup's children and its children's children, etc.) should
   * be retrieved
   * @return The IDs of the users belonging to the specified userGroup hierarchy.
   */
  private Set<String> getGroupHierarchyUsersIds(UserGroup userGroup, boolean includeAncestors,
                                                boolean includeDescendants) {
    Set<String> retVal = new HashSet<>(userGroup.getUsers().size());
    for (User user : userGroup.getUsers()) {
      retVal.add(user.getId());
    }

    // If children userGroup users should be included iterate over them
    // (and their children recursively) and add their users to
    // the return value. Same for the userGroup parents.
    if (includeDescendants) {
      for (UserGroup child : userGroup.getChildren()) {
        retVal.addAll(getGroupHierarchyUsersIds(child, false, true));
      }
    }
    if ((includeAncestors) && (userGroup.getParent() != null)) {
      retVal.addAll(getGroupHierarchyUsersIds(userGroup.getParent(), true, false));
    }

    return retVal;
  }

  private void addUsers(Collection<String> userIDs, UserGroup userGroup) {
    for (String userID : userIDs) {
      User user = userRepository.fetchById(userID);
      // TODO ask how else we do it
      if (userGroup.getUsers() == null) {
        userGroup.setUsers(new ArrayList<User>());
      }
      userGroup.getUsers().add(user);
      if (user.getUserGroups() == null) {
        user.setUserGroups(new ArrayList<UserGroup>());
      }
      user.getUserGroups().add(userGroup);
    }
  }

  public void addUser(String userID, String groupId) {
    List<String> userIds = new ArrayList<>(1);
    userIds.add(userID);
    addUsers(userIds, groupId);
  }

  public void addUsers(Collection<String> userIDs, String groupID) {
    addUsers(userIDs, userGroupRepository.fetchById(groupID));
  }

  public void addUserByGroupName(String userId, String groupName) {
    List<String> userIds = new ArrayList<>(1);
    userIds.add(userId);
    addUsersByGroupName(userIds, groupName);
  }

  public void addUsersByGroupName(Collection<String> userIDs, String groupName) {
    addUsers(userIDs, userGroupRepository.findByName(groupName));
  }

  public void removeUser(String userID, String groupID) {
    List<String> userIds = new ArrayList<>(1);
    userIds.add(userID);
    removeUsers(userIds, groupID);
  }

  public void removeUsers(Collection<String> userIDs, String groupID) {
    UserGroup userGroup = userGroupRepository.fetchById(groupID);
    for (String userID : userIDs) {
      User user = userRepository.fetchById(userID);
      userGroup.getUsers().remove(user);
    }
  }

  public Set<String> getGroupUsersIds(String groupID, boolean includeChildren) {
    UserGroup userGroup = userGroupRepository.fetchById(groupID);
    return getGroupHierarchyUsersIds(userGroup, false, includeChildren);
  }

  public Set<String> getUserGroupsIds(String userID) {
    User user = userRepository.fetchById(userID);
    Set<String> retVal = new HashSet<>();
    if (user.getUserGroups() != null) {
      for (UserGroup userGroup : user.getUserGroups()) {
        retVal.add(userGroup.getId());
      }
    }
    return retVal;
  }
}
