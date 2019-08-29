package com.eurodyn.qlack.fuse.aaa.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.eurodyn.qlack.fuse.aaa.InitTestValues;
import com.eurodyn.qlack.fuse.aaa.dto.UserGroupDTO;
import com.eurodyn.qlack.fuse.aaa.exception.InvalidGroupHierarchyException;
import com.eurodyn.qlack.fuse.aaa.mappers.UserGroupMapper;
import com.eurodyn.qlack.fuse.aaa.model.QUserGroup;
import com.eurodyn.qlack.fuse.aaa.model.User;
import com.eurodyn.qlack.fuse.aaa.model.UserGroup;
import com.eurodyn.qlack.fuse.aaa.repository.UserGroupRepository;
import com.eurodyn.qlack.fuse.aaa.repository.UserRepository;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import com.querydsl.core.types.Predicate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Sort;

/**
 * @author European Dynamics
 */

@RunWith(MockitoJUnitRunner.class)
public class UserGroupServiceTest {

  @InjectMocks
  public UserGroupService userGroupService;

  private UserGroupRepository userGroupRepository = mock(UserGroupRepository.class);
  private UserRepository userRepository = mock(UserRepository.class);

  @Spy
  private UserGroupMapper userGroupMapper;
  private QUserGroup qUserGroup;
  private InitTestValues initTestValues;
  private UserGroup userGroup;
  private UserGroup userGroupNoChildren;
  private UserGroupDTO userGroupDTO;
  private List<UserGroup> userGroups;
  private List<UserGroupDTO> userGroupsDTO;
  private User user;
  private List<User> users;

  @Before
  public void init() {
    userGroupService = new UserGroupService(userGroupRepository, userRepository, userGroupMapper);
    qUserGroup = new QUserGroup("userGroup");
    initTestValues = new InitTestValues();
    userGroup = initTestValues.createUserGroup();
    userGroupDTO = initTestValues.createUserGroupDTO();
    userGroups = initTestValues.createUserGroups();
    userGroupsDTO = initTestValues.createUserGroupsDTO();
    user = initTestValues.createUser();
    users = initTestValues.createUsers();
    userGroupNoChildren = initTestValues.createUserGroupNoChildren();
  }

  @Test
  public void testCreateGroup() {
    when(userGroupMapper.mapToEntity(userGroupDTO)).thenReturn(userGroup);
    String createGroupId = userGroupService.createGroup(userGroupDTO);

    assertEquals(userGroupDTO.getId(), createGroupId);
    verify(userGroupRepository, times(1)).save(userGroup);
  }

  @Test
  public void testCreateGroupWithParent() {
    UserGroup parent = initTestValues.createUserGroup();
    parent.setId("909626ef-df62-4ce0-a1ec-102f98a63a2a");
    userGroupDTO.setParentId("909626ef-df62-4ce0-a1ec-102f98a63a2a");
    when(userGroupMapper.mapToEntity(userGroupDTO)).thenReturn(userGroup);
    when(userGroupRepository.fetchById(userGroupDTO.getParentId())).thenReturn(parent);

    String createGroupId = userGroupService.createGroup(userGroupDTO);

    assertEquals(userGroupDTO.getId(), createGroupId);
    verify(userGroupRepository, times(1)).save(userGroup);
    verify(userGroupRepository, times(1)).fetchById(userGroupDTO.getParentId());
  }

  @Test
  public void testUpdateGroup() {
    userGroupDTO.setName("Updated Name");
    when(userGroupRepository.fetchById(userGroupDTO.getId())).thenReturn(userGroup);
    userGroupService.updateGroup(userGroupDTO);
    verify(userGroupMapper, times(1)).mapToExistingEntity(userGroupDTO, userGroup);
  }

  @Test
  public void testDeleteGroup() {
    UserGroup userGroup2 = initTestValues.createUserGroup();
    when(userGroupRepository.fetchById(userGroupDTO.getId())).thenReturn(userGroup2);
    userGroupService.deleteGroup(userGroupDTO.getId());
    verify(userGroupRepository, times(1)).delete(userGroup2);
  }

  @Test
  public void testMoveGroup() {
    UserGroup newParent = userGroups.get(1);
    when(userGroupRepository.fetchById(userGroup.getId())).thenReturn(userGroup);
    when(userGroupRepository.fetchById(newParent.getId())).thenReturn(newParent);
    userGroupService.moveGroup(userGroup.getId(), newParent.getId());
    assertEquals(userGroup.getParent(), newParent);
  }

  @Test(expected = InvalidGroupHierarchyException.class)
  public void testMoveGroupException() {
    when(userGroupRepository.fetchById(userGroup.getId())).thenReturn(userGroup);
    when(userGroupRepository.fetchById(userGroup.getId())).thenReturn(userGroup);
    userGroupService.moveGroup(userGroup.getId(), userGroup.getId());
  }

  @Test
  public void testGetGroupByID() {
    when(userGroupRepository.fetchById(userGroup.getId())).thenReturn(userGroup);
    when(userGroupMapper.mapToDTO(userGroup, false)).thenReturn(userGroupDTO);
    UserGroupDTO foundGroup = userGroupService.getGroupByID(userGroup.getId(), false);
    assertEquals(userGroupDTO.getId(), foundGroup.getId());
    assertEquals(userGroupDTO.getChildren().size(), foundGroup.getChildren().size());
  }

