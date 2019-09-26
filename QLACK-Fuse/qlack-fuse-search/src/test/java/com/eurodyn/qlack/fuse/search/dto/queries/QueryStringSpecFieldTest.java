package com.eurodyn.qlack.fuse.search.dto.queries;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class QueryStringSpecFieldTest {

  private QueryStringSpecField queryStringSpecField;

  @Before
  public void init() {
    queryStringSpecField = new QueryStringSpecField();
  }

  @Test
  public void queryTest() {
    String field = "field";
    String operator = "operator";
    queryStringSpecField.setTerm(field, this, operator);
    assertEquals(field, queryStringSpecField.getField());
    assertEquals(this, queryStringSpecField.getValue());
    assertEquals(operator, queryStringSpecField.getOperator());
  }

  @Test
  public void fieldTest() {
    String field = "field";
    queryStringSpecField.setField(field);
    assertEquals(field, queryStringSpecField.getField());
  }

  @Test
  public void valueTest() {
    queryStringSpecField.setValue(this);
    assertEquals(this, queryStringSpecField.getValue());
  }

  @Test
  public void operatorTest() {
    String operator = "operator";
    queryStringSpecField.setOperator(operator);
    assertEquals(operator, queryStringSpecField.getOperator());
  }

}
