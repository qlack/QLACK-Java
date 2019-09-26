package com.eurodyn.qlack.fuse.rules.mapper;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import com.eurodyn.qlack.fuse.rules.InitTestValues;
import com.eurodyn.qlack.fuse.rules.dto.KnowledgeBaseLibraryDTO;
import com.eurodyn.qlack.fuse.rules.model.KnowledgeBaseLibrary;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class KnowledgeBaseLibraryMapperTest {

  @InjectMocks
  private KnowledgeBaseLibraryMapperImpl knowledgeBaseLibraryMapper;

  private InitTestValues initTestValues;

  @Before
  public void init() {
    initTestValues = new InitTestValues();
  }

  @Test
  public void mapToDTOTest() {
    KnowledgeBaseLibrary knowledgeBaseLibrary = initTestValues.createKnowledgeBaseLibrary();
    KnowledgeBaseLibraryDTO knowledgeBaseLibraryDTO = knowledgeBaseLibraryMapper
        .mapToDTO(knowledgeBaseLibrary);

    assertArrayEquals(knowledgeBaseLibrary.getLibrary(), knowledgeBaseLibraryDTO.getLibrary());
  }

  @Test
  public void mapToDTONullLibraryTest() {
    KnowledgeBaseLibrary knowledgeBaseLibrary = initTestValues.createKnowledgeBaseLibrary();
    knowledgeBaseLibrary.setLibrary(null);

    assertArrayEquals(null, knowledgeBaseLibraryMapper
        .mapToDTO(knowledgeBaseLibrary).getLibrary());
  }

  @Test
  public void mapToDTOListTest() {
    List<KnowledgeBaseLibrary> knowledgeBaseLibraries = new ArrayList<>();
    knowledgeBaseLibraries.add(initTestValues.createFullKnowledgeBaseLibrary());
    List<KnowledgeBaseLibraryDTO> knowledgeBaseLibraryDTOS = knowledgeBaseLibraryMapper
        .mapToDTO(knowledgeBaseLibraries);

    assertEquals(knowledgeBaseLibraries.size(), knowledgeBaseLibraryDTOS.size());
  }

  @Test
  public void mapToDTONullTest() {
    assertEquals(null, knowledgeBaseLibraryMapper.mapToDTO((KnowledgeBaseLibrary) null));

    List<KnowledgeBaseLibraryDTO> knowledgeBaseLibraryDTOS = knowledgeBaseLibraryMapper.mapToDTO(
        (List<KnowledgeBaseLibrary>) null);
    assertEquals(null, knowledgeBaseLibraryDTOS);
  }

  @Test
  public void mapToEntityTest() {
    KnowledgeBaseLibraryDTO knowledgeBaseLibraryDTO = initTestValues
        .createKnowledgeBaseLibraryDTO();
    KnowledgeBaseLibrary knowledgeBaseLibrary = knowledgeBaseLibraryMapper
        .mapToEntity(knowledgeBaseLibraryDTO);

    assertArrayEquals(knowledgeBaseLibraryDTO.getLibrary(), knowledgeBaseLibrary.getLibrary());
  }

  @Test
  public void mapToEntityNullLibraryTest() {
    KnowledgeBaseLibraryDTO knowledgeBaseLibraryDTO = initTestValues
        .createKnowledgeBaseLibraryDTO();
    knowledgeBaseLibraryDTO.setLibrary(null);

    assertArrayEquals(null, knowledgeBaseLibraryMapper
        .mapToEntity(knowledgeBaseLibraryDTO).getLibrary());
  }

  @Test
  public void mapToEntityListTest() {
    List<KnowledgeBaseLibraryDTO> knowledgeBaseLibraryDTOS = new ArrayList<>();
    knowledgeBaseLibraryDTOS.add(initTestValues.createKnowledgeBaseLibraryDTO());
    List<KnowledgeBaseLibrary> knowledgeBaseLibraries = knowledgeBaseLibraryMapper
        .mapToEntity(knowledgeBaseLibraryDTOS);

    assertEquals(knowledgeBaseLibraryDTOS.size(), knowledgeBaseLibraries.size());
  }

  @Test
  public void mapToEntityNullTest() {
    assertEquals(null, knowledgeBaseLibraryMapper.mapToEntity((KnowledgeBaseLibraryDTO) null));

    List<KnowledgeBaseLibrary> knowledgeBaseLibraries = knowledgeBaseLibraryMapper.mapToEntity(
        (List<KnowledgeBaseLibraryDTO>) null);
    assertEquals(null, knowledgeBaseLibraries);
  }

}
