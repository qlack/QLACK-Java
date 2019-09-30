package com.eurodyn.qlack.fuse.lexicon.mapper;

import static org.junit.Assert.assertEquals;

import com.eurodyn.qlack.fuse.lexicon.InitTestValues;
import com.eurodyn.qlack.fuse.lexicon.dto.GroupDTO;
import com.eurodyn.qlack.fuse.lexicon.model.Group;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class GroupMapperTest {

  @InjectMocks
  private GroupMapperImpl groupMapper;

  private InitTestValues initTestValues;

  @Before
  public void init() {
    initTestValues = new InitTestValues();
  }

  @Test
  public void mapToDTOTest() {
    Group group = initTestValues.createGroup();
    GroupDTO groupDTO = groupMapper.mapToDTO(group);

    assertEquals(group.getId(), groupDTO.getId());
    assertEquals(group.getTitle(), groupDTO.getTitle());
    assertEquals(group.getDescription(), groupDTO.getDescription());
  }

  @Test
  public void mapToDTOListTest() {
    List<Group> groups = new ArrayList<>();
    groups.add(initTestValues.createGroup());
    List<GroupDTO> groupDTOS = groupMapper.mapToDTO(groups);

    assertEquals(groups.size(), groupDTOS.size());
  }

  @Test
  public void mapToDTONullTest() {
    assertEquals(null, groupMapper.mapToDTO((Group) null));

    List<GroupDTO> groupDTOS = groupMapper.mapToDTO(
        (List<Group>) null);
    assertEquals(null, groupDTOS);
  }

  @Test
  public void mapToEntityTest() {
    GroupDTO groupDTO = initTestValues.createGroupDTO();
    Group group = groupMapper.mapToEntity(groupDTO);

    assertEquals(groupDTO.getId(), group.getId());
    assertEquals(groupDTO.getTitle(), group.getTitle());
    assertEquals(groupDTO.getDescription(), group.getDescription());
  }

  @Test
  public void mapToEntityListTest() {
    List<GroupDTO> groupDTOS = new ArrayList<>();
    groupDTOS.add(initTestValues.createGroupDTO());
    List<Group> groups = groupMapper.mapToEntity(groupDTOS);

    assertEquals(groupDTOS.size(), groups.size());
  }

  @Test
  public void mapToEntityNullTest() {
    assertEquals(null, groupMapper.mapToEntity((GroupDTO) null));

    List<Group> groups = groupMapper.mapToEntity(
        (List<GroupDTO>) null);
    assertEquals(null, groups);
  }

}
