package com.eurodyn.qlack.fuse.lexicon.service;

import com.eurodyn.qlack.common.exception.QAlreadyExistsException;
import com.eurodyn.qlack.fuse.lexicon.criteria.KeySearchCriteria;
import com.eurodyn.qlack.fuse.lexicon.criteria.KeySearchCriteria.SortType;
import com.eurodyn.qlack.fuse.lexicon.dto.KeyDTO;
import com.eurodyn.qlack.fuse.lexicon.mappers.KeyMapper;
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
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import java.text.MessageFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Transactional
@Service
@Validated
@Log
public class KeyService {

  private final KeyRepository keyRepository;
  private final GroupRepository groupRepository;
  private final DataRepository dataRepository;
  private final LanguageRepository languageRepository;
  // Entities for queries
  QData qData = QData.data;
  QKey qKey = QKey.key;
  private KeyMapper keyMapper;

  @Autowired
  public KeyService(KeyRepository keyRepository, GroupRepository groupRepository, KeyMapper keyMapper,
    DataRepository dataRepository, LanguageRepository languageRepository) {
    this.keyRepository = keyRepository;
    this.keyMapper = keyMapper;
    this.groupRepository = groupRepository;
    this.dataRepository = dataRepository;
    this.languageRepository = languageRepository;
  }

  /**
   * Creates a translation Key and it's translations. Translations are created if they are provided, or if default creation is wanted.
   *
   * @param key a DTO containing the data of the Key
   * @param createDefaultTranslations a flag defining whether default translations should be created
   * @return the id of the created Key
   */
  public String createKey(KeyDTO key, boolean createDefaultTranslations) throws QAlreadyExistsException {

    log.info(MessageFormat.format("Creating key: {0}", key));

    // Create the new key.
    Key entity = new Key();
    entity.setName(key.getName());

    String keyGroupPair = key.getName() + "/";
    if (key.getGroupId() != null) {
      Group group = groupRepository.fetchById(key.getGroupId());
      entity.setGroup(group);
      keyGroupPair += group.getTitle();
    } else {
      keyGroupPair += "No Group";
    }

    try {
      if (getKeyByName(key.getName(), key.getGroupId(), false) != null) {
        throw new QAlreadyExistsException("Key/Group pair already exists:" + keyGroupPair);
      }
    } catch (IncorrectResultSizeDataAccessException e) {
      throw new QAlreadyExistsException("Key/Group pair already exists:" + keyGroupPair);
    }

    keyRepository.save(entity);

    if (createDefaultTranslations) {
      log.info(MessageFormat.format("Creating default translations for key with id: {0}", key.getId()));
      List<Language> languages = languageRepository.findAll();
      for (Language language : languages) {
        String translation = key.getTranslations() != null ? key.getTranslations().get(language.getId()) : key.getName();
        updateTranslation(entity.getId(), language.getId(), translation);
      }
    } else if (key.getTranslations() != null) {
      log.info(MessageFormat.format("Updating translations for key with id: {0}", key.getId()));
      for (String languageId : key.getTranslations().keySet()) {
        updateTranslation(entity.getId(), languageId, key.getTranslations().get(languageId));
      }
    }

    return entity.getId();
  }

  /**
   * Creates a translation for each item of the given list.
   *
   * @param keys a list of DTO, each containing the data of a Key
   * @param createDefaultTranslations a flag defining whether default translations should be created
   * @return a list containing the ids of the created Keys
   */
  public List<String> createKeys(List<KeyDTO> keys, boolean createDefaultTranslations) {
    log.info(MessageFormat.format("Creating keys : {0}", keys));
    List<String> ids = new ArrayList<>();
    for (KeyDTO key : keys) {
      ids.add(createKey(key, createDefaultTranslations));
    }
    return ids;
  }

  /**
   * Deletes a Key.
   */
  public void deleteKey(String keyId) {
    log.info(MessageFormat.format("Deleting key with id: {0}", keyId));
    keyRepository.deleteById(keyId);
  }

  /**
   * Deletes multiple Keys.
   *
   * @param keyIds a collection of Key ids to delete
   */
  public void deleteKeys(Collection<String> keyIds) {
    log.info(MessageFormat.format("Deleting keys: {0}", keyIds));
    for (String keyId : keyIds) {
      deleteKey(keyId);
    }
  }

