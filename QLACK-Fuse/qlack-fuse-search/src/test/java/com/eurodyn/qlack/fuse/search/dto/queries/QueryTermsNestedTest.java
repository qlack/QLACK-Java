package com.eurodyn.qlack.fuse.search.dto.queries;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class QueryTermsNestedTest {

  private QueryTermsNested queryTermsNested;

  @Before
  public void init() {
    queryTermsNested = new QueryTermsNested();
  }

  @Test
  public void termsTest() {
    String field = "field";
    String path = "path";
    String docvalueFields = "docvalueFields";
    queryTermsNested.setTerm(field, this, path, docvalueFields);
    assertEquals(field, queryTermsNested.getField());
    assertEquals(this, queryTermsNested.getValues());
    assertEquals(path, queryTermsNested.getPath());
    assertEquals(docvalueFields, queryTermsNested.getDocvalueFields());
  }
}
