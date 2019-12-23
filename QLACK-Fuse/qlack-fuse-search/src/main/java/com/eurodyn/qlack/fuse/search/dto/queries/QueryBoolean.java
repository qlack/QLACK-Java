package com.eurodyn.qlack.fuse.search.dto.queries;

import java.util.HashMap;
import java.util.Map;

/**
 * A query applying boolean logic on its terms. This query is constructed by
 * setting individual {@link QuerySpec} objects as terms while specifying one of
 * the three default boolean operators of ES. Example:
 *
 * <pre>
 * new QueryBoolean()
 * .setTerm(
 * new QueryMatch()
 * .setTerm("fooField", "foo")
 * .setIndex("bar")
 * .setType("FooBarDTO"), BooleanType.MUST)
 * .setTerm(
 * new QueryMatch()
 * .setTerm("barField", "bar")
 * .setIndex("foo")
 * .setType("BarFooDTO"), BooleanType.MUSTNOT
 * .setPageSize(10)
 * .setStartRecord(0)
 * .setExplain(false);
 * </pre>
 *
 * See also:<br> https://www.elastic.co/guide/en/elasticsearch/reference/1.7/query-dsl-bool-query.html
 */
public class QueryBoolean extends QuerySpec {

  private Map<QuerySpec, BooleanType> terms = new HashMap<>();

  /**
   * @return the terms
   */
  public Map<QuerySpec, BooleanType> getTerms() {
    return terms;
  }

  /**
   * @param terms the terms to set
   */
  public void setTerms(Map<QuerySpec, BooleanType> terms) {
    this.terms = terms;
  }

  /**
   * Appends a new search term to the boolean query.
   *
   * @param term The term to append.
   * @param type The boolean type to combine this term with existing terms.
   * @return a {@link QueryBoolean} object
   */
  public QueryBoolean setTerm(QuerySpec term, BooleanType type) {
    terms.put(term, type);

    return this;
  }

  /**
   * The available boolean operators.
   */
  public enum BooleanType {
    /**
     * Boolean AND-equivalent
     */
    MUST,
    /**
     * Boolean NOT-equivalent
     */
    MUSTNOT,
    /**
     * Boolean OR-equivalent
     */
    SHOULD
  }
}
