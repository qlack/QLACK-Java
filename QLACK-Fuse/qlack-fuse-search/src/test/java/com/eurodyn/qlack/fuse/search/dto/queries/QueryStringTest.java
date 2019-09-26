package com.eurodyn.qlack.fuse.search.dto.queries;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class QueryStringTest {

  private QueryString queryString;

  @Before
  public void init() {
    queryString = new QueryString();
  }

  @Test
  public void queryStringTest() {
    String queryStringValue = "queryString";
    queryString.setQueryString(queryStringValue);
    assertEquals(queryStringValue, queryString.getQueryString());
  }

}
