package com.eurodyn.qlack.fuse.aaa.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.eurodyn.qlack.fuse.aaa.InitTestValues;
import com.eurodyn.qlack.fuse.aaa.dto.UserGroupDTO;
import com.eurodyn.qlack.fuse.aaa.exception.InvalidGroupHierarchyException;
import com.eurodyn.qlack.fuse.aaa.mappers.UserGroupMapper;
import com.eurodyn.qlack.fuse.aaa.model.QUserGroup;
import com.eurodyn.qlack.fuse.aaa.model.UserGroup;
import com.eurodyn.qlack.fuse.aaa.repository.UserGroupRepository;
import com.eurodyn.qlack.fuse.aaa.repository.UserRepository;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Sort;

/**
 * @author European Dynamics
 */

@RunWith(MockitoJUnitRunner.class)
public class UserGroupServiceTest {

    @InjectMocks
    public UserGroupService userGroupService;

    private UserGroupRepository userGroupRepository = mock(UserGroupRepository.class);
    private UserRepository userRepository = mock(UserRepository.class);

    @Spy
    private UserGroupMapper userGroupMapper;
    private QUserGroup qUserGroup;
    private InitTestValues initTestValues;
    private UserGroup userGroup;
    private UserGroupDTO userGroupDTO;
    private List<UserGroup> userGroups;
    private List<UserGroupDTO> userGroupsDTO;

    @Before
    public void init() {
        userGroupService = new UserGroupService(userGroupRepository, userRepository, userGroupMapper);
        qUserGroup = new QUserGroup("userGroup");
        initTestValues = new InitTestValues();
        userGroup = initTestValues.createUserGroup();
        userGroupDTO = initTestValues.createUserGroupDTO();
        userGroups = initTestValues.createUserGroups();
        userGroupsDTO = initTestValues.createUserGroupsDTO();
    }

    @Test
    public void testCreateGroup() {
        when(userGroupMapper.mapToEntity(userGroupDTO)).thenReturn(userGroup);
        String createGroupId = userGroupService.createGroup(userGroupDTO);

        assertEquals(userGroupDTO.getId(), createGroupId);
        verify(userGroupRepository, times(1)).save(userGroup);
    }

    @Test
    public void testCreateGroupWithParent() {
        UserGroup parent = initTestValues.createUserGroup();
        parent.setId("909626ef-df62-4ce0-a1ec-102f98a63a2a");
        userGroupDTO.setParentId("909626ef-df62-4ce0-a1ec-102f98a63a2a");
        when(userGroupMapper.mapToEntity(userGroupDTO)).thenReturn(userGroup);
        when(userGroupRepository.fetchById(userGroupDTO.getParentId())).thenReturn(parent);

        String createGroupId = userGroupService.createGroup(userGroupDTO);

        assertEquals(userGroupDTO.getId(), createGroupId);
        verify(userGroupRepository, times(1)).save(userGroup);
        verify(userGroupRepository, times(1)).fetchById(userGroupDTO.getParentId());
    }

    @Test
    public void testUpdateGroup() {
        userGroupDTO.setName("Updated Name");
        when(userGroupRepository.fetchById(userGroupDTO.getId())).thenReturn(userGroup);
        userGroupService.updateGroup(userGroupDTO);
        verify(userGroupMapper, times(1)).mapToExistingEntity(userGroupDTO, userGroup);
    }

    @Test
    public void testDeleteGroup() {
        UserGroup userGroup2 = initTestValues.createUserGroup();
        when(userGroupRepository.fetchById(userGroupDTO.getId())).thenReturn(userGroup2);
        userGroupService.deleteGroup(userGroupDTO.getId());
        verify(userGroupRepository, times(1)).delete(userGroup2);
    }

    @Test
    public void testMoveGroup() {
        UserGroup newParent = userGroups.get(1);
        when(userGroupRepository.fetchById(userGroup.getId())).thenReturn(userGroup);
        when(userGroupRepository.fetchById(newParent.getId())).thenReturn(newParent);
        userGroupService.moveGroup(userGroup.getId(), newParent.getId());
        assertEquals(userGroup.getParent(), newParent);
    }

    @Test(expected = InvalidGroupHierarchyException.class)
    public void testMoveGroupException() {
        when(userGroupRepository.fetchById(userGroup.getId())).thenReturn(userGroup);
        when(userGroupRepository.fetchById(userGroup.getId())).thenReturn(userGroup);
        userGroupService.moveGroup(userGroup.getId(), userGroup.getId());
    }

    @Test
    public void testGetGroupByID() {
        when(userGroupRepository.fetchById(userGroup.getId())).thenReturn(userGroup);
        when(userGroupMapper.mapToDTO(userGroup, false)).thenReturn(userGroupDTO);
        UserGroupDTO foundGroup = userGroupService.getGroupByID(userGroup.getId(), false);
        assertEquals(userGroupDTO.getId(), foundGroup.getId());
        assertEquals(userGroupDTO.getChildren().size(), foundGroup.getChildren().size());
    }

    @Test
    public void testGetGroupByIDLazy() {
        userGroupDTO.setChildren(new HashSet<>());
        when(userGroupRepository.fetchById(userGroup.getId())).thenReturn(userGroup);
        when(userGroupMapper.mapToDTO(userGroup, true)).thenReturn(userGroupDTO);
        UserGroupDTO foundGroup = userGroupService.getGroupByID(userGroup.getId(), true);
        assertEquals(userGroupDTO.getId(), foundGroup.getId());
        assertEquals(0, foundGroup.getChildren().size());
    }

    @Test
    public void testGetGroupsById() {
        Collection<String> groupsId = new ArrayList<>();
        userGroups.stream().forEach(ug -> groupsId.add(ug.getId()));

        when(userGroupRepository.findAll(qUserGroup.id.in(groupsId), Sort.by("name").ascending())).thenReturn(userGroups);
        when(userGroupMapper.mapToDTO(userGroups, false)).thenReturn(userGroupsDTO);
        List<UserGroupDTO> foundGroups = userGroupService.getGroupsByID(groupsId, false);
        assertEquals(userGroupsDTO, foundGroups);
    }
}
