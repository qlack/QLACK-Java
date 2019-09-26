package com.eurodyn.qlack.fuse.search.dto;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class IndexingDTOTest {

  private IndexingDTO indexingDTO;

  @Before
  public void init() {
    indexingDTO = new IndexingDTO();
  }

  @Test
  public void sourceObjectTest() {
    indexingDTO.setSourceObject(this);
    assertEquals(this, indexingDTO.getSourceObject());
  }

  @Test
  public void jsonTest() {
    indexingDTO.setConvertToJSON(false);
    assertEquals(false, indexingDTO.isConvertToJSON());
  }

  @Test
  public void constructorWithAllArgumentsTest() {
    String index = "index";
    String type = "type";
    String id = "id";
    IndexingDTO indexingDTO = new IndexingDTO(index, type, id, this);

    assertEquals(index, indexingDTO.getIndex());
    assertEquals(type, indexingDTO.getType());
    assertEquals(id, indexingDTO.getId());
    assertEquals(this, indexingDTO.getSourceObject());
  }
}
