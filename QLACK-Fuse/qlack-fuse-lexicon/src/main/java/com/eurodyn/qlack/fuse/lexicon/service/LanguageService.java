package com.eurodyn.qlack.fuse.lexicon.service;

import com.eurodyn.qlack.common.exception.QAlreadyExistsException;
import com.eurodyn.qlack.fuse.lexicon.dto.LanguageDTO;
import com.eurodyn.qlack.fuse.lexicon.exception.LanguageProcessingException;
import com.eurodyn.qlack.fuse.lexicon.mapper.LanguageMapper;
import com.eurodyn.qlack.fuse.lexicon.model.Group;
import com.eurodyn.qlack.fuse.lexicon.model.Key;
import com.eurodyn.qlack.fuse.lexicon.model.Language;
import com.eurodyn.qlack.fuse.lexicon.repository.KeyRepository;
import com.eurodyn.qlack.fuse.lexicon.repository.LanguageRepository;
import com.eurodyn.qlack.fuse.lexicon.util.WorkbookUtil;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.regex.Pattern;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

/**
 * A LanguageService class that is used to implement crud operations in database
 * for Language entity.
 *
 * @author European Dynamics SA
 */
@Transactional
@Service
@Validated
@Log
@SuppressWarnings("squid:S4784")
public class LanguageService {

  // A pattern for RTL languages (from Google Closure Templates).
  private static final Pattern RtlLocalesRe = Pattern
    .compile(
      "^(ar|dv|he|iw|fa|nqo|ps|sd|ug|ur|yi|.*[-_](Arab|Hebr|Thaa|Nkoo|Tfng))"
        + "(?!.*[-_](Latn|Cyrl)($|-|_))($|-|_)");

  private final KeyRepository keyRepository;
  private final LanguageRepository languageRepository;

  private KeyService keyService;
  private GroupService groupService;

  private LanguageMapper languageMapper;

  @Autowired
  public LanguageService(KeyService keyService, GroupService groupService,
    LanguageRepository languageRepository,
    KeyRepository keyRepository, LanguageMapper languageMapper) {
    this.keyService = keyService;
    this.groupService = groupService;
    this.languageRepository = languageRepository;
    this.keyRepository = keyRepository;
    this.languageMapper = languageMapper;
  }

  /**
   * Creates a new language.
   *
   * @param language a dto containing all needed language data
   * @return the uuid of the created language
   */
  public String createLanguage(LanguageDTO language) {
    log.info(MessageFormat.format("Creating language: {0}", language));
    Language entity = languageMapper.mapToEntity(language);
    languageRepository.save(entity);
    return entity.getId();
  }

  /**
   * Checks if a language does not exist and then creates it.
   *
   * @param language a dto containing all needed language data
   * @return the uuid of the created language
   */
  public String createLanguageIfNotExists(LanguageDTO language) {
    log.info(MessageFormat.format("Creating language: {0}", language));
    if (languageRepository.findByLocale(language.getLocale()) == null) {
      return createLanguage(language);
    } else {
      throw new QAlreadyExistsException(
        "Language: " + language.getName()
          + " already exists and will not be created.");
    }
  }

  /**
   * Creates a language and adds given prefix before each available
   * translation.
   *
   * @param language a dto containing all needed language data
   * @param translationPrefix a language specific prefix that will be added
   * before every translation
   * @return the uuid of the created language
   */
  public String createLanguage(LanguageDTO language, String translationPrefix) {
    log.info(MessageFormat
      .format("Creating language: {0} and adding prefix : {1} to translations",
        language,
        translationPrefix));
    Language entity = languageMapper.mapToEntity(language);
    languageRepository.save(entity);
    Map<String, String> translations = new HashMap<>();
    for (Key key : keyRepository.findAll()) {
      translations.put(key.getId(),
        (translationPrefix != null ? (translationPrefix + key.getName())
          : key.getName()));
    }
    keyService.updateTranslationsForLanguage(entity.getId(), translations);

    return entity.getId();
  }

  /**
   * Creates a language and adds given prefix before each available
   * translation.
   *
   * @param language a dto containing all needed language data
   * @param sourceLanguageId the id of the language that will be used to find
   * the translations
   * @param translationPrefix a language specific prefix that will be added
   * before every translation
   * @return the uuid of the created language
   */
  public String createLanguage(LanguageDTO language, String sourceLanguageId,
    String translationPrefix) {
    log.info(MessageFormat.format(
      "Creating language: {0} and adding prefix : {1} to translations of language with id {2}: ",
      language, translationPrefix, sourceLanguageId));
    Language entity = languageMapper.mapToEntity(language);
    entity.setId(language.getId());
    languageRepository.save(entity);

    Map<String, String> translations = keyService
      .getTranslationsForLocale(
        (languageRepository.fetchById(sourceLanguageId)).getLocale());

    if (translationPrefix != null) {
      for (Map.Entry<String, String> entry : translations.entrySet()) {
        translations.put(entry.getKey(), translationPrefix + entry.getValue());
      }
    }
    keyService.updateTranslationsForLanguage(entity.getId(), translations);

    return entity.getId();
  }

