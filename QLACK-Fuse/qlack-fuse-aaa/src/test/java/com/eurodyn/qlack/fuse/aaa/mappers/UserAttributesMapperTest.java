package com.eurodyn.qlack.fuse.aaa.mappers;

import static org.junit.Assert.assertEquals;

import com.eurodyn.qlack.fuse.aaa.InitTestValues;
import com.eurodyn.qlack.fuse.aaa.dto.UserAttributeDTO;
import com.eurodyn.qlack.fuse.aaa.model.UserAttribute;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class UserAttributesMapperTest {

    @InjectMocks
    private UserAttributeMapperImpl userAttributeMapperImpl;

    private InitTestValues initTestValues;

    private UserAttribute userAttribute;

    private UserAttributeDTO userAttributeDTO;

    private List<UserAttribute> userAttributes;

    private List<UserAttributeDTO> userAttributesDTO;

    @Before
    public void init(){
        initTestValues = new InitTestValues();
        userAttribute = initTestValues.createUserAttribute(null);
        userAttributeDTO = initTestValues.createUserAttributeDTO(null);
        userAttributes = initTestValues.createUserAttributes(null);
        userAttributesDTO = initTestValues.createUserAttributesDTO(null);
    }

    @Test
    public void testMapToDTOId(){
        UserAttributeDTO userAttributeDTO  = userAttributeMapperImpl.mapToDTO(userAttribute);
        assertEquals(userAttribute.getId(), userAttributeDTO.getId());
    }

    @Test
    public void testMapToDTOName(){
        UserAttributeDTO userAttributeDTO  = userAttributeMapperImpl.mapToDTO(userAttribute);
        assertEquals(userAttribute.getName(), userAttributeDTO.getName());
    }

    @Test
    public void testMapToDTOData(){
        UserAttributeDTO userAttributeDTO  = userAttributeMapperImpl.mapToDTO(userAttribute);
        assertEquals(userAttribute.getData(), userAttributeDTO.getData());
    }

    @Test
    public void testMapToDTOContentType(){
        UserAttributeDTO userAttributeDTO  = userAttributeMapperImpl.mapToDTO(userAttribute);
        assertEquals(userAttribute.getContentType(), userAttributeDTO.getContentType());
    }

    @Test
    public void testMapToDTOUser(){
        UserAttributeDTO userAttributeDTO  = userAttributeMapperImpl.mapToDTO(userAttribute);
        assertEquals(userAttribute.getUser().getId(), userAttributeDTO.getUserId());
    }

    @Test
    public void testMapToEntityId(){
        UserAttribute userAttribute = userAttributeMapperImpl.mapToEntity(userAttributeDTO);
        assertEquals(userAttributeDTO.getId(), userAttribute.getId());
    }

    @Test
    public void testMapToEntityName(){
        UserAttribute userAttribute = userAttributeMapperImpl.mapToEntity(userAttributeDTO);
        assertEquals(userAttributeDTO.getName(), userAttribute.getName());
    }

    @Test
    public void testMapToEntityData(){
        UserAttribute userAttribute = userAttributeMapperImpl.mapToEntity(userAttributeDTO);
        assertEquals(userAttributeDTO.getData(), userAttribute.getData());
    }

    @Test
    public void testMapToEntityContentType(){
        UserAttribute userAttribute = userAttributeMapperImpl.mapToEntity(userAttributeDTO);
        assertEquals(userAttributeDTO.getContentType(), userAttribute.getContentType());
    }

    @Test
    public void testMapToDTOListSize(){
        List<UserAttributeDTO> userAttributeDTO  = userAttributeMapperImpl.mapToDTO(userAttributes);
        assertEquals(userAttributes.size(), userAttributeDTO.size());
    }

    @Test
    public void testMapToEntityListSize(){
        List<UserAttribute> userAttributes  = userAttributeMapperImpl.mapToEntity(userAttributesDTO);
        assertEquals(userAttributesDTO.size(), userAttributes.size());
    }

    @Test
    public void testMapToExistingEntity(){
        UserAttributeDTO userAttributeDTO = initTestValues.createUserAttributeDTO(null);
        userAttributeDTO.setName("updated name");
        userAttributeMapperImpl.mapToExistingEntity(userAttributeDTO, userAttribute);
        assertEquals(userAttributeDTO.getName(), userAttribute.getName());
    }
}
