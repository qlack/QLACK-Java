package com.eurodyn.qlack.fuse.aaa.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import bsh.Interpreter;
import com.eurodyn.qlack.fuse.aaa.InitTestValues;
import com.eurodyn.qlack.fuse.aaa.dto.OperationDTO;
import com.eurodyn.qlack.fuse.aaa.exception.DynamicOperationException;
import com.eurodyn.qlack.fuse.aaa.mapper.OperationMapper;
import com.eurodyn.qlack.fuse.aaa.mapper.ResourceMapper;
import com.eurodyn.qlack.fuse.aaa.mapper.UserGroupHasOperationMapper;
import com.eurodyn.qlack.fuse.aaa.model.OpTemplate;
import com.eurodyn.qlack.fuse.aaa.model.Operation;
import com.eurodyn.qlack.fuse.aaa.model.Resource;
import com.eurodyn.qlack.fuse.aaa.model.User;
import com.eurodyn.qlack.fuse.aaa.model.UserGroup;
import com.eurodyn.qlack.fuse.aaa.model.UserGroupHasOperation;
import com.eurodyn.qlack.fuse.aaa.model.UserHasOperation;
import com.eurodyn.qlack.fuse.aaa.repository.OpTemplateRepository;
import com.eurodyn.qlack.fuse.aaa.repository.OperationRepository;
import com.eurodyn.qlack.fuse.aaa.repository.ResourceRepository;
import com.eurodyn.qlack.fuse.aaa.repository.UserGroupHasOperationRepository;
import com.eurodyn.qlack.fuse.aaa.repository.UserGroupRepository;
import com.eurodyn.qlack.fuse.aaa.repository.UserHasOperationRepository;
import com.eurodyn.qlack.fuse.aaa.repository.UserRepository;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * @author European Dynamics
 */

@RunWith(MockitoJUnitRunner.class)
public class OperationServiceTest {

  @InjectMocks
  OperationService operationService;

  private OperationRepository operationRepository = mock(
    OperationRepository.class);
  private UserHasOperationRepository userHasOperationRepository = mock(
    UserHasOperationRepository.class);
  private UserRepository userRepository = mock(UserRepository.class);
  private ResourceRepository resourceRepository = mock(
    ResourceRepository.class);
  private OpTemplateRepository opTemplateRepository = mock(
    OpTemplateRepository.class);
  private UserGroupHasOperationRepository userGroupHasOperationRepository = mock(
    UserGroupHasOperationRepository.class);
  private UserGroupRepository userGroupRepository = mock(
    UserGroupRepository.class);
  @Mock
  private Interpreter i;

  @Spy
  private OperationMapper operationMapper;

  @Spy
  private ResourceMapper resourceMapper;

  @Spy
  private UserGroupHasOperationMapper userGroupHasOperationMapper;

  private InitTestValues initTestValues;
  private Operation operation;
  private OperationDTO operationDTO;
  private List<Operation> operations;
  private List<OperationDTO> operationsDTO;
  private User user;
  private UserHasOperation userHasOperation;
  private Resource resource;
  private OpTemplate opTemplate;
  private UserGroupHasOperation userGroupHasOperation;
  private UserGroup userGroup;
  private boolean isTrue;
  private boolean isFalse;
  private List<UserGroup> userGroups;

  private List<User> superadmins;
  private List<User> users;
  private Set<String> superadminsID;
  private User allowedUser;
  private User blockedUser;
  private Set<String> usersID;
  private List<UserHasOperation> userHasOperations;
  private List<UserGroupHasOperation> userGroupHasOperationList;

  private int countUsers;
  private long blockedUsers;
  private final String dynamicCode =
    "retVal = (userId != null && groupdId != null && resourceID != null)";


  @Before
  public void init() {
    operationService = new OperationService(operationRepository,
      userHasOperationRepository,
      userRepository, resourceRepository,
      opTemplateRepository, userGroupHasOperationRepository,
      userGroupRepository, operationMapper,
      resourceMapper,
      userGroupHasOperationMapper);
    initTestValues = new InitTestValues();
    operation = initTestValues.createOperation();
    operation.setUserHasOperations(initTestValues.createUserHasOperations());
    operationDTO = initTestValues.createOperationDTO();
    operations = initTestValues.createOperations();
    operationsDTO = initTestValues.createOperationsDTO();
    user = initTestValues.createUser();
    user.setUserHasOperations(initTestValues.createUserHasOperations());
    userHasOperation = initTestValues.createUserHasOperation();
    resource = initTestValues.createResource();
    opTemplate = initTestValues.createOpTemplate();
    opTemplate.setOpTemplateHasOperations(
      initTestValues.createOpTemplateHasOperations());
    userGroupHasOperation = initTestValues.createUserGroupHasOperation();
    userGroup = initTestValues.createUserGroup();
    userGroups = initTestValues.createUserGroupsOnlyOne();

    superadmins = initTestValues.createUsers();
    superadminsID = new HashSet<>();
    superadmins.stream()
      .forEach(superadmin -> superadminsID.add(superadmin.getId()));

    users = new ArrayList<>();

    allowedUser = initTestValues.createUser();
    allowedUser.setId("3857ae6e-ee0b-41c6-b905-8d5cdfab87c1");
    allowedUser.setSuperadmin(false);

    blockedUser = initTestValues.createUser();
    blockedUser.setId("1857ae6e-ee0b-41c6-b905-8d5cdfab87c1");
    blockedUser.setSuperadmin(false);

    users.add(allowedUser);
    users.add(blockedUser);

    usersID = new HashSet<>();
    users.stream().forEach(u -> usersID.add(u.getId()));

    userHasOperations = initTestValues.createUserHasOperations();

    //Operation is allowed for newuser and blocked for newuser2
    userHasOperations.get(0).setUser(allowedUser);
    userHasOperations.get(1).setUser(blockedUser);
    userHasOperations.get(1).setDeny(true);

    countUsers = superadmins.size() + users.size();
    blockedUsers = userHasOperations.stream().filter(uho -> uho.isDeny())
      .count();

    isFalse = false;
    isTrue = true;

    userGroupHasOperationList = initTestValues
      .createUserGroupHasOperationList();
  }

  @Test
  public void testCreateOperation() {
    when(operationMapper.mapToEntity(operationDTO)).thenReturn(operation);
    String createdOperationId = operationService.createOperation(operationDTO);
    assertEquals(operationDTO.getId(), createdOperationId);
    verify(operationRepository, times(1)).save(operation);
  }

  @Test
  public void testUpdateOperation() {
    when(operationRepository.fetchById(operationDTO.getId()))
      .thenReturn(operation);
    operationService.updateOperation(operationDTO);
    verify(operationMapper, times(1))
      .mapToExistingEntity(operationDTO, operation);
  }

  @Test
  public void testDeleteOperation() {
    Operation operation2 = new InitTestValues().createOperation();
    when(operationRepository.fetchById(operationDTO.getId()))
      .thenReturn(operation2);
    operationService.deleteOperation(operationDTO.getId());
    verify(operationRepository, times(1)).delete(operation2);
  }

  @Test
  public void testGetAllOperations() {
    when(operationRepository.findAll()).thenReturn(operations);
    when(operationMapper.mapToDTO(operations)).thenReturn(operationsDTO);
    List<OperationDTO> allOperations = operationService.getAllOperations();
    assertEquals(operations.size(), allOperations.size());
  }

