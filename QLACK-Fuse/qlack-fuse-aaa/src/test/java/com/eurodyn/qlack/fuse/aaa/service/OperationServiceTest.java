package com.eurodyn.qlack.fuse.aaa.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.eurodyn.qlack.fuse.aaa.InitTestValues;
import com.eurodyn.qlack.fuse.aaa.dto.OperationDTO;
import com.eurodyn.qlack.fuse.aaa.exception.DynamicOperationException;
import com.eurodyn.qlack.fuse.aaa.mappers.OperationMapper;
import com.eurodyn.qlack.fuse.aaa.mappers.ResourceMapper;
import com.eurodyn.qlack.fuse.aaa.mappers.UserGroupHasOperationMapper;
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
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * @author European Dynamics
 */

@RunWith(MockitoJUnitRunner.class)
public class OperationServiceTest {

    @InjectMocks
    OperationService operationService;

    private OperationRepository operationRepository = mock(OperationRepository.class);
    private UserHasOperationRepository userHasOperationRepository = mock(UserHasOperationRepository.class);
    private UserRepository userRepository = mock(UserRepository.class);
    private ResourceRepository resourceRepository = mock(ResourceRepository.class);
    private OpTemplateRepository opTemplateRepository = mock(OpTemplateRepository.class);
    private UserGroupHasOperationRepository userGroupHasOperationRepository = mock(UserGroupHasOperationRepository.class);
    private UserGroupRepository userGroupRepository = mock(UserGroupRepository.class);

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

    private List<User> superadmins;
    private List<User> users;
    private Set<String> superadminsID;
    private User allowedUser;
    private User blockedUser;
    private Set<String> usersID;
    private List<UserHasOperation> userHasOperations;

    private int countUsers;
    private long blockedUsers;


