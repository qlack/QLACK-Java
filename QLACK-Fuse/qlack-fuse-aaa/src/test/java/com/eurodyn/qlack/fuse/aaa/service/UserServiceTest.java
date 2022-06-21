package com.eurodyn.qlack.fuse.aaa.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.eurodyn.qlack.fuse.aaa.InitTestValues;
import com.eurodyn.qlack.fuse.aaa.criteria.UserSearchCriteria;
import com.eurodyn.qlack.fuse.aaa.dto.SessionDTO;
import com.eurodyn.qlack.fuse.aaa.dto.UserAttributeDTO;
import com.eurodyn.qlack.fuse.aaa.dto.UserDTO;
import com.eurodyn.qlack.fuse.aaa.dto.UserGroupDTO;
import com.eurodyn.qlack.fuse.aaa.mapper.SessionMapper;
import com.eurodyn.qlack.fuse.aaa.mapper.UserAttributeMapper;
import com.eurodyn.qlack.fuse.aaa.mapper.UserMapper;
import com.eurodyn.qlack.fuse.aaa.model.QSession;
import com.eurodyn.qlack.fuse.aaa.model.QUser;
import com.eurodyn.qlack.fuse.aaa.model.Session;
import com.eurodyn.qlack.fuse.aaa.model.User;
import com.eurodyn.qlack.fuse.aaa.model.UserAttribute;
import com.eurodyn.qlack.fuse.aaa.model.UserGroup;
import com.eurodyn.qlack.fuse.aaa.repository.SessionRepository;
import com.eurodyn.qlack.fuse.aaa.repository.UserAttributeRepository;
import com.eurodyn.qlack.fuse.aaa.repository.UserGroupRepository;
import com.eurodyn.qlack.fuse.aaa.repository.UserRepository;
import com.eurodyn.qlack.fuse.aaa.util.AAAProperties;
import com.querydsl.core.types.Predicate;
import org.apache.commons.collections.CollectionUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

  @InjectMocks
  private UserService userService;

  @Spy
  private SessionMapper sessionMapper;
  @Spy
  private UserMapper userMapper;
  @Spy
  private UserAttributeMapper userAttributeMapper;
  @Spy
  private UserGroup userGroup;
  @Spy
  private UserGroup userGroupNoChildren;
  @Mock
  private LdapService ldapUserUtil;
  @Mock
  private UserGroupRepository userGroupRepository;
  @Mock
  private AAAProperties aaaProperties;

  private AccountingService accountingService = mock(AccountingService.class);
  private UserRepository userRepository = mock(UserRepository.class);
  private UserAttributeRepository userAttributeRepository = mock(
    UserAttributeRepository.class);
  private SessionRepository sessionRepository = mock(SessionRepository.class);
  private PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
  private InitTestValues initTestValues;
  private QUser qUser;
  private QSession qSession;
  private User user;
  private UserDTO userDTO;
  private List<User> users;
  private List<UserGroup> userGroups;
  private List<UserDTO> usersDTO;
  private List<UserGroupDTO> userGroupDTOS;
  private List<UserAttributeDTO> userAttributeDTOList;
  private UserSearchCriteria.UserSearchCriteriaBuilder userSearchCriteriaBuilder;
  private UserSearchCriteria userSearchCriteria;


  @Before
  public void init() {
    initTestValues = new InitTestValues();
    userService = new UserService(accountingService, ldapUserUtil,
      userRepository, userAttributeRepository,
      sessionRepository, userGroupRepository, userMapper,
      sessionMapper, userAttributeMapper, aaaProperties, passwordEncoder);
    qUser = new QUser("user");
    qSession = new QSession(("session"));
    user = initTestValues.createUser();
    userDTO = initTestValues.createUserDTO();
    users = initTestValues.createUsers();
    usersDTO = initTestValues.createUsersDTO();
    userGroup = initTestValues.createUserGroup();
    userGroupDTOS = initTestValues.createUserGroupsDTO();
    userGroups = initTestValues.createUserGroups();
    userGroupNoChildren = initTestValues.createUserGroupNoChildren();
    userAttributeDTOList = initTestValues.createUserAttributesDTO(user.getId());
    userSearchCriteriaBuilder = UserSearchCriteria.UserSearchCriteriaBuilder
      .createCriteria();
    userSearchCriteria = userSearchCriteriaBuilder.build();
  }

  @Test
  public void testCreateUserWithoutUserAttributes() {
    UserDTO userDTO = initTestValues.createUserDTO();
    User user = initTestValues.createUser();
    user.setUserAttributes(new ArrayList<>());
    when(userMapper.mapToEntity(userDTO)).thenReturn(user);

    String userId = userService.createUser(userDTO, Optional.empty());
    assertEquals(user.getId(), userId);
    verify(userRepository, times(1)).save(user);
  }

  @Test
  public void testCreateUser() {
    when(userMapper.mapToEntity(userDTO)).thenReturn(user);

    String userId = userService.createUser(userDTO, Optional.empty());
    assertEquals(user.getId(), userId);
    verify(userRepository, times(1)).save(user);
  }

  @Test
  public void testUpdateUserWithoutUserAttibutes() {
    UserDTO userDTO = initTestValues.createUserDTO();
    userDTO.setUserAttributes(new HashSet<>());
    User user = initTestValues.createUser();
    user.setUserAttributes(new ArrayList<>());

    when(userRepository.fetchById(userDTO.getId())).thenReturn(user);
    userService.updateUser(userDTO, true, false);

    verify(userMapper, times(1)).mapToExistingEntity(userDTO, user);
    verify(userAttributeRepository, never()).findByUserIdAndName(any(), any());
  }

  @Test
  public void testUpdateUserWithoutUserAttibutesNoSalt() {
    UserDTO userDTO = initTestValues.createUserDTO();
    userDTO.setUserAttributes(new HashSet<>());
    User user = initTestValues.createUser();
    user.setUserAttributes(new ArrayList<>());
    user.setSalt(null);

    when(userRepository.fetchById(userDTO.getId())).thenReturn(user);
    userService.updateUser(userDTO, true, false);

    verify(userMapper, times(1)).mapToExistingEntity(userDTO, user);
    verify(userAttributeRepository, never()).findByUserIdAndName(any(), any());
  }

  @Test
  public void testUpdateUserWithoutUserAttibutesNoUpdatePassword() {
    UserDTO userDTO = initTestValues.createUserDTO();
    userDTO.setUserAttributes(new HashSet<>());
    User user = initTestValues.createUser();
    user.setUserAttributes(new ArrayList<>());
    user.setSalt(null);

    when(userRepository.fetchById(userDTO.getId())).thenReturn(user);
    userService.updateUser(userDTO, false, false);

    verify(userMapper, times(1)).mapToExistingEntity(userDTO, user);
    verify(userAttributeRepository, never()).findByUserIdAndName(any(), any());
  }

  private void testUpdateUserWithUserAttributes(boolean createIfMissing) {
    UserDTO userDTO = initTestValues.createUserDTO();

    userDTO.setUsername("updated username");

    int index = 0;
    for (
      Iterator<UserAttributeDTO> iter = userDTO.getUserAttributes().iterator();
      iter.hasNext(); ) {
      UserAttributeDTO u = iter.next();
      u.setData("updated " + u.getData());

      when(
        userAttributeRepository.findByUserIdAndName(user.getId(), u.getName()))
        .thenReturn(user.getUserAttributes().get(index));
      index++;
    }

    UserAttributeDTO userAttributeDTO = new UserAttributeDTO();
    userAttributeDTO.setId("b69381e6-a465-4137-8c82-6f54f07b0a7f");
    userAttributeDTO.setName("email");
    userAttributeDTO.setData("user@qlack.eurodyn.com");
    userAttributeDTO.setContentType("text");
    userDTO.getUserAttributes().add(userAttributeDTO);

    when(userRepository.fetchById(userDTO.getId())).thenReturn(user);
    userService.updateUser(userDTO, true, createIfMissing);

    verify(userMapper, times(1)).mapToExistingEntity(userDTO, user);
  }

  @Test
  public void testUpdateUserWithoutNewUserAttributes() {
    testUpdateUserWithUserAttributes(false);
    verify(userAttributeRepository, times(2)).save(any());
  }

  @Test
  public void testUpdateUserWithNewUserAttributes() {
    testUpdateUserWithUserAttributes(true);
    verify(userAttributeRepository, times(3)).save(any());
  }

  @Test
  public void testDeleteUser() {
    User user2 = initTestValues.createUser();

    when(userRepository.fetchById(user.getId())).thenReturn(user2);
    userService.deleteUser(user.getId());

    verify(userRepository, times(1)).delete(user2);
  }

  @Test
  public void testGetUserById() {
    when(userRepository.fetchById(userDTO.getId())).thenReturn(user);
    when(userMapper.mapToDTO(user)).thenReturn(userDTO);
    UserDTO foundUser = userService.getUserById(userDTO.getId());
    assertEquals(userDTO, foundUser);
  }

  public Collection<String> getUsersById() {
    Collection<String> userIds = new ArrayList<>();
    for (int i = 0; i < users.size(); i++) {
      userIds.add(users.get(i).getId());
      when(userMapper.mapToDTO(users.get(i))).thenReturn(usersDTO.get(i));
    }

    when(userRepository.findAll(qUser.id.in(userIds))).thenReturn(users);

    return userIds;
  }

  @Test
  public void testGetUsersById() {
    Collection<String> userIds = getUsersById();
    Set<UserDTO> foundUsers = userService.getUsersById(userIds);

    assertEquals(new HashSet<>(usersDTO), foundUsers);
  }

  @Test
  public void testGetUsersByIdAsHash() {
    Collection<String> userIds = getUsersById();
    Map<String, UserDTO> foundUsers = userService.getUsersByIdAsHash(userIds);
    Map<String, UserDTO> userHashMap = new HashMap<>();
    for (UserDTO u : usersDTO) {
      userHashMap.put(u.getId(), u);
    }

    assertEquals(userHashMap, foundUsers);
  }

  @Test
  public void testGetUserByName() {
    when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
    when(userMapper.mapToDTO(user)).thenReturn(userDTO);

    UserDTO foundUser = userService.getUserByName(user.getUsername());
    assertEquals(foundUser, userDTO);
  }

  @Test
  public void testUpdateUserStatus() {
    when(userRepository.fetchById(user.getId())).thenReturn(user);
    userService.updateUserStatus(user.getId(), (byte) 0);

    verify(userRepository, times(1)).fetchById(user.getId());
  }

  @Test
  public void testGetUserStatus() {
    when(userRepository.fetchById(user.getId())).thenReturn(user);
    userService.getUserStatus(user.getId());

    verify(userRepository, times(1)).fetchById(user.getId());
  }

  @Test
  public void testIsSuperAdmin() {
    when(userRepository.fetchById(user.getId())).thenReturn(user);
    boolean isSuperAdmin = userService.isSuperadmin(user.getId());

    assertEquals(isSuperAdmin, user.isSuperadmin());
  }

  @Test
  public void testIsSuperAdminNull() {
    when(userRepository.fetchById(user.getId())).thenReturn(null);
    boolean isSuperAdmin = userService.isSuperadmin(user.getId());

    assertFalse(isSuperAdmin);
  }

  @Test
  public void testIsExternal() {
    when(userRepository.fetchById(user.getId())).thenReturn(user);
    boolean isExternal = userService.isExternal(user.getId());

    assertEquals(isExternal, user.isExternal());
  }

  @Test
  public void testCanNotAuthenticate() {
    user.setSalt(null);
    when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
    String userId = userService
      .canAuthenticate(user.getUsername(), user.getPassword());
    verify(userRepository, times(1)).findByUsername(user.getUsername());
    assertNotEquals(userId, user.getId());
  }

  @Test
  public void testCanAuthenticateNullUser() {
    when(userRepository.findByUsername(user.getUsername())).thenReturn(null);
    when(aaaProperties.isLdapEnabled()).thenReturn(true);
    String userId = userService
      .canAuthenticate(user.getUsername(), user.getPassword());
    verify(userRepository, times(1)).findByUsername(user.getUsername());
    assertNotEquals(userId, user.getId());
  }

  @Test
  public void testCanAuthenticate() {
    when(passwordEncoder.matches(any(), any())).thenReturn(true);
    when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
    String userId = userService
      .canAuthenticate(user.getUsername(), user.getPassword());
    verify(userRepository, times(1)).findByUsername(user.getUsername());
    assertEquals(userId, user.getId());
  }

  @Test
  public void testCanAuthenticateLdap() {
    user.setExternal(true);
    when(aaaProperties.isLdapEnabled()).thenReturn(true);
    when(ldapUserUtil.canAuthenticate(any(), any())).thenReturn(user.getId());
    when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
    String userId = userService
      .canAuthenticate(user.getUsername(), user.getPassword());
    verify(userRepository, times(1)).findByUsername(user.getUsername());
    assertEquals(userId, user.getId());
  }

  @Test
  public void testLogin() {
    when(userRepository.fetchById(user.getId())).thenReturn(user);
    when(userMapper.mapToDTO(user)).thenReturn(userDTO);

    UserDTO loggedUser = userService
      .login(user.getId(), UUID.randomUUID().toString(), false);
    assertEquals(loggedUser, userDTO);
    verify(accountingService, never()).terminateSession(any());
  }

  @Test
  public void testLoginAndTerminateSessions() {
    when(userRepository.fetchById(user.getId())).thenReturn(user);
    when(userMapper.mapToDTO(user)).thenReturn(userDTO);
    user.getSessions().forEach(session -> session.setTerminatedOn(123L));

    UserDTO loggedUser = userService
      .login(user.getId(), UUID.randomUUID().toString(), true);
    assertEquals(loggedUser, userDTO);
    verify(accountingService, times(1)).createSession(any());
  }

  @Test
  public void testLoginAndTerminateSessionsNoTerminatedOn() {
    when(userRepository.fetchById(user.getId())).thenReturn(user);
    when(userMapper.mapToDTO(user)).thenReturn(userDTO);

    UserDTO loggedUser = userService
      .login(user.getId(), UUID.randomUUID().toString(), true);
    assertEquals(loggedUser, userDTO);
    verify(accountingService, times(2)).terminateSession(any());
  }

  @Test
  public void testLoginAndTerminateNullSessions() {
    when(userRepository.fetchById(user.getId())).thenReturn(user);
    when(userMapper.mapToDTO(user)).thenReturn(userDTO);
    user.setSessions(null);

    UserDTO loggedUser = userService
      .login(user.getId(), UUID.randomUUID().toString(), true);
    assertEquals(loggedUser, userDTO);
    verify(accountingService, times(1)).createSession(any());
  }

  @Test
  public void testLogout() {
    when(userRepository.fetchById(user.getId())).thenReturn(user);
    for (Session s : user.getSessions()) {
      userService.logout(user.getId(), s.getApplicationSessionId());
      verify(accountingService, times(1)).terminateSession(s.getId());
    }
  }

  @Test
  public void testLogoutNoApplicationId() {
    when(userRepository.fetchById(user.getId())).thenReturn(user);
    userService.logout(user.getId(), null);
    verify(userRepository, times(1)).fetchById(any());
  }

  @Test
  public void testLogoutNoSessionApplicationId() {
    user.getSessions()
      .forEach(session -> session.setApplicationSessionId(null));
    when(userRepository.fetchById(user.getId())).thenReturn(user);
    userService.logout(user.getId(), null);
    verify(userRepository, times(1)).fetchById(any());
  }

  @Test
  public void testLogoutNullSessions() {
    user.setSessions(null);
    when(userRepository.fetchById(user.getId())).thenReturn(user);
    userService.logout(user.getId(), null);
    verify(userRepository, times(1)).fetchById(any());
  }


  @Test
  public void testLogoutAll() {
    when(sessionRepository.findAll(qSession.terminatedOn.isNull()))
      .thenReturn(user.getSessions());
    when(userRepository.fetchById(user.getId())).thenReturn(user);
    userService.logoutAll();
    verify(accountingService, times(2)).terminateSession(any());
  }

  @Test
  public void testIsUserAlreadyLoggedIn() {
    List<SessionDTO> sessionsDTO = initTestValues
      .createSessionsDTO(user.getId());
    when(sessionRepository
      .findAll(
        qSession.user.id.eq(user.getId()).and(qSession.terminatedOn.isNull()),
        Sort.by("createdOn").ascending())).thenReturn(user.getSessions());
    when(sessionMapper.mapToDTO(user.getSessions())).thenReturn(sessionsDTO);

    List<SessionDTO> foundSessionsDTO = userService
      .isUserAlreadyLoggedIn(user.getId());
    assertEquals(foundSessionsDTO, sessionsDTO);
  }

  @Test
  public void testIsUserAlreadyLoggedInRetValNull() {
    List<SessionDTO> sessionsDTO = initTestValues
      .createSessionsDTO(user.getId());
    when(sessionRepository
      .findAll(
        qSession.user.id.eq(user.getId()).and(qSession.terminatedOn.isNull()),
        Sort.by("createdOn").ascending())).thenReturn(user.getSessions());
    when(sessionMapper.mapToDTO(user.getSessions()))
      .thenReturn(new ArrayList<>());

    List<SessionDTO> foundSessionsDTO = userService
      .isUserAlreadyLoggedIn(user.getId());
    assertNull(foundSessionsDTO);
  }

  @Test
  public void testBelongsToGroupByNameNoChildren() {
    userGroup.setName("groupName");
    userGroup.setUsers(users);
    when(userGroupRepository.findByName(any())).thenReturn(userGroup);
    when(userRepository.fetchById(user.getId())).thenReturn(user);
    userService.belongsToGroupByName(user.getId(), userGroup.getName(), false);
    verify(userRepository, times(1)).fetchById(user.getId());
  }

  @Test
  public void testBelongsToGroupByName() {
    userGroup.setName("groupName");
    userGroup.setUsers(users);
    when(userGroupRepository.findByName(userGroup.getName()))
      .thenReturn(userGroup);
    when(
      userGroupRepository.findByName(userGroup.getChildren().get(0).getName()))
      .thenReturn(userGroupNoChildren);
    userGroupNoChildren.setUsers(users);
    when(
      userGroupRepository.findByName(userGroup.getChildren().get(1).getName()))
      .thenReturn(userGroupNoChildren);
    when(userRepository.fetchById(user.getId())).thenReturn(user);
    userService.belongsToGroupByName(user.getId(), userGroup.getName(), true);
    verify(userRepository, times(3)).fetchById(user.getId());
  }

  @Test
  public void testUpdateAttributes() {
    userService.updateAttributes(userAttributeDTOList, false);
    verify(userAttributeRepository, times(2)).
      findByUserIdAndName(any(), any());
  }

  @Test
  public void testDeleteAttributeNull() {
    userService
      .deleteAttribute(user.getId(), user.getUserAttributes().get(0).getName());
    verify(userAttributeRepository, times(1)).findByUserIdAndName(any(), any());
  }

  @Test
  public void testDeleteAttribute() {
    when(userAttributeRepository.findByUserIdAndName(any(), any()))
      .thenReturn(user.getUserAttributes().get(0));
    userService
      .deleteAttribute(user.getId(), user.getUserAttributes().get(0).getName());
    verify(userAttributeRepository, times(1)).
      findByUserIdAndName(user.getId(),
        user.getUserAttributes().get(0).getName());
    verify(userAttributeRepository, times(1))
      .delete(user.getUserAttributes().get(0));
  }

  @Test
  public void testGetAttribute() {
    userService
      .getAttribute(user.getId(), user.getUserAttributes().get(0).getName());
    verify(userAttributeRepository, times(1)).findByUserIdAndName(any(), any());
  }

  @Test
  public void testGetUserIDsForAttribute() {
    Collection<String> userIds = new ArrayList<>();
    userIds.add(user.getId());
    userIds.add(user.getId());
    userService.getUserIDsForAttribute(userIds,
      user.getUserAttributes().get(0).getName(),
      user.getUserAttributes().get(0).getData());
    verify(userRepository, times(1)).findAll((Predicate) any());
  }

  @Test
  public void findUsersEmptyCriteriaTest() {
    Iterable<UserDTO> result = userService.findUsers(userSearchCriteria);
    assertTrue(Arrays.asList(result).size() > 0);
    verify(userRepository, times(1)).findAll((Predicate) any());
  }

  @Test
  public void findUsersBuildPredicateTest() {
    userSearchCriteria.setIncludeGroupIds(new ArrayList<>());
    userSearchCriteria.setExcludeGroupIds(new ArrayList<>());
    userSearchCriteria.setIncludeIds(new ArrayList<>());
    userSearchCriteria.setIncludeStatuses(new ArrayList<>());
    userSearchCriteria.setExcludeIds(new ArrayList<>());
    userSearchCriteria.setExcludeStatuses(new ArrayList<>());
    userSearchCriteria.setUsername(user.getUsername());
    userSearchCriteria.setSuperadmin(true);
    userService.findUsers(userSearchCriteria);
    verify(userRepository, times(1)).findAll((Predicate) any());
  }

  @Test
  public void findUserCountTest() {
    userService.findUserCount(userSearchCriteria);
    verify(userRepository, times(1)).findAll((Predicate) any());
  }

  @Test
  public void isAttributeValueUniqueEmptyListTest() {
    assertTrue(userService.isAttributeValueUnique("name", user.getId()));
    verify(userAttributeRepository, times(1)).findAll((Predicate) any());
  }

  @Test
  public void isAttributeValueUniqueTest() {
    List<UserAttribute> userAttributeList = initTestValues
      .createUserAttributes(user);
    when(userAttributeRepository.findAll((Predicate) any()))
      .thenReturn(userAttributeList);
    when(userAttributeMapper
      .mapToDTO(userAttributeRepository.findAll((Predicate) any())))
      .thenReturn(userAttributeDTOList);
    userService.isAttributeValueUnique("name", user.getId());
    verify(userAttributeRepository, times(2)).findAll((Predicate) any());
  }

  @Test
  public void addUserGroupsTest() {
    Collection<String> userGroupList = new ArrayList<>();
    for (UserGroupDTO userGroupDTO : userGroupDTOS){
      userGroupList.add(userGroupDTO.getId());
      when(userGroupRepository.fetchById(userGroupDTO.getId())).thenReturn(userGroup);
    }
    when(userRepository.fetchById(user.getId())).thenReturn(user);
    when(userGroupRepository.findByName(userGroupDTOS.get(0).getName())).thenReturn(userGroup);
    userService.addUserGroups(userGroupList, user.getId());
    assertNotNull(userService.belongsToGroupByName(user.getId(), userGroupDTOS.get(0).getName(),
        false));
    verify(userRepository, times(2)).fetchById(anyString());
  }

  @Test
  public void removeUserGroupsTest(){
    when(userRepository.fetchById(any())).thenReturn(user);
    Collection<String> userGroupList = new ArrayList<>();
    for (UserGroup userGroup : userGroups){
      userGroupList.add(userGroup.getId());
      when(userGroupRepository.fetchById(any())).thenReturn(userGroup);
    }
    userService.addUserGroups(userGroupList, user.getId());

    userService.removeUserGroups(userGroupList, user.getId());
    assertTrue(CollectionUtils.isEmpty(user.getUserGroups()));
    verify(userRepository, times(2)).fetchById(anyString());
  }

  @Test
  public void findDistinctDataAttributesByNameTest(){
    when(userAttributeRepository.findDistinctDataByName(any())).thenReturn(Arrays.asList("attr1", "attr2"));
    List<String> result = userService.findDistinctDataAttributesByName("attributeName");
    assertEquals(2, result.size());
  }
}
