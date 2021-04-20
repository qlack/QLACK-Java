package com.eurodyn.qlack.fuse.lexicon.service;

import static org.mockito.Mockito.mock;

import com.eurodyn.qlack.fuse.lexicon.InitTestValues;
import com.eurodyn.qlack.fuse.lexicon.dto.GroupDTO;
import com.eurodyn.qlack.fuse.lexicon.dto.KeyDTO;
import com.eurodyn.qlack.fuse.lexicon.mapper.KeyMapper;
import com.eurodyn.qlack.fuse.lexicon.mapper.LexiconMapper;
import com.eurodyn.qlack.fuse.lexicon.model.Data;
import com.eurodyn.qlack.fuse.lexicon.model.Group;
import com.eurodyn.qlack.fuse.lexicon.model.Key;
import com.eurodyn.qlack.fuse.lexicon.model.Language;
import com.eurodyn.qlack.fuse.lexicon.model.QData;
import com.eurodyn.qlack.fuse.lexicon.model.QKey;
import com.eurodyn.qlack.fuse.lexicon.repository.DataRepository;
import com.eurodyn.qlack.fuse.lexicon.repository.GroupRepository;
import com.eurodyn.qlack.fuse.lexicon.repository.KeyRepository;
import com.eurodyn.qlack.fuse.lexicon.repository.LanguageRepository;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * @author European Dynamics
 */
@RunWith(MockitoJUnitRunner.class)
public class KeyServiceTest {

  private static String NEW_NAME = "ATTACHMENT_DESC_NEW";
  private static String UPDATED_TRANSLATION = "Add Attachment Description";
  @InjectMocks
  private KeyService keyService;
  private KeyRepository keyRepository = mock(KeyRepository.class);
  private GroupRepository groupRepository = mock(GroupRepository.class);
  private DataRepository dataRepository = mock(DataRepository.class);
  private LanguageRepository languageRepository = mock(
      LanguageRepository.class);
  private GroupService groupService = mock(GroupService.class);
  @Spy
  private KeyMapper keyMapper;
  @Spy
  private LexiconMapper lexiconMapper;
  private InitTestValues initTestValues;
  private Key key;
  private KeyDTO keyDTO;
  private List<KeyDTO> keysDTO;
  private List<GroupDTO> groupsDTO;
  private List<Key> keys;
  private Group group;
  private List<Group> groups;
  private Language language;
  private List<Language> languages;
  private Data data;
  private QKey qKey;
  private QData qData;
  private List<Data> dataList;

