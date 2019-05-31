package com.eurodyn.qlack.fuse.settings.mappers;

import static org.junit.Assert.assertEquals;

import com.eurodyn.qlack.fuse.settings.InitTestValues;
import com.eurodyn.qlack.fuse.settings.dto.GroupDTO;
import com.eurodyn.qlack.fuse.settings.dto.SettingDTO;
import com.eurodyn.qlack.fuse.settings.model.Setting;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SettingMapperTest {

  @InjectMocks
  private SettingMapperImpl settingMapperImpl;

  private InitTestValues initTestValues;
  private Setting setting;
  private SettingDTO settingDTO;
  private List<Setting> settings;
  private List<SettingDTO> settingsDTO;
  private GroupDTO groupDTO;
  private List<GroupDTO> groupsDTO;

  @Before
  public void init() {
    initTestValues = new InitTestValues();

    setting = initTestValues.createSetting();
    settingDTO = initTestValues.createSettingDTO();

    setting.setVal("test value");
    settingDTO.setVal("test value");

    settings = initTestValues.createSettings();
    settingsDTO = initTestValues.createSettingsDTO();

    groupDTO = initTestValues.createGroupDTO();
    groupsDTO = initTestValues.createGroupsDTO();
  }

  @Test
  public void testMapToDTOList() {
    settingsDTO = settingMapperImpl.map(settings);
    assertEquals(settings.size(), settingsDTO.size());
  }

  @Test
  public void testMapToDTOId() {
    settingDTO = settingMapperImpl.map(setting);
    assertEquals(setting.getId(), settingDTO.getId());
  }

  @Test
  public void testMapToDTOOwner() {
    settingDTO = settingMapperImpl.map(setting);
    assertEquals(setting.getOwner(), settingDTO.getOwner());
  }

  @Test
  public void testMapToDTOGroup() {
    settingDTO = settingMapperImpl.map(setting);
    assertEquals(setting.getGroup(), settingDTO.getGroup());
  }

  @Test
  public void testMapToDTOKey() {
    settingDTO = settingMapperImpl.map(setting);
    assertEquals(setting.getKey(), settingDTO.getKey());
  }

  @Test
  public void testMapToDTOSensitive() {
    settingDTO = settingMapperImpl.map(setting);
    assertEquals(setting.isSensitive(), settingDTO.isSensitive());
  }

  @Test
  public void testMapToDTOPassword() {
    settingDTO = settingMapperImpl.map(setting);
    assertEquals(setting.getPassword(), settingDTO.isPassword());
  }

  @Test
  public void testMapToDTOPasswordCreatedOn() {
    settingDTO = settingMapperImpl.map(setting);
    assertEquals(setting.getCreatedOn(), settingDTO.getCreatedOn());
  }

  @Test
  public void testMapToDTOVal() {
    settingDTO = settingMapperImpl.map(setting);
    assertEquals(setting.getVal(), settingDTO.getVal());
  }

  @Test
  public void testMapToGroupDTOList() {
    List<GroupDTO> mappedList = settingMapperImpl.mapToGroupDTO(settings);
    assertEquals(groupsDTO.size(), mappedList.size());
  }

  @Test
  public void testMapToGroupDTO() {
    GroupDTO g = settingMapperImpl.mapToGroupDTO(setting);
    assertEquals(groupDTO.getName(), g.getName());
  }

  @Test
  public void testMapToEntityOwner() {
    setting = settingMapperImpl.mapToEntity(settingDTO);
    assertEquals(settingDTO.getOwner(), setting.getOwner());
  }

  @Test
  public void testMapToEntityGroup() {
    Setting setting = settingMapperImpl.mapToEntity(settingDTO);
    assertEquals(settingDTO.getGroup(), setting.getGroup());
  }

  @Test
  public void testMapToEntityKey() {
    Setting setting = settingMapperImpl.mapToEntity(settingDTO);
    assertEquals(settingDTO.getKey(), setting.getKey());
  }

  @Test
  public void testMapToEntitySensitive() {
    setting = settingMapperImpl.mapToEntity(settingDTO);
    assertEquals(settingDTO.isSensitive(), setting.isSensitive());
  }

  @Test
  public void testMapToEntityPassword() {
    Setting setting = settingMapperImpl.mapToEntity(settingDTO);
    assertEquals(settingDTO.isPassword(), setting.getPassword());
  }

  @Test
  public void testMapToEntityCreatedOn() {
    setting = settingMapperImpl.mapToEntity(settingDTO);
    assertEquals(settingDTO.getCreatedOn(), setting.getCreatedOn());
  }

  @Test
  public void testMapToEntityVal() {
    setting = settingMapperImpl.mapToEntity(settingDTO);
    assertEquals(settingDTO.getVal(), setting.getVal());
  }
}
