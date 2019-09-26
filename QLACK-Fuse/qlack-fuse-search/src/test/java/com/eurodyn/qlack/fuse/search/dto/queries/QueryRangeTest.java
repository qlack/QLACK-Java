package com.eurodyn.qlack.fuse.search.dto.queries;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class QueryRangeTest {

  private QueryRange queryRange;

  @Before
  public void init() {
    queryRange = new QueryRange();
  }

  @Test
  public void rangeTest() {
    String field = "field";
    queryRange.setTerm(field, this, this);
    assertEquals(field, queryRange.getField());
    assertEquals(this, queryRange.getFromValue());
    assertEquals(this, queryRange.getToValue());
  }

}
