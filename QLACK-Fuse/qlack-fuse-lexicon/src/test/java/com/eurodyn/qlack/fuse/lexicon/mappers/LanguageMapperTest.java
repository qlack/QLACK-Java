package com.eurodyn.qlack.fuse.lexicon.mappers;

import static org.junit.Assert.assertEquals;

import com.eurodyn.qlack.fuse.lexicon.InitTestValues;
import com.eurodyn.qlack.fuse.lexicon.dto.LanguageDTO;
import com.eurodyn.qlack.fuse.lexicon.model.Language;
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
public class LanguageMapperTest {

  @InjectMocks
  private LanguageMapperImpl languageMapperImpl;

  private InitTestValues initTestValues;
  private Language language;
  private LanguageDTO languageDTO;
  private List<Language> languages;
  private List<LanguageDTO> languagesDTO;

  @Before
  public void init() {
    initTestValues = new InitTestValues();
    languageDTO = initTestValues.createEnglishLanguageDTO();
    language = initTestValues.createEnglishLanguage();
    languages = initTestValues.createLanguages();
    languagesDTO = initTestValues.createLanguagesDTO();
  }

  @Test
  public void testMapToDTOId() {
    LanguageDTO languageDTO = languageMapperImpl.mapToDTO(language);
    assertEquals(language.getId(), languageDTO.getId());
  }

  @Test
  public void testMapToDTOName() {
    LanguageDTO languageDTO = languageMapperImpl.mapToDTO(language);
    assertEquals(language.getName(), languageDTO.getName());
  }

  @Test
  public void testMapToDTOLocale() {
    LanguageDTO languageDTO = languageMapperImpl.mapToDTO(language);
    assertEquals(language.getLocale(), languageDTO.getLocale());
  }

  @Test
  public void testMapToDTOActive() {
    LanguageDTO languageDTO = languageMapperImpl.mapToDTO(language);
    assertEquals(language.isActive(), languageDTO.isActive());
  }

  @Test
  public void testMapToDTOList() {
    languagesDTO = languageMapperImpl.mapToDTO(languages);
    assertEquals(languages.size(), languagesDTO.size());
  }

  @Test
  public void testMapToEntityId() {
    language = languageMapperImpl.mapToEntity(languageDTO);
    assertEquals(languageDTO.getId(), language.getId());
  }

  @Test
  public void testMapToEntityName() {
    language = languageMapperImpl.mapToEntity(languageDTO);
    assertEquals(languageDTO.getName(), language.getName());
  }

  @Test
  public void testMapToEntityLocale() {
    language = languageMapperImpl.mapToEntity(languageDTO);
    assertEquals(languageDTO.getLocale(), language.getLocale());
  }

  @Test
  public void testMapToEntityActive() {
    language = languageMapperImpl.mapToEntity(languageDTO);
    assertEquals(languageDTO.isActive(), language.isActive());
  }

  @Test
  public void testMapToEntityList() {
    languages = languageMapperImpl.mapToEntity(languagesDTO);
    assertEquals(languagesDTO.size(), languages.size());
  }
}
