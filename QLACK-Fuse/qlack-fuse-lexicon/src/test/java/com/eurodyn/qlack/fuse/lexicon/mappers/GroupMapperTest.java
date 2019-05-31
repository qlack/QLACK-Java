package com.eurodyn.qlack.fuse.lexicon.mappers;

import static org.junit.Assert.assertEquals;

import com.eurodyn.qlack.fuse.lexicon.InitTestValues;
import com.eurodyn.qlack.fuse.lexicon.dto.GroupDTO;
import com.eurodyn.qlack.fuse.lexicon.model.Group;
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
public class GroupMapperTest {

  @InjectMocks
  private GroupMapperImpl groupMapperImpl;

  private InitTestValues initTestValues;
  private Group group;
  private GroupDTO groupDTO;
  private List<Group> groups;
  private List<GroupDTO> groupsDTO;

  @Before
  public void init() {
    initTestValues = new InitTestValues();
    group = initTestValues.createGroup();
    groupDTO = initTestValues.createGroupDTO();
    groups = initTestValues.createGroups();
    groupsDTO = initTestValues.createGroupsDTO();
  }

  @Test
  public void testMapToDTOId() {
    groupDTO = groupMapperImpl.mapToDTO(group);
    assertEquals(group.getId(), groupDTO.getId());
  }

  @Test
  public void testMapToDTOTitle() {
    groupDTO = groupMapperImpl.mapToDTO(group);
    assertEquals(group.getTitle(), groupDTO.getTitle());
  }

  @Test
  public void testMapToDTODescription() {
    groupDTO = groupMapperImpl.mapToDTO(group);
    assertEquals(group.getDescription(), groupDTO.getDescription());
  }

  @Test
  public void testMapToDTOList() {
    groupsDTO = groupMapperImpl.mapToDTO(groups);
    assertEquals(groups.size(), groupsDTO.size());
  }

  @Test
  public void testMapToEntityId() {
    group = groupMapperImpl.mapToEntity(groupDTO);
    assertEquals(groupDTO.getId(), group.getId());
  }

  @Test
  public void testMapToEntityTitle() {
    group = groupMapperImpl.mapToEntity(groupDTO);
    assertEquals(groupDTO.getTitle(), group.getTitle());
  }

  @Test
  public void testMapToEntityDescription() {
    group = groupMapperImpl.mapToEntity(groupDTO);
    assertEquals(groupDTO.getDescription(), group.getDescription());
  }

  @Test
  public void testMapToEntityList() {
    groups = groupMapperImpl.mapToEntity(groupsDTO);
    assertEquals(groupsDTO.size(), groups.size());
  }
}
