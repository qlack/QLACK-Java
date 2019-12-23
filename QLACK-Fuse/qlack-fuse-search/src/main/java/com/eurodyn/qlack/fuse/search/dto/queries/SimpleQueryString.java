package com.eurodyn.qlack.fuse.search.dto.queries;

import lombok.Getter;
import lombok.Setter;

/**
 * A query performing a query string against ES. A query that uses a query
 * parser in order to parse its content Multiple fields can be specified. Simple
 * Query String is like the Query String query but will never throw an
 * exception, and discards invalid parts of the query
 *
 * <pre>
 * new SimpleQueryString()
 * 		.setTerm("fooField", "foo", "AND")
 * 		.setIndex("bar")
 * 		.setType("FooBarDTO")
 * 		.setPageSize(10)
 * 		.setStartRecord(0)
 * 		.setExplain(false);
 * </pre>
 *
 * See also:<br> https://www.elastic.co/guide/en/elasticsearch/reference/1.7/query-dsl-match-query.html
 */
@Getter
@Setter
public class SimpleQueryString extends QuerySpec {

  /**
   * The field to execute the search against.
   */
  private String field;

  /**
   * The value to lookup.
   */
  private Object value;

  /**
   * The field to execute the search against.
   */
  private String operator;

  /**
   * A convenience method to set the term of this query.
   *
   * @param field The field name to search against.
   * @param value The value to search.
   * @param operator The field to execute the search against.
   * @return a {@link SimpleQueryString} object
   */
  public SimpleQueryString setTerm(String field, Object value,
    String operator) {
    this.field = field;
    this.value = value;
    this.operator = operator;

    return this;
  }

}
