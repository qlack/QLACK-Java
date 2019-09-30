package com.eurodyn.qlack.fuse.lexicon.mapper;

import static org.junit.Assert.assertEquals;

import com.eurodyn.qlack.fuse.lexicon.InitTestValues;
import com.eurodyn.qlack.fuse.lexicon.dto.LanguageDTO;
import com.eurodyn.qlack.fuse.lexicon.model.Language;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class LanguageMapperTest {

  @InjectMocks
  private LanguageMapperImpl languageMapper;

  private InitTestValues initTestValues;

  @Before
  public void init() {
    initTestValues = new InitTestValues();
  }

  @Test
  public void mapToDTOTest() {
    Language language = initTestValues.createEnglishLanguage();
    LanguageDTO languageDTO = languageMapper.mapToDTO(language);

    assertEquals(language.getId(), languageDTO.getId());
    assertEquals(language.getLocale(), languageDTO.getLocale());
    assertEquals(language.getName(), languageDTO.getName());
  }

  @Test
  public void mapToDTOListTest() {
    List<Language> languages = new ArrayList<>();
    languages.add(initTestValues.createEnglishLanguage());
    List<LanguageDTO> languageDTOS = languageMapper.mapToDTO(languages);

    assertEquals(languages.size(), languageDTOS.size());
  }

  @Test
  public void mapToDTONullTest() {
    assertEquals(null, languageMapper.mapToDTO((Language) null));

    List<LanguageDTO> languageDTOS = languageMapper.mapToDTO(
        (List<Language>) null);
    assertEquals(null, languageDTOS);
  }

  @Test
  public void mapToEntityTest() {
    LanguageDTO languageDTO = initTestValues.createEnglishLanguageDTO();
    Language language = languageMapper.mapToEntity(languageDTO);

    assertEquals(languageDTO.getId(), language.getId());
    assertEquals(languageDTO.getLocale(), language.getLocale());
    assertEquals(languageDTO.getName(), language.getName());
  }

  @Test
  public void mapToEntityListTest() {
    List<LanguageDTO> languageDTOS = new ArrayList<>();
    languageDTOS.add(initTestValues.createEnglishLanguageDTO());
    List<Language> languages = languageMapper.mapToEntity(languageDTOS);

    assertEquals(languageDTOS.size(), languages.size());
  }

  @Test
  public void mapToEntityNullTest() {
    assertEquals(null, languageMapper.mapToEntity((LanguageDTO) null));

    List<Language> languages = languageMapper.mapToEntity(
        (List<LanguageDTO>) null);
    assertEquals(null, languages);
  }

}