  @Test
  public void testGetOperationByName() {
    when(operationRepository.findByName(operation.getName()))
      .thenReturn(operation);
    when(operationMapper.mapToDTO(operation)).thenReturn(operationDTO);
    OperationDTO operationBYNameDTO = operationService
      .getOperationByName(operation.getName());
    assertEquals(operationBYNameDTO, operationDTO);
  }

  @Test
  public void testAddOperationToUserExisting() {
    when(userHasOperationRepository
      .findByUserIdAndOperationName(user.getId(), operation.getName()))
      .thenReturn(userHasOperation);
    operationService
      .addOperationToUser(user.getId(), operation.getName(), true);
    assertTrue(userHasOperation.isDeny());
  }

  @Test
  public void testAddOperationToUserNew() {
    when(userRepository.fetchById(user.getId())).thenReturn(user);
    when(operationRepository.findByName(operation.getName()))
      .thenReturn(operation);
    operationService
      .addOperationToUser(user.getId(), operation.getName(), true);
    verify(userHasOperationRepository, times(1)).save(any());
  }

  @Test
  public void testAddOperationToUserWithResourceExisting() {
    when(userHasOperationRepository
      .findByUserIdAndResourceIdAndOperationName(user.getId(), resource.getId(),
        operation.getName()))
      .thenReturn(userHasOperation);
    operationService
      .addOperationToUser(user.getId(), operation.getName(), resource.getId(),
        true);
    assertTrue(userHasOperation.isDeny());
  }

  @Test
  public void testAddOperationToUserWithResourceNew() {
    when(userRepository.fetchById(user.getId())).thenReturn(user);
    when(operationRepository.findByName(operation.getName()))
      .thenReturn(operation);
    when(resourceRepository.fetchById(resource.getId())).thenReturn(resource);
    operationService
      .addOperationToUser(user.getId(), operation.getName(), resource.getId(),
        true);
    verify(userHasOperationRepository, times(1)).save(any());
  }

  @Test
  public void testAddOperationsToUserFromTemplateID() {
    when(userRepository.fetchById(user.getId())).thenReturn(user);
    when(operationRepository.findByName(operation.getName()))
      .thenReturn(operation);
    when(opTemplateRepository.fetchById(opTemplate.getId()))
      .thenReturn(opTemplate);
    operationService
      .addOperationsToUserFromTemplateID(user.getId(), opTemplate.getId());
    verify(userHasOperationRepository,
      times(opTemplate.getOpTemplateHasOperations().size()))
      .save(any());
  }

  @Test
  public void testAddOperationsToUserFromTemplateName() {
    when(userRepository.fetchById(user.getId())).thenReturn(user);
    when(operationRepository.findByName(operation.getName()))
      .thenReturn(operation);
    when(opTemplateRepository.findByName(opTemplate.getName()))
      .thenReturn(opTemplate);
    operationService
      .addOperationsToUserFromTemplateName(user.getId(), opTemplate.getName());
    verify(userHasOperationRepository,
      times(opTemplate.getOpTemplateHasOperations().size()))
      .save(any());
  }

  @Test
  public void testAddOperationsToUserFromTemplateIdWithResource() {
    opTemplate.getOpTemplateHasOperations().stream()
      .forEach(
        opTemplateHasOperation -> opTemplateHasOperation.setResource(resource));
    when(userRepository.fetchById(user.getId())).thenReturn(user);
    when(operationRepository.findByName(operation.getName()))
      .thenReturn(operation);
    when(opTemplateRepository.fetchById(opTemplate.getId()))
      .thenReturn(opTemplate);
    when(resourceRepository.fetchById(resource.getId())).thenReturn(resource);
    operationService
      .addOperationsToUserFromTemplateID(user.getId(), opTemplate.getId());
    verify(userHasOperationRepository,
      times(opTemplate.getOpTemplateHasOperations().size()))
      .save(any());
  }

  @Test
  public void testAddOperationsToUserFromTemplateNameWithResource() {
    opTemplate.getOpTemplateHasOperations().stream()
      .forEach(
        opTemplateHasOperation -> opTemplateHasOperation.setResource(resource));
    when(userRepository.fetchById(user.getId())).thenReturn(user);
    when(operationRepository.findByName(operation.getName()))
      .thenReturn(operation);
    when(opTemplateRepository.findByName(opTemplate.getName()))
      .thenReturn(opTemplate);
    when(resourceRepository.fetchById(resource.getId())).thenReturn(resource);
    operationService
      .addOperationsToUserFromTemplateName(user.getId(), opTemplate.getName());
    verify(userHasOperationRepository,
      times(opTemplate.getOpTemplateHasOperations().size()))
      .save(any());
  }

  @Test
  public void testAddOperationToGroupExisting() {
    when(userGroupHasOperationRepository
      .findByUserGroupIdAndOperationName(userGroup.getId(),
        operation.getName()))
      .thenReturn(userGroupHasOperation);
    operationService
      .addOperationToGroup(userGroup.getId(), operation.getName(), true);
    assertTrue(userGroupHasOperation.isDeny());
  }

  @Test
  public void testAddOperationToGroupNew() {
    when(userGroupRepository.fetchById(userGroup.getId()))
      .thenReturn(userGroup);
    when(operationRepository.findByName(operation.getName()))
      .thenReturn(operation);
    operationService
      .addOperationToGroup(userGroup.getId(), operation.getName(), true);
    verify(userGroupHasOperationRepository, times(1)).save(any());
  }

  @Test
  public void testAddOperationToGroupExistingWithResource() {
    when(userGroupHasOperationRepository
      .findByUserGroupIdAndResourceIdAndOperationName(userGroup.getId(),
        operation.getName(),
        resource.getId())).thenReturn(userGroupHasOperation);
    operationService
      .addOperationToGroup(userGroup.getId(), operation.getName(),
        resource.getId(), true);
    assertTrue(userGroupHasOperation.isDeny());
  }

  @Test
  public void testAddOperationToGroupNewWithResource() {
    when(userGroupRepository.fetchById(userGroup.getId()))
      .thenReturn(userGroup);
    when(operationRepository.findByName(operation.getName()))
      .thenReturn(operation);
    when(resourceRepository.fetchById(resource.getId())).thenReturn(resource);
    operationService
      .addOperationToGroup(userGroup.getId(), operation.getName(),
        resource.getId(), true);
    verify(userGroupHasOperationRepository, times(1)).save(any());
  }

  @Test
  public void testAddOperationsToGroupFromTemplateID() {
    when(userGroupRepository.fetchById(userGroup.getId()))
      .thenReturn(userGroup);
    when(operationRepository.findByName(operation.getName()))
      .thenReturn(operation);
    when(opTemplateRepository.fetchById(opTemplate.getId()))
      .thenReturn(opTemplate);
    operationService.addOperationsToGroupFromTemplateID(userGroup.getId(),
      opTemplate.getId());
    verify(userGroupHasOperationRepository,
      times(opTemplate.getOpTemplateHasOperations().size()))
      .save(any());
  }

  @Test
  public void testAddOperationsToGroupFromTemplateName() {
    when(userGroupRepository.fetchById(userGroup.getId()))
      .thenReturn(userGroup);
    when(operationRepository.findByName(operation.getName()))
      .thenReturn(operation);
    when(opTemplateRepository.findByName(opTemplate.getName()))
      .thenReturn(opTemplate);
    operationService.addOperationsToGroupFromTemplateName(userGroup.getId(),
      opTemplate.getName());
    verify(userGroupHasOperationRepository,
      times(opTemplate.getOpTemplateHasOperations().size()))
      .save(any());
  }