    @Before
    public void init() {
        operationService = new OperationService(operationRepository, userHasOperationRepository, userRepository, resourceRepository,
            opTemplateRepository, userGroupHasOperationRepository, userGroupRepository, operationMapper, resourceMapper,
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
        opTemplate.setOpTemplateHasOperations(initTestValues.createOpTemplateHasOperations());
        userGroupHasOperation = initTestValues.createUserGroupHasOperation();
        userGroup = initTestValues.createUserGroup();

        superadmins = initTestValues.createUsers();
        superadminsID = new HashSet<>();
        superadmins.stream().forEach(superadmin -> superadminsID.add(superadmin.getId()));

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
        blockedUsers = userHasOperations.stream().filter(uho -> uho.isDeny()).count();
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
        when(operationRepository.fetchById(operationDTO.getId())).thenReturn(operation);
        operationService.updateOperation(operationDTO);
        verify(operationMapper, times(1)).mapToExistingEntity(operationDTO, operation);
    }

    @Test
    public void testDeleteOperation() {
        Operation operation2 = new InitTestValues().createOperation();
        when(operationRepository.fetchById(operationDTO.getId())).thenReturn(operation2);
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
        when(operationRepository.findByName(operation.getName())).thenReturn(operation);
        when(operationMapper.mapToDTO(operation)).thenReturn(operationDTO);
        OperationDTO operationBYNameDTO = operationService.getOperationByName(operation.getName());
        assertEquals(operationBYNameDTO, operationDTO);
    }

    @Test
    public void testAddOperationToUserExisting() {
        when(userHasOperationRepository.findByUserIdAndOperationName(user.getId(), operation.getName())).thenReturn(userHasOperation);
        operationService.addOperationToUser(user.getId(), operation.getName(), true);
        assertTrue(userHasOperation.isDeny());
    }

    @Test
    public void testAddOperationToUserNew() {
        when(userRepository.fetchById(user.getId())).thenReturn(user);
        when(operationRepository.findByName(operation.getName())).thenReturn(operation);
        operationService.addOperationToUser(user.getId(), operation.getName(), true);
        verify(userHasOperationRepository, times(1)).save(any());
    }

    @Test
    public void testAddOperationToUserWithResourceExisting() {
        when(userHasOperationRepository.findByUserIdAndResourceIdAndOperationName(user.getId(), resource.getId(), operation.getName()))
            .thenReturn(userHasOperation);
        operationService.addOperationToUser(user.getId(), operation.getName(), resource.getId(), true);
        assertTrue(userHasOperation.isDeny());
    }

    @Test
    public void testAddOperationToUserWithResourceNew() {
        when(userRepository.fetchById(user.getId())).thenReturn(user);
        when(operationRepository.findByName(operation.getName())).thenReturn(operation);
        when(resourceRepository.fetchById(resource.getId())).thenReturn(resource);
        operationService.addOperationToUser(user.getId(), operation.getName(), resource.getId(), true);
        verify(userHasOperationRepository, times(1)).save(any());
    }

    @Test
    public void testAddOperationsToUserFromTemplateID() {
        when(userRepository.fetchById(user.getId())).thenReturn(user);
        when(operationRepository.findByName(operation.getName())).thenReturn(operation);
        when(opTemplateRepository.fetchById(opTemplate.getId())).thenReturn(opTemplate);
        operationService.addOperationsToUserFromTemplateID(user.getId(), opTemplate.getId());
        verify(userHasOperationRepository, times(opTemplate.getOpTemplateHasOperations().size())).save(any());
    }

    @Test
    public void testAddOperationsToUserFromTemplateName() {
        when(userRepository.fetchById(user.getId())).thenReturn(user);
        when(operationRepository.findByName(operation.getName())).thenReturn(operation);
        when(opTemplateRepository.findByName(opTemplate.getName())).thenReturn(opTemplate);
        operationService.addOperationsToUserFromTemplateName(user.getId(), opTemplate.getName());
        verify(userHasOperationRepository, times(opTemplate.getOpTemplateHasOperations().size())).save(any());
    }

    @Test
    public void testAddOperationsToUserFromTemplateIdWithResource() {
        opTemplate.getOpTemplateHasOperations().stream().forEach(opTemplateHasOperation -> opTemplateHasOperation.setResource(resource));
        when(userRepository.fetchById(user.getId())).thenReturn(user);
        when(operationRepository.findByName(operation.getName())).thenReturn(operation);
        when(opTemplateRepository.fetchById(opTemplate.getId())).thenReturn(opTemplate);
        when(resourceRepository.fetchById(resource.getId())).thenReturn(resource);
        operationService.addOperationsToUserFromTemplateID(user.getId(), opTemplate.getId());
        verify(userHasOperationRepository, times(opTemplate.getOpTemplateHasOperations().size())).save(any());
    }

    @Test
    public void testAddOperationsToUserFromTemplateNameWithResource() {
        opTemplate.getOpTemplateHasOperations().stream().forEach(opTemplateHasOperation -> opTemplateHasOperation.setResource(resource));
        when(userRepository.fetchById(user.getId())).thenReturn(user);
        when(operationRepository.findByName(operation.getName())).thenReturn(operation);
        when(opTemplateRepository.findByName(opTemplate.getName())).thenReturn(opTemplate);
        when(resourceRepository.fetchById(resource.getId())).thenReturn(resource);
        operationService.addOperationsToUserFromTemplateName(user.getId(), opTemplate.getName());
        verify(userHasOperationRepository, times(opTemplate.getOpTemplateHasOperations().size())).save(any());
    }

    @Test
    public void testAddOperationToGroupExisting() {
        when(userGroupHasOperationRepository.findByUserGroupIdAndOperationName(userGroup.getId(), operation.getName()))
            .thenReturn(userGroupHasOperation);
        operationService.addOperationToGroup(userGroup.getId(), operation.getName(), true);
        assertTrue(userGroupHasOperation.isDeny());
    }

    @Test
    public void testAddOperationToGroupNew() {
        when(userGroupRepository.fetchById(userGroup.getId())).thenReturn(userGroup);
        when(operationRepository.findByName(operation.getName())).thenReturn(operation);
        operationService.addOperationToGroup(userGroup.getId(), operation.getName(), true);
        verify(userGroupHasOperationRepository, times(1)).save(any());
    }

    @Test
    public void testAddOperationToGroupExistingWithResource() {
        when(userGroupHasOperationRepository.findByUserGroupIdAndResourceIdAndOperationName(userGroup.getId(), operation.getName(),
            resource.getId())).thenReturn(userGroupHasOperation);
        operationService.addOperationToGroup(userGroup.getId(), operation.getName(), resource.getId(), true);
        assertTrue(userGroupHasOperation.isDeny());
    }

    @Test
    public void testAddOperationToGroupNewWithResource() {
        when(userGroupRepository.fetchById(userGroup.getId())).thenReturn(userGroup);
        when(operationRepository.findByName(operation.getName())).thenReturn(operation);
        when(resourceRepository.fetchById(resource.getId())).thenReturn(resource);
        operationService.addOperationToGroup(userGroup.getId(), operation.getName(), resource.getId(), true);
        verify(userGroupHasOperationRepository, times(1)).save(any());
    }

    @Test
    public void testAddOperationsToGroupFromTemplateID() {
        when(userGroupRepository.fetchById(userGroup.getId())).thenReturn(userGroup);
        when(operationRepository.findByName(operation.getName())).thenReturn(operation);
        when(opTemplateRepository.fetchById(opTemplate.getId())).thenReturn(opTemplate);
        operationService.addOperationsToGroupFromTemplateID(userGroup.getId(), opTemplate.getId());
        verify(userGroupHasOperationRepository, times(opTemplate.getOpTemplateHasOperations().size())).save(any());
    }

    @Test
    public void testAddOperationsToGroupFromTemplateName() {
        when(userGroupRepository.fetchById(userGroup.getId())).thenReturn(userGroup);
        when(operationRepository.findByName(operation.getName())).thenReturn(operation);
        when(opTemplateRepository.findByName(opTemplate.getName())).thenReturn(opTemplate);
        operationService.addOperationsToGroupFromTemplateName(userGroup.getId(), opTemplate.getName());
        verify(userGroupHasOperationRepository, times(opTemplate.getOpTemplateHasOperations().size())).save(any());
    }

    @Test
    public void testAddOperationsToGroupFromTemplateIdWithResource() {
        opTemplate.getOpTemplateHasOperations().stream().forEach(opTemplateHasOperation -> opTemplateHasOperation.setResource(resource));
        when(opTemplateRepository.fetchById(opTemplate.getId())).thenReturn(opTemplate);
        when(userGroupRepository.fetchById(userGroup.getId())).thenReturn(userGroup);
        when(operationRepository.findByName(operation.getName())).thenReturn(operation);
        when(resourceRepository.fetchById(resource.getId())).thenReturn(resource);

        operationService.addOperationsToGroupFromTemplateID(userGroup.getId(), opTemplate.getId());
        verify(userGroupHasOperationRepository, times(opTemplate.getOpTemplateHasOperations().size())).save(any());
    }

    @Test
    public void testAddOperationsToGroupFromTemplateNameWithResource() {
        opTemplate.getOpTemplateHasOperations().stream().forEach(opTemplateHasOperation -> opTemplateHasOperation.setResource(resource));
        when(opTemplateRepository.findByName(opTemplate.getName())).thenReturn(opTemplate);
        when(userGroupRepository.fetchById(userGroup.getId())).thenReturn(userGroup);
        when(operationRepository.findByName(operation.getName())).thenReturn(operation);
        when(resourceRepository.fetchById(resource.getId())).thenReturn(resource);

        operationService.addOperationsToGroupFromTemplateName(userGroup.getId(), opTemplate.getName());
        verify(userGroupHasOperationRepository, times(opTemplate.getOpTemplateHasOperations().size())).save(any());
    }

    @Test
    public void testRemoveOperationFromUser() {
        UserHasOperation userHasOperation2 = initTestValues.createUserHasOperation();
        when(userHasOperationRepository.findByUserIdAndOperationName(user.getId(), operation.getName())).thenReturn(userHasOperation2);
        operationService.removeOperationFromUser(user.getId(), operation.getName());
        verify(userHasOperationRepository, times(1)).delete(userHasOperation2);
    }

    @Test
    public void testRemoveOperationFromUserWithResourceId() {
        UserHasOperation userHasOperation2 = initTestValues.createUserHasOperation();
        when(userHasOperationRepository.findByUserIdAndResourceIdAndOperationName(user.getId(), resource.getId(),
            operation.getName())).thenReturn(userHasOperation2);
        operationService.removeOperationFromUser(user.getId(), operation.getName(), resource.getId());
        verify(userHasOperationRepository, times(1)).delete(userHasOperation2);
    }

    @Test
    public void testRemoveOperationFromGroup() {
        UserGroupHasOperation userGroupHasOperation2 = initTestValues.createUserGroupHasOperation();
        when(userGroupHasOperationRepository.findByUserGroupIdAndOperationName(userGroup.getId(), operation.getName()))
            .thenReturn(userGroupHasOperation2);
        operationService.removeOperationFromGroup(userGroup.getId(), operation.getName());
        verify(userGroupHasOperationRepository, times(1)).delete(userGroupHasOperation2);
    }

    @Test
    public void testRemoveOperationFromGroupWithResourceId() {
        UserGroupHasOperation userGroupHasOperation2 = initTestValues.createUserGroupHasOperation();
        when(userGroupHasOperationRepository.findByUserGroupIdAndResourceIdAndOperationName(userGroup.getId(), resource.getId(),
            operation.getName())).thenReturn(userGroupHasOperation2);
        operationService.removeOperationFromGroup(userGroup.getId(), operation.getName(), resource.getId());
        verify(userGroupHasOperationRepository, times(1)).delete(userGroupHasOperation2);
    }

    @Test
    public void testIsPermittedForSuperadmin() {
        user.setSuperadmin(true);
        when(userRepository.fetchById(user.getId())).thenReturn(user);
        Boolean permitted = operationService.isPermitted(user.getId(), operation.getName());
        assertEquals(user.isSuperadmin(), permitted);
    }

    @Test
    public void testIsPermittedForUserNotDynamicOperation() {
        user.setSuperadmin(false);
        when(userRepository.fetchById(user.getId())).thenReturn(user);
        when(operationRepository.findByName(operation.getName())).thenReturn(operation);
        when(userHasOperationRepository.findByUserIdAndOperationName(user.getId(), operation.getName())).thenReturn(userHasOperation);
        Boolean permitted = operationService.isPermitted(user.getId(), operation.getName());
        assertEquals(!userHasOperation.isDeny(), permitted);
    }

    @Test
    public void testIsPermittedForUserWithResourceIdAndNotDynamicOperation() {
        user.setSuperadmin(false);
        when(userRepository.fetchById(user.getId())).thenReturn(user);
        when(operationRepository.findByName(operation.getName())).thenReturn(operation);
        when(resourceRepository.findByObjectId(resource.getObjectId())).thenReturn(resource);
        when(userHasOperationRepository.findByUserIdAndResourceIdAndOperationName(user.getId(), resource.getId(), operation.getName()))
            .thenReturn(userHasOperation);
        Boolean permitted = operationService.isPermitted(user.getId(), operation.getName(), resource.getObjectId());
        assertEquals(!userHasOperation.isDeny(), permitted);
    }

    @Test
    public void testIsPermittedForUserWithDynamicOperation() {
        String dynamicCode = "retVal = (userId != null && groupdId != null && resourceID != null)";
        user.setSuperadmin(false);
        operation.setDynamic(true);
        operation.setDynamicCode(dynamicCode);
        when(userRepository.fetchById(user.getId())).thenReturn(user);
        when(operationRepository.findByName(operation.getName())).thenReturn(operation);
        when(userHasOperationRepository.findByUserIdAndOperationName(user.getId(), operation.getName())).thenReturn(userHasOperation);
        Boolean permitted = operationService.isPermitted(user.getId(), operation.getName());
        assertTrue(permitted);
    }

    @Test(expected = DynamicOperationException.class)
    public void testIsPermittedForUserWithDynamicOperationException() {
        String dynamicCode = "retVal = (userId ";
        user.setSuperadmin(false);
        operation.setDynamic(true);
        operation.setDynamicCode(dynamicCode);
        when(userRepository.fetchById(user.getId())).thenReturn(user);
        when(operationRepository.findByName(operation.getName())).thenReturn(operation);
        when(userHasOperationRepository.findByUserIdAndOperationName(user.getId(), operation.getName())).thenReturn(userHasOperation);
        Boolean permitted = operationService.isPermitted(user.getId(), operation.getName());
        assertTrue(permitted);
    }


    @Test
    public void testIsPermittedForUserWithoutUserGroup() {
        user.setSuperadmin(false);
        user.setUserGroups(new ArrayList<>());
        when(userRepository.fetchById(user.getId())).thenReturn(user);
        Boolean permitted = operationService.isPermitted(user.getId(), operation.getName());
        assertNull(permitted);
    }

    @Test
    public void testIsPermittedForUserWithUserGroup() {
        user.setSuperadmin(false);
        user.setUserGroups(initTestValues.createUserGroups());
        when(userRepository.fetchById(user.getId())).thenReturn(user);
        when(operationRepository.findByName(operation.getName())).thenReturn(operation);
        for (UserGroup userGroup : user.getUserGroups()) {
            when(userGroupHasOperationRepository.findByUserGroupIdAndOperationName(userGroup.getId(), operation.getName()))
                .thenReturn(userGroupHasOperation);
        }
        Boolean permitted = operationService.isPermitted(user.getId(), operation.getName());
        assertTrue(permitted);
    }

    @Test
    public void testIsPermittedForGroupNotDynamicOperation() {
        when(userGroupRepository.fetchById(userGroup.getId())).thenReturn(userGroup);
        when(operationRepository.findByName(operation.getName())).thenReturn(operation);
        when(userGroupHasOperationRepository.findByUserGroupIdAndOperationName(userGroup.getId(), operation.getName()))
            .thenReturn(userGroupHasOperation);
        Boolean permittedForGroup = operationService.isPermittedForGroup(userGroup.getId(), operation.getName());
        assertEquals(!userGroupHasOperation.isDeny(), permittedForGroup);
    }

    @Test
    public void testIsPermittedForGroupWithResoourceAndNotDynamicOperation() {
        when(userGroupRepository.fetchById(userGroup.getId())).thenReturn(userGroup);
        when(operationRepository.findByName(operation.getName())).thenReturn(operation);
        when(resourceRepository.findByObjectId(resource.getObjectId())).thenReturn(resource);
        when(userGroupHasOperationRepository
            .findByUserGroupIdAndResourceIdAndOperationName(userGroup.getId(), resource.getId(), operation.getName()))
            .thenReturn(userGroupHasOperation);
        Boolean permittedForGroup = operationService.isPermittedForGroup(userGroup.getId(), operation.getName(), resource.getObjectId());
        assertEquals(!userGroupHasOperation.isDeny(), permittedForGroup);
    }

    @Test
    public void testIsPermittedForGroupWithDynamicOperation() {
        String dynamicCode = "retVal = (groupdId != null && resourceID != null)";
        operation.setDynamic(true);
        operation.setDynamicCode(dynamicCode);
        when(userGroupRepository.fetchById(userGroup.getId())).thenReturn(userGroup);
        when(operationRepository.findByName(operation.getName())).thenReturn(operation);
        when(userGroupHasOperationRepository.findByUserGroupIdAndOperationName(userGroup.getId(), operation.getName()))
            .thenReturn(userGroupHasOperation);
        Boolean permittedForGroup = operationService.isPermittedForGroup(userGroup.getId(), operation.getName());
        assertTrue(permittedForGroup);
    }

    @Test
    public void testIsPermittedForGroupByParent() {
        when(userGroupRepository.fetchById(userGroup.getId())).thenReturn(userGroup);
        when(operationRepository.findByName(operation.getName())).thenReturn(operation);
        when(userGroupHasOperationRepository.findByUserGroupIdAndOperationName(userGroup.getParent().getId(), operation.getName())).
            thenReturn(userGroupHasOperation);
        Boolean permittedForGroup = operationService.isPermittedForGroup(userGroup.getId(), operation.getName());
        assertEquals(!userGroupHasOperation.isDeny(), permittedForGroup);
    }

    @Test
    public void testGetUsersForOperationByOperationName() {
        when(userRepository.getUserIds(false)).thenReturn(usersID);
        when(userRepository.getUserIds(true)).thenReturn(superadminsID);
        when(userHasOperationRepository.findByOperationName(operation.getName())).thenReturn(userHasOperations);

        Set<String> allowedUsersForOperation = operationService.getAllowedUsersForOperation(operation.getName(), false);
        assertEquals(countUsers - 1, allowedUsersForOperation.size());
    }

    @Test
    public void testGetUsersForOperationByOperationNameAndResource() {

        when(userRepository.getUserIds(false)).thenReturn(usersID);
        when(userRepository.getUserIds(true)).thenReturn(superadminsID);
        when(resourceRepository.findByObjectId(resource.getObjectId())).thenReturn(resource);
        when(userHasOperationRepository.findByResourceIdAndOperationName(resource.getId(), operation.getName()))
            .thenReturn(userHasOperations);

        Set<String> allowedUsersForOperation = operationService
            .getAllowedUsersForOperation(operation.getName(), resource.getObjectId(), false);
        assertEquals(countUsers - blockedUsers, allowedUsersForOperation.size());
    }

    @Test
    public void testGetUsersForOperationWithDynamicCodeByOperationName() {
        String dynamicCode = "retVal = userID != \"1857ae6e-ee0b-41c6-b905-8d5cdfab87c1\"";
        userHasOperations.stream().forEach(uho -> uho.getOperation().setDynamic(true));
        userHasOperations.stream().forEach(uho -> uho.getOperation().setDynamicCode(dynamicCode));

        when(userRepository.getUserIds(false)).thenReturn(usersID);
        when(userRepository.getUserIds(true)).thenReturn(superadminsID);
        when(userHasOperationRepository.findByOperationName(operation.getName())).thenReturn(userHasOperations);

        Set<String> allowedUsersForOperation = operationService.getAllowedUsersForOperation(operation.getName(), false);
        assertEquals(countUsers - blockedUsers, allowedUsersForOperation.size());
    }

    @Test
    public void testGetBlockedUsersForOperationByOperationName() {

        when(userRepository.getUserIds(false)).thenReturn(usersID);
        when(userRepository.getUserIds(true)).thenReturn(superadminsID);
        when(userHasOperationRepository.findByOperationName(operation.getName())).thenReturn(userHasOperations);

        Set<String> allowedUsersForOperation = operationService.getBlockedUsersForOperation(operation.getName(), false);
        assertEquals(blockedUsers, allowedUsersForOperation.size());
    }

    @Test
    public void testGetBlockedUsersForOperationByOperationNameAndResource() {
        userHasOperations.stream().filter(uho -> uho.isDeny()).count();
        when(userRepository.getUserIds(false)).thenReturn(usersID);
        when(userRepository.getUserIds(true)).thenReturn(superadminsID);
        when(resourceRepository.findByObjectId(resource.getObjectId())).thenReturn(resource);
        when(userHasOperationRepository.findByResourceIdAndOperationName(resource.getId(), operation.getName()))
            .thenReturn(userHasOperations);

        Set<String> allowedUsersForOperation = operationService
            .getBlockedUsersForOperation(operation.getName(), resource.getObjectId(), false);
        assertEquals(blockedUsers, allowedUsersForOperation.size());
    }
}
