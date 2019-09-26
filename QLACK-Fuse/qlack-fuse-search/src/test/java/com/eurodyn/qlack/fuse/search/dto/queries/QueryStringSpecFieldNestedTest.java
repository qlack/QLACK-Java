package com.eurodyn.qlack.fuse.search.dto.queries;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class QueryStringSpecFieldNestedTest {

  private QueryStringSpecFieldNested queryStringSpecFieldNested;

  @Before
  public void init() {
    queryStringSpecFieldNested = new QueryStringSpecFieldNested();
  }

  @Test
  public void queryTest() {
    String field = "field";
    String operator = "operator";
    String path = "path";
    String docvalueFields = "docvalueFields";
    queryStringSpecFieldNested.setTerm(field, this, operator, path, docvalueFields);
    assertEquals(field, queryStringSpecFieldNested.getField());
    assertEquals(this, queryStringSpecFieldNested.getValue());
    assertEquals(operator, queryStringSpecFieldNested.getOperator());
    assertEquals(path, queryStringSpecFieldNested.getPath());
    assertEquals(docvalueFields, queryStringSpecFieldNested.getDocvalueFields());
  }

  @Test
  public void fieldTest() {
    String field = "field";
    queryStringSpecFieldNested.setField(field);
    assertEquals(field, queryStringSpecFieldNested.getField());
  }

  @Test
  public void valueTest() {
    queryStringSpecFieldNested.setValue(this);
    assertEquals(this, queryStringSpecFieldNested.getValue());
  }

  @Test
  public void operatorTest() {
    String operator = "operator";
    queryStringSpecFieldNested.setOperator(operator);
    assertEquals(operator, queryStringSpecFieldNested.getOperator());
  }

  @Test
  public void pathTest() {
    String path = "path";
    queryStringSpecFieldNested.setPath(path);
    assertEquals(path, queryStringSpecFieldNested.getPath());
  }

  @Test
  public void docvalueFieldsTest() {
    String docvalueFields = "docvalueFields";
    queryStringSpecFieldNested.setDocvalueFields(docvalueFields);
    assertEquals(docvalueFields, queryStringSpecFieldNested.getDocvalueFields());
  }

}
