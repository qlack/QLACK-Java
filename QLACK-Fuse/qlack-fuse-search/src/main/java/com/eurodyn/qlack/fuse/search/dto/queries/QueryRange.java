package com.eurodyn.qlack.fuse.search.dto.queries;

import lombok.Getter;

/**
 * The term query finds documents that contain the exact terms specified in the inverted index.
 * Example:
 *
 * <pre>
 * new QueryTerms()
 * 		.setTerms("fooField", "bar", "bar1")
 * 		.setIndex("foo")
 * 		.setType("FooBarDTO")
 * 		.setPageSize(10)
 * 		.setStartRecord(0)
 * 		.setExplain(false);
 * </pre>
 *
 * See also:<br> https://www.elastic.co/guide/en/elasticsearch/reference/1.7/query-dsl-term-query.html
 */
@Getter
public class QueryRange extends QuerySpec {

  private String field;
  private Object fromValue;
  private Object toValue;

  public QueryRange setTerm(String field, Object fromValue, Object toValue) {
    this.field = field;
    this.fromValue = fromValue;
    this.toValue = toValue;
    return this;
  }

}