  //TODO fix tests
  @Test
  public void noTest() {

  }

//  @Before
//  public void init() {
//    keyService = new KeyService(keyRepository, groupRepository, keyMapper, dataRepository,
//        languageRepository, groupService);
//    initTestValues = new InitTestValues();
//    key = initTestValues.createKey();
//    keys = initTestValues.createKeys();
//    keyDTO = initTestValues.createKeyDTO();
//    keysDTO = initTestValues.createKeysDTO();
//    group = initTestValues.createGroup();
//    groups = initTestValues.createGroups();
//    groupsDTO = initTestValues.createGroupsDTO();
//    languages = initTestValues.createLanguages();
//    qKey = new QKey("key1");
//    qData = new QData("data");
//    key.getData().forEach(d -> d.setKey(key));
//
//    data = initTestValues.createData();
//    data.setKey(key);
//    dataList = new ArrayList<>();
//    dataList.add(data);
//    language = initTestValues.createEnglishLanguage();
//    language.setData(dataList);
//  }
//
//  private KeySearchCriteria getKeySearchCriteria() {
//    KeySearchCriteria criteria = new KeySearchCriteria();
//    criteria.setKeyName(key.getName());
//    criteria.setGroupId(group.getId());
//    criteria.setPageable(PageRequest.of(0, 10));
//    return criteria;
//  }
//
//  @Test
//  public void testCreateKey() {
//    when(groupRepository.fetchById(keyDTO.getGroupId())).thenReturn(group);
//    keyService.createKey(keyDTO, false);
//    verify(keyRepository, times(1)).save(any());
//    verify(dataRepository, times(keyDTO.getTranslations().size())).save(any());
//  }
//
//  @Test(expected = QAlreadyExistsException.class)
//  public void testCreateExistingKey() {
//    when(groupRepository.fetchById(keyDTO.getGroupId())).thenReturn(group);
//    when(keyService.getKeyByName(keyDTO.getName(), keyDTO.getGroupId(), false))
//        .thenReturn(keyDTO);
//    keyService.createKey(keyDTO, false);
//    verify(keyRepository, times(0)).save(any());
//    verify(dataRepository, times(0)).save(any());
//  }

//
//  @Test(expected = QAlreadyExistsException.class)
//  public void testCreateExistingKeysWithoutGroups() {
//    when(groupRepository.fetchById(keyDTO.getGroupId())).thenReturn(group);
//    when(keyService.getKeyByName(keyDTO.getName(), keyDTO.getGroupId(), false))
//        .thenThrow(IncorrectResultSizeDataAccessException.class);
//    keyService.createKey(keyDTO, false);
//    verify(keyRepository, times(0)).save(any());
//    verify(dataRepository, times(0)).save(any());
//  }
//
//  @Test
//  public void testCreateKeyWithDefaultTranslations() {
//    when(groupRepository.fetchById(keyDTO.getGroupId())).thenReturn(group);
//    when(languageRepository.findAll()).thenReturn(languages);
//    keyService.createKey(keyDTO, true);
//    verify(keyRepository, times(1)).save(any());
//    verify(dataRepository, times(languages.size())).save(any());
//  }

//  @Test
//  public void testCreateKeyNoGroups() {
//    keyDTO.setGroupId(null);
//    keyDTO.setTranslations(null);
//    when(languageRepository.findAll()).thenReturn(languages);
//    keyService.createKey(keyDTO, true);
//    verify(keyRepository, times(1)).save(any());
//    verify(dataRepository, times(languages.size())).save(any());
//  }
//
//  @Test
//  public void testCreateKeyNoDeafaultTranslations() {
//    keyDTO.setTranslations(null);
//    when(groupRepository.fetchById(keyDTO.getGroupId())).thenReturn(group);
//    keyService.createKey(keyDTO, false);
//    verify(keyRepository, times(1)).save(any());
//  }
//
//  @Test
//  public void testCreateKeys() {
//    groups.forEach(group1 -> when(groupRepository.fetchById(group1.getId()))
//        .thenReturn(group1));
//    List<String> createdKeys = keyService.createKeys(keysDTO, true);
//    assertEquals(keysDTO.size(), createdKeys.size());
//  }
//
//  @Test
//  public void testDeleteKey() {
//    keyService.deleteKey(key.getId());
//    verify(keyRepository, times(1)).deleteById(key.getId());
//  }

//  @Test
//  public void testDeleteKeys() {
//    Collection<String> keyIds = keysDTO.stream().map(KeyDTO::getId)
//        .collect(Collectors.toCollection(ArrayList::new));
//    keyService.deleteKeys(keyIds);
//    keyIds.forEach(key -> verify(keyRepository, times(1)).deleteById(key));
//  }
//
//  @Test
//  public void testDeleteKeysByGroupId() {
//    Predicate predicate = qKey.group.id.eq(group.getId());
//    when(keyRepository.findAll(predicate)).thenReturn(keys);
//
//    keyService.deleteKeysByGroupId(group.getId());
//    verify(keyRepository, times(1)).deleteAll(keys);
//  }
//
//  @Test
//  public void testRenameKey() {
//    when(keyRepository.fetchById(key.getId())).thenReturn(key);
//    keyService.renameKey(key.getId(), NEW_NAME);
//    assertEquals(NEW_NAME, key.getName());
//  }

//  @Test
//  public void testMoveKey() {
//    Group destinationGroup = groups.get(groups.size() - 1);
//    when(keyRepository.fetchById(key.getId())).thenReturn(key);
//    when(groupRepository.fetchById(destinationGroup.getId()))
//        .thenReturn(destinationGroup);
//    keyService.moveKey(key.getId(), destinationGroup.getId());
//    assertEquals(destinationGroup.getId(), key.getGroup().getId());
//  }
//
//  @Test
//  public void testMoveKeys() {
//    Group destinationGroup = groups.get(groups.size() - 1);
//    Collection<String> keyIds = keysDTO.stream().map(KeyDTO::getId)
//        .collect(Collectors.toCollection(ArrayList::new));
//
//    keys.forEach(k -> when(keyRepository.fetchById(k.getId())).thenReturn(k));
//
//    when(groupRepository.fetchById(destinationGroup.getId()))
//        .thenReturn(destinationGroup);
//    keyService.moveKeys(keyIds, destinationGroup.getId());
//    keys.forEach(
//        k -> assertEquals(destinationGroup.getId(), k.getGroup().getId()));
//  }
//
//  @Test
//  public void testGetKeyById() {
//    when(keyRepository.fetchById(key.getId())).thenReturn(key);
//    when(keyMapper.mapToDTO(key, true)).thenReturn(keyDTO);
//    KeyDTO foundKeyDTO = keyService.getKeyById(key.getId(), true);
//    assertEquals(keyDTO, foundKeyDTO);
//  }
//
//  @Test
//  public void testGetKeyByName() {
//    keyDTO.setTranslations(null);
//    when(keyRepository.findByNameAndGroupId(key.getName(), group.getId()))
//        .thenReturn(key);
//    when(keyMapper.mapToDTO(key, false)).thenReturn(keyDTO);
//    KeyDTO foundKeyDTO = keyService
//        .getKeyByName(key.getName(), group.getId(), false);
//    assertNull(foundKeyDTO.getTranslations());
//  }
//
//  @Test
//  public void testGetFindKeys() {
//    Page<Key> keyPages = new PageImpl<>(keys);
//    KeySearchCriteria criteria = getKeySearchCriteria();
//
//    Predicate predicate = new BooleanBuilder()
//        .and(qKey.name.eq(criteria.getKeyName())
//            .and(qKey.group.id.eq(criteria.getGroupId())));
//
//    when(keyRepository.findAll(predicate, criteria.getPageable()))
//        .thenReturn(keyPages);
//    keyPages.forEach(k -> {
//      KeyDTO dto = keysDTO.stream()
//          .filter(keyDTO1 -> keyDTO1.getId().equals(k.getId())).findAny()
//          .get();
//      when(keyMapper.mapToDTO(k, false)).thenReturn(dto);
//    });
//
//    List<KeyDTO> foundKeys = keyService.findKeys(criteria, false);
//    assertEquals(keysDTO, foundKeys);
//  }
//
//  @Test
//  public void testGetFindKeysNullKeyName() {
//    Page<Key> keyPages = new PageImpl<>(keys);
//    KeySearchCriteria criteria = getKeySearchCriteria();
//    criteria.setKeyName(null);
//
//    Predicate predicate = new BooleanBuilder()
//        .and(qKey.group.id.eq(criteria.getGroupId()));
//
//    when(keyRepository.findAll(predicate, criteria.getPageable()))
//        .thenReturn(keyPages);
//
//    List<KeyDTO> foundKeys = keyService.findKeys(criteria, false);
//    foundKeys.forEach(Assert::assertNull);
//  }
//
//  @Test
//  public void testGetFindKeysNullGroupId() {
//    Page<Key> keyPages = new PageImpl<>(keys);
//    KeySearchCriteria criteria = getKeySearchCriteria();
//    criteria.setGroupId(null);
//
//    Predicate predicate = new BooleanBuilder()
//        .and(qKey.name.eq(criteria.getKeyName()));
//
//    when(keyRepository.findAll(predicate, criteria.getPageable()))
//        .thenReturn(keyPages);
//
//    List<KeyDTO> foundKeys = keyService.findKeys(criteria, false);
//    foundKeys.forEach(Assert::assertNull);
//  }
//
//  @Test
//  public void testGetUpdateTranslationNewData() {
//    when(languageRepository.fetchById(language.getId())).thenReturn(language);
//    keyService
//        .updateTranslation(key.getId(), language.getId(), UPDATED_TRANSLATION);
//    verify(keyRepository, times(1)).fetchById(key.getId());
//    verify(dataRepository, times(1)).save(any());
//  }
//
//  @Test
//  public void testGetUpdateTranslationExistingData() {
//    when(dataRepository.findByKeyIdAndLanguageId(key.getId(), language.getId()))
//        .thenReturn(data);
//    keyService
//        .updateTranslation(key.getId(), language.getId(), UPDATED_TRANSLATION);
//    verify(keyRepository, times(0)).fetchById(key.getId());
//    verify(dataRepository, times(1)).save(data);
//    assertEquals(UPDATED_TRANSLATION, data.getValue());
//  }
//
//
//  @Test
//  public void testGetUpdateTranslationByLocaleNewData() {
//    String locale = language.getLocale();
//    when(languageRepository.findByLocale(locale)).thenReturn(language);
//    keyService
//        .updateTranslationByLocale(key.getId(), locale, UPDATED_TRANSLATION);
//    verify(keyRepository, times(1)).fetchById(key.getId());
//    verify(dataRepository, times(1)).save(any());
//  }
//
//  @Test
//  public void testGetUpdateTranslationByLocaleExistingData() {
//    String locale = language.getLocale();
//    when(dataRepository.findByKeyIdAndLanguageLocale(key.getId(), locale))
//        .thenReturn(data);
//    keyService
//        .updateTranslationByLocale(key.getId(), locale, UPDATED_TRANSLATION);
//    verify(keyRepository, times(0)).fetchById(key.getId());
//    verify(dataRepository, times(1)).save(data);
//    assertEquals(UPDATED_TRANSLATION, data.getValue());
//  }
//
//  @Test
//  public void testUpdateTranslationByGroupId() {
//    Predicate predicate = qData.key.name.eq(key.getName())
//        .and(qData.key.group.id.eq(group.getId()))
//        .and(qData.language.id.eq(language.getId()));
//    when(dataRepository.findOne(predicate)).thenReturn(Optional.of(data));
//    keyService.updateTranslationByGroupId(key.getName(), group.getId(),
//        language.getId(),
//        "Update by group id!");
//    verify(keyRepository, times(0))
//        .findByNameAndGroupId(key.getName(), group.getId());
//    verify(dataRepository, times(1)).save(data);
//  }
//
//  @Test
//  public void testUpdateTranslationByKeyName() {
//    when(keyRepository.findByNameAndGroupId(key.getName(), group.getId()))
//        .thenReturn(key);
//    when(languageRepository.fetchById(language.getId())).thenReturn(language);
//
//    keyService.updateTranslationByKeyName(key.getName(), group.getId(),
//        language.getId(),
//        "Update by group id!");
//
//    verify(keyRepository, times(1))
//        .findByNameAndGroupId(key.getName(), group.getId());
//    verify(languageRepository, times(1)).fetchById(language.getId());
//    verify(dataRepository, times(1)).save(any());
//  }
//
//  @Test
//  public void testUpdateTranslationsByGroupId() {
//    Map<String, String> keysMap = new HashMap<>();
//    keysMap.put(key.getName(), "Some Value");
//
//    Predicate predicate = qData.key.name.eq(key.getName())
//        .and(qData.key.group.id.eq(group.getId()))
//        .and(qData.language.id.eq(language.getId()));
//    when(dataRepository.findOne(predicate)).thenReturn(Optional.of(data));
//    keyService
//        .updateTranslationsByGroupId(keysMap, group.getId(), language.getId());
//    verify(keyRepository, times(0))
//        .findByNameAndGroupId(key.getName(), group.getId());
//    verify(dataRepository, times(1)).save(data);
//  }
//
//  @Test
//  public void testUpdateTranslationsForKey() {
//    Map<String, String> translationsMap = new HashMap<>();
//    languages.forEach(language1 -> {
//      translationsMap
//          .put(language1.getId(), "New translation in " + language1.getName());
//      when(languageRepository.fetchById(language1.getId()))
//          .thenReturn(language1);
//    });
//
//    when(keyRepository.fetchById(key.getId())).thenReturn(key);
//    keyService.updateTranslationsForKey(key.getId(), translationsMap);
//
//    verify(keyRepository, times(translationsMap.size())).fetchById(key.getId());
//    verify(dataRepository, times(translationsMap.size())).save(any());
//  }
//
//  @Test
//  public void testUpdateTranslationsForKeyByLocale() {
//    Map<String, String> translationsLocaleMap = new HashMap<>();
//    languages.forEach(l -> {
//      translationsLocaleMap
//          .put(l.getLocale(), "New translation in " + l.getName());
//      when(languageRepository.findByLocale(l.getLocale())).thenReturn(l);
//    });
//
//    when(keyRepository.fetchById(key.getId())).thenReturn(key);
//    keyService
//        .updateTranslationsForKeyByLocale(key.getId(), translationsLocaleMap);
//
//    verify(keyRepository, times(translationsLocaleMap.size()))
//        .fetchById(key.getId());
//    verify(dataRepository, times(translationsLocaleMap.size())).save(any());
//  }
//
//  @Test
//  public void testUpdateTranslationsForLanguage() {
//    Map<String, String> updatedKeyTranslationsMap = new HashMap<>();
//    keys.forEach(k -> {
//      updatedKeyTranslationsMap
//          .put(k.getId(), "New translation in " + k.getName());
//      when(keyRepository.fetchById(k.getId())).thenReturn(k);
//    });
//
//    when(languageRepository.fetchById(language.getId())).thenReturn(language);
//    keyService.updateTranslationsForLanguage(language.getId(),
//        updatedKeyTranslationsMap);
//
//    keys
//        .forEach(key1 -> verify(keyRepository, times(1)).fetchById(key1.getId()));
//    verify(dataRepository, times(updatedKeyTranslationsMap.size())).save(any());
//  }
//
//  @Test
//  public void testUpdateTranslationsForLanguageByKeyName() {
//    Map<String, String> keyNameTranslationsMap = new HashMap<>();
//    keys.forEach(k -> {
//      keyNameTranslationsMap.put(k.getName(), "Updated translation");
//      when(keyRepository.findByNameAndGroupId(k.getName(), group.getId()))
//          .thenReturn(k);
//    });
//    when(languageRepository.fetchById(language.getId())).thenReturn(language);
//    keyService
//        .updateTranslationsForLanguageByKeyName(language.getId(), group.getId(),
//            keyNameTranslationsMap);
//
//    keys.forEach(key1 ->
//        verify(keyRepository, times(1))
//            .findByNameAndGroupId(key1.getName(), group.getId()));
//    verify(languageRepository, times(keyNameTranslationsMap.size()))
//        .fetchById(language.getId());
//    verify(dataRepository, times(keyNameTranslationsMap.size())).save(any());
//  }
//
//  @Test
//  public void testGetTranslation() {
//    when(dataRepository
//        .findByKeyNameAndLanguageLocale(key.getName(), language.getLocale()))
//        .thenReturn(data);
//    String foundTranslation = keyService
//        .getTranslation(key.getName(), language.getLocale());
//    assertEquals(data.getValue(), foundTranslation);
//  }
//
//  @Test
//  public void testGetTranslationNotFoundData() {
//    String foundTranslation = keyService
//        .getTranslation(key.getName(), language.getLocale());
//    assertNull(foundTranslation);
//  }
//
//
//  @Test
//  public void testGetTranslationsForKeyName() {
//    Map<String, String> expectedTranslations = new HashMap<>();
//    key.getData().forEach(
//        d -> expectedTranslations.put(d.getLanguage().getLocale(), d.getValue()));
//
//    when(keyRepository.findByNameAndGroupId(key.getName(), group.getId()))
//        .thenReturn(key);
//    Map<String, String> translationsForKeyName = keyService
//        .getTranslationsForKeyName(key.getName(), group.getId());
//    assertEquals(expectedTranslations, translationsForKeyName);
//  }
//
//  @Test
//  public void testGetTranslationForKeyGroupLocale() {
//    Predicate predicate = qData.key.name.eq(key.getName())
//        .and(qData.key.group.title.eq(group.getTitle()))
//        .and(qData.language.locale.eq(language.getLocale()));
//    when(dataRepository.findAll(predicate)).thenReturn(key.getData());
//    String translationForKeyGroupLocale = keyService
//        .getTranslationForKeyGroupLocale(key.getName(), group.getTitle(),
//            language.getLocale());
//    assertEquals(data.getValue(), translationForKeyGroupLocale);
//  }
//
//  @Test
//  public void testGetTranslationForKeyGroupLocaleNull() {
//    String translationForKeyGroupLocale = keyService
//        .getTranslationForKeyGroupLocale(key.getName(), group.getTitle(),
//            language.getLocale());
//    assertNull(translationForKeyGroupLocale);
//  }
//
//  @Test
//  public void testGetTranslationsForLocale() {
//    Map<String, String> expectedTranslationsForLocale = new HashMap<>();
//    expectedTranslationsForLocale.put(key.getName(), data.getValue());
//    when(languageRepository.findByLocale(language.getLocale()))
//        .thenReturn(language);
//    Map<String, String> translationsForLocale = keyService
//        .getTranslationsForLocale(language.getLocale());
//    assertEquals(expectedTranslationsForLocale, translationsForLocale);
//  }
//
//  @Test
//  public void testGetTranslationsForGroupAndLocale() {
//    Map<String, String> expectedTranslationsForGroupAndLocale = new HashMap<>();
//    expectedTranslationsForGroupAndLocale.put(key.getName(), data.getValue());
//    when(dataRepository
//        .findByKeyGroupIdAndLanguageLocale(group.getId(), language.getLocale()))
//        .thenReturn(language.getData());
//    Map<String, String> translationsForGroupAndLocale = keyService
//        .getTranslationsForGroupAndLocale(group.getId(), language.getLocale());
//    assertEquals(expectedTranslationsForGroupAndLocale,
//        translationsForGroupAndLocale);
//  }
//
//  @Test
//  public void testGetTranslationsForGroupNameAndLocale() {
//    List<Data> dataList = initTestValues.createDataList();
//    dataList.get(0).setKey(key);
//    dataList.get(1).setKey(keys.get(1));
//
//    Predicate predicate = (qData.key.group.title.eq(group.getTitle()))
//        .and(qData.language.locale.eq(language.getLocale()));
//    when(dataRepository.findAll(predicate)).thenReturn(dataList);
//    Map<String, String> translationsForGroupNameAndLocale = keyService
//        .getTranslationsForGroupNameAndLocale(group.getTitle(),
//            language.getLocale());
//    assertEquals(dataList.size(), translationsForGroupNameAndLocale.size());
//  }
//
//  @Test
//  public void testGetTranslationsForLocaleGroupByGroupTitle() {
//    when(groupService.getGroups()).thenReturn(new HashSet<>(groupsDTO));
//    Map<String, Map<String, String>> translations = keyService
//        .getTranslationsForLocaleGroupByGroupTitle("en");
//
//    assertEquals(groupsDTO.size(), translations.size());
//    assertNotNull(translations.get(groupsDTO.get(0).getTitle()));
//  }
//
//  @Test
//  public void testGetTranslationsForGroupNameAndLocaleSorted() {
//    List<Data> dataList = initTestValues.createDataList();
//    dataList.get(0).setKey(key);
//    dataList.get(1).setKey(keys.get(1));
//
//    Map<String, String> sortedKeyValue = new HashMap<>();
//    sortedKeyValue
//        .put(dataList.get(1).getKey().getName(), dataList.get(1).getValue());
//    sortedKeyValue
//        .put(dataList.get(0).getKey().getName(), dataList.get(0).getValue());
//
//    Predicate predicate = (qData.key.group.title.eq(group.getTitle()))
//        .and(qData.language.locale.eq(language.getLocale()));
//    when(dataRepository.findAll(predicate)).thenReturn(dataList);
//    Map<String, String> translationsForGroupNameAndLocaleSorted = keyService
//        .getTranslationsForGroupNameAndLocaleSorted(group.getTitle(),
//            language.getLocale(),
//            SortType.DESCENDING);
//
//    assertEquals(sortedKeyValue, translationsForGroupNameAndLocaleSorted);
//  }
//
//  @Test
//  public void testGetKeysSortedByTranslation() {
//    List<Data> dataList = initTestValues.createDataList();
//    dataList.get(0).setKey(key);
//    dataList.get(1).setKey(keys.get(1));
//
//    List<String> sortedKeys = new ArrayList<>();
//    sortedKeys.add(dataList.get(1).getKey().getName());
//    sortedKeys.add(dataList.get(0).getKey().getName());
//
//    Predicate predicate = (qData.key.group.title.eq(group.getTitle()))
//        .and(qData.language.locale.eq(language.getLocale()));
//    when(dataRepository.findAll(predicate)).thenReturn(dataList);
//    List<String> keysSortedByTranslation = keyService
//        .getKeysSortedByTranslation(group.getTitle(), language.getLocale(),
//            SortType.DESCENDING);
//
//    assertEquals(sortedKeys, keysSortedByTranslation);
//  }
//
//  @Test
//  public void testFindTotalKeys() {
//    Long countKeys = 2L;
//    KeySearchCriteria criteria = getKeySearchCriteria();
//
//    Predicate predicate = new BooleanBuilder()
//        .and(qKey.name.eq(criteria.getKeyName())
//            .and(qKey.group.id.eq(criteria.getGroupId())));
//
//    when(keyRepository.count(predicate)).thenReturn(countKeys);
//    Long totalKeys = keyService.findTotalKeys(criteria);
//    assertEquals(keysDTO.size(), totalKeys.intValue());
//    verify(keyRepository, times(1)).count(predicate);
//  }
//
//  @Test
//  public void testFindTotalKeysNullKeyName() {
//    Long countKeys = 2L;
//    KeySearchCriteria criteria = getKeySearchCriteria();
//    criteria.setKeyName(null);
//
//    Predicate predicate = new BooleanBuilder()
//        .and(qKey.group.id.eq(criteria.getGroupId()));
//
//    when(keyRepository.count(predicate)).thenReturn(countKeys);
//    Long totalKeys = keyService.findTotalKeys(criteria);
//    assertEquals(keysDTO.size(), totalKeys.intValue());
//    verify(keyRepository, times(1)).count(predicate);
//  }
//
//  @Test
//  public void testFindTotalKeysGroupId() {
//    Long countKeys = 2L;
//    KeySearchCriteria criteria = getKeySearchCriteria();
//    criteria.setGroupId(null);
//
//    Predicate predicate = new BooleanBuilder()
//        .and(qKey.name.eq(criteria.getKeyName()));
//
//    when(keyRepository.count(predicate)).thenReturn(countKeys);
//    Long totalKeys = keyService.findTotalKeys(criteria);
//    assertEquals(keysDTO.size(), totalKeys.intValue());
//    verify(keyRepository, times(1)).count(predicate);
//  }
}
