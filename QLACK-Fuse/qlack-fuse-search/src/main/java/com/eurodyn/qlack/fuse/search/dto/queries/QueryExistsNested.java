package com.eurodyn.qlack.fuse.search.dto.queries;

/**
 * The term nested query exists finds documents that have a value specified in the inverted index.
 *
 * <pre>
 * new QueryExistsNested()
 * 		.setTerm("searchField", "nestedObject")
 * </pre>
 *
 * See also: https://www.elastic.co/guide/en/elasticsearch/reference/current/query-dsl-exists-query.html
 */

public class QueryExistsNested extends QuerySpec {

  // The field to execute the search against
  private String field;
  // The nested object name
  private String path;

  public QueryExistsNested setTerm(String field, String path) {
    this.field = field;
    this.path = path;

    return this;
  }

  /**
   * @return field
   */
  public String getField() {
    return field;
  }

  /**
   * @return path
   */
  public String getPath() {
    return path;
  }
}
