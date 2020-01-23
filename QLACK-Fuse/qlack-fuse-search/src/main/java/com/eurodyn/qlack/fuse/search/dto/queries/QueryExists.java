package com.eurodyn.qlack.fuse.search.dto.queries;

/**
 * The term query exists finds documents that have a value specified in the inverted index.
 *
 * <pre>
 * new QueryExists()
 * 		.setTerm("searchField")
 * </pre>
 *
 * See also: https://www.elastic.co/guide/en/elasticsearch/reference/current/query-dsl-exists-query.html
 */

public class QueryExists extends QuerySpec {

  private String field;

  public String getField() {
    return field;
  }

  public QueryExists setField(String field) {
    this.field = field;
    return this;
  }
}
