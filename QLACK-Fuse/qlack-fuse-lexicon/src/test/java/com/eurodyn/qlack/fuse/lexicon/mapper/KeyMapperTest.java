package com.eurodyn.qlack.fuse.lexicon.mapper;

import static org.junit.jupiter.api.Assertions.*;

import com.eurodyn.qlack.fuse.lexicon.InitTestValues;
import com.eurodyn.qlack.fuse.lexicon.dto.KeyDTO;
import com.eurodyn.qlack.fuse.lexicon.model.Group;
import com.eurodyn.qlack.fuse.lexicon.model.Key;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class KeyMapperTest {

  @InjectMocks
  private KeyMapperImpl keyMapper;

  private InitTestValues initTestValues;

  @BeforeEach
  public void init() {
    initTestValues = new InitTestValues();
  }

  @Test
  public void mapToDTOTest() {
    Key key = initTestValues.createKey();
    KeyDTO keyDTO = keyMapper.mapToDTO(key);

    assertEquals(key.getId(), keyDTO.getId());
    assertEquals(key.getName(), keyDTO.getName());
  }

  @Test
  public void mapToDTOListTest() {
    List<Key> keys = new ArrayList<>();
    keys.add(initTestValues.createKey());
    List<KeyDTO> keyDTOS = keyMapper.mapToDTO(keys);

    assertEquals(keys.size(), keyDTOS.size());
  }

  @Test
  public void mapToDTONullTest() {
    assertNull(keyMapper.mapToDTO((Key) null));

    List<KeyDTO> keyDTOS = keyMapper.mapToDTO(
      (List<Key>) null);
    assertNull(keyDTOS);
  }

  @Test
  public void mapToEntityTest() {
    KeyDTO keyDTO = initTestValues.createKeyDTO();
    Key key = keyMapper.mapToEntity(keyDTO);

    assertEquals(keyDTO.getId(), key.getId());
    assertEquals(keyDTO.getName(), key.getName());
  }

  @Test
  public void mapToEntityListTest() {
    List<KeyDTO> keyDTOS = new ArrayList<>();
    keyDTOS.add(initTestValues.createKeyDTO());
    List<Key> keys = keyMapper.mapToEntity(keyDTOS);

    assertEquals(keyDTOS.size(), keys.size());
  }

  @Test
  public void mapToEntityNullTest() {
    assertNull(keyMapper.mapToEntity((KeyDTO) null));

    List<Key> keys = keyMapper.mapToEntity(
      (List<KeyDTO>) null);
    assertNull(keys);
  }

  @Test
  public void mapToDTOIncludeTranslationsTest() {
    Key key = initTestValues.createKey();
    KeyDTO keyDTO = keyMapper.mapToDTO(key, true);

    assertEquals(key.getId(), keyDTO.getId());
    assertEquals(key.getName(), keyDTO.getName());
  }

  @Test
  public void mapToDTOIncludeTranslationsNullTest() {
    assertNull(keyMapper.mapToDTO(null, true));
  }

  @Test
  public void mapToDTOIncludeTranslationsNullGroupTest() {

    Key key = initTestValues.createKey();
    key.setGroup(null);
    KeyDTO keyDTO = keyMapper.mapToDTO(key, true);

    assertEquals(key.getId(), keyDTO.getId());
    assertEquals(key.getName(), keyDTO.getName());
  }

  @Test
  public void mapToDTOIncludeTranslationsNullGroupIdTest() {
    Key key = initTestValues.createKey();
    Group keyGroup = key.getGroup();
    keyGroup.setId(null);
    key.setGroup(keyGroup);
    KeyDTO keyDTO = keyMapper.mapToDTO(key, true);

    assertEquals(key.getId(), keyDTO.getId());
    assertEquals(key.getName(), keyDTO.getName());
  }

  @Test
  public void mapToDataTranslationsFalseTest() {
    Map<String, String> expectedMap = new HashMap<>();
    Map<String, String> map = keyMapper
      .mapData(initTestValues.createKey().getData(), false);
    assertEquals(expectedMap, map);
  }

  @Test
  public void mapToDataNullDataTest() {
    Map<String, String> expectedMap = new HashMap<>();
    Map<String, String> map = keyMapper.mapData(null, true);
    assertEquals(expectedMap, map);
  }

}
