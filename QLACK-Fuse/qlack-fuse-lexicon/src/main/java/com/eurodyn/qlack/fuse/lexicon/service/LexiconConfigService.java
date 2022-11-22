package com.eurodyn.qlack.fuse.lexicon.service;

import com.eurodyn.qlack.fuse.lexicon.dto.GroupDTO;
import com.eurodyn.qlack.fuse.lexicon.dto.KeyDTO;
import com.eurodyn.qlack.fuse.lexicon.dto.LanguageDTO;
import com.eurodyn.qlack.fuse.lexicon.exception.LexiconYMLProcessingException;
import com.eurodyn.qlack.fuse.lexicon.model.Application;
import com.eurodyn.qlack.fuse.lexicon.repository.ApplicationRepository;
import javax.annotation.PostConstruct;
import lombok.extern.java.Log;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.CustomClassLoaderConstructor;

import java.io.IOException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

/**
 * A lexicon Service class that is used to configure lexicon data.
 *
 * @author European Dynamics SA
 */
@Service
@Validated
@Transactional
@Log
@SuppressWarnings("squid:S4790")
public class LexiconConfigService {

  //Holds groupId + key pairs of translations with forceDelete
  private static final List<Map<String, String>> deletedGroupKeys = new ArrayList<>();
  private static final List<Map<String, String>> skippedGroupKeys = new ArrayList<>();
  // Service references.
  private final GroupService groupService;
  private final LanguageService languageService;
  private final KeyService keyService;
  private final ApplicationRepository applicationRepository;

  private final ClassLoader classLoader;

  private static final String FORCE_UPDATE = "forceUpdate";

  private static final String ROOT_YAML_FILE = "qlack-lexicon-config.yaml";

  @Autowired
  public LexiconConfigService(GroupService groupService,
      LanguageService languageService, KeyService keyService,
      ApplicationRepository applicationRepository) {
    this.groupService = groupService;
    this.languageService = languageService;
    this.keyService = keyService;
    this.applicationRepository = applicationRepository;
    this.classLoader = this.getClass().getClassLoader();
  }

  @PostConstruct
  public void init() {
    try {

      Enumeration<URL> entries = classLoader
          .getResources(ROOT_YAML_FILE);
      if (entries != null) {
        while (entries.hasMoreElements()) {
          updateTranslations(entries.nextElement(), ROOT_YAML_FILE);
        }
      }
    } catch (IOException e) {
      log.log(Level.SEVERE,
          "Could not search QLACK Lexicon configuration files.", e);
    }
  }

  /**
   * Processes a yaml file containing the QLACK lexicon configuration and then persists all lexicon components
   * (languages, groups and translations).
   *
   * @param yamlUrl the URL of the yaml file
   * @param symbolicName the name of the yaml file
   */
  public void updateTranslations(URL yamlUrl, String symbolicName) {

    try {
      log.finest("Processing yaml with QLACK lexicon configuration");

      String checksum = DigestUtils.md5Hex(yamlUrl.openStream());

      List<Application> applications = applicationRepository
          .findBySymbolicName(symbolicName);

      Application application =
          applications.isEmpty() ? null : applications.get(0);

      if (application == null) {
        application = new Application();
        application.setSymbolicName(symbolicName);
      } else if (application.getChecksum().equals(checksum)) {
        return;
      }

      Yaml yaml = new Yaml(
          new CustomClassLoaderConstructor(getClass().getClassLoader()));

      Map<String, Object> contents = yaml.load(yamlUrl.openStream());

      updateTranslationsGroups(contents);
      updateLanguages(contents);
      updateTranslations(contents);

      application.setChecksum(checksum);
      application.setExecutedOn(Calendar.getInstance().getTimeInMillis());
      applicationRepository.save(application);
    } catch (IOException | SecurityException ex) {
      log.log(Level.SEVERE, "Error handling lexicon YAML file", ex);
      throw new LexiconYMLProcessingException(
          "Error handling lexicon YAML file");
    }
  }

  /**
   * Updates the translation groups based on the yaml contents.
   *
   * @param contents the yaml contents
   */
  private void updateTranslationsGroups(Map<String, Object> contents) {
    // Process translation groups
    @SuppressWarnings("unchecked")
    List<Map<String, Object>> groups = (List<Map<String, Object>>) contents
        .get("groups");
    if (groups != null) {
      log.finest("Processing configuration of lexicon groups");
      for (Map<String, Object> group : groups) {
        String groupName = (String) group.get("name");
        String groupDescription = (String) group.get("description");
        GroupDTO groupDTO = groupService.getGroupByTitle(groupName);
        // If a group with this name does not exist create it.
        if (groupDTO == null) {
          groupDTO = new GroupDTO();
          groupDTO.setTitle(groupName);
          groupDTO.setDescription(groupDescription);
          groupService.createGroup(groupDTO);
        }
        // Else check the value of the forceUpdate flag
        else if ((group.get(FORCE_UPDATE) != null) && (Boolean.TRUE
            .equals(group.get(FORCE_UPDATE)))) {
          groupDTO.setDescription(groupDescription);
          groupService.updateGroup(groupDTO);
        }
      }
    }
  }

