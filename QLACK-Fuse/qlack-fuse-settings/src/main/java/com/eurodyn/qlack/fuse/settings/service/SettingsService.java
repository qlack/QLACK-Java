package com.eurodyn.qlack.fuse.settings.service;

import com.eurodyn.qlack.common.exception.QAlreadyExistsException;
import com.eurodyn.qlack.common.exception.QDoesNotExistException;
import com.eurodyn.qlack.common.exception.QMismatchException;
import com.eurodyn.qlack.fuse.settings.dto.GroupDTO;
import com.eurodyn.qlack.fuse.settings.dto.SettingDTO;
import com.eurodyn.qlack.fuse.settings.mapper.SettingMapper;
import com.eurodyn.qlack.fuse.settings.model.QSetting;
import com.eurodyn.qlack.fuse.settings.model.Setting;
import com.eurodyn.qlack.fuse.settings.repository.SettingRepository;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import javax.transaction.Transactional;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Provides settings administration functionality
 *
 * @author European Dynamics SA.
 */
@Log
@Service
@Validated
@Transactional
public class SettingsService {

  /**
   * Settings mapper
   */
  private final SettingMapper settingMapper;

  /**
   * Settings repository
   */
  private final SettingRepository settingRepository;

  /**
   * Querydsl query type for Setting
   */
  private QSetting qsetting = QSetting.setting;

  @Autowired
  public SettingsService(SettingMapper settingMapper, SettingRepository settingRepository) {
    this.settingMapper = settingMapper;
    this.settingRepository = settingRepository;
  }

  /**
   * Finds all the settings that are owned by the given owner.
   *
   * @param owner the owner of the persisted settings
   * @param includeSensitive flag determines whether to include the sensitive settings in the
   * results
   * @return a list of settings owned by the specific owner
   */
  public List<SettingDTO> getSettings(String owner, boolean includeSensitive) {
    log.info(MessageFormat.format("Retrieving settings owned by: {0}.", owner));
    BooleanExpression predicate = qsetting.owner.endsWith(owner);
    if (!includeSensitive) {
      predicate = predicate.and(qsetting.sensitive.ne(true));
    }

    return settingMapper.map(settingRepository.findAll(predicate));
  }

  /**
   * Return multiple settings by a list of settings keys.
   *
   * @param owner The owner of the settings.
   * @param keys The list of settings to return.
   * @param group The group of the settings.
   * @return a list containing the found settings
   */
  public List<SettingDTO> getSettings(String owner, List<String> keys, String group) {
    return keys.stream()
        .map(key -> getSetting(owner, key, group))
        .collect(Collectors.toList());
  }

  /**
   * Finds all the groups that are owned by the given owner.
   *
   * @param owner the owner of the persisted groups
   * @return a list of group names owned by the specific owner
   */
  public List<GroupDTO> getGroupNames(String owner) {
    log.info(MessageFormat.format("Retrieving groups owned by: {0}.", owner));
    Predicate predicate = qsetting.owner.eq(owner);

    return settingMapper.mapToGroupDTO(settingRepository.findAll(predicate));
  }

  /**
   * Finds a setting that matches the given owner, key and group.
   *
   * @param owner the owner of the persisted setting
   * @param key the key of the persisted setting
   * @param group the group of the persisted setting
   * @return the setting that is owned by the specific owner and has the specific key and group
   */
  public SettingDTO getSetting(String owner, String key, String group) {
    Optional<Setting> setting = getOptionalSetting(owner, key, group);
    log.info(
        MessageFormat
            .format("Retrieving setting with key: {0}, owned by: {1} and in group: {2} ", key,
                owner, group));

    return settingMapper.map(setting.orElseThrow(
        () -> new QDoesNotExistException(MessageFormat.format(
            "Did not find a setting with key: {0}.", key))));
  }

  /**
   * Finds all settings that match the given owner and group.
   *
   * @param owner the owner of the persisted settings
   * @param group the group of the persisted settings
   * @return a list of settings owned by specific owner and in specific group
   */
  public List<SettingDTO> getGroupSettings(String owner, String group) {
    log.finest(
        MessageFormat.format("Retrieving settings owned by: {0} and in group: {1} ", owner, group));
    Predicate predicate = qsetting.owner.eq(owner).and(qsetting.group.eq(group));
    return settingMapper.map(settingRepository.findAll(predicate));
  }

  /**
   * Creates a setting from the given Setting DTO.
   *
   * @param dto a setting dto, used to create the persisted setting
   */
  public void createSetting(SettingDTO dto) {
    String owner = dto.getOwner();
    String key = dto.getKey();
    String group = dto.getGroup();

    log.finest(
        MessageFormat
            .format("Creating setting with key: {0}, owned by: {1} and in group: {2} ", key, owner,
                group));
    try {
      getSetting(dto.getOwner(), key, group);
      throw new QAlreadyExistsException(MessageFormat.format(
          "A setting already exists with key: {0}.", key));
    } catch (QDoesNotExistException e) {
      Setting setting = settingMapper.mapToEntity(dto);

      settingRepository.save(setting);
    }
  }

  /**
   * Sets the value of a setting that matches the given params.
   *
   * @param owner the owner of the persisted setting
   * @param key the key of the persisted setting
   * @param val the value that will be set to the persisted setting
   * @param group the group of the persisted setting
   */
  public void setVal(String owner, String key, String val, String group) {
    log.finest(MessageFormat
        .format("Setting the value of setting with key: {0}, owned by: {1} and in group: {2} ", key,
            owner, group));
    Setting setting = getOptionalSetting(owner, key, group).orElseThrow(
        () -> new QDoesNotExistException(
            MessageFormat.format("Did not find a setting with key: {0}.", key)));
    setting.setVal(val);
  }

  /**
   * Set multiple settings at once.
   *
   * @param owner The owner of the settings.
   * @param keys The keys to set.
   * @param vals The values to set.
   * @param group The group of the settings.
   */
  public void setVals(String owner, List<String> keys, List<String> vals, String group) {
    if (keys.size() != vals.size()) {
      throw new QMismatchException("Number of keys is different to the number of values.");
    }

    IntStream.range(0, keys.size())
        .forEach(idx -> setVal(owner, keys.get(idx), vals.get(idx), group));
  }

  /**
   * Searches for a setting matching the params.
   *
   * @param owner the owner of the persisted setting
   * @param key the key of the persisted setting
   * @param group the group of the persisted setting
   * @return a optional object containing the setting ,if found, empty otherwise
   */
  private Optional<Setting> getOptionalSetting(String owner, String key, String group) {
    Predicate predicate = qsetting.owner.eq(owner)
        .and(qsetting.key.eq(key))
        .and(qsetting.group.eq(group));
    return settingRepository.findOne(predicate);
  }
}
