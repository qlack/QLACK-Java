package com.eurodyn.qlack.fuse.search.dto.queries;

import lombok.Getter;
import lombok.Setter;

/**
 * 23/01/2018 : A query performing a query string against ES. A query that uses a query parser in
 * order to parse its content Multiple fields can be specified. In addition to the simple Query
 * String we search directly in the nested objects. This will additional return a inner_hits Object
 * that contains the Id's for the matched nested terms/objects.
 *
 * <pre>
 * new QueryStringSpecField()
 * 		.setTerm("searchField", "searchTerm", "AND", "NestedObjectName", "idOfMatchedNestedObject")
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
public class QueryStringSpecFieldNested extends QuerySpec {

  // The field to execute the search against.
  private String field;
  // The value to lookup.
  private Object value;
  // The field to execute the search against.
  private String operator;
  // The nested object
  private String path;
  // The Object name of the inner search results
  private String docvalueFields;

  /**
   * A convenience method to set the term of this query.
   *
   * @param field The field name to search against.
   * @param value The value to search.
   */
  public QueryStringSpecFieldNested setTerm(String field, Object value, String operator,
      String path, String docvalueFields) {
    this.field = field;
    this.value = value;
    this.operator = operator;
    this.path = path;
    this.docvalueFields = docvalueFields;

    return this;
  }

}
