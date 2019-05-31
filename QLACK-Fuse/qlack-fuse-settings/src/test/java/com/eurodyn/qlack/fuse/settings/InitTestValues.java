package com.eurodyn.qlack.fuse.settings;

import com.eurodyn.qlack.fuse.settings.dto.GroupDTO;
import com.eurodyn.qlack.fuse.settings.dto.SettingDTO;
import com.eurodyn.qlack.fuse.settings.model.Setting;
import java.util.ArrayList;
import java.util.List;

/**
 * @author European Dynamics
 */
public class InitTestValues {

  public Setting createSetting() {
    Setting setting = new Setting();
    setting.setId("6a31f573-6588-4964-aa2a-3b4380f60ecc");
    setting.setKey("Setting Key 1");
    setting.setGroup("Test Group");
    setting.setOwner("Test Owner");
    setting.setPassword(true);
    setting.setCreatedOn(1625145120000L);
    return setting;
  }

  public SettingDTO createSettingDTO() {
    SettingDTO settingDTO = new SettingDTO();
    settingDTO.setId("6a31f573-6588-4964-aa2a-3b4380f60ecc");
    settingDTO.setKey("Setting Key 1");
    settingDTO.setGroup("Test Group");
    settingDTO.setOwner("Test Owner");
    settingDTO.setPassword(true);
    settingDTO.setCreatedOn(1625145120000L);
    return settingDTO;
  }

  public List<Setting> createSettings() {
    List<Setting> settings = new ArrayList<>();
    settings.add(createSetting());

    Setting setting = createSetting();
    setting.setId("b608248f-58da-4337-8c03-c99eceae154c");
    setting.setKey("Setting Key 2");
    setting.setSensitive(true);

    Setting setting2 = createSetting();
    setting2.setId("73c2d9e1-ed69-4f48-8244-c737a975d2be");
    setting2.setKey("Setting Key 3");
    setting2.setGroup("Test Group 2");

    settings.add(setting);
    settings.add(setting2);

    return settings;
  }

  public List<SettingDTO> createSettingsDTO() {
    List<SettingDTO> settingsDTO = new ArrayList<>();
    settingsDTO.add(createSettingDTO());

    SettingDTO settingDTO = createSettingDTO();
    settingDTO.setId("b608248f-58da-4337-8c03-c99eceae154c");
    settingDTO.setKey("Setting Key 2");
    settingDTO.setSensitive(true);

    SettingDTO settingDTO2 = createSettingDTO();
    settingDTO2.setId("73c2d9e1-ed69-4f48-8244-c737a975d2be");
    settingDTO2.setKey("Setting Key 3");

    settingsDTO.add(settingDTO);
    settingsDTO.add(settingDTO2);

    return settingsDTO;
  }

  public GroupDTO createGroupDTO() {
    GroupDTO groupDTO = new GroupDTO();
    groupDTO.setName("Test Group");
    return groupDTO;
  }

  public List<GroupDTO> createGroupsDTO() {
    List<GroupDTO> groupsDTO = new ArrayList<>();
    for (Setting setting : createSettings()) {
      GroupDTO g = new GroupDTO();
      g.setName(setting.getGroup());
      groupsDTO.add(g);
    }

    return groupsDTO;
  }
}
