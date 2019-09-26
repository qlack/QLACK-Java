package com.eurodyn.qlack.fuse.search.dto.queries;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class QueryMultiMatchTest {

  private QueryMultiMatch queryMultiMatch;

  @Before
  public void init() {
    queryMultiMatch = new QueryMultiMatch();
  }

  @Test
  public void termTest() {
    String[] fields = {"field1"};
    queryMultiMatch.setTerm(this, fields);
    assertEquals(this, queryMultiMatch.getValue());
    assertArrayEquals(fields, queryMultiMatch.getFields());
  }

}
