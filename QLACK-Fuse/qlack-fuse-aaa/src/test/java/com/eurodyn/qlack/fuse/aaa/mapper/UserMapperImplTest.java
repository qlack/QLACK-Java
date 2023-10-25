package com.eurodyn.qlack.fuse.aaa.mapper;

import static org.junit.jupiter.api.Assertions.*;

import com.eurodyn.qlack.fuse.aaa.InitTestValues;
import com.eurodyn.qlack.fuse.aaa.dto.UserAttributeDTO;
import com.eurodyn.qlack.fuse.aaa.dto.UserDTO;
import com.eurodyn.qlack.fuse.aaa.model.User;
import com.eurodyn.qlack.fuse.aaa.model.UserAttribute;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserMapperImplTest {

  @InjectMocks
  private UserMapperImpl userMapper;

  @Mock
  private UserAttributeMapperImpl userAttributeMapper;

  private InitTestValues initTestValues;

  private User user;

  private UserDTO userDTO;

  private UserAttributeDTO userAttributeDTO;

  private UserAttribute userAttribute;

  private Set<UserAttributeDTO> userAttributes;

  private List<UserAttribute> userAttributeList;


  @BeforeEach
  public void init() {
    initTestValues = new InitTestValues();
    user = initTestValues.createUser();
    userDTO = initTestValues.createUserDTO();
    userAttributeDTO = initTestValues.createUserAttributeDTO(userDTO.getId());
    userAttribute = initTestValues.createUserAttribute(null);
    userAttributeList = new ArrayList<>();
    userAttributeMapper = new UserAttributeMapperImpl();
  }


  @Test
  public void mapToDTONullTest() {
    UserDTO userDTO = userMapper.mapToDTO((User) null);
    assertNull(userDTO);
  }

  @Test
  public void mapToDTOListNullTest() {
    List<UserDTO> userDTOList = userMapper.mapToDTO((List<User>) null);
    assertNull(userDTOList);
  }

  @Test
  public void mapToDTOListTest() {
    List<User> users = new ArrayList<>();
    users.add(initTestValues.createUser());
    List<UserDTO> userDTOList = userMapper.mapToDTO(users);
    assertEquals(users.size(), userDTOList.size());
  }

  @Test
  public void mapToEntityNullTest() {
    User user = userMapper.mapToEntity((UserDTO) null);
    assertNull(user);
  }


  @Test
  public void mapToEntityListTest() {
    List<UserDTO> userDTOList = new ArrayList<>();
    userDTOList.add(initTestValues.createUserDTO());
    List<User> users = userMapper.mapToEntity(userDTOList);
    assertEquals(userDTOList.size(), users.size());
  }

  @Test
  public void mapToEntityListNullTest() {
    assertNull(userMapper.mapToEntity((List<UserDTO>) null));
    List<User> userList = userMapper.mapToEntity((List<UserDTO>) null);
    assertNull(userList);

  }


  @Test
  public void mapToExistingEntitySetDTOTest() {
    UserDTO userDTO = initTestValues.createUserDTO();
    userDTO.setUsername(null);
    User user = initTestValues.createUser();
    user.setUsername(null);
    userMapper.mapToExistingEntity(userDTO, user);
    assertNull(userDTO.getUsername());
  }

  @Test
  public void mapToExistingEntitySetDTONullTest() {
    UserDTO userDTO = initTestValues.createUserDTO();
    userDTO.setUsername(null);
    userMapper.mapToExistingEntity(null, user);
    assertNull(userDTO.getUsername());
  }

  @Test
  public void userAttributeListToUserAttributeDTOSetNullTest() {
    assertNull(userMapper.userAttributeListToUserAttributeDTOSet(null));
  }

  @Test
  public void userAttributeDTOSetToUserAttributeListNullTest() {
    assertNull(userMapper.userAttributeDTOSetToUserAttributeList(null));
  }
}
