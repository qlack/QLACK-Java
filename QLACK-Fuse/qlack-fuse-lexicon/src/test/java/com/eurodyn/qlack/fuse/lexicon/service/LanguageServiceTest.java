package com.eurodyn.qlack.fuse.lexicon.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.eurodyn.qlack.common.exception.QAlreadyExistsException;
import com.eurodyn.qlack.fuse.lexicon.InitTestValues;
import com.eurodyn.qlack.fuse.lexicon.dto.LanguageDTO;
import com.eurodyn.qlack.fuse.lexicon.exception.LanguageProcessingException;
import com.eurodyn.qlack.fuse.lexicon.mapper.LanguageMapper;
import com.eurodyn.qlack.fuse.lexicon.model.Data;
import com.eurodyn.qlack.fuse.lexicon.model.Group;
import com.eurodyn.qlack.fuse.lexicon.model.Key;
import com.eurodyn.qlack.fuse.lexicon.model.Language;
import com.eurodyn.qlack.fuse.lexicon.repository.KeyRepository;
import com.eurodyn.qlack.fuse.lexicon.repository.LanguageRepository;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * @author European Dynamics
 */
@RunWith(MockitoJUnitRunner.class)
public class LanguageServiceTest {

  @InjectMocks
  private LanguageService languageService;

  private KeyRepository keyRepository = mock(KeyRepository.class);
  private LanguageRepository languageRepository = mock(LanguageRepository.class);

  private KeyService keyService = mock(KeyService.class);
  private GroupService groupService = mock(GroupService.class);

  @Spy
  private LanguageMapper languageMapper;

  private InitTestValues initTestValues;
  private LanguageDTO languageDTO;
  private Language language;
  private List<LanguageDTO> languagesDTO;
  private List<Language> languages;
  private Key key;
  private List<Key> keys;
  private Data data;

  private Map<String, String> translationsMap;
  private Map<String, String> prefixedTranslationsMap;
  private Map<String, String> translationsForLocale;
  private Map<String, String> prefixedTranslationsForLocale;

  private byte[] lgXl;

  @Before
  public void init() {
    languageService = new LanguageService(keyService, groupService, languageRepository,
        keyRepository, languageMapper);
    initTestValues = new InitTestValues();
    languageDTO = initTestValues.createEnglishLanguageDTO();
    language = initTestValues.createEnglishLanguage();
    key = initTestValues.createKey();
    keys = initTestValues.createKeys();
    translationsMap = new HashMap<>();
    prefixedTranslationsMap = new HashMap<>();
    translationsForLocale = new HashMap<>();
    prefixedTranslationsForLocale = new HashMap<>();
    languages = initTestValues.createLanguages();
    languagesDTO = initTestValues.createLanguagesDTO();
    lgXl = initTestValues.getLanguageByteArray();
    data = initTestValues.createData();
    createTranslationMaps();
  }

  private void createTranslationMaps() {
    keys.forEach(k -> {
      translationsMap.put(k.getId(), k.getName());
      prefixedTranslationsMap.put(k.getId(), "en_" + k.getName());
    });

    key.getData().forEach(data1 -> translationsForLocale.put(key.getName(), data1.getValue()));
    translationsForLocale.forEach((k, v) -> prefixedTranslationsForLocale.put(k, "en_" + v));
  }

  @Test
  public void testCreateLanguage() {
    when(languageMapper.mapToEntity(languageDTO)).thenReturn(language);
    String createdLanguageId = languageService.createLanguage(languageDTO);
    verify(languageRepository, times(1)).save(language);
    assertEquals(languageDTO.getId(), createdLanguageId);
  }

  @Test
  public void testCreateLanguageIfNotExists() {
    when(languageMapper.mapToEntity(languageDTO)).thenReturn(language);
    String createdLanguageId = languageService.createLanguageIfNotExists(languageDTO);
    verify(languageRepository, times(1)).save(language);
    assertEquals(languageDTO.getId(), createdLanguageId);
  }