  /**
   * Deletes all Keys of a group.
   *
   * @param groupId the id of the group
   */
  public void deleteKeysByGroupId(String groupId) {
    log.info(MessageFormat.format("Deleting all keys from group with id: {0}", groupId));
    Predicate predicate = qKey.group.id.eq(groupId);
    keyRepository.deleteAll(keyRepository.findAll(predicate));
  }

  /**
   * Renames an existing Key.
   *
   * @param keyId the id o the Key to rename
   * @param newName the new name of the Key
   */
  public void renameKey(String keyId, String newName) {
    log.info(MessageFormat.format("Renaming key with id: {0} to {1}", keyId, newName));
    Key key = keyRepository.fetchById(keyId);
    key.setName(newName);
  }

  /**
   * Moves a Key to another group.
   *
   * @param keyId the id of the Key to move
   * @param newGroupId the id of the destination group
   */
  public void moveKey(String keyId, String newGroupId) {
    log.info(MessageFormat.format("Moving key with id: {0} to group with id: {1}", keyId, newGroupId));
    Key key = keyRepository.fetchById(keyId);
    key.setGroup(groupRepository.fetchById(newGroupId));
  }

  /**
   * Moves multiple Keys to a new Group.
   */
  public void moveKeys(Collection<String> keyIds, String newGroupId) {
    log.info(MessageFormat.format("Moving keys with ids: {0} to group with id: {1}", keyIds, newGroupId));
    for (String keyId : keyIds) {
      moveKey(keyId, newGroupId);
    }
  }

  /**
   * Fetches a persisted Key based on its id. Key translations can be included.
   *
   * @param keyId the id of the persisted Key
   * @param includeTranslations a flag to determine if Key translations should be included in the returned DTO
   * @return the persisted Key
   */
  public KeyDTO getKeyById(String keyId, boolean includeTranslations) {
    log.info(MessageFormat.format("Fetching key with id: {0}", keyId));
    Key key = keyRepository.fetchById(keyId);
    return getKey(key, includeTranslations);
  }

  /**
   * Fetches a persisted Key based on its name and group. Key translations can be included.
   *
   * @param keyName the name of the persisted Key
   * @param groupId the id of the persisted Group
   * @param includeTranslations a flag to determine if Key translations should be included in the returned DTO
   * @return the persisted Key
   */
  public KeyDTO getKeyByName(String keyName, String groupId, boolean includeTranslations) {
    log.info(MessageFormat.format("Fetching key with name: {0} and in group with id : {1}", keyName, groupId));
    Key key = keyRepository.findByNameAndGroupId(keyName, groupId);
    return getKey(key, includeTranslations);
  }

  private KeyDTO getKey(Key key, boolean includeTranslations) {
    return keyMapper.mapToDTO(key, includeTranslations);
  }

  /**
   * Finds all keys that match given criteria.
   *
   * @param criteria the criteria used to search the keys
   * @param includeTranslations flag to determine whether translations should be included in the returned DTO
   * @return a list of persisted Keys matching the given criteria
   */
  public List<KeyDTO> findKeys(KeySearchCriteria criteria, boolean includeTranslations) {
    log.info(MessageFormat.format("Fetching all keys matching the criteria: {0}", criteria));

    Predicate predicate = new BooleanBuilder();

    if (criteria.getKeyName() != null) {
      predicate = ((BooleanBuilder) predicate).and(qKey.name.eq(criteria.getKeyName()));
    }
    if (criteria.getGroupId() != null) {
      predicate = ((BooleanBuilder) predicate).and(qKey.group.id.eq(criteria.getGroupId()));
    }

    List<KeyDTO> dtos = new ArrayList<>();
    keyRepository.findAll(predicate, criteria.getPageable()).forEach(entity -> dtos.add(getKey(entity, includeTranslations)));
    return dtos;
  }

  /**
   * Finds the total number of  keys that match given criteria.
   *
   * @param criteria the criteria used to search the keys
   * @return a Long type of the total number of Keys matching the given criteria
   */
    public Long findTotalKeys(KeySearchCriteria criteria) {
      log.info(MessageFormat.format("Fetching the total number of keys matching the criteria: {0}", criteria));

      Predicate predicate = new BooleanBuilder();

      if (criteria.getKeyName() != null) {
        predicate = ((BooleanBuilder) predicate).and(qKey.name.eq(criteria.getKeyName()));
      }
      if (criteria.getGroupId() != null) {
        predicate = ((BooleanBuilder) predicate).and(qKey.group.id.eq(criteria.getGroupId()));
      }

      return keyRepository.count(predicate);
    }

