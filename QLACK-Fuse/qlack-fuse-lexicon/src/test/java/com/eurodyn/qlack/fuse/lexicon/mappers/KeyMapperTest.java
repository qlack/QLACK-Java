package com.eurodyn.qlack.fuse.lexicon.mappers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.eurodyn.qlack.fuse.lexicon.InitTestValues;
import com.eurodyn.qlack.fuse.lexicon.dto.KeyDTO;
import com.eurodyn.qlack.fuse.lexicon.model.Key;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * @author European Dynamics
 */
@RunWith(MockitoJUnitRunner.class)
public class KeyMapperTest {

  @InjectMocks
  private KeyMapperImpl keyMapperImpl;

  private InitTestValues initTestValues;
  private Key key;
  private KeyDTO keyDTO;
  private List<Key> keys;
  private List<KeyDTO> keysDTO;

  @Before
  public void init() {
    initTestValues = new InitTestValues();
    key = initTestValues.createKey();
    keyDTO = initTestValues.createKeyDTO();
    keys = initTestValues.createKeys();
    keysDTO = initTestValues.createKeysDTO();
  }

  @Test
  public void testMapToDTOId() {
    keyDTO = keyMapperImpl.mapToDTO(key);
    assertEquals(key.getId(), keyDTO.getId());
  }

  @Test
  public void testMapToDTOName() {
    keyDTO = keyMapperImpl.mapToDTO(key);
    assertEquals(key.getName(), key.getName());
  }

  @Test
  public void testMapToDTOList() {
    keysDTO = keyMapperImpl.mapToDTO(keys);
    assertEquals(keys.size(), keysDTO.size());
  }

  @Test
  public void testMapToDtoWithTranslations() {
    keyDTO = keyMapperImpl.mapToDTO(key, true);
    assertEquals(key.getData().size(), keyDTO.getTranslations().size());
  }

  @Test
  public void testMapToDtoWithoutTranslations() {
    keyDTO = keyMapperImpl.mapToDTO(key, false);
    assertTrue(keyDTO.getTranslations().isEmpty());
  }


  @Test
  public void testMapToEntityId() {
    key = keyMapperImpl.mapToEntity(keyDTO);
    assertEquals(keyDTO.getId(), key.getId());
  }

  @Test
  public void testMapToEntityName() {
    key = keyMapperImpl.mapToEntity(keyDTO);
    assertEquals(keyDTO.getName(), key.getName());
  }

  @Test
  public void testMapToEntityList() {
    keys = keyMapperImpl.mapToEntity(keysDTO);
    assertEquals(keysDTO.size(), keys.size());
  }
}
