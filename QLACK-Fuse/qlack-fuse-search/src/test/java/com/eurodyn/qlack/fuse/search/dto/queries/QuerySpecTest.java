package com.eurodyn.qlack.fuse.search.dto.queries;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class QuerySpecTest {

  private QuerySpec querySpec;

  @Before
  public void init() {
    querySpec = new QueryStringSpecFieldNested();
  }

  @Test
  public void indicesTest() {
    String index = "index";
    List<String> indices = Arrays.asList(index);
    querySpec.setIndex(index);
    assertEquals(indices, querySpec.getIndices());
  }

  @Test
  public void typesTest() {
    String type = "types";
    List<String> types = Arrays.asList(type);
    querySpec.setType(type);
    assertEquals(types, querySpec.getTypes());
  }

  @Test
  public void includeAllSourceTest() {
    querySpec.includeAllSources();
    assertEquals(true, querySpec.isIncludeAllSource());
  }

  @Test
  public void includeResultsTest() {
    querySpec.excludeResults();
    assertEquals(false, querySpec.isIncludeResults());
  }

  @Test
  public void startRecordTest() {
    int startRecord = 12;
    querySpec.setStartRecord(startRecord);
    assertEquals(startRecord, querySpec.getStartRecord());
  }

  @Test
  public void pageSizeTest() {
    int pageSize = 12;
    querySpec.setPageSize(pageSize);
    assertEquals(pageSize, querySpec.getPageSize());
  }

  @Test
  public void explainTest() {
    querySpec.setExplain(true);
    assertEquals(true, querySpec.isExplain());
  }

  @Test
  public void countOnlyTest() {
    querySpec.setCountOnly(true);
    assertEquals(true, querySpec.isCountOnly());
  }

  @Test
  public void scrollTest() {
    Integer scroll = 20;
    querySpec.setScroll(scroll);
    assertEquals(scroll, querySpec.getScroll());
  }

  @Test
  public void aggregateTest() {
    String aggregate = "aggregate";
    querySpec.setAggregate(aggregate);
    assertEquals(aggregate, querySpec.getAggregate());
  }

  @Test
  public void aggregateSizeTest() {
    int aggregateSize = 200;
    querySpec.setAggregateSize(aggregateSize);
    assertEquals(aggregateSize, querySpec.getAggregateSize());
  }

  @Test
  public void querySortTest() {
    QuerySort querySort = new QuerySort();
    querySpec.setQuerySort(querySort);
    assertEquals(querySort, querySpec.getQuerySort());
  }

}