  private void update(Data data) {
    data.setLastUpdatedOn(Instant.now().toEpochMilli());
    dataRepository.save(data);
  }

  /**
   * Updates a translation based on it's Key and language ids.
   *
   * @param keyId the id of the Key that will be updated
   * @param languageId the id of the translation's language
   * @param value the updated value of the translation
   */
  public void updateTranslation(String keyId, String languageId, String value) {
    log.info(MessageFormat.format("Updating translation for key: {0} and language: {1}", keyId, languageId));
    Data data = dataRepository.findByKeyIdAndLanguageId(keyId, languageId);
    Language language = data == null ? languageRepository.fetchById(languageId) : null;
    commonUpdateTranslationWithKeyId(data, keyId, language, value);
  }

  /**
   * Updates a translation based on it's Key id and locale.
   *
   * @param keyId the id of the Key that will be updated
   * @param locale the locale of the translation's language
   * @param value the updated value of the translation
   */
  public void updateTranslationByLocale(String keyId, String locale, String value) {
    log.info(MessageFormat.format("Updating translation for key: {0} and locale: {1}", keyId, locale));
    Data data = dataRepository.findByKeyIdAndLanguageLocale(keyId, locale);
    Language language = data == null ? languageRepository.findByLocale(locale) : null;
    commonUpdateTranslationWithKeyId(data, keyId, language, value);
  }

  private void commonUpdateTranslationWithKeyId(Data data, String keyId, Language language, String value) {
    if (data == null) {
      data = new Data();
      data.setKey(keyRepository.fetchById(keyId));
      data.setLanguage(language);
    }
    data.setValue(value);
    update(data);
  }

  /**
   * Updates a translation based on it's key name, language and group ids.
   *
   * @param keyName the name of the Key that will be updated
   * @param groupId the id of the group the Key is part of
   * @param languageId the id of the translation's language
   * @param value the updated value of the translation
   */
  public void updateTranslationByGroupId(String keyName, String groupId, String languageId, String value) {
    Predicate predicate = qData.key.name.eq(keyName).and(qData.key.group.id.eq(groupId)).and(qData.language.id.eq(languageId));
    Data data = dataRepository.findOne(predicate).get();
    commonUpdateTranslationWithGroupId(data, keyName, groupId, languageId, value);
  }

  /**
   * Updates a translation based on it's key name and language id.
   *
   * @param keyName the name of the Key that will be updated
   * @param groupId the id of the group the Key is part of
   * @param languageId the id of the translations's language
   * @param value the updated value of the translation
   */
  public void updateTranslationByKeyName(String keyName, String groupId, String languageId, String value) {
    Data data = dataRepository.findByKeyNameAndLanguageId(keyName, languageId);
    commonUpdateTranslationWithGroupId(data, keyName, groupId, languageId, value);
  }

  private void commonUpdateTranslationWithGroupId(Data data, String keyName, String groupId, String languageId, String value) {
    if (data == null) {
      data = new Data();
      data.setKey(keyRepository.findByNameAndGroupId(keyName, groupId));
      data.setLanguage(languageRepository.fetchById(languageId));
    }
    data.setValue(value);
    update(data);
  }

  /**
   * Updates multiple translations based on their key, group and language id.
   *
   * @param keys a map containing the keys that will be updated and the new values
   * @param groupId the id of the group the keys are part of
   * @param languageId the id of the translation's id
   */
  public void updateTranslationsByGroupId(Map<String, String> keys, String groupId, String languageId) {
    log.info(MessageFormat.format("Updating translations: {0} of group {1} and language {2}", keys, groupId, languageId));
    for (Map.Entry<String, String> key : keys.entrySet()) {
      updateTranslationByGroupId(key.getKey(), groupId, languageId, key.getValue());
    }
  }

  /**
   * Updates multiple translations of a Key.
   *
   * @param keyId the id of the Key that will be updated
   * @param translations a map containing pairs of languageId/value that will be updated
   */
  public void updateTranslationsForKey(String keyId, Map<String, String> translations) {
    log.info(MessageFormat.format("Updating key {0} with translations {1}", keyId, translations));
    for (String languageId : translations.keySet()) {
      updateTranslation(keyId, languageId, translations.get(languageId));
    }
  }

