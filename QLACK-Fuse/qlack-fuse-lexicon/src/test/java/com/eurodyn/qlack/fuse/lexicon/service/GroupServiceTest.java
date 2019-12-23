package com.eurodyn.qlack.fuse.lexicon.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.eurodyn.qlack.fuse.lexicon.InitTestValues;
import com.eurodyn.qlack.fuse.lexicon.dto.GroupDTO;
import com.eurodyn.qlack.fuse.lexicon.mapper.GroupMapper;
import com.eurodyn.qlack.fuse.lexicon.model.Data;
import com.eurodyn.qlack.fuse.lexicon.model.Group;
import com.eurodyn.qlack.fuse.lexicon.model.Language;
import com.eurodyn.qlack.fuse.lexicon.model.QData;
import com.eurodyn.qlack.fuse.lexicon.model.QGroup;
import com.eurodyn.qlack.fuse.lexicon.model.QKey;
import com.eurodyn.qlack.fuse.lexicon.model.QLanguage;
import com.eurodyn.qlack.fuse.lexicon.repository.DataRepository;
import com.eurodyn.qlack.fuse.lexicon.repository.GroupRepository;
import com.eurodyn.qlack.fuse.lexicon.repository.LanguageRepository;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.JPAExpressions;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
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
public class GroupServiceTest {

  @InjectMocks
  private GroupService groupService;

  private GroupRepository groupRepository = mock(GroupRepository.class);
  private LanguageRepository languageRepository = mock(
    LanguageRepository.class);
  private DataRepository dataRepository = mock(DataRepository.class);

  @Spy
  private GroupMapper groupMapper;

  private QGroup qGroup;
  private QKey qKey;
  private QData qData;
  private QLanguage qLanguage;
  private InitTestValues initTestValues;

  private Group group;
  private GroupDTO groupDTO;
  private List<Group> groups;
  private List<GroupDTO> groupsDTO;
  private Language language;
  private Data data;

  @Before
  public void init() {
    groupService = new GroupService(groupRepository, groupMapper,
      languageRepository,
      dataRepository);
    qGroup = new QGroup("group1");
    qKey = new QKey("key1");
    qData = new QData("data");
    qLanguage = new QLanguage("language");

    initTestValues = new InitTestValues();
    group = initTestValues.createGroup();
    groupDTO = initTestValues.createGroupDTO();
    groups = initTestValues.createGroups();
    groupsDTO = initTestValues.createGroupsDTO();
    language = initTestValues.createEnglishLanguage();
    data = initTestValues.createData();
  }

  @Test
  public void testCreateGroup() {
    when(groupMapper.mapToEntity(groupDTO)).thenReturn(group);
    String groupId = groupService.createGroup(groupDTO);
    verify(groupRepository, times(1)).save(group);
    assertEquals(groupDTO.getId(), groupId);
  }

  @Test
  public void testUpdateGroup() {
    groupDTO.setTitle("Updated title");
    groupDTO.setDescription("Updated desc");
    when(groupRepository.fetchById(groupDTO.getId())).thenReturn(group);
    groupService.updateGroup(groupDTO);
    verify(groupRepository, times(1)).save(group);
    assertEquals(groupDTO.getTitle(), group.getTitle());
    assertEquals(groupDTO.getDescription(), group.getDescription());
  }

  @Test
  public void testDelete() {
    groupService.deleteGroup(group.getId());
    verify(groupRepository, times(1)).deleteById(group.getId());
  }

  @Test
  public void testGetGroup() {
    when(groupRepository.fetchById(group.getId())).thenReturn(group);
    when(groupMapper.mapToDTO(group)).thenReturn(groupDTO);
    GroupDTO foundGroupDTO = groupService.getGroup(this.group.getId());
    assertEquals(groupDTO, foundGroupDTO);
  }

  @Test
  public void testGetGroupByTitle() {
    when(groupRepository.findByTitle(group.getTitle())).thenReturn(group);
    when(groupMapper.mapToDTO(group)).thenReturn(groupDTO);
    GroupDTO foundGroupDTO = groupService.getGroupByTitle(group.getTitle());
    assertEquals(groupDTO, foundGroupDTO);
  }