  /**
   * Updates name and locale of a language.
   *
   * @param language a dto containing all needed data to update a language
   */
  public void updateLanguage(LanguageDTO language) {
    log.info(MessageFormat.format("Updating language : {0}", language));
    Language entity = languageRepository.fetchById(language.getId());
    entity.setName(language.getName());
    entity.setLocale(language.getLocale());
  }

  /**
   * Deletes a language by given id.
   *
   * @param languageId the id of the language to delete
   */
  public void deleteLanguage(String languageId) {
    log.info(MessageFormat.format("Deleting language with id {0}", languageId));
    languageRepository.deleteById(languageId);
  }

  /**
   * Activates a language.
   *
   * @param languageId the id of the language to activate
   */
  public void activateLanguage(String languageId) {
    log.info(
      MessageFormat.format("Activating language with id {0}", languageId));
    Language language = languageRepository.fetchById(languageId);
    language.setActive(true);
  }

  /**
   * Deactivates a language.
   *
   * @param languageId the id of the language to deactivate
   */
  public void deactivateLanguage(String languageId) {
    Language language = languageRepository.fetchById(languageId);
    language.setActive(false);
  }

  /**
   * Fetches a language by given id.
   *
   * @param languageId the id of the language to fetch
   * @return a dto containing the language that matches the specific id
   */
  public LanguageDTO getLanguage(String languageId) {
    log.info(MessageFormat.format("Fetching language with id {0}", languageId));
    return languageMapper.mapToDTO(languageRepository.fetchById(languageId));
  }

  /**
   * Fetches a language by given locale.
   *
   * @param locale the locale of the language to fetch
   * @return a dto containing the language that matches the specific locale
   */
  public LanguageDTO getLanguageByLocale(String locale) {
    log.info(MessageFormat.format("Fetching language with locale {0}", locale));
    return getLanguageByLocale(locale, false);
  }

  /**
   * Fetches a language by given locale. If needed, it can further process the
   * given locale and then search for the language.
   *
   * @param locale the locale of the language to fetch
   * @param fallback flag to define if search should be repeated with
   * processed locale after failing to find any language
   * @return a dto containing the language that matches the given (or
   * processed) locale.
   */
  public LanguageDTO getLanguageByLocale(String locale, boolean fallback) {
    String fallbackMsg =
      "Fallback will " + (fallback ? "be attempted." : "not be attempted.");
    log.info(MessageFormat.format("Fetching language with locale {0}. ", locale)
      + fallbackMsg);
    Language language = languageRepository.findByLocale(locale);
    if (fallback && language == null) {
      language = languageRepository
        .findByLocale(getEffectiveLanguage(locale, null));
    }
    return languageMapper.mapToDTO(language);
  }

  /**
   * Fetches all active languages. Inactive languages can be also included, if
   * wanted.
   *
   * @param includeInactive a flag to define whether inactive languages will
   * be included
   * @return a list containing DTO of languages.
   */
  public List<LanguageDTO> getLanguages(boolean includeInactive) {
    String languageMsg =
      includeInactive ? "languages" : "only active languages";
    log.info("Fetching all " + languageMsg);
    List<Language> languages =
      includeInactive ? languageRepository.findAllByOrderByNameAsc()
        : languageRepository.findByActiveTrueOrderByNameAsc();
    return languageMapper.mapToDTO(languages);
  }

  /**
   * Searches for a language matching the given locale. If nothing is found
   * with first attempt, the locale is processed by removing special
   * characters (_-) and searching is re-attempted. In case of no result after
   * the second search, a final search is executed using the provided default
   * locale.
   *
   * @param locale the locale to be searched in the lexicon
   * @param defaultLocale the default locale to be searched if the locale does
   * not exist
   * @return the locale name to use
   */
  public String getEffectiveLanguage(String locale, String defaultLocale) {
    log.info(
      MessageFormat.format("Searching for language with locale: {0} ", locale));
    Language language = languageRepository.findByLocale(locale);
    if ((language != null) && (language.isActive())) {
      return locale;
    }

    int index = StringUtils.indexOfAny(locale, "_-");
    if (index > 0) {
      String reducedLocale = locale.substring(0, index);
      log.info(MessageFormat
        .format(
          "No language has been found. Re-attempting search with locale: {0}",
          reducedLocale));
      language = languageRepository.findByLocale(reducedLocale);
      if ((language != null) && (language.isActive())) {
        return reducedLocale;
      }
    }

    log.info(MessageFormat
      .format(
        "No language has been found. Re-attempting search with default locale: {0}",
        defaultLocale));
    Language defaultLanguage = languageRepository.findByLocale(defaultLocale);
    if ((defaultLanguage != null) && (defaultLanguage.isActive())) {
      return defaultLocale;
    }
    return null;
  }