  /**
   * Updates multiple translations of a Key.
   *
   * @param keyId the id of the Key that will be updated
   * @param translations a map containing pairs of locale/value that will be updated
   */
  public void updateTranslationsForKeyByLocale(String keyId, Map<String, String> translations) {
    log.info(MessageFormat.format("Updating key {0} with translations: {1}", keyId, translations));
    for (String locale : translations.keySet()) {
      updateTranslationByLocale(keyId, locale, translations.get(locale));
    }
  }

  /**
   * Updates translations of a language by given key ids.
   *
   * @param languageId the id of the language that will be updated
   * @param translations a map containing pairs of translation key id /value that will be updated
   */
  public void updateTranslationsForLanguage(String languageId, Map<String, String> translations) {
    log.info(MessageFormat.format("Updating language {0} with translations: {1}", languageId, translations));
    for (String keyId : translations.keySet()) {
      updateTranslation(keyId, languageId, translations.get(keyId));
    }
  }

  /**
   * Updates translations of a language by given key names.
   *
   * @param languageId the id of the language that will be updated
   * @param groupId the id of the group that the keys are part of
   * @param translations a map containing pairs of translation key names/values that will be updated
   */
  public void updateTranslationsForLanguageByKeyName(String languageId, String groupId, Map<String, String> translations) {
    log.info(MessageFormat.format("Updating language {0} with translations {1} of group {2} ", languageId,
      translations, groupId));
    for (String keyName : translations.keySet()) {
      updateTranslationByKeyName(keyName, groupId, languageId, translations.get(keyName));
    }
  }

  /**
   * Fetches a translation based on the Key name and the locale.
   *
   * @param keyName the name of the key to fetch
   * @param locale the locale of the translation
   * @return the translation of given key in the given locale
   */
  public String getTranslation(String keyName, String locale) {
    log.info(MessageFormat.format("Fetching translation of key {0} for locale {1}", keyName, locale));
    Data data = dataRepository.findByKeyNameAndLanguageLocale(keyName, locale);
    return data != null ? data.getValue() : null;
  }

  /**
   * Fetching all translations of a key in a given group.
   *
   * @param keyName the name of the translation Key
   * @param groupId the id of the group that the Key is part of
   * @return a map containing pairs of locale/value translations
   */
  public Map<String, String> getTranslationsForKeyName(String keyName, String groupId) {
    log.info(MessageFormat.format("Fetching translations of key {0} in group {1}", keyName, groupId));
    Key key = keyRepository.findByNameAndGroupId(keyName, groupId);
    Map<String, String> translations = new HashMap<>();
    for (Data data : key.getData()) {
      translations.put(data.getLanguage().getLocale(), data.getValue());
    }
    return translations;
  }

  /**
   * Fetching translations of a key in given group name and locale
   *
   * @param keyName the name of the translation Key
   * @param groupName the name of the group that the key is part of
   * @param locale the locale of the translation
   */
  public String getTranslationForKeyGroupLocale(String keyName, String groupName, String locale) {
    log.info(MessageFormat.format("Fetching translation of key {0} in group {1} and locale {2}",
      keyName, groupName, locale));
    Predicate predicate = qData.key.name.eq(keyName).and(qData.key.group.title.eq(groupName)).and(qData.language.locale.eq(locale));
    List<Data> data = dataRepository.findAll(predicate);
    List<String> list = new ArrayList<>();

    for (Data d : data) {
      list.add(d.getValue());
    }

    return !list.isEmpty() ? list.get(0) : null;
  }

  /**
   * Fetches all translations for a locale.
   *
   * @param locale the locale whose translations will be returned
   * @return a map containing pairs of key name/locale translations for the given locale
   */
  public Map<String, String> getTranslationsForLocale(String locale) {
    log.info(MessageFormat.format("Fetching all translations for locale {0}", locale));
    Language language = languageRepository.findByLocale(locale);
    return getTranslations(language.getData());
  }

  /**
   * Fetches all translations of given group for given locale.
   *
   * @param groupId the id of the group
   * @param locale the locale whose translations will be returned
   * @return a map containing pairs of key name/locale translations for given group and locale
   */
  public Map<String, String> getTranslationsForGroupAndLocale(String groupId, String locale) {
    log.info(MessageFormat.format("Fetching all translations of group {0} for locale {1}", groupId, locale));
    List<Data> dataList = dataRepository.findByKeyGroupIdAndLanguageLocale(groupId, locale);
    return getTranslations(dataList);
  }