  @Test
  public void testAddOperationsToGroupFromTemplateIdWithResource() {
    opTemplate.getOpTemplateHasOperations().stream()
      .forEach(
        opTemplateHasOperation -> opTemplateHasOperation.setResource(resource));
    when(opTemplateRepository.fetchById(opTemplate.getId()))
      .thenReturn(opTemplate);
    when(userGroupRepository.fetchById(userGroup.getId()))
      .thenReturn(userGroup);
    when(operationRepository.findByName(operation.getName()))
      .thenReturn(operation);
    when(resourceRepository.fetchById(resource.getId())).thenReturn(resource);

    operationService.addOperationsToGroupFromTemplateID(userGroup.getId(),
      opTemplate.getId());
    verify(userGroupHasOperationRepository,
      times(opTemplate.getOpTemplateHasOperations().size()))
      .save(any());
  }

  @Test
  public void testAddOperationsToGroupFromTemplateNameWithResource() {
    opTemplate.getOpTemplateHasOperations().stream()
      .forEach(
        opTemplateHasOperation -> opTemplateHasOperation.setResource(resource));
    when(opTemplateRepository.findByName(opTemplate.getName()))
      .thenReturn(opTemplate);
    when(userGroupRepository.fetchById(userGroup.getId()))
      .thenReturn(userGroup);
    when(operationRepository.findByName(operation.getName()))
      .thenReturn(operation);
    when(resourceRepository.fetchById(resource.getId())).thenReturn(resource);

    operationService.addOperationsToGroupFromTemplateName(userGroup.getId(),
      opTemplate.getName());
    verify(userGroupHasOperationRepository,
      times(opTemplate.getOpTemplateHasOperations().size()))
      .save(any());
  }

  @Test
  public void testRemoveOperationFromUser() {
    UserHasOperation userHasOperation2 = initTestValues
      .createUserHasOperation();
    when(userHasOperationRepository
      .findByUserIdAndOperationName(user.getId(), operation.getName()))
      .thenReturn(userHasOperation2);
    operationService.removeOperationFromUser(user.getId(), operation.getName());
    verify(userHasOperationRepository, times(1)).delete(userHasOperation2);
  }

  @Test
  public void testRemoveOperationFromUserWithResourceId() {
    UserHasOperation userHasOperation2 = initTestValues
      .createUserHasOperation();
    when(userHasOperationRepository
      .findByUserIdAndResourceIdAndOperationName(user.getId(), resource.getId(),
        operation.getName())).thenReturn(userHasOperation2);
    operationService.removeOperationFromUser(user.getId(), operation.getName(),
      resource.getId());
    verify(userHasOperationRepository, times(1)).delete(userHasOperation2);
  }

  @Test
  public void testRemoveOperationFromUserNull() {
    when(userHasOperationRepository
      .findByUserIdAndOperationName(user.getId(), operation.getName()))
      .thenReturn(null);
    operationService.removeOperationFromUser(user.getId(), operation.getName());
    verify(userHasOperationRepository, times(1))
      .findByUserIdAndOperationName(any(), any());
  }

  @Test
  public void testRemoveOperationFromUserNullUho() {
    when(userHasOperationRepository
      .findByUserIdAndResourceIdAndOperationName(user.getId(), resource.getId(),
        operation.getName())).thenReturn(null);
    operationService.removeOperationFromUser(user.getId(), operation.getName(),
      resource.getId());
    verify(userHasOperationRepository, times(1))
      .findByUserIdAndResourceIdAndOperationName(any(), any(), any());
  }

  @Test
  public void testRemoveOperationFromGroup() {
    UserGroupHasOperation userGroupHasOperation2 = initTestValues
      .createUserGroupHasOperation();
    when(userGroupHasOperationRepository
      .findByUserGroupIdAndOperationName(userGroup.getId(),
        operation.getName()))
      .thenReturn(userGroupHasOperation2);
    operationService
      .removeOperationFromGroup(userGroup.getId(), operation.getName());
    verify(userGroupHasOperationRepository, times(1))
      .delete(userGroupHasOperation2);
  }

  @Test
  public void testRemoveOperationFromGroupWithResourceId() {
    UserGroupHasOperation userGroupHasOperation2 = initTestValues
      .createUserGroupHasOperation();
    when(userGroupHasOperationRepository
      .findByUserGroupIdAndResourceIdAndOperationName(userGroup.getId(),
        resource.getId(),
        operation.getName())).thenReturn(userGroupHasOperation2);
    operationService
      .removeOperationFromGroup(userGroup.getId(), operation.getName(),
        resource.getId());
    verify(userGroupHasOperationRepository, times(1))
      .delete(userGroupHasOperation2);
  }

  @Test
  public void testRemoveOperationFromGroupNull() {
    when(userGroupHasOperationRepository
      .findByUserGroupIdAndOperationName(userGroup.getId(),
        operation.getName()))
      .thenReturn(null);
    operationService
      .removeOperationFromGroup(userGroup.getId(), operation.getName());
    verify(userGroupHasOperationRepository, times(1))
      .findByUserGroupIdAndOperationName(any(), any());
  }

  @Test
  public void testRemoveOperationFromGroupNullUho() {
    when(userGroupHasOperationRepository
      .findByUserGroupIdAndResourceIdAndOperationName(userGroup.getId(),
        resource.getId(),
        operation.getName())).thenReturn(null);
    operationService
      .removeOperationFromGroup(userGroup.getId(), operation.getName(),
        resource.getId());
    verify(userGroupHasOperationRepository, times(1)).
      findByUserGroupIdAndResourceIdAndOperationName(any(), any(), any());
  }

  @Test
  public void testIsPermittedForSuperadmin() {
    user.setSuperadmin(true);
    when(userRepository.fetchById(user.getId())).thenReturn(user);
    Boolean permitted = operationService
      .isPermitted(user.getId(), operation.getName());
    assertEquals(user.isSuperadmin(), permitted);
  }

  @Test
  public void testIsPermittedForUserNotDynamicOperation() {
    user.setSuperadmin(false);
    userHasOperation.setDeny(true);
    when(userRepository.fetchById(user.getId())).thenReturn(user);
    when(operationRepository.findByName(operation.getName()))
      .thenReturn(operation);
    when(userHasOperationRepository
      .findByUserIdAndOperationName(user.getId(), operation.getName()))
      .thenReturn(userHasOperation);
    Boolean permitted = operationService
      .isPermitted(user.getId(), operation.getName());
    assertEquals(!userHasOperation.isDeny(), permitted);
  }

  @Test
  public void testIsPermittedForUserWithResourceIdAndNotDynamicOperation() {
    user.setSuperadmin(false);
    when(userRepository.fetchById(user.getId())).thenReturn(user);
    when(operationRepository.findByName(operation.getName()))
      .thenReturn(operation);
    when(resourceRepository.findByObjectId(resource.getObjectId()))
      .thenReturn(resource);
    when(userHasOperationRepository
      .findByUserIdAndResourceIdAndOperationName(user.getId(), resource.getId(),
        operation.getName()))
      .thenReturn(userHasOperation);
    Boolean permitted = operationService
      .isPermitted(user.getId(), operation.getName(), resource.getObjectId());
    assertEquals(!userHasOperation.isDeny(), permitted);
  }

