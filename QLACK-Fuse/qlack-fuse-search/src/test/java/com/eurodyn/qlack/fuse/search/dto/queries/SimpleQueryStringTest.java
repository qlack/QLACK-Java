package com.eurodyn.qlack.fuse.search.dto.queries;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SimpleQueryStringTest {

  private SimpleQueryString simpleQueryString;

  @Before
  public void init() {
    simpleQueryString = new SimpleQueryString();
  }

  @Test
  public void queryTest() {
    String field = "field";
    String operator = "operator";
    simpleQueryString.setTerm(field, this, operator);
    assertEquals(field, simpleQueryString.getField());
    assertEquals(this, simpleQueryString.getValue());
    assertEquals(operator, simpleQueryString.getOperator());
  }

  @Test
  public void fieldTest() {
    String field = "field";
    simpleQueryString.setField(field);
    assertEquals(field, simpleQueryString.getField());
  }

  @Test
  public void valueTest() {
    simpleQueryString.setValue(this);
    assertEquals(this, simpleQueryString.getValue());
  }

  @Test
  public void operatorTest() {
    String operator = "operator";
    simpleQueryString.setOperator(operator);
    assertEquals(operator, simpleQueryString.getOperator());
  }

}