  private Map<String, String> getTranslations(List<Data> dataList) {
    Map<String, String> translations = new HashMap<>();
    for (Data data : dataList) {
      translations.put(data.getKey().getName(), data.getValue());
    }
    return translations;
  }

  /**
   * Fetches all translations of given group and given locale.
   *
   * @param groupName the name of the group
   * @param locale the locale whose translations will be returned
   * @return a map containing pairs of key name/locale translations for given group and locale
   */
  public Map<String, String> getTranslationsForGroupNameAndLocale(String groupName, String locale) {
    log.info(MessageFormat.format("Fetching all translations of group {0} for locale {1}", groupName, locale));
    Predicate predicate = (qData.key.group.title.eq(groupName)).and(qData.language.locale.eq(locale));
    return dataRepository.findAll(predicate).stream().collect(
      Collectors.toMap(a -> a.getKey().getName(), a -> a.getValue()));
  }

  /**
   * Fetches sorted translations for given group name and locale.
   *
   * @param groupName the name of the group the translations are part of
   * @param locale the locale of the translations
   * @param sortType the type of sorting (ascending/descending)
   * @return a map containing sorted pairs of translation key/value
   */
  public Map<String, String> getTranslationsForGroupNameAndLocaleSorted(String groupName, String locale, SortType sortType) {
    log.info(MessageFormat.format("Fetching sorted translations of group {0} and locale {1}. Translation order is {2}.",
      groupName, locale, sortType.toString().toLowerCase()));
    SortedSet<TranslationKV> sortedSet = getSortedTranslationSetForGroupNameAndLocale(groupName, locale, sortType);
    HashMap<String, String> sortedMap = new LinkedHashMap<>();
    for (TranslationKV x : sortedSet) {
      sortedMap.put(x.key, x.value);
    }
    return sortedMap;
  }

  /**
   * Fetches sorted Keys for given group name and locale.
   *
   * @param groupName the name of the group that the Keys are part of
   * @param locale the locale of the translations
   * @param sortType the type of sorting (ascending/descending)
   * @return a list containing the translation Keys sorted
   */
  public List<String> getKeysSortedByTranslation(String groupName, String locale, SortType sortType) {
    log.info(MessageFormat.format("Fetching sorted keys of group {0} and locale {1}. Translation order is {2}.",
      groupName, locale, sortType.toString().toLowerCase()));
    SortedSet<TranslationKV> sortedSet = getSortedTranslationSetForGroupNameAndLocale(groupName, locale, sortType);
    List<String> sortedList = new ArrayList<>(sortedSet.size());
    for (TranslationKV tkv : sortedSet) {
      sortedList.add(tkv.key);
    }
    return sortedList;
  }

  private SortedSet<TranslationKV> getSortedTranslationSetForGroupNameAndLocale(String groupName, String locale, SortType sortType) {
    Map<String, String> translations = getTranslationsForGroupNameAndLocale(groupName, locale);
    // sort in java side as we cannot order by in SQL side because translation data value is CLOB and not string
    SortedSet<TranslationKV> sortedSet = new TreeSet<>(new TranslationKV(sortType));
    for (Map.Entry<String, String> entry : translations.entrySet()) {
      sortedSet.add(new TranslationKV(entry.getKey(), entry.getValue()));
    }
    return sortedSet;
  }

  // This class is used to sort translation keys ordered by value.
  private static class TranslationKV implements Comparator<TranslationKV>, Comparable<TranslationKV> {

    String key;
    String value;
    SortType sortType;

    public TranslationKV(SortType sortType) {
      this.sortType = sortType;
    }

    public TranslationKV(String key, String value) {
      this.key = key;
      this.value = value;
    }

    @Override
    public int compare(TranslationKV arg0, TranslationKV arg1) {
      return sortType.equals(SortType.ASCENDING) ? arg0.value.compareTo(arg1.value)
        : arg1.value.compareTo(arg0.value);
    }

    @Override
    public int compareTo(TranslationKV arg0) {
      return sortType.equals(SortType.ASCENDING) ? value.compareTo(arg0.value)
        : arg0.value.compareTo(value);
    }

    @Override
    public boolean equals(Object obj) {
      if (obj == null) {
        return false;
      }
      if (!(obj instanceof TranslationKV)) {
        return false;
      }
      TranslationKV x = (TranslationKV) obj;
      if (value == null) {
        return (x.value == null);
      }
      return value.equals(x.value);
    }

    @Override
    public int hashCode() {
      return super.hashCode();
    }
  }
}