  @Test
  public void testIsPermittedForUserWithDynamicOperation() {
    user.setSuperadmin(false);
    operation.setDynamic(true);
    operation.setDynamicCode(dynamicCode);
    when(userRepository.fetchById(user.getId())).thenReturn(user);
    when(operationRepository.findByName(operation.getName()))
      .thenReturn(operation);
    when(userHasOperationRepository
      .findByUserIdAndOperationName(user.getId(), operation.getName()))
      .thenReturn(userHasOperation);
    Boolean permitted = operationService
      .isPermitted(user.getId(), operation.getName());
    assertTrue(permitted);
  }

  @Test(expected = DynamicOperationException.class)
  public void testIsPermittedForUserWithDynamicOperationException() {
    String dynamicCode = "retVal = (userId ";
    user.setSuperadmin(false);
    operation.setDynamic(true);
    operation.setDynamicCode(dynamicCode);
    when(userRepository.fetchById(user.getId())).thenReturn(user);
    when(operationRepository.findByName(operation.getName()))
      .thenReturn(operation);
    when(userHasOperationRepository
      .findByUserIdAndOperationName(user.getId(), operation.getName()))
      .thenReturn(userHasOperation);
    Boolean permitted = operationService
      .isPermitted(user.getId(), operation.getName());
    assertTrue(permitted);
  }


  @Test
  public void testIsPermittedForUserWithoutUserGroup() {
    user.setSuperadmin(false);
    user.setUserGroups(new ArrayList<>());
    when(userRepository.fetchById(user.getId())).thenReturn(user);
    Boolean permitted = operationService
      .isPermitted(user.getId(), operation.getName());
    assertFalse(permitted);
  }

  @Test
  public void testIsPermittedForUserWithUserGroup() {
    user.setSuperadmin(false);
    user.setUserGroups(userGroups);
    when(userRepository.fetchById(user.getId())).thenReturn(user);
    when(operationRepository.findByName(operation.getName()))
      .thenReturn(operation);
    for (UserGroup userGroup : user.getUserGroups()) {
      when(userGroupHasOperationRepository
        .findByUserGroupIdAndOperationName(userGroup.getId(),
          operation.getName()))
        .thenReturn(userGroupHasOperation);
    }
    Boolean permitted = operationService
      .isPermitted(user.getId(), operation.getName());
    assertFalse(permitted);
  }

  @Test
  public void testIsPermittedGroupPermission() {
    user.setSuperadmin(false);
    user.setUserGroups(userGroups);
    operation.setDynamic(true);
    operation.setDynamicCode("retVal = false");
    userGroupHasOperation.setDeny(true);
    operationService.setPrioritisePositive(false);
    when(userRepository.fetchById(user.getId())).thenReturn(user);
    when(operationRepository.findByName(operation.getName()))
      .thenReturn(operation);
    for (UserGroup userGroup : user.getUserGroups()) {
      when(userGroupHasOperationRepository
        .findByUserGroupIdAndOperationName(userGroup.getId(),
          operation.getName()))
        .thenReturn(userGroupHasOperation);
    }
    when(userHasOperationRepository.findByUserIdAndOperationName(any(), any()))
      .thenReturn(null);
    Boolean permitted = operationService
      .isPermitted(user.getId(), operation.getName());
    assertFalse(permitted);
  }

  @Test
  public void testIsPermittedNullGroupPermission() {
    user.setSuperadmin(false);
    user.setUserGroups(userGroups);
    userGroup.setParent(null);
    when(userRepository.fetchById(user.getId())).thenReturn(user);
    when(userGroupRepository.fetchById(any())).thenReturn(userGroup);
    when(operationRepository.findByName(operation.getName()))
      .thenReturn(operation);
    for (UserGroup userGroup : user.getUserGroups()) {
      when(userGroupHasOperationRepository
        .findByUserGroupIdAndOperationName(userGroup.getId(),
          operation.getName()))
        .thenReturn(null);
    }
    Boolean permitted = operationService
      .isPermitted(user.getId(), operation.getName());
    assertFalse(permitted);
  }

  @Test
  public void testIsPermittedForGroupNotDynamicOperation() {
    when(userGroupRepository.fetchById(userGroup.getId()))
      .thenReturn(userGroup);
    when(operationRepository.findByName(operation.getName()))
      .thenReturn(operation);
    when(userGroupHasOperationRepository
      .findByUserGroupIdAndOperationName(userGroup.getId(),
        operation.getName()))
      .thenReturn(userGroupHasOperation);
    Boolean permittedForGroup = operationService
      .isPermittedForGroup(userGroup.getId(), operation.getName());
    assertEquals(!userGroupHasOperation.isDeny(), permittedForGroup);
  }


  @Test
  public void testIsPermittedForGroupWithResoourceAndNotDynamicOperation() {
    when(userGroupRepository.fetchById(userGroup.getId()))
      .thenReturn(userGroup);
    when(operationRepository.findByName(operation.getName()))
      .thenReturn(operation);
    when(resourceRepository.findByObjectId(resource.getObjectId()))
      .thenReturn(resource);
    when(userGroupHasOperationRepository
      .findByUserGroupIdAndResourceIdAndOperationName(userGroup.getId(),
        resource.getId(),
        operation.getName()))
      .thenReturn(userGroupHasOperation);
    Boolean permittedForGroup = operationService
      .isPermittedForGroup(userGroup.getId(), operation.getName(),
        resource.getObjectId());
    assertEquals(!userGroupHasOperation.isDeny(), permittedForGroup);
  }

  @Test
  public void testIsPermittedForGroupWithDynamicOperation() {
    String dynamicCode = "retVal = (groupdId != null && resourceID != null)";
    operation.setDynamic(true);
    operation.setDynamicCode(dynamicCode);
    when(userGroupRepository.fetchById(userGroup.getId()))
      .thenReturn(userGroup);
    when(operationRepository.findByName(operation.getName()))
      .thenReturn(operation);
    when(userGroupHasOperationRepository
      .findByUserGroupIdAndOperationName(userGroup.getId(),
        operation.getName()))
      .thenReturn(userGroupHasOperation);
    Boolean permittedForGroup = operationService
      .isPermittedForGroup(userGroup.getId(), operation.getName());
    assertTrue(permittedForGroup);
  }

  @Test
  public void testIsPermittedForGroupByParent() {
    when(userGroupRepository.fetchById(userGroup.getId()))
      .thenReturn(userGroup);
    when(operationRepository.findByName(operation.getName()))
      .thenReturn(operation);
    when(userGroupHasOperationRepository
      .findByUserGroupIdAndOperationName(userGroup.getParent().getId(),
        operation.getName())).
      thenReturn(userGroupHasOperation);
    Boolean permittedForGroup = operationService
      .isPermittedForGroup(userGroup.getId(), operation.getName());
    assertEquals(!userGroupHasOperation.isDeny(), permittedForGroup);
  }

