package com.eurodyn.qlack.fuse.aaa.mapper;

import static org.junit.jupiter.api.Assertions.*;

import com.eurodyn.qlack.fuse.aaa.InitTestValues;
import com.eurodyn.qlack.fuse.aaa.dto.UserAttributeDTO;
import com.eurodyn.qlack.fuse.aaa.model.UserAttribute;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class UserAttributesMapperTest {

  @InjectMocks
  private UserAttributeMapperImpl userAttributeMapperImpl;

  private InitTestValues initTestValues;

  private UserAttribute userAttribute;

  private UserAttributeDTO userAttributeDTO;

  private List<UserAttribute> userAttributes;

  private List<UserAttributeDTO> userAttributesDTO;

  @BeforeEach
  public void init() {
    initTestValues = new InitTestValues();
    userAttribute = initTestValues.createUserAttribute(null);
    userAttributeDTO = initTestValues.createUserAttributeDTO(null);
    userAttributes = initTestValues.createUserAttributes(null);
    userAttributesDTO = initTestValues.createUserAttributesDTO(null);
  }

  @Test
  public void testMapToDTOId() {
    UserAttributeDTO userAttributeDTO = userAttributeMapperImpl.mapToDTO(userAttribute);
    assertEquals(userAttribute.getId(), userAttributeDTO.getId());
  }

  @Test
  public void testMapToDTOName() {
    UserAttributeDTO userAttributeDTO = userAttributeMapperImpl.mapToDTO(userAttribute);
    assertEquals(userAttribute.getName(), userAttributeDTO.getName());
  }

  @Test
  public void testMapToDTOData() {
    UserAttributeDTO userAttributeDTO = userAttributeMapperImpl.mapToDTO(userAttribute);
    assertEquals(userAttribute.getData(), userAttributeDTO.getData());
  }

  @Test
  public void testMapToDTOContentType() {
    UserAttributeDTO userAttributeDTO = userAttributeMapperImpl.mapToDTO(userAttribute);
    assertEquals(userAttribute.getContentType(), userAttributeDTO.getContentType());
  }

  @Test
  public void testMapToDTOUser() {
    UserAttributeDTO userAttributeDTO = userAttributeMapperImpl.mapToDTO(userAttribute);
    assertEquals(userAttribute.getUser().getId(), userAttributeDTO.getUserId());
  }

  @Test
  public void testMapToEntityId() {
    UserAttribute userAttribute = userAttributeMapperImpl.mapToEntity(userAttributeDTO);
    assertEquals(userAttributeDTO.getId(), userAttribute.getId());
  }

  @Test
  public void testMapToEntityName() {
    UserAttribute userAttribute = userAttributeMapperImpl.mapToEntity(userAttributeDTO);
    assertEquals(userAttributeDTO.getName(), userAttribute.getName());
  }

  @Test
  public void testMapToEntityData() {
    UserAttribute userAttribute = userAttributeMapperImpl.mapToEntity(userAttributeDTO);
    assertEquals(userAttributeDTO.getData(), userAttribute.getData());
  }

  @Test
  public void testMapToEntityContentType() {
    UserAttribute userAttribute = userAttributeMapperImpl.mapToEntity(userAttributeDTO);
    assertEquals(userAttributeDTO.getContentType(), userAttribute.getContentType());
  }

  @Test
  public void testMapToDTOListSize() {
    List<UserAttributeDTO> userAttributeDTO = userAttributeMapperImpl.mapToDTO(userAttributes);
    assertEquals(userAttributes.size(), userAttributeDTO.size());
  }

  @Test
  public void testMapToEntityListSize() {
    List<UserAttribute> userAttributes = userAttributeMapperImpl.mapToEntity(userAttributesDTO);
    assertEquals(userAttributesDTO.size(), userAttributes.size());
  }

  @Test
  public void testMapToExistingEntity() {
    UserAttributeDTO userAttributeDTO = initTestValues.createUserAttributeDTO(null);
    userAttributeDTO.setName("updated name");
    userAttributeMapperImpl.mapToExistingEntity(userAttributeDTO, userAttribute);
    assertEquals(userAttributeDTO.getName(), userAttribute.getName());
  }

  @Test
  public void mapToDTONullTest() {
    userAttributesDTO = userAttributeMapperImpl.mapToDTO((List<UserAttribute>) null);
    assertNull(userAttributesDTO);
  }

  @Test
  public void mapToEntityNullTest() {
    userAttribute = userAttributeMapperImpl.mapToEntity((UserAttributeDTO) null);
    assertNull(userAttribute);
  }

  @Test
  public void mapToExistingEntity() {
    userAttributeDTO.setData(null);
    userAttributeMapperImpl.mapToExistingEntity(null, userAttribute);
    assertNull(userAttributeDTO.getData());
  }

  @Test
  public void mapUserAttributeToDTONullTest() {
    userAttributes = userAttributeMapperImpl.mapToEntity((List<UserAttributeDTO>) null);
    assertNull(userAttributes);

  }

  @Test
  public void UserAttributeMapToDTOTest() {
    userAttributeDTO = userAttributeMapperImpl.mapToDTO((UserAttribute) null);
    assertNull(userAttributeDTO);
  }

  @Test
  public void userAttributeUserIdNulTest() {
    userAttribute.setUser(null);
    userAttributeDTO = userAttributeMapperImpl.mapToDTO(userAttribute);
    assertEquals(userAttribute.getUser(), userAttribute.getUser());

  }

  @Test
  public void userAttributeNullTest() {
    userAttribute.getUser().setId(null);
    userAttributeDTO = userAttributeMapperImpl.mapToDTO(userAttribute);
    assertEquals(userAttributeDTO.getUserId(), userAttribute.getUser().getId());
  }

}
