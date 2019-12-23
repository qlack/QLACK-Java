package com.eurodyn.qlack.fuse.aaa.mapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.eurodyn.qlack.fuse.aaa.InitTestValues;
import com.eurodyn.qlack.fuse.aaa.dto.UserGroupDTO;
import com.eurodyn.qlack.fuse.aaa.model.UserGroup;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * @author European Dynamics
 */
@RunWith(MockitoJUnitRunner.class)
public class UserGroupMapperTest {

  @InjectMocks
  private UserGroupMapperImpl userGroupMapperImpl;

  private InitTestValues initTestValues;

  private UserGroup userGroup;

  private UserGroupDTO userGroupDTO;

  private List<UserGroup> userGroups;

  private List<UserGroupDTO> userGroupsDTO;

  @Before
  public void init() {
    initTestValues = new InitTestValues();
    userGroup = initTestValues.createUserGroup();
    userGroupDTO = initTestValues.createUserGroupDTO();
    userGroups = initTestValues.createUserGroups();
    userGroupsDTO = initTestValues.createUserGroupsDTO();
  }

  @Test
  public void testMapToDTOId() {
    UserGroupDTO userGroupDTO = userGroupMapperImpl.mapToDTO(userGroup, true);
    assertEquals(userGroup.getId(), userGroupDTO.getId());
  }

  @Test
  public void testMapToDTOName() {
    UserGroupDTO userGroupDTO = userGroupMapperImpl.mapToDTO(userGroup, true);
    assertEquals(userGroup.getName(), userGroupDTO.getName());
  }

  @Test
  public void testMapToDTOObjectId() {
    UserGroupDTO userGroupDTO = userGroupMapperImpl.mapToDTO(userGroup, true);
    assertEquals(userGroup.getObjectId(), userGroupDTO.getObjectId());
  }

  @Test
  public void testMapToDTODescription() {
    UserGroupDTO userGroupDTO = userGroupMapperImpl.mapToDTO(userGroup, true);
    assertEquals(userGroup.getDescription(), userGroupDTO.getDescription());
  }

  @Test
  public void testMapToDTOLazyChildren() {
    UserGroupDTO userGroupDTO = userGroupMapperImpl.mapToDTO(userGroup, true);
    assertEquals(0, userGroupDTO.getChildren().size());
  }

  @Test
  public void testMapToDTOEagerChildren() {
    UserGroupDTO userGroupDTO = userGroupMapperImpl.mapToDTO(userGroup, false);
    assertEquals(userGroup.getChildren().size(),
      userGroupDTO.getChildren().size());
  }

  @Test
  public void testMapToDTOParent() {
    UserGroupDTO userGroupDTO = userGroupMapperImpl.mapToDTO(userGroup, true);
    assertEquals(userGroup.getParent().getId(), userGroupDTO.getParentId());
  }

  @Test
  public void testMapToEntityId() {
    UserGroup userGroup = userGroupMapperImpl.mapToEntity(userGroupDTO);
    assertEquals(userGroupDTO.getId(), userGroup.getId());
  }

  @Test
  public void testMapToEntityName() {
    UserGroup userGroup = userGroupMapperImpl.mapToEntity(userGroupDTO);
    assertEquals(userGroupDTO.getName(), userGroup.getName());
  }

  @Test
  public void testMapToEntityObjectId() {
    UserGroup userGroup = userGroupMapperImpl.mapToEntity(userGroupDTO);
    assertEquals(userGroupDTO.getObjectId(), userGroup.getObjectId());
  }

  @Test
  public void testMapToEntityDescription() {
    UserGroup userGroup = userGroupMapperImpl.mapToEntity(userGroupDTO);
    assertEquals(userGroupDTO.getDescription(), userGroup.getDescription());
  }

  @Test
  public void testMapToEntityParent() {
    UserGroup userGroup = userGroupMapperImpl.mapToEntity(userGroupDTO);
    assertEquals(userGroupDTO.getParentId(), userGroup.getParent().getId());
  }

  @Test
  public void testMapToEntityChildren() {
    UserGroup userGroup = userGroupMapperImpl.mapToEntity(userGroupDTO);
    assertEquals(userGroupDTO.getChildren().size(),
      userGroup.getChildren().size());
  }

