package com.eurodyn.qlack.fuse.search.dto.queries;

import lombok.Getter;

/**
 * 22/01/2018 : The term query finds documents that contain the exact terms
 * specified in the inverted index. In addition to the simple Query Terms we
 * search directly in the nested objects. This will additional return a
 * inner_hits Object that contains the Id's for the matched nested
 * terms.Example:
 *
 * <pre>
 * new QueryTerms()
 * 		.setTerms("searchField", "searchTermString", "NestedObjectName", "idOfMatchedNestedObject")
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
public class QueryTermsNested extends QuerySpec {

  /**
   * The field to execute the search against
   */
  private String field;

  /**
   * The comma delimited Search Values
   */
  private Object values;

  /**
   * The nested object
   */
  private String path;

  /**
   * The Object name of the inner search results
   */
  private String docvalueFields;

  public QueryTermsNested setTerm(String field, Object values, String path,
    String docvalueFields) {
    this.field = field;
    this.values = values;
    this.path = path;
    this.docvalueFields = docvalueFields;

    return this;
  }

}
