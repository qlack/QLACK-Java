package com.eurodyn.qlack.fuse.search.dto.queries;

import lombok.Getter;

/**
 * Similar to {@link QueryMatch}, a query performing a match against ES but on multiple fields.
 * Example:
 *
 * <pre>
 * new QueryMultiMatch()
 * 		.setTerm("foo", "bar1Field", "bar2Field")
 * 		.setIndex("foo")
 * 		.setType("FooBarDTO")
 * 		.setPageSize(10)
 * 		.setStartRecord(0)
 * 		.setExplain(false);
 * </pre>
 *
 * See also:<br> https://www.elastic.co/guide/en/elasticsearch/reference/1.7/query-dsl-multi-match-query.html
 */
@Getter
public class QueryMultiMatch extends QuerySpec {

  /**
   * The list of fields to search against.
   */
  private String[] fields;

  /**
   *The value to search for.
   */
  private Object value;

  /**
   * A convenience method to set the search term of this query.
   *
   * @param value The value to search for.
   * @param fields The list of fields to search against.
   * @return a {@link QueryMultiMatch} object
   */
  public QueryMultiMatch setTerm(Object value, String... fields) {
    this.value = value;
    this.fields = fields;

    return this;
  }

}
