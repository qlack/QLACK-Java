package com.eurodyn.qlack.fuse.security;

import com.eurodyn.qlack.fuse.aaa.dto.OperationDTO;
import com.eurodyn.qlack.fuse.aaa.dto.UserDTO;
import com.eurodyn.qlack.fuse.aaa.dto.UserDetailsDTO;
import com.eurodyn.qlack.fuse.aaa.dto.UserGroupDTO;
import com.eurodyn.qlack.fuse.aaa.dto.UserGroupHasOperationDTO;
import com.eurodyn.qlack.fuse.aaa.dto.UserHasOperationDTO;
import com.eurodyn.qlack.fuse.aaa.model.User;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class InitTestValues {

  public User createUser(){
    User user = new User();
    user.setId("57d30f8d-cf0c-4742-9893-09e2aa08c255");
    user.setUsername("test@test.com");
    user.setPassword("Test1234");
    user.setSalt("thisisaveryrandomsalt");
    user.setStatus((byte)1);
    user.setSuperadmin(true);
    user.setExternal(false);

    return user;
  }

  public UserDTO userDTO(User user) {
    UserDTO userDTO = new UserDTO();

    userDTO.setId(user.getId());
    userDTO.setUsername(user.getUsername());
    userDTO.setPassword(user.getPassword());
    userDTO.setStatus(user.getStatus());
    userDTO.setSuperadmin(user.isSuperadmin());
    userDTO.setExternal(user.isExternal());
    userDTO.setSessionId("1234567");

    return userDTO;
  }

  public UserDetailsDTO createUserDetailDTO(User user){
    UserDetailsDTO userDetailsDTO = new UserDetailsDTO();

    userDetailsDTO.setId( user.getId() );
    userDetailsDTO.setUsername( user.getUsername() );
    userDetailsDTO.setPassword( user.getPassword() );
    userDetailsDTO.setSalt( user.getSalt() );
    userDetailsDTO.setStatus( user.getStatus() );
    userDetailsDTO.setSuperadmin( user.isSuperadmin() );
    userDetailsDTO.setExternal( user.isExternal() );
    userDetailsDTO.setUserHasOperations(createUserHasOperations());
    userDetailsDTO.setUserGroupHasOperations(createUserGroupHasOperationList(createUserGroupHasOperation()));
    userDetailsDTO.setUserGroups(createUserGroupsDTO());
    userDetailsDTO.setSessionId("1234567");
    userDetailsDTO.setUserGroups(createUserGroupsDTO());

    return userDetailsDTO;
  }

  public List<UserHasOperationDTO> createUserHasOperations() {
    List<UserHasOperationDTO> userHasOperationDTOS = new ArrayList<>();
    userHasOperationDTOS.add(createUserHasOperationDTO());

    UserHasOperationDTO userHasOperation2 = createUserHasOperationDTO();
    userHasOperation2.setId("fef0b119-c239-49b8-8ed1-2a63ea11c040");
    userHasOperationDTOS.add(userHasOperation2);

    userHasOperationDTOS.stream().forEach(userHasOperation -> userHasOperation.setOperation(
        createOperationDTO()));

    return userHasOperationDTOS;
  }

  public UserHasOperationDTO createUserHasOperationDTO() {
    UserHasOperationDTO userHasOperationDTO = new UserHasOperationDTO();
    userHasOperationDTO.setId("7d7f41bc-6545-429b-a5d6-0115478fe6a8");
    userHasOperationDTO.setOperation(createOperationDTO());
    return userHasOperationDTO;
  }

  public OperationDTO createOperationDTO() {
    OperationDTO operationDTO = new OperationDTO();
    operationDTO.setId("2bb94daa-50d1-462a-9700-5be893855989");
    operationDTO.setName("Test operation");
    return operationDTO;
  }

  public UserGroupHasOperationDTO createUserGroupHasOperation() {
    UserGroupHasOperationDTO userGroupHasOperationDTO = new UserGroupHasOperationDTO();
    userGroupHasOperationDTO.setId("d89c12a9-fa91-4e0c-a1bc-92d175ee76ea");
    userGroupHasOperationDTO.setOperationDTO(createOperationDTO());
    return userGroupHasOperationDTO;
  }

  public List<UserGroupHasOperationDTO> createUserGroupHasOperationList(UserGroupHasOperationDTO groupHasOperationDTO) {
    List<UserGroupHasOperationDTO> userGroupHasOperationDTOS = new ArrayList<>();
    userGroupHasOperationDTOS.add(groupHasOperationDTO);

    return userGroupHasOperationDTOS;
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

    userGroupDTO.setChildren(new HashSet<>(children));

    return userGroupDTO;
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
}
