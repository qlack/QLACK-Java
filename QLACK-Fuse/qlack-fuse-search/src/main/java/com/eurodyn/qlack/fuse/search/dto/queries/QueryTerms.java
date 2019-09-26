package com.eurodyn.qlack.fuse.search.dto.queries;

import lombok.Getter;

/**
 * The term query finds documents that contain the exact terms specified in the inverted index.
 * Example:
 *
 * <pre>
 * new QueryTerms()
 * 		.setTerms("fooField", "bar")
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
public class QueryTerms extends QuerySpec {

  private String field;
  private Object values;

  public QueryTerms setTerm(String field, Object values) {
    this.field = field;
    this.values = values;

    return this;
  }

}