  @Test
  public void testGetGroupByIDLazy() {
    userGroupDTO.setChildren(new HashSet<>());
    when(userGroupRepository.fetchById(userGroup.getId())).thenReturn(userGroup);
    when(userGroupMapper.mapToDTO(userGroup, true)).thenReturn(userGroupDTO);
    UserGroupDTO foundGroup = userGroupService.getGroupByID(userGroup.getId(), true);
    assertEquals(userGroupDTO.getId(), foundGroup.getId());
    assertEquals(0, foundGroup.getChildren().size());
  }

  @Test
  public void testGetGroupsById() {
    Collection<String> groupsId = new ArrayList<>();
    userGroups.stream().forEach(ug -> groupsId.add(ug.getId()));

    when(userGroupRepository.findAll(qUserGroup.id.in(groupsId), Sort.by("name").ascending())).thenReturn(userGroups);
    when(userGroupMapper.mapToDTO(userGroups, false)).thenReturn(userGroupsDTO);
    List<UserGroupDTO> foundGroups = userGroupService.getGroupsByID(groupsId, false);
    assertEquals(userGroupsDTO, foundGroups);
  }

  @Test
  public void testGetGroupByName(){
    userGroupService.getGroupByName(userGroup.getName(), true);
    verify(userGroupRepository, times(1)).findByName(userGroup.getName());
  }

  @Test
  public void testGetGroupByNames(){
    List<String> groupsId = new ArrayList<>();
    userGroups.stream().forEach(ug -> groupsId.add(ug.getId()));

    userGroupService.getGroupByNames(groupsId, true);
    verify(userGroupRepository, times(1)).findAll((Predicate) any());
  }

  @Test
  public void testGetGroupByObjectId(){
    userGroupService.getGroupByObjectId(userGroup.getObjectId(), true);
    verify(userGroupRepository, times(1)).
        findByObjectId(userGroup.getObjectId());
  }

  @Test
  public void testListGroups(){
    userGroupService.listGroups();
    verify(userGroupRepository, times(1)).
        findAll(Sort.by("name").ascending());
  }

  @Test
  public void testListGroupAsTree(){
    userGroupService.listGroupsAsTree();
    verify(userGroupRepository, times(1)).
        findAll(qUserGroup.parent.isNull(),Sort.by("name").ascending());
  }

  @Test
  public void testGetGroupParent(){
    when(userGroupRepository.fetchById(userGroup.getId())).thenReturn(userGroup);
    userGroupService.getGroupParent(userGroup.getId());
    verify(userGroupRepository, times(1)).
        fetchById(userGroup.getId());
  }

  @Test
  public void testGetGroupChildrenNullId(){
    userGroupService.getGroupChildren(null);
    verify(userGroupRepository, times(1)).
        findAll(qUserGroup.parent.isNull(), Sort.by("name").ascending());
  }

  @Test
  public void testGetGroupChildren(){
    userGroupService.getGroupChildren(userGroup.getId());
    verify(userGroupRepository, times(1)).
        findAll(qUserGroup.parent.id.eq(userGroup.getId()), Sort.by("name").ascending());
  }

  @Test
  public void testAddUser(){
    when(userGroupRepository.fetchById(userGroup.getId())).thenReturn(userGroup);
    when(userRepository.fetchById(userGroup.getId())).thenReturn(user);
    userGroupService.addUser(userGroup.getId(), userGroup.getId());
    verify(userGroupRepository, times(1)).
        fetchById(userGroup.getId());
  }

  @Test
  public void testAddUserByGroupName(){
    when(userGroupRepository.findByName(userGroup.getName())).thenReturn(userGroup);
    when(userRepository.fetchById(userGroup.getId())).thenReturn(user);
    userGroupService.addUserByGroupName(userGroup.getId(), userGroup.getName());
    verify(userGroupRepository, times(1)).
        findByName(userGroup.getName());
  }

  @Test
  public void testRemoveUser(){
    when(userGroupRepository.fetchById(userGroup.getId())).thenReturn(userGroup);
    when(userRepository.fetchById(userGroup.getId())).thenReturn(user);
    userGroup.setUsers(users);
    userGroupService.removeUser(userGroup.getId(), userGroup.getId());
    verify(userGroupRepository, times(1)).
        fetchById(userGroup.getId());
  }

  @Test
  public void testGetGroupUsersIdsNoDescendants(){
    when(userGroupRepository.fetchById(userGroup.getId())).thenReturn(userGroup);
    userGroup.setUsers(users);
    userGroupService.getGroupUsersIds(userGroup.getId(), false);
    verify(userGroupRepository, times(1)).
        fetchById(userGroup.getId());
  }

  @Test
  public void testGetGroupUsersIds(){
    when(userGroupRepository.fetchById(userGroup.getId())).thenReturn(userGroup);
    userGroup.setUsers(users);
    userGroupNoChildren.setUsers(users);
    when(userGroupRepository.fetchById(userGroup.getId())).thenReturn(userGroupNoChildren);
    when(userGroupRepository.fetchById(userGroup.getId())).thenReturn(userGroupNoChildren);
    userGroupService.getGroupUsersIds(userGroup.getId(), true);
    verify(userGroupRepository, times(1)).
        fetchById(userGroup.getId());
  }

  @Test
  public void testGetUserGroupsIdsNullGroups(){
    when(userRepository.fetchById(userGroup.getId())).thenReturn(user);
    userGroupService.getUserGroupsIds(userGroup.getId());
    verify(userRepository, times(1)).
        fetchById(userGroup.getId());
  }

  @Test
  public void testGetUserGroupsIds(){
    when(userRepository.fetchById(userGroup.getId())).thenReturn(user);
    user.setUserGroups(userGroups);
    userGroupService.getUserGroupsIds(userGroup.getId());
    verify(userRepository, times(1)).
        fetchById(userGroup.getId());
  }
}