  @Test
  public void testIsPermittedForGroupByResource() {
    when(userGroupRepository.fetchById(userGroup.getId()))
      .thenReturn(userGroup);
    when(userGroupHasOperationRepository
      .findByUserGroupIdAndOperationNameAndResourceNameAndResourceObjectId(
        userGroup.getId(),
        operation.getName(), resource.getName(), resource.getObjectId()))
      .thenReturn(userGroupHasOperation);
    Boolean permittedForGroupByResource = operationService
      .isPermittedForGroupByResource(userGroup.getId(), operation.getName(),
        resource.getName(),
        resource.getObjectId());
    assertEquals(!userGroupHasOperation.isDeny(), permittedForGroupByResource);

    userGroupHasOperation.setDeny(true);
    operationService
      .isPermittedForGroupByResource(userGroup.getId(), operation.getName(),
        resource.getName(),
        resource.getObjectId());
    verify(userGroupHasOperationRepository, times(2)).
      findByUserGroupIdAndOperationNameAndResourceNameAndResourceObjectId(any(),
        any(), any(),
        any());
  }

  @Test
  public void testIsPermittedForGroupByResourceNullGho() {
    when(userGroupRepository.fetchById(userGroup.getId()))
      .thenReturn(userGroup);
    when(userGroupRepository.fetchById(userGroup.getParent().getId()))
      .thenReturn(new UserGroup());
    when(userGroupHasOperationRepository
      .findByUserGroupIdAndOperationNameAndResourceNameAndResourceObjectId(
        userGroup.getId(),
        operation.getName(), resource.getName(), resource.getObjectId()))
      .thenReturn(null);
    when(resourceRepository.findByObjectId(resource.getObjectId()))
      .thenReturn(resource);
    Boolean permittedForGroupByResource = operationService
      .isPermittedForGroupByResource(userGroup.getId(), operation.getName(),
        resource.getName(),
        resource.getObjectId());
    assertNull(permittedForGroupByResource);

    userGroup.setParent(null);
    permittedForGroupByResource = operationService
      .isPermittedForGroupByResource(userGroup.getId(), operation.getName(),
        resource.getName(),
        resource.getObjectId());
    assertNull(permittedForGroupByResource);
    verify(userGroupRepository, times(3)).fetchById(any());
  }

  @Test
  public void testGetUsersForOperationByOperationName() {
    when(userRepository.getUserIds(false)).thenReturn(usersID);
    when(userRepository.getUserIds(true)).thenReturn(superadminsID);
    when(userHasOperationRepository.findByOperationName(operation.getName()))
      .thenReturn(userHasOperations);

    Set<String> allowedUsersForOperation = operationService.
      getAllowedUsersForOperation(operation.getName(), false);
    assertEquals(countUsers - 1, allowedUsersForOperation.size());
  }

  @Test
  public void testGetAllowedUsersForOperation() {
    when(userRepository.getUserIds(false)).thenReturn(usersID);
    when(userRepository.getUserIds(true)).thenReturn(superadminsID);
    when(userRepository.fetchById(any())).thenReturn(user);
    user.setUserGroups(userGroups);
    when(userGroupRepository.fetchById(any())).thenReturn(new UserGroup());
    when(operationRepository.findByName(any())).thenReturn(operation);
    when(userGroupHasOperationRepository
      .findByUserGroupIdAndOperationName(any(), any()))
      .thenReturn(userGroupHasOperation);

    operationService.getAllowedUsersForOperation(operation.getName(), true);
    verify(userRepository, times(2)).fetchById(any());
  }

  @Test
  public void testGetBlockedUsersForOperationNoAllowed() {
    when(userRepository.getUserIds(false)).thenReturn(usersID);
    when(userRepository.getUserIds(true)).thenReturn(superadminsID);
    when(userRepository.fetchById(any())).thenReturn(user);
    user.setUserGroups(userGroups);
    when(userGroupRepository.fetchById(any())).thenReturn(new UserGroup());
    when(operationRepository.findByName(any())).thenReturn(operation);
    when(userGroupHasOperationRepository
      .findByUserGroupIdAndOperationName(any(), any()))
      .thenReturn(userGroupHasOperation);

    operationService.getBlockedUsersForOperation(operation.getName(), true);
    verify(userRepository, times(2)).fetchById(any());
  }

  @Test
  public void testGetAllowedUsersForOperationResourceId() {
    userGroupHasOperation.setDeny(true);
    when(userRepository.getUserIds(false)).thenReturn(usersID);
    when(userRepository.getUserIds(true)).thenReturn(superadminsID);
    when(userRepository.fetchById(any())).thenReturn(user);
    user.setUserGroups(userGroups);
    when(userGroupRepository.fetchById(userGroup.getId()))
      .thenReturn(userGroup);
    when(userGroupRepository.fetchById(userGroup.getParent().getId()))
      .thenReturn(new UserGroup());
    when(userGroupHasOperationRepository.
      findByUserGroupIdAndResourceIdAndOperationName(
        userGroup.getParent().getId(),
        resource.getId(), operation.getName()))
      .thenReturn(userGroupHasOperation);
    when(operationRepository.findByName(operation.getName()))
      .thenReturn(operation);
    when(resourceRepository.findByObjectId(resource.getObjectId()))
      .thenReturn(resource);

    operationService
      .getAllowedUsersForOperation(operation.getName(), resource.getObjectId(),
        true);
    verify(userRepository, times(2)).getUserIds(any());
  }

  @Test
  public void testGetAllowedUsersForOperationNullGroupPermission() {
    userGroupHasOperation.setDeny(true);
    when(userRepository.getUserIds(false)).thenReturn(usersID);
    when(userRepository.getUserIds(true)).thenReturn(superadminsID);
    when(userRepository.fetchById(any())).thenReturn(user);
    user.setUserGroups(userGroups);
    userGroup.setParent(null);
    when(userGroupRepository.fetchById(userGroup.getId()))
      .thenReturn(userGroup);
    when(operationRepository.findByName(operation.getName()))
      .thenReturn(operation);
    when(resourceRepository.findByObjectId(resource.getObjectId()))
      .thenReturn(resource);

    operationService
      .getAllowedUsersForOperation(operation.getName(), resource.getObjectId(),
        true);
    verify(userRepository, times(2)).getUserIds(any());
  }

  @Test
  public void testGetBlockedUsersForOperationNotGetAllowed() {
    userGroupHasOperation.setDeny(true);
    when(userRepository.getUserIds(false)).thenReturn(usersID);
    when(userRepository.getUserIds(true)).thenReturn(superadminsID);
    when(userRepository.fetchById(any())).thenReturn(user);
    user.setUserGroups(userGroups);
    when(userGroupRepository.fetchById(userGroup.getId()))
      .thenReturn(userGroup);
    when(userGroupRepository.fetchById(userGroup.getParent().getId()))
      .thenReturn(new UserGroup());
    when(userGroupHasOperationRepository.
      findByUserGroupIdAndResourceIdAndOperationName(
        userGroup.getParent().getId(),
        resource.getId(), operation.getName()))
      .thenReturn(userGroupHasOperation);
    when(operationRepository.findByName(operation.getName()))
      .thenReturn(operation);
    when(resourceRepository.findByObjectId(resource.getObjectId()))
      .thenReturn(resource);

    operationService
      .getBlockedUsersForOperation(operation.getName(), resource.getObjectId(),
        true);
    verify(userRepository, times(2)).getUserIds(any());
  }