  @Test(expected = QAlreadyExistsException.class)
  public void testCreateLanguageIfNotExistsException() {
    when(languageRepository.findByLocale(languageDTO.getLocale())).thenReturn(language);
    String createdLanguageId = languageService.createLanguageIfNotExists(languageDTO);
    verify(languageRepository, times(1)).save(language);
    assertEquals(languageDTO.getId(), createdLanguageId);
  }

  @Test
  public void testCreateLanguageWithoutPrefix() {
    when(languageMapper.mapToEntity(languageDTO)).thenReturn(language);
    when(keyRepository.findAll()).thenReturn(keys);
    String createdLanguageId = languageService.createLanguage(languageDTO, null);
    verify(languageRepository, times(1)).save(language);
    verify(keyService, times(1)).updateTranslationsForLanguage(language.getId(), translationsMap);
    assertEquals(languageDTO.getId(), createdLanguageId);
  }

  @Test
  public void testCreateLanguageWithPrefix() {
    when(languageMapper.mapToEntity(languageDTO)).thenReturn(language);
    when(keyRepository.findAll()).thenReturn(keys);
    String createdLanguageId = languageService.createLanguage(languageDTO, "en_");
    verify(languageRepository, times(1)).save(language);
    verify(keyService, times(1))
        .updateTranslationsForLanguage(language.getId(), prefixedTranslationsMap);
    assertEquals(languageDTO.getId(), createdLanguageId);
  }

  @Test
  public void testCreateLanguageByLocaleWithoutPrefix() {
    Map<String, String> translationsForLocale = new HashMap<>();
    key.getData().forEach(data1 -> translationsForLocale.put(key.getName(), data1.getValue()));

    when(languageMapper.mapToEntity(languageDTO)).thenReturn(language);
    when(languageRepository.fetchById(language.getId())).thenReturn(language);
    when(keyService.getTranslationsForLocale(language.getLocale()))
        .thenReturn(translationsForLocale);
    String createdLanguageId = languageService.createLanguage(languageDTO, language.getId(), null);
    verify(languageRepository, times(1)).save(language);
    verify(keyService, times(1))
        .updateTranslationsForLanguage(language.getId(), translationsForLocale);
    assertEquals(languageDTO.getId(), createdLanguageId);
  }

  @Test
  public void testCreateLanguageByLocaleWithPrefix() {
    Map<String, String> translationsForLocale = new HashMap<>();
    key.getData().forEach(data1 -> translationsForLocale.put(key.getName(), data1.getValue()));

    when(languageMapper.mapToEntity(languageDTO)).thenReturn(language);
    when(languageRepository.fetchById(language.getId())).thenReturn(language);
    when(keyService.getTranslationsForLocale(language.getLocale()))
        .thenReturn(translationsForLocale);
    String createdLanguageId = languageService.createLanguage(languageDTO, language.getId(), "en_");
    verify(languageRepository, times(1)).save(language);
    verify(keyService, times(1))
        .updateTranslationsForLanguage(language.getId(), prefixedTranslationsForLocale);
    assertEquals(languageDTO.getId(), createdLanguageId);
  }

  @Test
  public void testUpdateLanguage() {
    language.setLocale("gr");
    language.setName("Greek");
    when(languageRepository.fetchById(languageDTO.getId())).thenReturn(language);
    languageService.updateLanguage(languageDTO);
    assertEquals(languageDTO.getName(), language.getName());
    assertEquals(languageDTO.getLocale(), language.getLocale());
  }

  @Test
  public void testDeleteLanguage() {
    languageService.deleteLanguage(language.getId());
    verify(languageRepository, times(1)).deleteById(language.getId());
  }

  @Test
  public void testActivateLanguage() {
    language.setActive(false);
    when(languageRepository.fetchById(language.getId())).thenReturn(language);
    languageService.activateLanguage(language.getId());
    assertTrue(language.isActive());
  }

