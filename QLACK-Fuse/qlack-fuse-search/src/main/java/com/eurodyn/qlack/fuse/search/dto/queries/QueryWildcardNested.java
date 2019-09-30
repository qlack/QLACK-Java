package com.eurodyn.qlack.fuse.search.dto.queries;

import lombok.Getter;

/**
 * 23/01/2018 : atches documents that have fields matching a wildcard expression (not analyzed).
 * Supported wildcards are *, which matches any character sequence (including the empty one), and ?,
 * which matches any single character. Note this query can be slow, as it needs to iterate over many
 * terms. In order to prevent extremely slow wildcard queries, a wildcard term should not start with
 * one of the wildcards * or ?. In addition to the simple Query String we search directly in the
 * nested objects. This will additional return a inner_hits Object that contains the Id's for the
 * matched nested terms/objects.
 *
 * <pre>
 * new QueryWildcard()
 * 		.setTerm("searchField", "searchTermWildcard*", "NestedObjectName", "idOfMatchedNestedObject"))
 * 		.setIndex("foo")
 * 		.setType("FooBarDTO")
 * 		.setPageSize(10)
 * 		.setStartRecord(0)
 * 		.setExplain(false);
 * </pre>
 *
 * See:<br> https://www.elastic.co/guide/en/elasticsearch/reference/1.7/query-dsl-wildcard-query.html
 */
@Getter
public class QueryWildcardNested extends QuerySpec {

  private String field;
  private String wildcard;

  /**
   * The nested object
   */
  private String path;

  /**
   * The Object name of the inner search results
   */
  private String docvalueFields;

  public QueryWildcardNested setTerm(String field, String wildcard, String path,
      String docvalueFields) {
    this.field = field;
    this.wildcard = wildcard;
    this.path = path;
    this.docvalueFields = docvalueFields;

    return this;
  }

}
