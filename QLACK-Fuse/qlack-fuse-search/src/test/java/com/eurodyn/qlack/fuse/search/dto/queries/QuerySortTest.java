package com.eurodyn.qlack.fuse.search.dto.queries;

import static org.junit.Assert.assertEquals;

import java.util.LinkedHashMap;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class QuerySortTest {

  public QuerySort querySort;

  @Before
  public void init() {
    querySort = new QuerySort();
  }

  @Test
  public void sortMapTest() {
    Map<String, String> sortMap = new LinkedHashMap<>();
    sortMap.put("field", "string");
    querySort.setSort("field", "string");
    assertEquals(sortMap, querySort.getSortMap());
  }

}
