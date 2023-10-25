package com.eurodyn.qlack.fuse.settings.mapper;

import static org.junit.jupiter.api.Assertions.*;

import com.eurodyn.qlack.fuse.settings.InitTestValues;
import com.eurodyn.qlack.fuse.settings.dto.GroupDTO;
import com.eurodyn.qlack.fuse.settings.dto.SettingDTO;
import com.eurodyn.qlack.fuse.settings.model.Setting;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class SettingMapperImplTest {

  @InjectMocks
  private SettingMapperImpl settingMapper;

  private InitTestValues initTestValues;

  private Setting setting;

  private SettingDTO settingDTO;

  private List<Setting> settingList;

  private List<SettingDTO> settingDTOS;

  private GroupDTO groupDTO;

  private List<GroupDTO> groupDTOS;


  @BeforeEach
  public void init() {
    initTestValues = new InitTestValues();
    setting = initTestValues.createSetting();
    settingDTO = initTestValues.createSettingDTO();
    settingList = initTestValues.createSettings();
    settingDTOS = initTestValues.createSettingsDTO();
    groupDTO = initTestValues.createGroupDTO();
    groupDTOS = initTestValues.createGroupsDTO();
  }

  @Test
  public void mapListToListDTOTest() {
    settingDTOS = settingMapper.map(settingList);
    assertEquals(settingDTOS.size(), settingList.size());
  }

  @Test
  public void mapToDTOListNullTest() {
    settingDTOS = settingMapper.map((List<Setting>) null);
    assertNull(settingDTOS);
  }

  @Test
  public void mapDTOToSetting() {
    settingDTO = settingMapper.map(setting);
    assertEquals(setting.getOwner(), settingDTO.getOwner());
  }


  @Test
  public void mapDTOToSettingNullTest() {
    settingDTO = settingMapper.map((Setting) null);
    assertNull(settingDTO);
  }

  @Test
  public void mapToGroupDTOTest() {
    groupDTO = settingMapper.mapToGroupDTO(setting);
    assertEquals(groupDTO.getName(), setting.getGroup());
  }

  @Test
  public void mapToGroupDTONullTest() {
    groupDTO = settingMapper.mapToGroupDTO((Setting) null);
    assertNull(groupDTO);
  }

  @Test
  public void mapListToGroupDTO() {
    groupDTOS = settingMapper.mapToGroupDTO(settingList);
    assertEquals(groupDTOS.size(), settingList.size());
  }

  @Test
  public void mapListToGroupDTONullTest() {
    groupDTOS = settingMapper.mapToGroupDTO((List<Setting>) null);
    assertNull(groupDTOS);
  }

  @Test
  public void mapToEntityTest() {
    setting = settingMapper.mapToEntity(settingDTO);
    assertEquals(setting.getOwner(), settingDTO.getOwner());
  }

  @Test
  public void mapToEntityNullTest() {
    setting = settingMapper.mapToEntity(null);
    assertNull(setting);
  }

  @Test
  public void testSettingDTOToSettingPasswordNotNull() {
    Setting setting = initTestValues.createSetting();
    setting.setPassword(null);
    assertFalse(settingMapper.map(setting).isPassword());
  }

}
