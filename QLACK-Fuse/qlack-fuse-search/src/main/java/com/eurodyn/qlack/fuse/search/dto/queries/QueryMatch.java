package com.eurodyn.qlack.fuse.search.dto.queries;

import lombok.Getter;
import lombok.Setter;

/**
 * A query performing a match against ES. The default match query is of type boolean. The value provided is analyzed and the analysis
 * process constructs a boolean query from the provided text using boolean OR. Example:
 *
 * <pre>
 * new QueryMatch()
 * 		.setTerm("fooField", "foo")
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
public class QueryMatch extends QuerySpec {

  // The field to execute the search against.
  private String field;

  // The value to lookup.
  private Object value;

  /**
   * A convenience method to set the term of this query.
   *
   * @param field The field name to search against.
   * @param value The value to search.
   */
  public QueryMatch setTerm(String field, Object value) {
    this.field = field;
    this.value = value;

    return this;
  }

}
