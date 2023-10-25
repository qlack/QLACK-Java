package com.eurodyn.qlack.fuse.lexicon.mapper;

import static org.junit.jupiter.api.Assertions.*;

import com.eurodyn.qlack.fuse.lexicon.InitTestValues;
import com.eurodyn.qlack.fuse.lexicon.dto.LanguageDTO;
import com.eurodyn.qlack.fuse.lexicon.model.Language;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class LanguageMapperTest {

  @InjectMocks
  private LanguageMapperImpl languageMapper;

  private InitTestValues initTestValues;

  @BeforeEach
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
    assertNull( languageMapper.mapToDTO((Language) null));
    List<LanguageDTO> languageDTOS = languageMapper.mapToDTO(
      (List<Language>) null);
    assertNull(languageDTOS);
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
    assertNull(languageMapper.mapToEntity((LanguageDTO) null));

    List<Language> languages = languageMapper.mapToEntity(
      (List<LanguageDTO>) null);
    assertNull(languages);
  }

}
