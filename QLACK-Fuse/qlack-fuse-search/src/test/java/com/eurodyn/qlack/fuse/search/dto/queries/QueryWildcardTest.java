package com.eurodyn.qlack.fuse.search.dto.queries;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class QueryWildcardTest {

  private QueryWildcard queryWildcard;

  @Before
  public void init() {
    queryWildcard = new QueryWildcard();
  }

  @Test
  public void wildcardTest() {
    String field = "field";
    String wildcard = "wildcard";
    queryWildcard.setTerm(field, wildcard);
    assertEquals(field, queryWildcard.getField());
    assertEquals(wildcard, queryWildcard.getWildcard());
  }

}