  @Test
  public void testMapToDTOListSize() {
    List<UserGroupDTO> userGroupsDTO = userGroupMapperImpl
      .mapToDTO(userGroups, true);
    assertEquals(userGroups.size(), userGroupsDTO.size());
  }

  @Test
  public void testMapToDTOListEagerChildren() {
    List<UserGroupDTO> userGroupsDTO = userGroupMapperImpl
      .mapToDTO(userGroups, false);
    for (int i = 0; i < userGroups.size(); i++) {
      assertEquals(userGroups.get(i).getChildren().size(),
        userGroupsDTO.get(i).getChildren().size());
    }
  }

  @Test
  public void testMapToDTOListLazyChildren() {
    List<UserGroupDTO> userGroupsDTO = userGroupMapperImpl
      .mapToDTO(userGroups, true);
    for (UserGroupDTO u : userGroupsDTO) {
      assertEquals(0, u.getChildren().size());
    }
  }

  @Test
  public void testMapToEntityListSize() {
    List<UserGroup> userGroups = userGroupMapperImpl.mapToEntity(userGroupsDTO);
    assertEquals(userGroupsDTO.size(), userGroups.size());
  }

  @Test
  public void mapToDTONullTest() {
    userGroupDTO = userGroupMapperImpl.mapToDTO((UserGroup) null, false);
    assertEquals(null, userGroupDTO);
  }

  @Test
  public void mapToDTOListNullTest() {
    userGroupsDTO = userGroupMapperImpl.mapToDTO((List<UserGroup>) null, false);
    assertEquals(null, userGroupsDTO);

  }

  @Test
  public void mapToEntityNullTest() {
    userGroup = userGroupMapperImpl.mapToEntity((UserGroupDTO) null);
    assertEquals(null, userGroup);

  }

  @Test
  public void mapListToListEntityNullTest() {
    userGroups = userGroupMapperImpl.mapToEntity((List<UserGroupDTO>) null);
    assertEquals(null, userGroups);
  }

  @Test
  public void userGroupParentIdTest() {
    userGroup.getParent().setId(null);
    userGroupDTO = userGroupMapperImpl.mapToDTO(userGroup, false);
    assertEquals(userGroup.getParent().getId(), userGroupDTO.getParentId());
  }

  @Test
  public void userGroupDTOToUserGroupTest() {
    userGroup = userGroupMapperImpl.userGroupDTOToUserGroup(null);
    assertEquals(null, userGroup);
  }

  @Test
  public void mapToExistingEntityNullTest() {
    UserGroupDTO userGroupDTO = initTestValues.createUserGroupDTO();
    userGroupDTO.setName("updated name");
    userGroupMapperImpl.mapToExistingEntity(userGroupDTO, userGroup);
    assertEquals(userGroupDTO.getName(), userGroup.getName());
  }

  @Test
  public void mapToExistingChildrenTest() {
    UserGroupDTO userGroupDTO = initTestValues.createUserGroupDTO();
    userGroupDTO.setChildren(null);
    userGroupMapperImpl.mapToExistingEntity(userGroupDTO, userGroup);
    assertEquals(userGroupDTO.getChildren(), userGroup.getChildren());
  }

  @Test
  public void mapToExistingEntityUserGroupDTONull() {
    UserGroup userGroup = initTestValues.createUserGroup();
    userGroup.setName(null);
    userGroupMapperImpl.mapToExistingEntity(null, userGroup);
    assertEquals(userGroup.getName(), userGroupDTO.getName());
  }

  @Test
  public void userGroupDTOSetToUserGroupListTest() {
    UserGroup userGroup = initTestValues.createUserGroup();
    List<UserGroup> userGroups = initTestValues.createUserGroups();
    userGroup.setChildren(null);
    userGroupMapperImpl.mapToExistingEntity(userGroupDTO, userGroup);
    userGroups = userGroupMapperImpl.userGroupDTOSetToUserGroupList(null);
    assertEquals(null, userGroups);
  }

  @Test
  public void mapToExistingEntityNullListTest() {
    UserGroup userGroup = initTestValues.createUserGroup();
    userGroup.setChildren(null);
    assertNull(userGroup.getChildren());
    UserGroupDTO userGroupDTO = initTestValues.createUserGroupDTO();
    userGroupDTO.setChildren(null);
    assertNull(userGroupDTO.getChildren());
    userGroupMapperImpl.mapToExistingEntity(userGroupDTO, userGroup);
  }


}