  /**
   * Given an existing language id, it returns the byte representation of an
   * excel file that contains all the keys and the values for that
   * translation.
   *
   * @param languageId the id of the language to process
   * @return a byte array containing the Excel representation of the
   * language's translations.
   */
  public byte[] downloadLanguage(String languageId) {
    log.info(
      MessageFormat.format("Downloading language with id: {0}", languageId));

    // Check that the language exists and get its translations
    Language language = languageRepository.fetchById(languageId);

    // Create an Excel workbook. The workbook will contain a sheet for each
    // group.
    Workbook wb = WorkbookUtil.createHssfWorkbook();
    CreationHelper createHelper = wb.getCreationHelper();

    // Iterate over all existing groups and create a sheet for each one.
    // Creating a new list below and not using the one retrieved from
    // Group.findAll since result lists are read only and
    // we need to add the empty group below to the list.
    List<Group> groups = groupService.findAll();
    // Add an dummy entry to the list to also check for translations without
    // a group.
    Group emptyGroup = new Group();
    emptyGroup.setId(null);
    emptyGroup.setTitle("<No group>");
    groups.add(0, emptyGroup);
    for (Group group : groups) {
      Map<String, String> translations = keyService
        .getTranslationsForGroupAndLocale(group.getId(), language.getLocale());
      if (!translations.isEmpty()) {
        Sheet sheet = wb.createSheet(group.getTitle());

        // Add the header.
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0)
          .setCellValue(createHelper.createRichTextString("Key"));
        headerRow.createCell(1)
          .setCellValue(createHelper.createRichTextString("Translation"));

        // Add the data.
        int rowCounter = 1;
        for (Map.Entry<String, String> entry : translations.entrySet()) {
          Row row = sheet.createRow(rowCounter++);
          row.createCell(0)
            .setCellValue(createHelper.createRichTextString(entry.getKey()));
          row.createCell(1)
            .setCellValue(createHelper.createRichTextString(entry.getValue()));
        }
      }
    }

    // Create the byte[] holding the Excel data.
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    try {
      wb.write(bos);
      return bos.toByteArray();
    } catch (IOException ex) {
      // Convert to a runtime exception in order to roll back transaction
      log.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
      throw new LanguageProcessingException(
        "Error creating Excel file for language " + languageId);
    } finally {
      try {
        wb.close();
      } catch (IOException e) {
        log.severe(e.getLocalizedMessage());
      }
    }
  }

  /**
   * Given an excel file that contains all the keys and the values for a
   * translation, persists the language and all it's keys and values.
   *
   * @param languageId the id of the language to persist
   * @param lgXL a byte array containing the Excel representation of the
   * language's translations.
   */
  public void uploadLanguage(String languageId, byte[] lgXL) {
    try (Workbook wb = WorkbookFactory
      .create(new BufferedInputStream(new ByteArrayInputStream(lgXL)))) {
      for (int si = 0; si < wb.getNumberOfSheets(); si++) {
        Map<String, String> translations = new HashMap<>();
        Sheet sheet = wb.getSheetAt(si);
        String groupName = sheet.getSheetName();
        String groupId = null;
        if (StringUtils.isNotBlank(groupName)) {
          groupId = groupService.findByTitle(groupName).getId();
        }
        // Skip first row (the header of the Excel file) and start
        // parsing translations.
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
          String keyName = sheet.getRow(i).getCell(0).getStringCellValue();
          String keyValue = sheet.getRow(i).getCell(1).getStringCellValue();
          translations.put(keyName, keyValue);
        }
        keyService.updateTranslationsForLanguageByKeyName(languageId, groupId,
          translations);
      }
    } catch (IOException ex) {
      // Convert to a runtime exception in order to roll back transaction
      log.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
      throw new LanguageProcessingException(
        "Error reading Excel file for language " + languageId);
    }
  }

  /**
   * Checks if the syntax of the language is right to left.
   *
   * @param locale the locale to be checked
   * @return true if the locale is RTL, else if if LTR
   */
  public boolean isLocaleRTL(String locale) {
    return RtlLocalesRe.matcher(locale).find();
  }
}