  @Test
  public void testFindAll() {
    when(groupRepository.findAll()).thenReturn(groups);
    List<Group> allGroups = groupService.findAll();
    assertEquals(groups, allGroups);
  }

  @Test
  public void testFindByTitle() {
    when(groupRepository.findByTitle(group.getTitle())).thenReturn(group);
    Group foundGroup = groupService.findByTitle(group.getTitle());
    assertEquals(group, foundGroup);
  }

  @Test
  public void testGetRemainingGroups() {
    List<String> groupNames = groups.stream().map(g -> g.getTitle())
      .collect(Collectors.toList());
    List<String> excludedGroupNames = groupNames.stream()
      .filter(gn -> !gn.equals("Application UI"))
      .collect(Collectors.toList());
    List<Group> remaining = new ArrayList<>();
    remaining.add(group);

    List<GroupDTO> remainingDTO = new ArrayList<>();
    remainingDTO.add(groupDTO);

    Predicate predicate = qGroup.title.notIn(excludedGroupNames);
    when(groupRepository.findAll(predicate)).thenReturn(remaining);
    when(groupMapper.mapToDTO(remaining)).thenReturn(remainingDTO);

    Set<GroupDTO> expectedGroupSet = remainingDTO.stream()
      .collect(Collectors.toSet());
    Set<GroupDTO> actualGroupSet = groupService
      .getRemainingGroups(excludedGroupNames);

    assertEquals(expectedGroupSet, actualGroupSet);
  }

  @Test
  public void testGetGroups() {
    when(groupRepository.findAll()).thenReturn(groups);
    when(groupMapper.mapToDTO(groups)).thenReturn(groupsDTO);
    Set<GroupDTO> expectedGroupSet = groupsDTO.stream()
      .collect(Collectors.toSet());
    Set<GroupDTO> actualGroupSet = groupService.getGroups();
    assertEquals(expectedGroupSet, actualGroupSet);
  }

  @Test
  public void testDeleteLanguageTranslations() {
    when(languageRepository.fetchById(language.getId())).thenReturn(language);

    List<Data> dataList = new ArrayList<>();
    dataList.add(data);

    when(dataRepository
      .findByKeyGroupIdAndLanguageLocale(group.getId(), language.getLocale()))
      .thenReturn(dataList);
    groupService.deleteLanguageTranslations(group.getId(), language.getId());
    verify(dataRepository, times(1)).delete(data);
  }

  @Test
  public void testGetLastUpdateDateForLocale() {
    List<Data> dataList = initTestValues.createDataList();
    long expectedLastUpdateDate = dataList.stream().findFirst().get()
      .getLastUpdatedOn();
    Predicate predicate = qData.key.group.id.eq(group.getId())
      .and(qData.language.id
        .eq(JPAExpressions.select(qLanguage.id).from(qLanguage)
          .where(qLanguage.locale.eq(language.getLocale()))));
    when(
      dataRepository.findAll(predicate, Sort.by("lastUpdatedOn").descending()))
      .thenReturn(dataList);

    long lastUpdateDateForLocale = groupService
      .getLastUpdateDateForLocale(group.getId(), language.getLocale());
    assertEquals(expectedLastUpdateDate, lastUpdateDateForLocale);
  }

  @Test
  public void getLastUpdateDateForLocaleNullTest() {
    Predicate predicate = qData.key.group.id.eq(group.getId())
      .and(qData.language.id
        .eq(JPAExpressions.select(qLanguage.id).from(qLanguage)
          .where(qLanguage.locale.eq(language.getLocale()))));
    when(
      dataRepository.findAll(predicate, Sort.by("lastUpdatedOn").descending()))
      .thenReturn(new ArrayList<>());
    assertNotNull(groupService
      .getLastUpdateDateForLocale(group.getId(), language.getLocale()));
  }

}
