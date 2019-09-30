package com.eurodyn.qlack.fuse.lexicon.service;

import com.eurodyn.qlack.fuse.lexicon.dto.GroupDTO;
import com.eurodyn.qlack.fuse.lexicon.mapper.GroupMapper;
import com.eurodyn.qlack.fuse.lexicon.model.Data;
import com.eurodyn.qlack.fuse.lexicon.model.Group;
import com.eurodyn.qlack.fuse.lexicon.model.Language;
import com.eurodyn.qlack.fuse.lexicon.model.QData;
import com.eurodyn.qlack.fuse.lexicon.model.QGroup;
import com.eurodyn.qlack.fuse.lexicon.model.QLanguage;
import com.eurodyn.qlack.fuse.lexicon.repository.DataRepository;
import com.eurodyn.qlack.fuse.lexicon.repository.GroupRepository;
import com.eurodyn.qlack.fuse.lexicon.repository.LanguageRepository;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.JPAExpressions;
import java.text.MessageFormat;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

/**
 * A Service class for Group . It is used to implement the crud operations
 *
 * @author European Dynamics SA
 */
@Transactional
@Service
@Validated
@Log
public class GroupService {

  private final GroupRepository groupRepository;
  private final LanguageRepository languageRepository;
  private final DataRepository dataRepository;
  private final GroupMapper groupMapper;

  private QGroup qGroup = QGroup.group;
  private QData qData = QData.data;
  private QLanguage qLanguage = QLanguage.language;

  @Autowired
  public GroupService(GroupRepository groupRepository, GroupMapper groupMapper,
      LanguageRepository languageRepository,
      DataRepository dataRepository) {
    this.groupRepository = groupRepository;
    this.groupMapper = groupMapper;
    this.languageRepository = languageRepository;
    this.dataRepository = dataRepository;
  }

  /**
   * Creates a group.
   *
   * @param group a DTO containing all information about the persisted group
   * @return the id of the group
   */
  public String createGroup(GroupDTO group) {
    log.info(MessageFormat.format("Creating group {0}: ", group));

    Group entity = groupMapper.mapToEntity(group);
    groupRepository.save(entity);
    return entity.getId();
  }

  /**
   * Updates a group.
   *
   * @param group a DTO containing all the information to update a persisted group
   */
  public void updateGroup(GroupDTO group) {
    log.info(MessageFormat.format("Updating group {0}: ", group));
    Group entity = groupRepository.fetchById(group.getId());
    entity.setTitle(group.getTitle());
    entity.setDescription(group.getDescription());
    groupRepository.save(entity);
  }

  /**
   * Deletes a group.
   *
   * @param groupID the id of the group to delete
   */
  public void deleteGroup(String groupID) {
    log.info(MessageFormat.format("Deleting group with id: {0}", groupID));
    groupRepository.deleteById(groupID);
  }

  /**
   * Fetches a group by given id.
   *
   * @param groupID the id of the group
   * @return a DTO containing the group that matches the specific id
   */
  public GroupDTO getGroup(String groupID) {
    log.info(MessageFormat.format("Fetching group with id: {0}", groupID));
    return groupMapper.mapToDTO(groupRepository.fetchById(groupID));
  }

  /**
   * Fetches a group by given title.
   *
   * @param groupTitle the title of the group
   * @return a DTO containing the group that matches the specific title
   */
  public GroupDTO getGroupByTitle(String groupTitle) {
    log.info(MessageFormat.format("Fetching group with title: {0}", groupTitle));
    return groupMapper.mapToDTO(findByTitle(groupTitle));
  }

  /**
   * Fetches all groups.
   *
   * @return a list containing all persisted groups
   */
  public List<Group> findAll() {
    log.info("Fetching all groups");
    return groupRepository.findAll();
  }

  /**
   * Fetches a group by given title.
   *
   * @param title the title of the group
   * @return a DTO containing the group that matches the specific title
   */
  public Group findByTitle(String title) {
    log.info(MessageFormat.format("Fetching group with title: {0}", title));
    return groupRepository.findByTitle(title);
  }

  /**
   * Fetches all included groups.
   *
   * @param excludedGroupNames a list containing the names of excluded groups
   * @return a set of DTO containing all included groups
   */
  public Set<GroupDTO> getRemainingGroups(List<String> excludedGroupNames) {
    log.info("Fetching all included groups");
    Predicate predicate = qGroup.title.notIn(excludedGroupNames);
    return groupMapper.mapToDTO(groupRepository.findAll(predicate)).stream()
        .collect(Collectors.toSet());
  }

  /**
   * Fetches all groups.
   *
   * @return a set of DTO containing all groups
   */
  public Set<GroupDTO> getGroups() {
    log.info("Fetching all groups");
    return new HashSet<>(groupMapper.mapToDTO(groupRepository.findAll()));
  }


  /**
   * Deletes all translations of a given group for given language.
   *
   * @param groupID the id of the group
   * @param languageID the id of the language
   */
  public void deleteLanguageTranslations(String groupID, String languageID) {
    log.info(MessageFormat
        .format("Deleting all translation from group with id {0} for language with id {1}",
            groupID, languageID));
    Language language = languageRepository.fetchById(languageID);
    deleteLanguageTranslationsByLocale(groupID, language.getLocale());
  }

  /**
   * Deletes all translation of a given group by given locale.
   *
   * @param groupID the id of the group
   * @param locale the locale name
   */
  public void deleteLanguageTranslationsByLocale(String groupID, String locale) {
    log.info(MessageFormat.format("Deleting all translation from group with id {0} for locale {1}",
        groupID, locale));
    List<Data> dataList = dataRepository.findByKeyGroupIdAndLanguageLocale(groupID, locale);
    for (Data data : dataList) {
      dataRepository.delete(data);
    }
  }

  /**
   * Finds when was the last update of any key in given group for given locale.
   *
   * @param groupID the group id
   * @param locale the locale name
   * @return a number representing the date of last update. The default return date is 'now'
   */
  public long getLastUpdateDateForLocale(String groupID, String locale) {
    log.info(MessageFormat
        .format("Getting the last update date of all keys from group with id {0} for locale {1}",
            groupID, locale));
    Predicate predicate = qData.key.group.id.eq(groupID)
        .and(qData.language.id.eq(JPAExpressions.select(qLanguage.id)
            .from(qLanguage)
            .where(qLanguage.locale.eq(locale))));
    List<Data> datas = dataRepository.findAll(predicate, Sort.by("lastUpdatedOn").descending());

    if (datas.iterator().hasNext()) {
      return datas.iterator().next().getLastUpdatedOn();
    } else {
      return Instant.now().toEpochMilli();
    }
  }

}
