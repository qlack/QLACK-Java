package com.eurodyn.qlack.fuse.search.dto.queries;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class QueryMatchTest {

  private QueryMatch queryMatch;

  @Before
  public void init() {
    queryMatch = new QueryMatch();
  }

  @Test
  public void matchTest() {
    String field = "field";
    queryMatch.setTerm(field, this);
    assertEquals(field, queryMatch.getField());
    assertEquals(this, queryMatch.getValue());
  }

  @Test
  public void fieldTest() {
    String field = "field";
    queryMatch.setField(field);
    assertEquals(field, queryMatch.getField());
  }

  @Test
  public void valueTest() {
    queryMatch.setValue(this);
    assertEquals(this, queryMatch.getValue());
  }
}
