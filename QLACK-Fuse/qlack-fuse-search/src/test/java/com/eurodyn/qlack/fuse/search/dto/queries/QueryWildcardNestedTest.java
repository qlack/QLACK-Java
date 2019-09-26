package com.eurodyn.qlack.fuse.search.dto.queries;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class QueryWildcardNestedTest {

  private QueryWildcardNested queryWildcardNested;

  @Before
  public void init() {
    queryWildcardNested = new QueryWildcardNested();
  }

  @Test
  public void wildcardTest() {
    String field = "field";
    String wildcard = "wildcard";
    String path = "path";
    String docvalueFields = "docvalueFields";
    queryWildcardNested.setTerm(field, wildcard, path, docvalueFields);
    assertEquals(field, queryWildcardNested.getField());
    assertEquals(wildcard, queryWildcardNested.getWildcard());
    assertEquals(path, queryWildcardNested.getPath());
    assertEquals(docvalueFields, queryWildcardNested.getDocvalueFields());
  }
}
