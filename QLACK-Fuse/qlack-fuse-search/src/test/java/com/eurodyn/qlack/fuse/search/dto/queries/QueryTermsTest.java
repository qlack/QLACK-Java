package com.eurodyn.qlack.fuse.search.dto.queries;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class QueryTermsTest {

  private QueryTerms queryTerms;

  @Before
  public void init() {
    queryTerms = new QueryTerms();
  }

  @Test
  public void termsTest() {
    String field = "field";
    queryTerms.setTerm(field, this);
    assertEquals(field, queryTerms.getField());
    assertEquals(this, queryTerms.getValues());
  }
}