  @Test
  public void testGetUsersForOperationByOperationNameAndResource() {

    when(userRepository.getUserIds(false)).thenReturn(usersID);
    when(userRepository.getUserIds(true)).thenReturn(superadminsID);
    when(resourceRepository.findByObjectId(resource.getObjectId()))
      .thenReturn(resource);
    when(userHasOperationRepository
      .findByResourceIdAndOperationName(resource.getId(), operation.getName()))
      .thenReturn(userHasOperations);

    Set<String> allowedUsersForOperation = operationService
      .getAllowedUsersForOperation(operation.getName(), resource.getObjectId(),
        false);
    assertEquals(countUsers - blockedUsers, allowedUsersForOperation.size());
  }

  @Test
  public void testGetUsersForOperationWithDynamicCodeByOperationName() {
    String dynamicCode = "retVal = userID != \"1857ae6e-ee0b-41c6-b905-8d5cdfab87c1\"";
    userHasOperations.stream()
      .forEach(uho -> uho.getOperation().setDynamic(true));
    userHasOperations.stream()
      .forEach(uho -> uho.getOperation().setDynamicCode(dynamicCode));

    when(userRepository.getUserIds(false)).thenReturn(usersID);
    when(userRepository.getUserIds(true)).thenReturn(superadminsID);
    when(userHasOperationRepository.findByOperationName(operation.getName()))
      .thenReturn(userHasOperations);

    Set<String> allowedUsersForOperation = operationService.
      getAllowedUsersForOperation(operation.getName(), false);
    assertEquals(countUsers - blockedUsers, allowedUsersForOperation.size());
  }

  @Test
  public void testGetUsersForOperationWithDynamicCodeByOperationNameDynamicResultNull() {
    userHasOperations.stream()
      .forEach(uho -> uho.getOperation().setDynamic(true));
    userHasOperations.stream()
      .forEach(uho -> uho.getOperation().setDynamicCode(dynamicCode));

    when(userRepository.getUserIds(false)).thenReturn(usersID);
    when(userRepository.getUserIds(true)).thenReturn(superadminsID);
    when(userHasOperationRepository.findByOperationName(operation.getName()))
      .thenReturn(userHasOperations);

    Set<String> allowedUsersForOperation = operationService.
      getAllowedUsersForOperation(operation.getName(), false);
    assertEquals(countUsers, allowedUsersForOperation.size());
  }

  @Test
  public void testGetBlockedUsersForOperationByOperationName() {

    when(userRepository.getUserIds(false)).thenReturn(usersID);
    when(userRepository.getUserIds(true)).thenReturn(superadminsID);
    when(userHasOperationRepository.findByOperationName(operation.getName()))
      .thenReturn(userHasOperations);

    Set<String> allowedUsersForOperation = operationService
      .getBlockedUsersForOperation(operation.getName(), false);
    assertEquals(blockedUsers, allowedUsersForOperation.size());
  }

  @Test
  public void testGetBlockedUsersForOperationByOperationNameAndResource() {
    userHasOperations.stream().filter(uho -> uho.isDeny()).count();
    when(userRepository.getUserIds(false)).thenReturn(usersID);
    when(userRepository.getUserIds(true)).thenReturn(superadminsID);
    when(resourceRepository.findByObjectId(resource.getObjectId()))
      .thenReturn(resource);
    when(userHasOperationRepository
      .findByResourceIdAndOperationName(resource.getId(), operation.getName()))
      .thenReturn(userHasOperations);

    Set<String> allowedUsersForOperation = operationService
      .getBlockedUsersForOperation(operation.getName(), resource.getObjectId(),
        false);
    assertEquals(blockedUsers, allowedUsersForOperation.size());
  }

  @Test
  public void testGetBlockedUsersForOperation() {
    userHasOperations.stream().filter(uho -> uho.isDeny()).count();
    when(userRepository.getUserIds(false)).thenReturn(usersID);
    when(userRepository.getUserIds(true)).thenReturn(superadminsID);
    when(resourceRepository.findByObjectId(resource.getObjectId()))
      .thenReturn(resource);
    when(userHasOperationRepository
      .findByResourceIdAndOperationName(resource.getId(), operation.getName()))
      .thenReturn(userHasOperations);

    Set<String> allowedUsersForOperation = operationService
      .getBlockedUsersForOperation(operation.getName(), resource.getObjectId(),
        true);
    assertEquals(blockedUsers, allowedUsersForOperation.size());
  }

  @Test
  public void testGetPrioritisePositive() {
    operationService.setPrioritisePositive(isTrue);
    assertTrue(operationService.getPrioritisePositive());

    operationService.setPrioritisePositive(isFalse);
    assertFalse(operationService.getPrioritisePositive());
  }

  @Test
  public void testGetAllowedGroupsForOperation() {
    operationService.getAllowedGroupsForOperation(operation.getName(), false);
    verify(userGroupRepository, times(1)).getAllIds();
  }

  @Test
  public void testGetAllowedGroupsForOperationNullResourceId() {
    operationService
      .getAllowedGroupsForOperation(operation.getName(), null, false);
    verify(userGroupRepository, times(1)).getAllIds();
  }

  @Test
  public void testGetAllowedGroupsForOperationResourceId() {
    when(resourceRepository.findByObjectId(any())).thenReturn(resource);
    when(userGroupHasOperationRepository
      .findByResourceIdAndOperationName(any(), any()))
      .thenReturn(userGroupHasOperationList);
    userGroupHasOperationList
      .forEach(userGroupHasOperation1 -> userGroupHasOperation1
        .setUserGroup(userGroup));
    userGroupHasOperationList
      .forEach(userGroupHasOperation1 -> userGroupHasOperation1
        .setOperation(operation));
    operationService
      .getAllowedGroupsForOperation(operation.getName(), "resourceId", false);

    operation.setDynamic(true);
    operation.setDynamicCode(dynamicCode);
    operationService
      .getAllowedGroupsForOperation(operation.getName(), "resourceId", false);

    userGroupHasOperationList
      .forEach(userGroupHasOperation1 -> userGroupHasOperation1.getOperation()
        .setDynamic(false));
    operationService
      .getBlockedGroupsForOperation(operation.getName(), "resourceId", false);

    userGroupHasOperationList
      .forEach(userGroupHasOperation1 -> userGroupHasOperation1.setDeny(true));
    operationService
      .getBlockedGroupsForOperation(operation.getName(), "resourceId", false);
    verify(userGroupRepository, times(4)).getAllIds();
  }

  @Test
  public void testGetBlockedGroupsForOperationGetAllowed() {
    when(resourceRepository.findByObjectId(any())).thenReturn(resource);
    when(userGroupHasOperationRepository
      .findByResourceIdAndOperationName(any(), any()))
      .thenReturn(userGroupHasOperationList);
    userGroupHasOperationList
      .forEach(userGroupHasOperation1 -> userGroupHasOperation1
        .setUserGroup(userGroup));
    userGroupHasOperationList
      .forEach(userGroupHasOperation1 -> userGroupHasOperation1
        .setOperation(operation));
    operation.setDynamic(true);
    operation.setDynamicCode(dynamicCode);
    operationService
      .getBlockedGroupsForOperation(operation.getName(), "resourceId", false);

    Set<String> setIds = new HashSet<>();
    setIds.add(userGroup.getId());
    when(userGroupRepository.getAllIds()).thenReturn(setIds);
    when(userGroupHasOperationRepository
      .findByUserGroupIdAndOperationName(any(), any()))
      .thenReturn(userGroupHasOperation);
    when(operationRepository.findByName(any())).thenReturn(operation);
    operationService
      .getBlockedGroupsForOperation(operation.getName(), null, true);

    when(userGroupHasOperationRepository
      .findByResourceIdAndOperationName(any(), any()))
      .thenReturn(new ArrayList<>());
    operationService
      .getAllowedGroupsForOperation(operation.getName(), null, true);

    when(userGroupHasOperationRepository
      .findByResourceIdAndOperationName(any(), any()))
      .thenReturn(new ArrayList<>());
    when(userGroupRepository.fetchById(userGroup.getId()))
      .thenReturn(new UserGroup());
    operationService
      .getBlockedGroupsForOperation(operation.getName(), "resourceId", true);

    verify(userGroupRepository, times(4)).getAllIds();
  }

