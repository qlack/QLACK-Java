package com.eurodyn.qlack.fuse.settings.mapper;

import com.eurodyn.qlack.fuse.settings.InitTestValues;
import com.eurodyn.qlack.fuse.settings.dto.GroupDTO;
import com.eurodyn.qlack.fuse.settings.dto.SettingDTO;
import com.eurodyn.qlack.fuse.settings.model.Setting;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
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


    @Before
    public void init(){
        initTestValues = new InitTestValues();
        setting = initTestValues.createSetting();
        settingDTO = initTestValues.createSettingDTO();
        settingList = initTestValues.createSettings();
        settingDTOS = initTestValues.createSettingsDTO();
        groupDTO = initTestValues.createGroupDTO();
        groupDTOS = initTestValues.createGroupsDTO();
    }

    @Test
    public void mapListToListDTOTest(){
        settingDTOS = settingMapper.map(settingList);
        assertEquals(settingDTOS.size(),settingList.size());
    }

    @Test
    public void mapToDTOListNullTest() {
        settingDTOS = settingMapper.map((List<Setting> ) null);
        assertEquals(null, settingDTOS);
    }

    @Test
    public void mapDTOToSetting(){
        settingDTO = settingMapper.map(setting);
        assertEquals(setting.getOwner(),settingDTO.getOwner());
    }


    @Test
    public void mapDTOToSettingNullTest() {
        settingDTO = settingMapper.map((Setting) null);
        assertEquals(null, settingDTO);
    }

    @Test
    public void mapToGroupDTOTest(){
        groupDTO = settingMapper.mapToGroupDTO(setting);
        assertEquals(groupDTO.getName(),setting.getGroup());
    }

    @Test
    public void mapToGroupDTONullTest(){
        groupDTO = settingMapper.mapToGroupDTO((Setting) null);
        assertEquals(null , groupDTO);
    }

    @Test
    public void mapListToGroupDTO(){
        groupDTOS = settingMapper.mapToGroupDTO(settingList);
        assertEquals(groupDTOS.size(),settingList.size());
    }

    @Test
    public void mapListToGroupDTONullTest(){
        groupDTOS = settingMapper.mapToGroupDTO((List<Setting>) null);
        assertEquals(null,groupDTOS);
    }

    @Test
    public void mapToEntityTest(){
        setting = settingMapper.mapToEntity(settingDTO);
        assertEquals(setting.getOwner(),settingDTO.getOwner());
    }

    @Test
    public void mapToEntityNullTest(){
        setting = settingMapper.mapToEntity((SettingDTO) null);
        assertEquals(null,setting);
    }

    @Test
    public void testSettingDTOToSettingPasswordNotNull() {
        Setting setting = initTestValues.createSetting();
        setting.setPassword(null);
        assertEquals(false,settingMapper.map(setting).isPassword());
    }

}