  @Test
  public void testDeactivateLanguage() {
    language.setActive(true);
    when(languageRepository.fetchById(language.getId())).thenReturn(language);
    languageService.deactivateLanguage(language.getId());
    assertFalse(language.isActive());
  }

  @Test
  public void testGetLanguage() {
    when(languageRepository.fetchById(language.getId())).thenReturn(language);
    when(languageMapper.mapToDTO(language)).thenReturn(languageDTO);
    LanguageDTO foundLanguageDTO = languageService.getLanguage(language.getId());
    assertEquals(languageDTO, foundLanguageDTO);
  }

  @Test
  public void testGetLanguageByLocale() {
    when(languageRepository.findByLocale(language.getLocale()))
        .thenReturn(language);
    when(languageMapper.mapToDTO(language)).thenReturn(languageDTO);
    LanguageDTO foundLanguageDTO = languageService
        .getLanguageByLocale(language.getLocale());
    assertEquals(languageDTO, foundLanguageDTO);
  }

  @Test
  public void testGetLanguageByLocaleWithFallbackAndReducedLocale() {
    language.setActive(true);
    when(languageMapper.mapToDTO(language)).thenReturn(languageDTO);
    when(languageRepository.findByLocale(language.getLocale())).thenReturn(language);
    LanguageDTO foundLanguageDTO = languageService.getLanguageByLocale("en_", true);
    assertEquals(languageDTO, foundLanguageDTO);
  }

  @Test
  public void getLanguageByLocaleWithFallbackAndReducedLocaleInactiveTest() {
    when(languageRepository.findByLocale(language.getLocale())).thenReturn(language);
    LanguageDTO foundLanguageDTO = languageService.getLanguageByLocale("en_", true);
    assertNull(foundLanguageDTO);
  }

  @Test
  public void getEffectiveLanguageNullTest() {
    assertNull(languageService.getEffectiveLanguage("en_-en", "en"));
  }

  @Test
  public void getLanguageByLocaleNoFallbackTest() {
    LanguageDTO foundLanguageDTO = languageService.getLanguageByLocale("en_", false);
    assertNull(foundLanguageDTO);
  }

  @Test
  public void getLanguageByLocaleNoLanguageTest() {
    when(languageRepository.findByLocale(language.getLocale())).thenReturn(language);
    LanguageDTO foundLanguageDTO = languageService.getLanguageByLocale(language.getLocale(), true);
    assertNull(foundLanguageDTO);
  }

  @Test
  public void testGetLanguages() {
    when(languageRepository.findAllByOrderByNameAsc()).thenReturn(languages);
    when(languageMapper.mapToDTO(languages)).thenReturn(languagesDTO);
    List<LanguageDTO> allLanguagesDTO = languageService.getLanguages(true);
    assertEquals(languagesDTO, allLanguagesDTO);
  }

  @Test
  public void testGetActiveLanguages() {
    List<Language> activeLanguages = languages.stream().filter(l -> l.isActive())
        .collect(Collectors.toList());
    List<LanguageDTO> activeLanguagesDTO = languagesDTO.stream().filter(languageDTO ->
        languageDTO.isActive()).collect(Collectors.toList());

    when(languageRepository.findByActiveTrueOrderByNameAsc()).thenReturn(activeLanguages);
    when(languageMapper.mapToDTO(activeLanguages)).thenReturn(activeLanguagesDTO);
    List<LanguageDTO> foundActiveLanguagesDTO = languageService.getLanguages(false);
    assertEquals(activeLanguagesDTO, foundActiveLanguagesDTO);
  }

  @Test
  public void testGetEffectiveLanguage() {
    String locale = "en";
    language.setActive(true);
    when(languageRepository.findByLocale(locale)).thenReturn(language);
    String effectiveLanguage = languageService.getEffectiveLanguage("en", locale);
    assertEquals(locale, effectiveLanguage);
  }

