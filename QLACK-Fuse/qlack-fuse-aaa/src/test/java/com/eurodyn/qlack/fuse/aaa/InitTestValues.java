package com.eurodyn.qlack.fuse.aaa;

import com.eurodyn.qlack.fuse.aaa.dto.OpTemplateDTO;
import com.eurodyn.qlack.fuse.aaa.dto.OperationDTO;
import com.eurodyn.qlack.fuse.aaa.dto.ResourceDTO;
import com.eurodyn.qlack.fuse.aaa.dto.SessionDTO;
import com.eurodyn.qlack.fuse.aaa.dto.UserAttributeDTO;
import com.eurodyn.qlack.fuse.aaa.dto.UserDTO;
import com.eurodyn.qlack.fuse.aaa.dto.UserGroupDTO;
import com.eurodyn.qlack.fuse.aaa.model.OpTemplate;
import com.eurodyn.qlack.fuse.aaa.model.OpTemplateHasOperation;
import com.eurodyn.qlack.fuse.aaa.model.Operation;
import com.eurodyn.qlack.fuse.aaa.model.Resource;
import com.eurodyn.qlack.fuse.aaa.model.Session;
import com.eurodyn.qlack.fuse.aaa.model.User;
import com.eurodyn.qlack.fuse.aaa.model.UserAttribute;
import com.eurodyn.qlack.fuse.aaa.model.UserGroup;
import com.eurodyn.qlack.fuse.aaa.model.UserGroupHasOperation;
import com.eurodyn.qlack.fuse.aaa.model.UserHasOperation;
import com.eurodyn.qlack.fuse.aaa.model.VerificationToken;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class InitTestValues {

    public UserAttribute createUserAttribute(User user){
        UserAttribute userAttribute = new UserAttribute();
        userAttribute.setId("dca76ec3-0423-4a17-8287-afd311697dbf");
        userAttribute.setName("fullName");
        userAttribute.setData("FirstName LastName");
        userAttribute.setContentType("text");
        if (user == null){
            userAttribute.setUser(this.createUser());
        } else{
            userAttribute.setUser(user);
        }

        return userAttribute;
    }

    public UserAttributeDTO createUserAttributeDTO(String userId){
        UserAttributeDTO userAttributeDTO = new UserAttributeDTO();
        userAttributeDTO.setId("dca76ec3-0423-4a17-8287-afd311697dbf");
        userAttributeDTO.setName("fullName");
        userAttributeDTO.setData("FirstName LastName");
        userAttributeDTO.setContentType("text");
        if (userId == null){
            userAttributeDTO.setUserId(this.createUser().getId());
        } else{
            userAttributeDTO.setUserId(userId);
        }

        return userAttributeDTO;
    }

    public List<UserAttribute> createUserAttributes(User user){
        List<UserAttribute> userAttributes = new ArrayList<>();
        userAttributes.add(this.createUserAttribute(user));

        UserAttribute userAttribute = new UserAttribute();
        userAttribute.setId("ef682d4c-be43-4a33-8262-8af497816277");
        userAttribute.setName("company");
        userAttribute.setData("European Dynamics");
        userAttribute.setContentType("text");
        if (user == null){
            userAttribute.setUser(this.createUser());
        } else{
            userAttribute.setUser(user);
        }
        userAttributes.add(userAttribute);

        return userAttributes;
    }

    public List<UserAttributeDTO> createUserAttributesDTO(String userId){
        List<UserAttributeDTO> userAttributesDTO = new ArrayList<>();
        userAttributesDTO.add(this.createUserAttributeDTO(userId));

        UserAttributeDTO userAttributeDTO = new UserAttributeDTO();
        userAttributeDTO.setId("ef682d4c-be43-4a33-8262-8af497816277");
        userAttributeDTO.setName("company");
        userAttributeDTO.setData("European Dynamics");
        userAttributeDTO.setContentType("text");
        if (userId == null){
            userAttributeDTO.setUserId(this.createUser().getId());
        } else{
            userAttributeDTO.setUserId(userId);
        }
        userAttributesDTO.add(userAttributeDTO);

        return userAttributesDTO;
    }

    public User createUser(){
        User user = new User();
        user.setId("57d30f8d-cf0c-4742-9893-09e2aa08c255");
        user.setUsername("AAA Default User");
        user.setPassword("thisisaverysecurepassword");
        user.setSalt("thisisaveryrandomsalt");
        user.setStatus((byte)1);
        user.setSuperadmin(true);
        user.setExternal(false);
        user.setUserAttributes(this.createUserAttributes(user));
        user.setSessions(this.createSessions(user));

        return user;
    }

    public UserDTO createUserDTO(){
        UserDTO userDTO = new UserDTO();
        userDTO.setId("57d30f8d-cf0c-4742-9893-09e2aa08c255");
        userDTO.setUsername("AAA Default User");
        userDTO.setPassword("thisisaverysecurepassword");
        userDTO.setStatus((byte)1);
        userDTO.setSuperadmin(true);
        userDTO.setExternal(false);
        userDTO.setUserAttributes(new HashSet<>(this.createUserAttributesDTO(userDTO.getId())));

        return userDTO;
    }

    public List<User> createUsers(){
        List<User> users = new ArrayList<>();
        users.add(this.createUser());

        User user = new User();
        user.setId("0b422f60-a66b-4526-937d-26802cd9c8a1");
        user.setUsername("AAA Additional User");
        user.setPassword("thisisanextremelysecurepassword");
        user.setStatus((byte)1);
        user.setSuperadmin(true);
        user.setExternal(false);
        users.add(user);

        return users;
    }

    public List<UserDTO> createUsersDTO(){
        List<UserDTO> usersDTO = new ArrayList<>();
        usersDTO.add(this.createUserDTO());

        UserDTO userDTO = new UserDTO();
        userDTO.setId("0b422f60-a66b-4526-937d-26802cd9c8a1");
        userDTO.setUsername("AAA Additional User");
        userDTO.setPassword("thisisanextremelysecurepassword");
        userDTO.setStatus((byte)1);
        userDTO.setSuperadmin(true);
        userDTO.setExternal(false);
        usersDTO.add(userDTO);

        return usersDTO;
    }

    public Session createSession(User user){
        Session session = new Session();
        session.setId("aa47e3e3-b732-4016-a921-18412fdf25ce");
        if (user != null){
            session.setUser(user);
        } else{
            session.setUser(this.createUser());
        }
        session.setApplicationSessionId("749547a7-ff2f-4ca5-829a-6ce33d2c3ef7");

        return session;
    }

    public SessionDTO createSessionDTO(String userId){
        SessionDTO sessionDTO = new SessionDTO();
        sessionDTO.setId("aa47e3e3-b732-4016-a921-18412fdf25ce");
        if(userId == null){
            sessionDTO.setUserId(this.createUserDTO().getId());
        } else{
            sessionDTO.setUserId(userId);
        }
        sessionDTO.setApplicationSessionId("749547a7-ff2f-4ca5-829a-6ce33d2c3ef7");

        return sessionDTO;
    }

    public List<Session> createSessions(User user){
        List<Session> sessions = new ArrayList<>();
        sessions.add(this.createSession(user));

        Session session = new Session();
        session.setId("46484114-8273-41f3-b0f6-7645596ab418");
        if (user != null){
            session.setUser(user);
        } else{
            session.setUser(this.createUser());
        }
        session.setApplicationSessionId("1e8ecaa6-1990-4ebd-a9f0-f267a01b02d6");
        sessions.add(session);

        return sessions;
    }

    public List<SessionDTO> createSessionsDTO(String userId){
        List<SessionDTO> sessionsDTO = new ArrayList<>();
        sessionsDTO.add(this.createSessionDTO(userId));

        SessionDTO sessionDTO = new SessionDTO();
        sessionDTO.setId("46484114-8273-41f3-b0f6-7645596ab418");
        if (userId != null){
            sessionDTO.setUserId(userId);
        } else{
            sessionDTO.setUserId(this.createUserDTO().getId());
        }
        sessionDTO.setApplicationSessionId("1e8ecaa6-1990-4ebd-a9f0-f267a01b02d6");
        sessionsDTO.add(sessionDTO);

        return sessionsDTO;
    }

    public Resource createResource() {
        Resource resource = new Resource();
        resource.setDescription("DELETE USER");
        resource.setName("DELETE");
        resource.setDbversion(0);
        resource.setObjectId("0b422f60-a66b-4526-937d-26802cd9c8a1");
        resource.setId("d1c86593-6b99-463d-93c6-2429119473de");
        return resource;
    }

    public ResourceDTO createResourceDTO() {
        ResourceDTO resourceDTO = new ResourceDTO();
        resourceDTO.setDescription("DELETE USER");
        resourceDTO.setName("DELETE");
        resourceDTO.setId("d1c86593-6b99-463d-93c6-2429119473de");
        resourceDTO.setObjectId("0b422f60-a66b-4526-937d-26802cd9c8a1");
        return resourceDTO;
    }

    public List<Resource> createResources() {
        List<Resource> resources = new ArrayList<>();
        resources.add(createResource());
        Resource resource = createResource();
        resource.setName("EDIT");
        resource.setDescription("EDIT USER");
        resource.setId("71430011-7bea-4fcf-b11e-ea7acd65dc34");
        resources.add(resource);
        return resources;
    }

    public VerificationToken createVerificationToken() {
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setId("fb0f80a9-ac4e-4a68-8fa6-d309ad6dd9fc");
        verificationToken.setUser(createUser());
        verificationToken.setExpiresOn(1577836800000L);
        verificationToken.setData("Test verification data");
        return verificationToken;
    }

    public UserGroup createUserGroup() {
        UserGroup userGroup = new UserGroup();
        userGroup.setId("7ea962bc-1ea8-46b4-8b6d-588e3a8aae60");

        UserGroup userGroupParent = new UserGroup();
        userGroupParent.setId("aabba4fd-ce44-46ad-b2ed-cb9346e3e521");

        UserGroup userGroupFirstChild = new UserGroup();
        userGroupFirstChild.setId("35bd2469-fb3e-4137-af80-52692a765805");
        userGroupFirstChild.setName("FirstChildName");

        UserGroup userGroupSecondChild = new UserGroup();
        userGroupSecondChild.setId("4a4f65ce-2ee2-42c0-ad92-2d98f25dd331");
        userGroupSecondChild.setName("SecondChildName");

        List<UserGroup> children = new ArrayList<>();
        children.add(userGroupFirstChild);
        children.add(userGroupSecondChild);

        userGroup.setChildren(children);

        userGroup.setParent(userGroupParent);
        return userGroup;
    }

    public UserGroup createUserGroupNoChildren() {
        UserGroup userGroup = new UserGroup();
        userGroup.setId("7ea962bc-1ea8-46b4-8b6d-588e3a8aae61");

        UserGroup userGroupParent = new UserGroup();
        userGroupParent.setId("aabba4fd-ce44-46ad-b2ed-cb9346e3e522");

        List<UserGroup> children = new ArrayList<>();
        userGroup.setChildren(children);

        userGroup.setParent(userGroupParent);
        return userGroup;
    }

    public UserGroupDTO createUserGroupDTO() {
        UserGroupDTO userGroupDTO = new UserGroupDTO();
        userGroupDTO.setId("7ea962bc-1ea8-46b4-8b6d-588e3a8aae60");

        UserGroupDTO userGroupParent = new UserGroupDTO();
        userGroupParent.setId("aabba4fd-ce44-46ad-b2ed-cb9346e3e521");

        userGroupDTO.setParentId(userGroupParent.getId());

        UserGroupDTO userGroupFirstChildDTO = new UserGroupDTO();
        userGroupFirstChildDTO.setId("35bd2469-fb3e-4137-af80-52692a765805");
        userGroupFirstChildDTO.setName("FirstChildName");

        UserGroupDTO userGroupSecondChildDTO = new UserGroupDTO();
        userGroupSecondChildDTO.setId("4a4f65ce-2ee2-42c0-ad92-2d98f25dd331");
        userGroupSecondChildDTO.setName("SecondChildName");

        List<UserGroupDTO> children = new ArrayList<>();
        children.add(userGroupFirstChildDTO);
        children.add(userGroupSecondChildDTO);

        userGroupDTO.setChildren(new HashSet(children));

        return userGroupDTO;
    }

    public List<UserGroup> createUserGroups() {
        List<UserGroup> userGroups = new ArrayList<>();
        userGroups.add(createUserGroup());

        UserGroup userGroup2 = createUserGroup();
        userGroup2.setId("909626ef-df62-4ce0-a1ec-102f98a63a2a");

        UserGroup userGroup3 = createUserGroup();
        userGroup3.setId("180cb78d-7447-49b3-8693-e959bcddea7a");

        userGroups.add(userGroup2);
        userGroups.add(userGroup3);

        return userGroups;
    }

    public List<UserGroupDTO> createUserGroupsDTO(){
        List<UserGroupDTO> userGroupsDTO = new ArrayList<>();
        userGroupsDTO.add(createUserGroupDTO());

        UserGroupDTO userGroupDTO2 = createUserGroupDTO();
        userGroupDTO2.setId("909626ef-df62-4ce0-a1ec-102f98a63a2a");

        UserGroupDTO userGroupDTO3 = createUserGroupDTO();
        userGroupDTO3.setId("180cb78d-7447-49b3-8693-e959bcddea7a");

        userGroupsDTO.add(userGroupDTO2);
        userGroupsDTO.add(userGroupDTO3);

        return userGroupsDTO;
    }

    public OpTemplate createOpTemplate() {
        OpTemplate template = new OpTemplate();
        template.setId("abc29181-cee0-44f8-9f5a-5e91f05f7230");
        template.setName("Test template");
        return template;
    }

    public OpTemplateDTO createOpTemplateDTO() {
        OpTemplateDTO templateDTO = new OpTemplateDTO();
        templateDTO.setId("abc29181-cee0-44f8-9f5a-5e91f05f7230");
        templateDTO.setName("Test template");
        return templateDTO;
    }

    public Operation createOperation() {
        Operation operation = new Operation();
        operation.setId("2bb94daa-50d1-462a-9700-5be893855989");
        operation.setName("Test operation");
        return operation;
    }

    public OperationDTO createOperationDTO() {
        OperationDTO operationDTO = new OperationDTO();
        operationDTO.setId("2bb94daa-50d1-462a-9700-5be893855989");
        operationDTO.setName("Test operation");
        return operationDTO;
    }

    public List<Operation> createOperations() {
        List<Operation> operations = new ArrayList<>();
        operations.add(createOperation());

        Operation operation2 = createOperation();
        operation2.setId(" e038a322-009d-41f6-bee3-d21d28e70f76");

        Operation operation3 = createOperation();
        operation3.setId(" 7d7f41bc-6545-429b-a5d6-0115478fe6a8");

        operations.add(operation2);
        operations.add(operation3);
        return operations;
    }

    public List<OperationDTO> createOperationsDTO() {
        List<OperationDTO> operationsDTO = new ArrayList<>();
        operationsDTO.add(createOperationDTO());

        OperationDTO operationDTO2 = createOperationDTO();
        operationDTO2.setId(" e038a322-009d-41f6-bee3-d21d28e70f76");

        OperationDTO operationDTO3 = createOperationDTO();
        operationDTO3.setId(" 7d7f41bc-6545-429b-a5d6-0115478fe6a8");

        operationsDTO.add(operationDTO2);
        operationsDTO.add(operationDTO3);
        return operationsDTO;
    }

    public OpTemplateHasOperation createOpTemplateHasOperation() {
        OpTemplateHasOperation opTemplateHasOperation = new OpTemplateHasOperation();
        opTemplateHasOperation.setId("c54197e9-d3c6-49be-bce2-563dc8966462");
        return opTemplateHasOperation;
    }

    public List<OpTemplateHasOperation> createOpTemplateHasOperations() {
        List<Operation> operations = createOperations();
        List<OpTemplateHasOperation> opTemplateHasOperations = new ArrayList<>();
        opTemplateHasOperations.add(createOpTemplateHasOperation());
        opTemplateHasOperations.get(0).setOperation(operations.get(1));

        OpTemplateHasOperation opTemplateHasOperation2 = createOpTemplateHasOperation();
        opTemplateHasOperation2.setId("09952fb7-092c-4355-9995-3370c81fb369");
        opTemplateHasOperation2.setOperation(operations.get(1));

        OpTemplateHasOperation opTemplateHasOperation3 = createOpTemplateHasOperation();
        opTemplateHasOperation3.setId("51cdff03-6710-402d-9987-c5600669e251");
        opTemplateHasOperation3.setOperation(operations.get(2));

        opTemplateHasOperations.add(opTemplateHasOperation2);
        opTemplateHasOperations.add(opTemplateHasOperation3);

        return opTemplateHasOperations;

    }

    public UserHasOperation createUserHasOperation() {
        UserHasOperation userHasOperation = new UserHasOperation();
        userHasOperation.setId("7d7f41bc-6545-429b-a5d6-0115478fe6a8");
        userHasOperation.setOperation(createOperation());
        return userHasOperation;
    }

    public List<UserHasOperation> createUserHasOperations() {
        List<UserHasOperation> userHasOperations = new ArrayList<>();
        userHasOperations.add(createUserHasOperation());

        UserHasOperation userHasOperation2 = createUserHasOperation();
        userHasOperation2.setId("fef0b119-c239-49b8-8ed1-2a63ea11c040");
        userHasOperations.add(userHasOperation2);

        userHasOperations.stream().forEach(userHasOperation -> userHasOperation.setOperation(createOperation()));

        return userHasOperations;
    }

    public UserGroupHasOperation createUserGroupHasOperation() {
        UserGroupHasOperation userGroupHasOperation = new UserGroupHasOperation();
        userGroupHasOperation.setId("d89c12a9-fa91-4e0c-a1bc-92d175ee76ea");
        return userGroupHasOperation;
    }

}