  @Test
  public void testGetBlockedGroupsForOperation() {
    operationService.getBlockedGroupsForOperation(operation.getName(), false);
    verify(userGroupRepository, times(1)).getAllIds();
  }

  @Test
  public void testGetBlockedGroupsForOperationResourceId() {
    operationService
      .getBlockedGroupsForOperation(operation.getName(), null, false);
    verify(userGroupRepository, times(1)).getAllIds();
  }

  @Test
  public void testGetPermittedOperationsForUser() {
    List<Operation> operationList = new ArrayList<>();
    operationList.add(operation);
    user.setSuperadmin(true);
    when(userRepository.fetchById(user.getId())).thenReturn(user);
    when(operationRepository.findAll()).thenReturn(operationList);
    operationService
      .getPermittedOperationsForUser(user.getId(), false);
    verify(userRepository, times(1)).fetchById(any());
  }

  @Test
  public void testGetPermittedOperationsForUserResourceId() {
    user.setSuperadmin(false);
    user.getUserHasOperations().forEach(uho -> uho.setResource(resource));
    when(userRepository.fetchById(user.getId())).thenReturn(user);
    when(resourceRepository.findByObjectId(any())).thenReturn(resource);

    operationService
      .getPermittedOperationsForUser(user.getId(), null, false);
    verify(userRepository, times(1)).fetchById(any());
    verify(resourceRepository, times(1)).findByObjectId(any());
  }

  @Test
  public void testGetPermittedOperationsForUserNoResource() {
    user.setSuperadmin(false);
    when(userRepository.fetchById(user.getId())).thenReturn(user);
    when(resourceRepository.findByObjectId(any())).thenReturn(resource);

    operationService
      .getPermittedOperationsForUser(user.getId(), null, false);
    verify(userRepository, times(1)).fetchById(any());
    verify(resourceRepository, times(1)).findByObjectId(any());
  }

  @Test
  public void testGetPermittedOperationsForUserNoDynamic() {
    user.setSuperadmin(false);
    user.getUserHasOperations().forEach(uho -> uho.setResource(resource));
    user.getUserHasOperations()
      .forEach(uho -> uho.getOperation().setDynamic(false));
    user.getUserHasOperations().forEach(uho -> uho.setDeny(false));
    when(userRepository.fetchById(user.getId())).thenReturn(user);
    when(resourceRepository.findByObjectId(any())).thenReturn(resource);
    operationService
      .getPermittedOperationsForUser(user.getId(), null, false);

    user.getUserHasOperations().forEach(uho -> uho.setDeny(true));
    operationService
      .getPermittedOperationsForUser(user.getId(), null, false);

    verify(userRepository, times(2)).fetchById(any());
    verify(resourceRepository, times(2)).findByObjectId(any());
  }

  @Test
  public void testGetPermittedOperationsForUserIsDynamic() {
    user.setSuperadmin(false);
    user.getUserHasOperations().forEach(uho -> uho.setResource(resource));
    user.getUserHasOperations()
      .forEach(uho -> uho.getOperation().setDynamic(true));
    user.getUserHasOperations()
      .forEach(uho -> uho.getOperation().setDynamicCode("retVal = false"));
    user.getUserHasOperations().forEach(uho -> uho.setDeny(false));
    when(userRepository.fetchById(user.getId())).thenReturn(user);
    when(resourceRepository.findByObjectId(any())).thenReturn(resource);
    operationService
      .getPermittedOperationsForUser(user.getId(), null, false);

    user.getUserHasOperations().forEach(uho -> uho.setDeny(true));
    operationService
      .getPermittedOperationsForUser(user.getId(), null, false);

    verify(userRepository, times(2)).fetchById(any());
    verify(resourceRepository, times(2)).findByObjectId(any());
  }

  @Test
  public void testGetPermittedOperationsForUserIsDynamicBranchCoverage() {
    user.setSuperadmin(false);
    user.getUserHasOperations().forEach(uho -> uho.setResource(resource));
    user.getUserHasOperations()
      .forEach(uho -> uho.getOperation().setDynamic(true));
    user.getUserHasOperations()
      .forEach(uho -> uho.getOperation().setDynamicCode("retVal = true"));
    user.getUserHasOperations().forEach(uho -> uho.setDeny(false));
    when(userRepository.fetchById(user.getId())).thenReturn(user);
    when(resourceRepository.findByObjectId(any())).thenReturn(resource);
    operationService
      .getPermittedOperationsForUser(user.getId(), null, false);

    verify(userRepository, times(1)).fetchById(any());
    verify(resourceRepository, times(1)).findByObjectId(any());
  }

  @Test(expected = DynamicOperationException.class)
  public void testGetPermittedOperationsForUserEx() {
    user.setSuperadmin(false);
    user.getUserHasOperations().forEach(uho -> uho.setResource(resource));
    when(userRepository.fetchById(user.getId())).thenReturn(user);
    when(resourceRepository.findByObjectId(any())).thenReturn(resource);

    user.getUserHasOperations()
      .forEach(uho -> uho.getOperation().setDynamic(true));
    user.getUserHasOperations()
      .forEach(uho -> uho.getOperation().setDynamicCode("retVal, true"));
    operationService
      .getPermittedOperationsForUser(user.getId(), null, false);

    verify(userRepository, times(1)).fetchById(any());
    verify(resourceRepository, times(1)).findByObjectId(any());
  }

  @Test
  public void testGetPermittedOperationsForUserUserGroup() {
    List<UserGroup> userGroups1 = new ArrayList<>();
    userGroupHasOperation.setResource(resource);
    userGroupHasOperation.setOperation(operation);
    user.setSuperadmin(false);
    userGroups1.add(initTestValues.createUserGroupNoParent());
    userGroups1
      .forEach(usergroup -> usergroup
        .setUserGroupHasOperations(userGroupHasOperationList));
    user.setUserGroups(userGroups1);
    user.getUserHasOperations().forEach(uho -> uho.setResource(resource));
    when(userRepository.fetchById(user.getId())).thenReturn(user);
    when(resourceRepository.findByObjectId(any())).thenReturn(resource);

    user.getUserHasOperations()
      .forEach(uho -> uho.getOperation().setDynamic(false));
    operationService
      .getPermittedOperationsForUser(user.getId(), null, true);

    verify(userRepository, times(1)).fetchById(any());
    verify(resourceRepository, times(1)).findByObjectId(any());
  }

