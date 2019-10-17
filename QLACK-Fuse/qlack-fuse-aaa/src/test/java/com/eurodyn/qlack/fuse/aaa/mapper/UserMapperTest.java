package com.eurodyn.qlack.fuse.aaa.mapper;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import com.eurodyn.qlack.fuse.aaa.InitTestValues;
import com.eurodyn.qlack.fuse.aaa.dto.UserAttributeDTO;
import com.eurodyn.qlack.fuse.aaa.dto.UserDTO;
import com.eurodyn.qlack.fuse.aaa.model.User;
import com.eurodyn.qlack.fuse.aaa.model.UserAttribute;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class UserMapperTest {

  @InjectMocks
  private UserMapperImpl userMapperImpl;

  @Spy
  private UserAttributeMapper userAttributeMapper;

  private InitTestValues initTestValues;

  private User user;

  private UserDTO userDTO;

  private List<User> users;

  private List<UserDTO> usersDTO;

  @Before
  public void init() {
    initTestValues = new InitTestValues();
    user = initTestValues.createUser();
    userDTO = initTestValues.createUserDTO();
    users = initTestValues.createUsers();
    usersDTO = initTestValues.createUsersDTO();
  }

  @Test
  public void testMapToDTOId() {
    UserDTO userDTO = userMapperImpl.mapToDTO(user);
    assertEquals(user.getId(), userDTO.getId());
  }

  @Test
  public void testMapToDTOUsername() {
    UserDTO userDTO = userMapperImpl.mapToDTO(user);
    assertEquals(user.getUsername(), userDTO.getUsername());
  }

  @Test
  public void testMapToDTOPassword() {
    UserDTO userDTO = userMapperImpl.mapToDTO(user);
    assertEquals(user.getPassword(), userDTO.getPassword());
  }

  @Test
  public void testMapToDTOStatus() {
    UserDTO userDTO = userMapperImpl.mapToDTO(user);
    assertEquals(user.getStatus(), userDTO.getStatus());
  }

  @Test
  public void testMapToDTOSuperAdmin() {
    UserDTO userDTO = userMapperImpl.mapToDTO(user);
    assertEquals(user.isSuperadmin(), userDTO.isSuperadmin());
  }

  @Test
  public void testMapToDTOExternal() {
    UserDTO userDTO = userMapperImpl.mapToDTO(user);
    assertEquals(user.isExternal(), userDTO.isExternal());
  }

  @Test
  public void testMapToDTOUserAttributes() {
    for (UserAttribute a : user.getUserAttributes()) {
      when(userAttributeMapper.mapToDTO(a))
          .thenReturn(new UserAttributeDTO(a.getName(), a.getData()));
    }
    UserDTO userDTO = userMapperImpl.mapToDTO(user);
    assertEquals(user.getUserAttributes().size(), userDTO.getUserAttributes().size());
  }

  @Test
  public void testMapToEntityId() {
    User user = userMapperImpl.mapToEntity(userDTO);
    assertEquals(userDTO.getId(), user.getId());
  }

  @Test
  public void testMapToEntityUsername() {
    User user = userMapperImpl.mapToEntity(userDTO);
    assertEquals(userDTO.getUsername(), user.getUsername());
  }

  @Test
  public void testMapToEntityPassword() {
    User user = userMapperImpl.mapToEntity(userDTO);
    assertEquals(userDTO.getPassword(), user.getPassword());
  }

  @Test
  public void testMapToEntityStatus() {
    User user = userMapperImpl.mapToEntity(userDTO);
    assertEquals(userDTO.getStatus(), user.getStatus());
  }

  @Test
  public void testMapToEntitySuperAdmin() {
    User user = userMapperImpl.mapToEntity(userDTO);
    assertEquals(userDTO.isSuperadmin(), user.isSuperadmin());
  }

  @Test
  public void testMapToEntityExternal() {
    User user = userMapperImpl.mapToEntity(userDTO);
    assertEquals(userDTO.isExternal(), user.isExternal());
  }

  @Test
  public void testMapToEntityUserAttributes() {
    for (UserAttributeDTO a : userDTO.getUserAttributes()) {
      when(userAttributeMapper.mapToEntity(a)).thenReturn(new UserAttribute());
    }
    User user = userMapperImpl.mapToEntity(userDTO);
    assertEquals(userDTO.getUserAttributes().size(), user.getUserAttributes().size());
  }

  @Test
  public void testMapToDTOListSize() {
    List<UserDTO> usersDTO = userMapperImpl.mapToDTO(users);
    assertEquals(users.size(), usersDTO.size());
  }

  @Test
  public void testMapToEntityListSize() {
    List<User> users = userMapperImpl.mapToEntity(usersDTO);
    assertEquals(usersDTO.size(), users.size());
  }

  @Test
  public void testMapToExistingEntity() {
    UserDTO userDTO = initTestValues.createUserDTO();
    userDTO.setUsername("updated username");
    userMapperImpl.mapToExistingEntity(userDTO, user);
    assertEquals(userDTO.getUsername(), user.getUsername());
  }
}
