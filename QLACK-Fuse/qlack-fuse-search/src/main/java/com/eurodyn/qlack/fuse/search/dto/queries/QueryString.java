package com.eurodyn.qlack.fuse.search.dto.queries;

import lombok.Getter;

/**
 * A query that uses a query parser in order to parse its content. Example:
 *
 * <pre>
 * new QueryString()
 * 		.setQueryStringValue("foo")
 * 		// .setQueryStringValue("foo*")
 * 		// .setQueryStringValue("foo AND bar")
 * 		// .setQueryStringValue("foo -bar")
 * 		.setIndex("bar")
 * 		.setType("FooBarDTO")
 * 		.setPageSize(10)
 * 		.setStartRecord(0)
 * 		.setExplain(false);
 *
 * </pre>
 *
 * See also:<br> https://www.elastic.co/guide/en/elasticsearch/reference/1.7/query-dsl-query-string-query.html
 */
@Getter
public class QueryString extends QuerySpec {

  private String queryStringValue;

  public QueryString setQueryStringValue(String queryStringValue) {
    this.queryStringValue = queryStringValue;

    return this;
  }

}
