package com.eurodyn.qlack.fuse.aaa.mappers;

import com.eurodyn.qlack.fuse.aaa.InitTestValues;
import com.eurodyn.qlack.fuse.aaa.dto.UserGroupDTO;
import com.eurodyn.qlack.fuse.aaa.model.UserGroup;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;

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
    public void testMapToDTOId(){
        UserGroupDTO userGroupDTO  = userGroupMapperImpl.mapToDTO(userGroup, true);
        assertEquals(userGroup.getId(), userGroupDTO.getId());
    }

    @Test
    public void testMapToDTOName(){
        UserGroupDTO userGroupDTO  = userGroupMapperImpl.mapToDTO(userGroup, true);
        assertEquals(userGroup.getName(), userGroupDTO.getName());
    }

    @Test
    public void testMapToDTOObjectId(){
        UserGroupDTO userGroupDTO  = userGroupMapperImpl.mapToDTO(userGroup, true);
        assertEquals(userGroup.getObjectId(), userGroupDTO.getObjectId());
    }

    @Test
    public void testMapToDTODescription(){
        UserGroupDTO userGroupDTO  = userGroupMapperImpl.mapToDTO(userGroup, true);
        assertEquals(userGroup.getDescription(), userGroupDTO.getDescription());
    }

    @Test
    public void testMapToDTOLazyChildren(){
        UserGroupDTO userGroupDTO = userGroupMapperImpl.mapToDTO(userGroup, true);
        assertEquals(0, userGroupDTO.getChildren().size());
    }

    @Test
    public void testMapToDTOEagerChildren(){
        UserGroupDTO userGroupDTO = userGroupMapperImpl.mapToDTO(userGroup, false);
        assertEquals(userGroup.getChildren().size(), userGroupDTO.getChildren().size());
    }

    @Test
    public void testMapToDTOParent(){
        UserGroupDTO userGroupDTO  = userGroupMapperImpl.mapToDTO(userGroup, true);
        assertEquals(userGroup.getParent().getId(), userGroupDTO.getParentId());
    }

    @Test
    public void testMapToEntityId(){
        UserGroup userGroup = userGroupMapperImpl.mapToEntity(userGroupDTO);
        assertEquals(userGroupDTO.getId(), userGroup.getId());
    }

    @Test
    public void testMapToEntityName(){
        UserGroup userGroup = userGroupMapperImpl.mapToEntity(userGroupDTO);
        assertEquals(userGroupDTO.getName(), userGroup.getName());
    }

    @Test
    public void testMapToEntityObjectId(){
        UserGroup userGroup = userGroupMapperImpl.mapToEntity(userGroupDTO);
        assertEquals(userGroupDTO.getObjectId(), userGroup.getObjectId());
    }

    @Test
    public void testMapToEntityDescription(){
        UserGroup userGroup = userGroupMapperImpl.mapToEntity(userGroupDTO);
        assertEquals(userGroupDTO.getDescription(), userGroup.getDescription());
    }

    @Test
    public void testMapToEntityParent(){
        UserGroup userGroup = userGroupMapperImpl.mapToEntity(userGroupDTO);
        assertEquals(userGroupDTO.getParentId(), userGroup.getParent().getId());
    }

    @Test
    public void testMapToEntityChildren(){
        UserGroup userGroup = userGroupMapperImpl.mapToEntity(userGroupDTO);
        assertEquals(userGroupDTO.getChildren().size(), userGroup.getChildren().size());
    }

    @Test
    public void testMapToDTOListSize(){
        List<UserGroupDTO> userGroupsDTO = userGroupMapperImpl.mapToDTO(userGroups, true);
        assertEquals(userGroups.size(), userGroupsDTO.size());
    }

    @Test
    public void testMapToDTOListEagerChildren(){
        List<UserGroupDTO> userGroupsDTO = userGroupMapperImpl.mapToDTO(userGroups, false);
        for(int i=0; i<userGroups.size(); i++){
            assertEquals(userGroups.get(i).getChildren().size(), userGroupsDTO.get(i).getChildren().size());
        }
    }

    @Test
    public void testMapToDTOListLazyChildren(){
        List<UserGroupDTO> userGroupsDTO = userGroupMapperImpl.mapToDTO(userGroups, true);
        for(UserGroupDTO u: userGroupsDTO){
            assertEquals(0, u.getChildren().size());
        }
    }

    @Test
    public void testMapToEntityListSize(){
        List<UserGroup> userGroups = userGroupMapperImpl.mapToEntity(userGroupsDTO);
        assertEquals(userGroupsDTO.size(), userGroups.size());
    }
}
