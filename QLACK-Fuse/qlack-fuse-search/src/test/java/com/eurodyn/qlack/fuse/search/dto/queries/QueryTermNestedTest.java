package com.eurodyn.qlack.fuse.search.dto.queries;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class QueryTermNestedTest {

  private QueryTermNested queryTermNested;

  @Before
  public void init() {
    queryTermNested = new QueryTermNested();
  }

  @Test
  public void termTest() {
    String field = "field";
    String path = "path";
    String docvalueFields = "docvalueFields";
    queryTermNested.setTerm(field, this, path, docvalueFields);
    assertEquals(field, queryTermNested.getField());
    assertEquals(this, queryTermNested.getValue());
    assertEquals(path, queryTermNested.getPath());
    assertEquals(docvalueFields, queryTermNested.getDocvalueFields());
  }
}
