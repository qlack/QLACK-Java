package com.eurodyn.qlack.fuse.search.dto.queries;

import lombok.Getter;
import org.elasticsearch.search.sort.SortOrder;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A query performing a match against ES. The default match query is of type boolean. The value
 * provided is analyzed and the analysis process constructs a boolean query from the provided text
 * using boolean OR. Example:
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
public class QuerySort {

  /**
   * The fields to order against. The key is the field to execute the sort against. The value should
   * be asc or desc.
   */
  private final Map<String, SortOrder> sortMap = new LinkedHashMap<>();

  /**
   * A convenience method to set the term of this query.
   *
   * @param field The field name to sort against.
   * @param order The order of the sorting.
   * @return a {@link QuerySort} object
   */
  public QuerySort setSort(String field, SortOrder order) {
    sortMap.put(field, order);
    return this;
  }

}