  @Test
  public void getEffectiveLanguageInactiveTest() {
    String locale = "en";
    when(languageRepository.findByLocale(locale)).thenReturn(language);
    String effectiveLanguage = languageService.getEffectiveLanguage("en", locale);
    assertNull(effectiveLanguage);
  }

  @Test
  public void testGetEffectiveLanguageByDefaultLocale() {
    String defaultLocale = "en";
    language.setActive(true);
    when(languageRepository.findByLocale(defaultLocale)).thenReturn(language);
    String effectiveLanguage = languageService.getEffectiveLanguage("eng", defaultLocale);
    assertEquals(defaultLocale, effectiveLanguage);
  }

  @Test
  public void testGetEffectiveLanguageNoResult() {
    String defaultLocale = "en";
    language.setActive(false);
    String effectiveLanguage = languageService.getEffectiveLanguage("esp", defaultLocale);
    assertNull(effectiveLanguage);
  }

  @Test
  public void testDownloadLanguage() {
    List<Group> groups = initTestValues.createGroups();

    Map<String, String> translationsOfApplicationUI = new HashMap<>();
    translationsOfApplicationUI.put(key.getName(), data.getValue());

    Map<String, String> translationsOfApplicationReports = new HashMap<>();
    translationsOfApplicationReports.put("change_password", "Change Password");

    when(languageRepository.fetchById(language.getId())).thenReturn(language);
    when(groupService.findAll()).thenReturn(groups);

    when(keyService.getTranslationsForGroupAndLocale(groups.get(0).getId(), language.getLocale()))
        .thenReturn(translationsOfApplicationUI);
    when(keyService.getTranslationsForGroupAndLocale(groups.get(1).getId(), language.getLocale()))
        .thenReturn(translationsOfApplicationReports);

    byte[] bytes = languageService.downloadLanguage(language.getId());
    Path resourceDirectory = Paths.get("target/eng_translations_generated.xls");
    try {
      Files.write(resourceDirectory, bytes);
    } catch (IOException e) {
      e.printStackTrace();
    }
    groups.forEach(group -> verify(keyService, times(1))
        .getTranslationsForGroupAndLocale(group.getId(), language.getLocale()));
    //assertArrayEquals(lgXl, bytes);
  }

  @Test
  public void testUploadLanguage() throws IOException {
    List<Group> groups = initTestValues.createGroups();
    List<String> groupsIds = new ArrayList<>();
    for (Group group : groups) {
      when(groupService.findByTitle(group.getTitle())).thenReturn(group);
      groupsIds.add(group.getId());
    }
    languageService.uploadLanguage(language.getId(), lgXl);

    Workbook wb = WorkbookFactory.create(new BufferedInputStream(new ByteArrayInputStream(lgXl)));
    for (int si = 0; si < wb.getNumberOfSheets(); si++) {
      Map<String, String> translations = new HashMap<>();
      Sheet sheet = wb.getSheetAt(si);
      String groupName = sheet.getSheetName();
      if (StringUtils.isNotBlank(groupName)) {
        verify(groupService, times(1)).findByTitle(groupName);
      }

      for (int i = 1; i <= sheet.getLastRowNum(); i++) {
        String keyName = sheet.getRow(i).getCell(0).getStringCellValue();
        String keyValue = sheet.getRow(i).getCell(1).getStringCellValue();
        translations.put(keyName, keyValue);
      }
      verify(keyService, times(1))
          .updateTranslationsForLanguageByKeyName(language.getId(), groupsIds.get(si),
              translations);
    }
  }

  @Test(expected = LanguageProcessingException.class)
  public void uploadLanguageIoExceptionTest() {
    languageService.uploadLanguage(language.getId(), "unsupported type".getBytes());
  }

  @Test
  public void testIsLocaleRTL() {
    assertTrue(languageService.isLocaleRTL("ar"));
  }

  @Test
  public void testIsLocaleRTLFalse() {
    assertFalse(languageService.isLocaleRTL(language.getLocale()));
  }

}
