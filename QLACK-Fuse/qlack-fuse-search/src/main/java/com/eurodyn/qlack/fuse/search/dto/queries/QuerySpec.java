package com.eurodyn.qlack.fuse.search.dto.queries;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;

/**
 * The superclass of all different types of queries supported by this module. It provides commonly
 * used properties between all subclasses as well as it allows to tune the number and type of search
 * results.
 */
@Getter
public abstract class QuerySpec {

  protected QuerySort querySort;
  // The list of indices a query is executed against.
  private List<String> indices = new ArrayList<>();
  // The list of document types a query is executed against.
  private List<String> types = new ArrayList<>();
  // Whether to include the complete query output (JSON string) as it comes
  // from ES - useful for debugging purposes or to extract information not
  // encapsulated in this module's logic.
  private boolean includeAllSource = false;
  // Whether to include the actual search results - useful in case you need to
  // execute queries such as "Are there any results matching?" without being
  // interested for the results themselves.
  private boolean includeResults = true;
  // The first record to return from the list of results - useful for paging.
  private int startRecord = 0;
  // The size of each page of search results - useful for paging.
  private int pageSize = 100;
  // Whether to include ES's explain info.
  // See: https://www.elastic.co/guide/en/elasticsearch/reference/1.7/search-explain.html
  private boolean explain = false;
  // If set to true then a _count request is sent instead of a _search which only returns the count
  // of the query results. In this case aggregate, includeResults, includeAllSource, explain, startRecord,
  // pageSize, scroll, and querySort are ignored.
  private boolean countOnly = false;
  // If not null then a scroll request is generated. In this case startRecord is ignored. This
  // number indicates the number of minutes for which the scroll context remains active.
  private Integer scroll;
  // By giving a value to this field an aggregate query will be created. This field should contain
  // the name of a field of the searched document.
  // Only the values of this field are going to be returned. Also the response will contain a set of
  // results contains distinct values for this field.
  // See https://www.elastic.co/guide/en/elasticsearch/reference/5.5/search-aggregations-bucket-terms-aggregation.html
  private String aggregate;
  // Only relevant if aggregate is given. In this case this sets the maximum result of the aggregation.
  private int aggregateSize = 100;

  /**
   * Sets the indices against which the query is executed.
   *
   * @param indexName The names of the indices to add.
   */
  public QuerySpec setIndex(String... indexName) {
    indices.addAll(Arrays.asList(indexName));
    return this;
  }

  /**
   * Sets the document types against which the query is executed.
   *
   * @param typeName The names of the document types to search.
   */
  public QuerySpec setType(String... typeName) {
    types.addAll(Arrays.asList(typeName));
    return this;
  }

  /**
   * Sets the first record from which search results are paginated.
   *
   * @param startRecord The number of record to start from.
   */
  public QuerySpec setStartRecord(int startRecord) {
    this.startRecord = startRecord;
    return this;
  }

  /**
   * Sets the number of search results returned.
   *
   * @param pageSize The number of results to return.
   */
  public QuerySpec setPageSize(int pageSize) {
    this.pageSize = pageSize;
    return this;
  }

  /**
   * Sets whether ES explain info is included in the results.
   *
   * @param explain Whether to enable or disable the ES explain info.
   */
  public QuerySpec setExplain(boolean explain) {
    this.explain = explain;
    return this;
  }

  /**
   * Convenience method to include the complete query output in the results.
   */
  public QuerySpec includeAllSources() {
    this.includeAllSource = true;
    return this;
  }

  /**
   * Convenience method to exclude search hits from the results.
   */
  public QuerySpec excludeResults() {
    this.includeResults = false;
    return this;
  }

  public QuerySpec setQuerySort(QuerySort querySort) {
    this.querySort = querySort;
    return this;
  }

  public QuerySpec setCountOnly(boolean countOnly) {
    this.countOnly = countOnly;
    return this;
  }

  public QuerySpec setScroll(Integer scroll) {
    this.scroll = scroll;
    return this;
  }

  public QuerySpec setAggregate(String aggregate) {
    this.aggregate = aggregate;
    return this;
  }

  public QuerySpec setAggregateSize(int aggregateSize) {
    this.aggregateSize = aggregateSize;
    return this;
  }
}
