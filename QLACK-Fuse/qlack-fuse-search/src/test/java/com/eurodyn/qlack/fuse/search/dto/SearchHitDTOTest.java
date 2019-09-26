package com.eurodyn.qlack.fuse.search.dto;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SearchHitDTOTest {

  private SearchHitDTO searchHitDTO;

  @Before
  public void init() {
    searchHitDTO = new SearchHitDTO();
  }

  @Test
  public void sourceTest() {
    String source = "source";
    searchHitDTO.setSource(source);
    assertEquals(source, searchHitDTO.getSource());
  }

  @Test
  public void innerHitsTest() {
    String innerHits = "10";
    searchHitDTO.setInnerHits(innerHits);
    assertEquals(innerHits, searchHitDTO.getInnerHits());
  }

  @Test
  public void scoreTest() {
    float score = (float) 23.0;
    searchHitDTO.setScore(score);
    assertEquals(score, searchHitDTO.getScore(), 0);
  }

  @Test
  public void typeTest() {
    String type = "type";
    searchHitDTO.setType(type);
    assertEquals(type, searchHitDTO.getType());
  }

  @Test
  public void idTest() {
    String id = "id";
    searchHitDTO.setId(id);
    assertEquals(id, searchHitDTO.getId());
  }

  @Test
  public void toStringTest() {
    assertNotNull(searchHitDTO.toString());
  }

}
