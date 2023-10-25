package com.eurodyn.qlack.fuse.settings.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.eurodyn.qlack.common.exception.QAlreadyExistsException;
import com.eurodyn.qlack.common.exception.QDoesNotExistException;
import com.eurodyn.qlack.common.exception.QMismatchException;
import com.eurodyn.qlack.fuse.settings.InitTestValues;
import com.eurodyn.qlack.fuse.settings.dto.GroupDTO;
import com.eurodyn.qlack.fuse.settings.dto.SettingDTO;
import com.eurodyn.qlack.fuse.settings.mapper.SettingMapper;
import com.eurodyn.qlack.fuse.settings.model.QSetting;
import com.eurodyn.qlack.fuse.settings.model.Setting;
import com.eurodyn.qlack.fuse.settings.repository.SettingRepository;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * @author European Dynamics
 */

@ExtendWith(MockitoExtension.class)
public class SettingsServiceTest {

  @InjectMocks
  private SettingsService settingsService;

  @Spy
  private SettingMapper settingMapper;

  final private SettingRepository settingRepository = mock(SettingRepository.class);
  private QSetting qSetting;
  private InitTestValues initTestValues;

  private Setting setting;
  private SettingDTO settingDTO;

  private List<Setting> settings;
  private List<SettingDTO> settingsDTO;
  private Predicate ownerKeyGroupPredicate;

  @BeforeEach
  public void init() {
    settingsService = new SettingsService(settingMapper, settingRepository);
    qSetting = new QSetting("setting");
    initTestValues = new InitTestValues();

    setting = initTestValues.createSetting();
    settingDTO = initTestValues.createSettingDTO();
    settings = initTestValues.createSettings();
    settingsDTO = initTestValues.createSettingsDTO();

    ownerKeyGroupPredicate = qSetting.owner.eq(setting.getOwner())
      .and(qSetting.key.eq(setting.getKey()))
      .and(qSetting.group.eq(setting.getGroup()));
  }

  @Test
  public void testGetSettings() {

    when(
      settingRepository.findAll(qSetting.owner.endsWith(settingDTO.getOwner())))
      .thenReturn(settings);
    when(settingMapper.map(settings)).thenReturn(settingsDTO);

    List<SettingDTO> foundSettings = settingsService
      .getSettings(setting.getOwner(), true);
    assertEquals(settingsDTO, foundSettings);
  }

  @Test
  public void testGetSettingsExcludingSensitive() {

    List<Setting> notSensitiveSettings = settings.stream()
      .filter(s -> !s.isSensitive())
      .collect(Collectors.toList());
    List<SettingDTO> notSensitiveSettingsDTO = settingsDTO.stream()
      .filter(s -> !s.isSensitive())
      .collect(Collectors.toList());
    Predicate predicate = qSetting.owner.endsWith(settingDTO.getOwner());
    predicate = ((BooleanExpression) predicate)
      .and(qSetting.sensitive.ne(true));

    when(settingRepository.findAll(predicate)).thenReturn(notSensitiveSettings);
    when(settingMapper.map(notSensitiveSettings))
      .thenReturn(notSensitiveSettingsDTO);

    List<SettingDTO> foundSettings = settingsService
      .getSettings(setting.getOwner(), false);
    assertEquals(notSensitiveSettingsDTO, foundSettings);
  }

  @Test
  public void testGetGroupNames() {
    List<String> names = settings.stream().map(s -> s.getGroup())
      .collect(Collectors.toList());
    Predicate predicate = qSetting.owner.eq(setting.getOwner());

    List<GroupDTO> groupsDTO = new ArrayList<>();
    names.forEach(name -> {
      GroupDTO groupDTO = new GroupDTO();
      groupDTO.setName(name);
      groupsDTO.add(groupDTO);
    });

    when(settingRepository.findAll(predicate)).thenReturn(settings);
    when(settingMapper.mapToGroupDTO(settings)).thenReturn(groupsDTO);
    List<GroupDTO> foundGroups = settingsService
      .getGroupNames(setting.getOwner());
    assertEquals(groupsDTO, foundGroups);
  }

  @Test
  public void testGetSetting() {

    Optional<Setting> optionalSetting = Optional.of(setting);
    when(settingRepository.findOne(ownerKeyGroupPredicate))
      .thenReturn(optionalSetting);
    when(settingMapper.map(optionalSetting.get())).thenReturn(settingDTO);
    SettingDTO foundSetting = settingsService
      .getSetting(setting.getOwner(), setting.getKey(), setting.getGroup());
    assertEquals(settingDTO, foundSetting);
  }

