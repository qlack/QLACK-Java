package com.eurodyn.qlack.fuse.lexicon.mapper;

import static org.junit.jupiter.api.Assertions.*;

import com.eurodyn.qlack.fuse.lexicon.InitTestValues;
import com.eurodyn.qlack.fuse.lexicon.dto.GroupDTO;
import com.eurodyn.qlack.fuse.lexicon.model.Group;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class GroupMapperTest {

  @InjectMocks
  private GroupMapperImpl groupMapper;

  private InitTestValues initTestValues;

  @BeforeEach
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
    assertNull(groupMapper.mapToDTO((Group) null));

    List<GroupDTO> groupDTOS = groupMapper.mapToDTO(
      (List<Group>) null);
    assertNull(groupDTOS);
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
    assertNull(groupMapper.mapToEntity((GroupDTO) null));

    List<Group> groups = groupMapper.mapToEntity(
      (List<GroupDTO>) null);
    assertNull(groups);
  }

}
