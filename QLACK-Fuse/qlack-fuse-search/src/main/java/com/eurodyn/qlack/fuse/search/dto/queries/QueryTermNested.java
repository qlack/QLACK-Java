package com.eurodyn.qlack.fuse.search.dto.queries;

import lombok.Getter;

/**
 * 22/01/2018 : The term query finds documents that contain the exact term
 * specified in the inverted index. In addition to the simple Query Term this
 * query searches directly in the nested objects. This will additional return a
 * inner_hits Object that contains the Id's for the matched nested
 * terms.Example:
 *
 * <pre>
 * new QueryTerm()
 * 		.setTerm("searchField", "searchTerma","NestedObjectName", "idOfMatchedNestedObject")
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
public class QueryTermNested extends QuerySpec {

  private String field;
  private Object value;
  private String path;

  /**
   * The Object name of the inner search results
   */
  private String docvalueFields;

  public QueryTermNested setTerm(String field, Object value, String path,
    String docvalueFields) {
    this.field = field;
    this.value = value;
    this.path = path;
    this.docvalueFields = docvalueFields;

    return this;
  }

}
