package com.eurodyn.qlack.fuse.aaa.mapper;


import com.eurodyn.qlack.fuse.aaa.InitTestValues;
import com.eurodyn.qlack.fuse.aaa.dto.UserAttributeDTO;
import com.eurodyn.qlack.fuse.aaa.dto.UserDTO;
import com.eurodyn.qlack.fuse.aaa.model.User;
import com.eurodyn.qlack.fuse.aaa.model.UserAttribute;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(MockitoJUnitRunner.class)
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

    private List<UserAttribute> userAttributeList ;


    @Before
    public void init(){
        initTestValues = new InitTestValues();
        user = initTestValues.createUser();
        userDTO = initTestValues.createUserDTO();
        userAttributeDTO = initTestValues.createUserAttributeDTO(userDTO.getId());
        userAttribute = initTestValues.createUserAttribute(null);
        userAttributeList = new ArrayList<>();
        userAttributeMapper = new UserAttributeMapperImpl();
    }



    @Test
    public void mapToDTONullTest(){
        UserDTO userDTO = userMapper.mapToDTO((User) null);
        assertEquals(null,userDTO);
    }

    @Test
    public void mapToDTOListNullTest() {
        List<UserDTO> userDTOList = userMapper.mapToDTO((List<User>) null);
        assertEquals(null, userDTOList);
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
        assertEquals(null, user);
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
        assertEquals(null, userMapper.mapToEntity((List<UserDTO>) null));
        List<User> userList = userMapper.mapToEntity((List<UserDTO>) null);
        assertEquals(null, userList);

    }


    @Test
    public void mapToExistingEntitySetDTOTest(){
        UserDTO userDTO = initTestValues.createUserDTO();
        userDTO.setUsername(null);
        User user = initTestValues.createUser();
        user.setUsername(null);
        userMapper.mapToExistingEntity(userDTO,user);
    }

    @Test
    public void mapToExistingEntitySetDTONullTest(){
        UserDTO userDTO = initTestValues.createUserDTO();
        userDTO.setUsername(null);
        userMapper.mapToExistingEntity(null,user);
        assertEquals(null,userDTO.getUsername());
    }

    @Test
    public void userAttributeListToUserAttributeDTOSetNullTest(){
        assertNull(userMapper.userAttributeListToUserAttributeDTOSet(null));
    }

    @Test
    public void userAttributeDTOSetToUserAttributeListNullTest(){
        assertNull(userMapper.userAttributeDTOSetToUserAttributeList(null));
    }
}