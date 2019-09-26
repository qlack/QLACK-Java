package com.eurodyn.qlack.fuse.search.dto.queries;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class QueryTermTest {

  private QueryTerm queryTerm;

  @Before
  public void init() {
    queryTerm = new QueryTerm();
  }

  @Test
  public void termTest() {
    String field = "field";
    queryTerm.setTerm(field, this);
    assertEquals(field, queryTerm.getField());
    assertEquals(this, queryTerm.getValue());
  }

}
