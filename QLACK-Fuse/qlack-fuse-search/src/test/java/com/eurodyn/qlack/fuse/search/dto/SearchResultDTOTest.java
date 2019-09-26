package com.eurodyn.qlack.fuse.search.dto;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SearchResultDTOTest {

  private SearchResultDTO searchResultDTO;

  @Before
  public void init() {
    searchResultDTO = new SearchResultDTO();
  }

  @Test
  public void sourceTest() {
    String source = "source";
    searchResultDTO.setSource(source);
    assertEquals(source, searchResultDTO.getSource());
  }

  @Test
  public void executionTimeTest() {
    long executionTime = Instant.now().toEpochMilli();
    searchResultDTO.setExecutionTime(executionTime);
    assertEquals(executionTime, searchResultDTO.getExecutionTime());
  }

  @Test
  public void timedOutTest() {
    searchResultDTO.setTimedOut(true);
    assertEquals(true, searchResultDTO.isTimedOut());
  }

  @Test
  public void shardsTest() {
    int shards = 10;
    searchResultDTO.setShardsTotal(shards);
    assertEquals(shards, searchResultDTO.getShardsTotal());
  }

  @Test
  public void shardsSuccessfulTest() {
    int shards = 10;
    searchResultDTO.setShardsSuccessful(shards);
    assertEquals(shards, searchResultDTO.getShardsSuccessful());
  }

  @Test
  public void shardsFailedTest() {
    int shards = 10;
    searchResultDTO.setShardsFailed(shards);
    assertEquals(shards, searchResultDTO.getShardsFailed());
  }

  @Test
  public void totalHitsTest() {
    long totalHits = 1000L;
    searchResultDTO.setTotalHits(totalHits);
    assertEquals(totalHits, searchResultDTO.getTotalHits());
  }

  @Test
  public void scoreTest() {
    float score = (float) 23.0;
    searchResultDTO.setBestScore(score);
    assertEquals(score, searchResultDTO.getBestScore(), 0);
  }

  @Test
  public void hasMoreTest() {
    searchResultDTO.setHasMore(true);
    assertEquals(true, searchResultDTO.isHasMore());
  }

  @Test
  public void scrollIdTest() {
    String source = "scrollId";
    searchResultDTO.setScrollId(source);
    assertEquals(source, searchResultDTO.getScrollId());
  }

  @Test
  public void hitsTest() {
    List<SearchHitDTO> hits = new ArrayList<>();
    searchResultDTO.setHits(hits);
    assertEquals(hits, searchResultDTO.getHits());
  }

  @Test
  public void aggregationsTest() {
    Map<String, Long> aggregations = new LinkedHashMap<>();
    searchResultDTO.setAggregations(aggregations);
    assertEquals(aggregations, searchResultDTO.getAggregations());
  }

  @Test
  public void toStringTest() {
    assertNotNull(searchResultDTO.toString());
  }

}