  @Test
  public void testGetPermittedOperationsForUserGetOperationsForGroup() {
    userGroupHasOperationList
      .forEach(
        userGroupHasOperation1 -> userGroupHasOperation1.setResource(resource));
    userGroupHasOperationList
      .forEach(userGroupHasOperation1 -> userGroupHasOperation1
        .setOperation(operation));
    userGroupHasOperationList
      .forEach(userGroupHasOperation1 -> userGroupHasOperation1.getOperation()
        .setDynamic(true));
    userGroupHasOperationList.forEach(
      userGroupHasOperation1 -> userGroupHasOperation1.getOperation()
        .setDynamicCode("retVal = true"));
    List<UserGroup> userGroups1 = new ArrayList<>();
    userGroupHasOperation.setResource(resource);
    userGroupHasOperation.setOperation(operation);
    user.setSuperadmin(false);
    userGroups1.add(initTestValues.createUserGroupNoParent());
    userGroups1
      .forEach(usergroup -> usergroup
        .setUserGroupHasOperations(userGroupHasOperationList));
    user.setUserGroups(userGroups1);
    user.getUserHasOperations().forEach(uho -> uho.setResource(resource));
    when(userRepository.fetchById(user.getId())).thenReturn(user);
    when(resourceRepository.findByObjectId(any())).thenReturn(resource);

    user.getUserHasOperations()
      .forEach(uho -> uho.getOperation().setDynamic(false));
    operationService
      .getPermittedOperationsForUser(user.getId(), null, true);

    userGroupHasOperationList
      .forEach(userGroupHasOperation1 -> userGroupHasOperation1.getOperation()
        .setDynamic(false));
    userGroupHasOperationList.forEach(
      userGroupHasOperation1 -> userGroupHasOperation1.getOperation()
        .setDynamicCode("retVal = true"));
    userGroupHasOperationList
      .forEach(userGroupHasOperation1 -> userGroupHasOperation1.setDeny(true));
    operationService
      .getPermittedOperationsForUser(user.getId(), null, true);

    verify(userRepository, times(2)).fetchById(any());
    verify(resourceRepository, times(2)).findByObjectId(any());
  }

  @Test
  public void testGetPermittedOperationsForUserGetOperationsForGroupNoResourceId() {
    userGroupHasOperationList
      .forEach(
        userGroupHasOperation1 -> userGroupHasOperation1.setResource(null));
    userGroupHasOperationList
      .forEach(userGroupHasOperation1 -> userGroupHasOperation1
        .setOperation(operation));
    List<UserGroup> userGroups1 = new ArrayList<>();
    userGroupHasOperation.setResource(resource);
    userGroupHasOperation.setOperation(operation);
    user.setSuperadmin(false);
    userGroups1.add(initTestValues.createUserGroupNoParent());
    userGroups1
      .forEach(usergroup -> usergroup
        .setUserGroupHasOperations(userGroupHasOperationList));
    user.setUserGroups(userGroups1);
    user.getUserHasOperations().forEach(uho -> uho.setResource(resource));
    when(userRepository.fetchById(user.getId())).thenReturn(user);
    when(resourceRepository.findByObjectId(any())).thenReturn(null);

    user.getUserHasOperations()
      .forEach(uho -> uho.getOperation().setDynamic(false));
    operationService
      .getPermittedOperationsForUser(user.getId(), null, true);

    verify(userRepository, times(1)).fetchById(any());
    verify(resourceRepository, times(1)).findByObjectId(any());
  }

  @Test
  public void testGetPermittedOperationsForUserGetOperationsForGroupDeniedOperations() {
    userGroupHasOperationList
      .forEach(
        userGroupHasOperation1 -> userGroupHasOperation1.setResource(resource));
    userGroupHasOperationList
      .forEach(userGroupHasOperation1 -> userGroupHasOperation1
        .setOperation(operation));
    List<UserGroup> userGroups1 = new ArrayList<>();
    userGroupHasOperation.setResource(resource);
    userGroupHasOperation.setOperation(operation);
    user.setSuperadmin(false);
    userGroups1.add(initTestValues.createUserGroupNoParent());
    userGroups1
      .forEach(usergroup -> usergroup
        .setUserGroupHasOperations(userGroupHasOperationList));
    user.setUserGroups(userGroups1);
    user.getUserHasOperations().forEach(uho -> uho.setResource(resource));
    when(userRepository.fetchById(user.getId())).thenReturn(user);
    when(resourceRepository.findByObjectId(any())).thenReturn(resource);

    user.getUserHasOperations()
      .forEach(uho -> uho.getOperation().setDynamic(false));
    user.getUserHasOperations().forEach(uho -> uho.setDeny(true));
    operationService
      .getPermittedOperationsForUser(user.getId(), null, true);

    operationService.setPrioritisePositive(true);
    operationService
      .getPermittedOperationsForUser(user.getId(), null, true);

    verify(userRepository, times(2)).fetchById(any());
    verify(resourceRepository, times(2)).findByObjectId(any());
  }

  @Test
  public void testGetResourceForOperation() {
    when(userRepository.fetchById(user.getId())).thenReturn(user);
    user.getUserHasOperations().get(0).setResource(resource);
    user.getUserHasOperations().get(1).setResource(resource);
    operationService
      .getResourceForOperation(user.getId(), operation.getName());
    verify(userRepository, times(1)).fetchById(any());
  }

  @Test
  public void testGetResourceForOperationResourceId() {
    when(userRepository.fetchById(user.getId())).thenReturn(user);
    operationService
      .getResourceForOperation(user.getId(), false, (String[]) null);
    verify(userRepository, times(1)).fetchById(any());
  }

  @Test
  public void testGetResourceForOperationCheckUserGroups() {
    user.setUserHasOperations(userHasOperations);
    user.getUserHasOperations().forEach(user -> user.setResource(resource));
    userGroupHasOperationList.forEach(user -> user.setOperation(operation));
    userGroupHasOperationList.forEach(user -> user.setResource(resource));
    userGroups
      .forEach(userGroup1 -> userGroup1
        .setUserGroupHasOperations(userGroupHasOperationList));
    user.setUserGroups(userGroups);
    when(userRepository.fetchById(user.getId())).thenReturn(user);
    operationService
      .getResourceForOperation(user.getId(), true, true, "Test operation");

    operationService
      .getResourceForOperation(user.getId(), false, true, "operation");

    user.getUserGroups().get(0).getUserGroupHasOperations().get(0)
      .setDeny(true);
    operationService
      .getResourceForOperation(user.getId(), false, true, "operation");
    verify(userRepository, times(3)).fetchById(any());
  }

  @Test
  public void testGetOperationByIDNull() {
    assertNull(operationService.getOperationByID(operation.getId()));
    verify(operationRepository, times(1)).fetchById(operation.getId());
  }

  @Test
  public void testGetOperationByID() {
    when(operationRepository.fetchById(any())).thenReturn(operation);
    operationService.getOperationByID(operation.getId());
    verify(operationRepository, times(1)).fetchById(operation.getId());
    verify(operationMapper, times(1)).mapToDTO(operation);
  }

  @Test
  public void testGetGroupOperations() {
    operationService.getGroupOperations(userGroup.getName());
    verify(userGroupHasOperationRepository, times(1))
      .findByUserGroupName(userGroup.getName());
  }

  @Test
  public void testGetGroupOperationsGroupNames() {
    List<String> groupNames = new ArrayList<>();
    groupNames.add(userGroup.getName());
    operationService.getGroupOperations(groupNames);
    verify(userGroupHasOperationRepository, times(1))
      .findByUserGroupName(userGroup.getName());
  }
}
