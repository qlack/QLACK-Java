package com.eurodyn.qlack.fuse.rules.mapper;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import com.eurodyn.qlack.fuse.rules.InitTestValues;
import com.eurodyn.qlack.fuse.rules.dto.KnowledgeBaseDTO;
import com.eurodyn.qlack.fuse.rules.model.KnowledgeBase;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class KnowledgeBaseMapperTest {

  @InjectMocks
  private KnowledgeBaseMapperImpl knowledgeBaseMapper;

  @Spy
  private KnowledgeBaseLibraryMapper knowledgeBaseLibraryMapper;

  @Spy
  private KnowledgeBaseRuleMapper knowledgeBaseRuleMapper;

  private InitTestValues initTestValues;

  @Before
  public void init() {
    initTestValues = new InitTestValues();
  }

  @Test
  public void mapToDTOTest() {
    KnowledgeBase knowledgeBase = initTestValues.createFullKnowledgeBase();
    KnowledgeBaseDTO knowledgeBaseDTO = knowledgeBaseMapper.mapToDTO(knowledgeBase);

    assertEquals(knowledgeBase.getId(), knowledgeBaseDTO.getId());
    assertArrayEquals(knowledgeBase.getState(), knowledgeBaseDTO.getState());
    assertEquals(knowledgeBase.getRules().size(), knowledgeBaseDTO.getRules().size());
    assertEquals(knowledgeBase.getLibraries().size(), knowledgeBaseDTO.getLibraries().size());
  }

  @Test
  public void mapToDTONullStateTest() {
    KnowledgeBase knowledgeBase = initTestValues.createFullKnowledgeBase();
    knowledgeBase.setState(null);

    assertEquals(null, knowledgeBaseMapper.mapToDTO(knowledgeBase).getState());
  }

  @Test
  public void mapToDTOListTest() {
    List<KnowledgeBase> knowledgeBases = new ArrayList<>();
    knowledgeBases.add(initTestValues.createFullKnowledgeBase());
    List<KnowledgeBaseDTO> knowledgeBaseDTOS = knowledgeBaseMapper.mapToDTO(knowledgeBases);

    assertEquals(knowledgeBases.size(), knowledgeBaseDTOS.size());
  }

  @Test
  public void mapToDTONullTest() {
    assertEquals(null, knowledgeBaseMapper.mapToDTO((KnowledgeBase) null));

    List<KnowledgeBaseDTO> knowledgeBaseDTOS = knowledgeBaseMapper.mapToDTO(
        (List<KnowledgeBase>) null);
    assertEquals(null, knowledgeBaseDTOS);
  }

  @Test
  public void mapToEntityTest() {
    KnowledgeBaseDTO knowledgeBaseDTO = initTestValues.createKnowledgeBaseDTO();
    KnowledgeBase knowledgeBase = knowledgeBaseMapper.mapToEntity(knowledgeBaseDTO);

    assertEquals(knowledgeBaseDTO.getId(), knowledgeBase.getId());
    assertArrayEquals(knowledgeBaseDTO.getState(), knowledgeBase.getState());
    assertEquals(knowledgeBaseDTO.getRules().size(), knowledgeBase.getRules().size());
    assertEquals(knowledgeBaseDTO.getLibraries().size(), knowledgeBase.getLibraries().size());
  }

  @Test
  public void mapToEntityNullStateTest() {
    KnowledgeBaseDTO knowledgeBaseDTO = initTestValues.createKnowledgeBaseDTO();
    knowledgeBaseDTO.setState(null);

    assertEquals(null, knowledgeBaseMapper.mapToEntity(knowledgeBaseDTO).getState());
  }

  @Test
  public void mapToEntityListTest() {
    List<KnowledgeBaseDTO> knowledgeBaseDTOS = new ArrayList<>();
    knowledgeBaseDTOS.add(initTestValues.createKnowledgeBaseDTO());
    List<KnowledgeBase> knowledgeBases = knowledgeBaseMapper.mapToEntity(knowledgeBaseDTOS);

    assertEquals(knowledgeBaseDTOS.size(), knowledgeBases.size());
  }

  @Test
  public void mapToEntityNullTest() {
    assertEquals(null, knowledgeBaseMapper.mapToEntity((KnowledgeBaseDTO) null));

    List<KnowledgeBase> knowledgeBases = knowledgeBaseMapper.mapToEntity(
        (List<KnowledgeBaseDTO>) null);
    assertEquals(null, knowledgeBases);
  }

}
