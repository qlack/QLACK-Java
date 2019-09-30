package com.eurodyn.qlack.fuse.search.mapper.request;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class InternalSearchRequestTest {

  private InternalSearchRequest internalSearchRequest;

  @Before
  public void init() {
    internalSearchRequest = new InternalSearchRequest();
  }

  @Test
  public void testFrom() {
    Integer from = 10;
    internalSearchRequest.setFrom(from);
    assertEquals(from, internalSearchRequest.getFrom());
  }

  @Test
  public void testSize() {
    Integer size = 10;
    internalSearchRequest.setSize(size);
    assertEquals(size, internalSearchRequest.getSize());
  }

  @Test
  public void testExplain() {
    internalSearchRequest.setExplain(true);
    assertEquals(true, internalSearchRequest.getExplain());
  }

  @Test
  public void testQuery() {
    String query = "query";
    internalSearchRequest.setQuery(query);
    assertEquals(query, internalSearchRequest.getQuery());
  }

  @Test
  public void testSource() {
    List<String> source = Arrays.asList("source1", "source2");
    internalSearchRequest.setSource(source);
    assertEquals(source, internalSearchRequest.getSource());
  }

  @Test
  public void testAggs() {
    String aggs = "aggs";
    internalSearchRequest.setAggs(aggs);
    assertEquals(aggs, internalSearchRequest.getAggs());
  }

  @Test
  public void testSort() {
    String sort = "sort";
    internalSearchRequest.setSort(sort);
    assertEquals(sort, internalSearchRequest.getSort());
  }

}