  /**
   * Updates the languages based on the yaml contents.
   *
   * @param contents the yaml contents
   */
  private void updateLanguages(Map<String, Object> contents) {
    // Process languages
    @SuppressWarnings("unchecked")
    List<Map<String, Object>> languages = (List<Map<String, Object>>) contents
        .get("languages");
    if (languages != null) {
      log.finest("Processing configuration of lexicon languages");
      for (Map<String, Object> language : languages) {
        String languageName = (String) language.get("name");
        String locale = (String) language.get("locale");
        LanguageDTO languageDTO = languageService.getLanguageByLocale(locale);
        // If a language with this locale does not exist create it.
        if (languageDTO == null) {
          languageDTO = new LanguageDTO();
          languageDTO.setName(languageName);
          languageDTO.setLocale(locale);
          languageDTO.setActive(true);
          languageService.createLanguage(languageDTO, null);
        }
        // Else check the value of the forceUpdate flag
        else if ((language.get(FORCE_UPDATE) != null) && (Boolean.TRUE
            .equals(language.get(FORCE_UPDATE)))) {
          languageDTO.setName(languageName);
          languageService.updateLanguage(languageDTO);
        }
      }
    }
  }

  /**
   * Updates the translations based on the yaml contents.
   *
   * @param contents the yaml contents
   */
  private void updateTranslations(Map<String, Object> contents) {
    // Process translations
    @SuppressWarnings("unchecked")
    List<Map<String, Object>> translationContents = (List<Map<String, Object>>) contents
        .get("translations");
    if (translationContents != null) {
      log.finest("Processing configuration of lexicon translations");
      for (Map<String, Object> translationContent : translationContents) {
        @SuppressWarnings("unchecked")
        List<String> excludedGroupName = (List<String>) translationContent
            .get("not_in_group");
        String locale = (String) translationContent.get("locale");
        String languageId = languageService.getLanguageByLocale(locale).getId();
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> translations = (List<Map<String, Object>>) translationContent
            .get("keys");

        // Process not_in_group
        if (excludedGroupName != null) {
          Set<GroupDTO> newGroups = groupService
              .getRemainingGroups(excludedGroupName);
          for (GroupDTO group : newGroups) {
            updateKeys(translations, group.getId(), languageId, locale);
          }
        } else { // Process group
          String groupName = (String) translationContent.get("group");
          String groupId =
              groupName != null ? groupService.getGroupByTitle(groupName).getId()
                  : null;
          updateKeys(translations, groupId, languageId, locale);
        }
      }
    }
  }

  /**
   * Updating translations of QLACK lexicon
   *
   * @param translations a map containing the translations
   * @param groupId the id of the group that the translations key are part of
   * @param languageId the id of the translation language
   */
  private void updateKeys(List<Map<String, Object>> translations,
      String groupId, String languageId, String locale) {
    log.finest(MessageFormat
        .format(
            "Updating translations of group with id {0} for language with id {1}",
            groupId,
            languageId));
    for (Map<String, Object> translation : translations) {
      String translationKey = translation.keySet().iterator().next();
      String translationValue = (String) translation.get(translationKey);
      boolean shouldDelete =
          translation.get("forceDelete") != null && Boolean.TRUE
              .equals(translation.get("forceDelete"));
      KeyDTO keyDTO = keyService.getKeyByName(translationKey, groupId, true);

      Map<String, String> candidate = new HashMap<>();
      candidate.put(groupId, translationKey);

      // If the key does not exist in the DB then create it.
      if (keyDTO == null) {
        if (shouldDelete || deletedGroupKeys.contains(candidate)
            || skippedGroupKeys
            .contains(candidate)) {
          skippedGroupKeys.add(candidate);
          log.log(Level.WARNING,
              "Skipped key: " + translationKey
                  + " because it was deleted recently. "
                  + "QLACK Lexicon configuration file should be updated manually.");
        } else {
          keyDTO = new KeyDTO();
          keyDTO.setGroupId(groupId);
          keyDTO.setName(translationKey);
          Map<String, String> keyTranslations = new HashMap<>();
          keyTranslations.put(languageId, translationValue);
          keyDTO.setTranslations(keyTranslations);
          keyService.createKey(keyDTO, false);
        }
      } else if (shouldDelete) {
        deletedGroupKeys.add(candidate);
        keyService.deleteKey(keyDTO.getId());
      }
      // If the key exists check if a translation exists and if it
      // does check if it is the same as the key name, which means
      // that the translation was created automatically (ex. when
      // adding a new language) and therefore it should be
      // updated. Otherwise only update the key if the forceUpdate
      // flag is set to true.
      else if ((keyDTO.getTranslations().get(locale) == null)
          || (keyDTO.getTranslations().get(locale).equals(translationKey))
          || ((translation.get(FORCE_UPDATE) != null)
          && (Boolean.TRUE.equals(translation.get(FORCE_UPDATE))))) {
        keyService
            .updateTranslation(keyDTO.getId(), languageId, translationValue);
      }
    }
  }
}