  @Test
  public void testGetSettingException() {
    assertThrows(QDoesNotExistException.class, () -> {
      SettingDTO foundSetting = settingsService
              .getSetting(setting.getOwner(), setting.getKey(), setting.getGroup());
      assertEquals(settingDTO, foundSetting);
    });
  }

  @Test
  public void testGetGroupSettings() {
    List<Setting> expectedSettings = settings.stream()
      .filter(
        s -> setting.getOwner().equals(s.getOwner()) && setting.getGroup()
          .equals(s.getGroup()))
      .collect(Collectors.toList());

    List<SettingDTO> expectedSettingsDTO = settingsDTO.stream()
      .filter(
        dto -> setting.getOwner().equals(dto.getOwner()) && setting.getGroup()
          .equals(dto.getGroup()))
      .collect(Collectors.toList());

    Predicate predicate = qSetting.owner.eq(setting.getOwner())
      .and(qSetting.group.eq(setting.getGroup()));

    when(settingRepository.findAll(predicate)).thenReturn(expectedSettings);
    when(settingMapper.map(expectedSettings)).thenReturn(expectedSettingsDTO);

    List<SettingDTO> foundGroupSettings = settingsService
      .getGroupSettings(setting.getOwner(), setting.getGroup());
    assertEquals(expectedSettingsDTO, foundGroupSettings);
  }

  @Test
  public void testCreateSettingExisting() {
    assertThrows(QAlreadyExistsException.class, () -> {
      Optional<Setting> optionalSetting = Optional.of(setting);
      when(settingRepository.findOne(ownerKeyGroupPredicate))
              .thenReturn(optionalSetting);
      when(settingMapper.map(optionalSetting.get())).thenReturn(settingDTO);
      settingsService.createSetting(settingDTO);
    });
  }

  @Test
  public void testCreateSetting() {
    SettingDTO newDTO = initTestValues.createSettingDTO();
    newDTO.setGroup("New Group");
    newDTO.setKey("New Key Setting");
    newDTO.setOwner("New Owner");
    newDTO.setId("33ac0d99-4309-4545-98ac-0f081bcfa3db");

    Setting newSetting = initTestValues.createSetting();
    newSetting.setGroup("New Group");
    newSetting.setKey("New Key Setting");
    newSetting.setOwner("New Owner");
    newSetting.setId("33ac0d99-4309-4545-98ac-0f081bcfa3db");

    when(settingMapper.mapToEntity(newDTO)).thenReturn(newSetting);
    settingsService.createSetting(newDTO);
    verify(settingRepository, times(1)).save(newSetting);
  }

  @Test
  public void testSetValException() {
    assertThrows(QDoesNotExistException.class, () -> {
      settingsService.setVal(setting.getOwner(), setting.getKey(), "New Val",
              setting.getGroup());
    });
  }

  @Test
  public void testSetVal() {
    String newVal = "New Val";
    Optional<Setting> optionalSetting = Optional.of(setting);
    when(settingRepository.findOne(ownerKeyGroupPredicate))
      .thenReturn(optionalSetting);
    settingsService
      .setVal(setting.getOwner(), setting.getKey(), newVal, setting.getGroup());
    assertEquals(newVal, setting.getVal());
  }

  @Test
  public void getSettingsTest() {
    List<String> keys = new ArrayList<>();
    keys.add(setting.getKey());

    List<SettingDTO> expectedSettings = new ArrayList<>();
    expectedSettings.add(settingDTO);

    Optional<Setting> optionalSetting = Optional.of(setting);
    when(settingRepository.findOne(ownerKeyGroupPredicate))
      .thenReturn(optionalSetting);
    when(settingMapper.map(optionalSetting.get())).thenReturn(settingDTO);
    assertEquals(expectedSettings, settingsService
      .getSettings(setting.getOwner(), keys, setting.getGroup()));
  }

  @Test
  public void setValsTest() {
    String newVal = "New Val";
    List<String> keys = new ArrayList<>();
    keys.add(setting.getKey());
    List<String> vals = new ArrayList<>();
    vals.add(newVal);

    Optional<Setting> optionalSetting = Optional.of(setting);
    when(settingRepository.findOne(ownerKeyGroupPredicate))
      .thenReturn(optionalSetting);
    settingsService.setVals(setting.getOwner(), keys, vals, setting.getGroup());
    assertEquals(newVal, setting.getVal());
  }

  @Test
  public void setValsExceptionTest() {
    assertThrows(QMismatchException.class, () -> {
      List<String> keys = new ArrayList<>();
      keys.add("key");
      List<String> vals = new ArrayList<>();

      settingsService.setVals(null, keys, vals, null);
    });
  }
}
