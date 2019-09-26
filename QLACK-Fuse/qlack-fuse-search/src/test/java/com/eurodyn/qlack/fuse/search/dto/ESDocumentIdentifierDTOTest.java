package com.eurodyn.qlack.fuse.search.dto;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ESDocumentIdentifierDTOTest {

  private ESDocumentIdentifierDTO esDocumentIdentifierDTO;

  @Before
  public void init() {
    esDocumentIdentifierDTO = new ESDocumentIdentifierDTO();
  }

  @Test
  public void indexTest() {
    String index = "index";
    esDocumentIdentifierDTO.setIndex(index);
    assertEquals(index, esDocumentIdentifierDTO.getIndex());
  }

  @Test
  public void typeTest() {
    String type = "type";
    esDocumentIdentifierDTO.setType(type);
    assertEquals(type, esDocumentIdentifierDTO.getType());
  }

  @Test
  public void idTest() {
    String id = "id";
    esDocumentIdentifierDTO.setId(id);
    assertEquals(id, esDocumentIdentifierDTO.getId());
  }

  @Test
  public void refreshTest() {
    esDocumentIdentifierDTO.setRefresh(true);
    assertEquals(true, esDocumentIdentifierDTO.isRefresh());
  }

  @Test
  public void contructorWith3ArgumentsTest() {
    String index = "index";
    String type = "type";
    String id = "id";
    ESDocumentIdentifierDTO esDocumentIdentifierDTO = new ESDocumentIdentifierDTO(index, type, id);

    assertEquals(index, esDocumentIdentifierDTO.getIndex());
    assertEquals(type, esDocumentIdentifierDTO.getType());
    assertEquals(id, esDocumentIdentifierDTO.getId());
  }

  @Test
  public void contructorWithAllArgumentsTest() {
    String index = "index";
    String type = "type";
    String id = "id";
    ESDocumentIdentifierDTO esDocumentIdentifierDTO = new ESDocumentIdentifierDTO(index, type, id,
        true);

    assertEquals(index, esDocumentIdentifierDTO.getIndex());
    assertEquals(type, esDocumentIdentifierDTO.getType());
    assertEquals(id, esDocumentIdentifierDTO.getId());
    assertEquals(true, esDocumentIdentifierDTO.isRefresh());
  }

}
