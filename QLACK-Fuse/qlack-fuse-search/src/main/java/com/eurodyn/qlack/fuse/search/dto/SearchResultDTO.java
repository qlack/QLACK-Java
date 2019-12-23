package com.eurodyn.qlack.fuse.search.dto;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

/**
 * The search results obtained after having executed a search
 *
 * @author European Dynamics SA.
 */
@Getter
@Setter
public class SearchResultDTO {

  /**
   * The JSON representation of the search result as it comes from ES.
   */
  private String source;

  /**
   * The amount of time the query took to be executed (in msec).
   */
  private long executionTime;

  /**
   * Indicates whether ES timed out while executing the search.
   */
  private boolean timedOut;

  /**
   * Total number of shards that needed to be searched.
   */
  private int shardsTotal;

  /**
   * The number of shards successfully searched.
   */
  private int shardsSuccessful;

  /**
   * The number of shards failed to be searched.
   */
  private int shardsFailed;

  /**
   * The total number of hitList for this search.
   */
  private long totalHits;

  /**
   * The best score received for this search.
   */
  private float bestScore;

  /**
   * An indicator of whether there are more results available (useful in
   * paging).
   */
  private boolean hasMore;

  /**
   * If a scroll request was generated then the scroll id is set here
   */
  private String scrollId;

  /**
   * The list of hitList generated for this search.
   */
  private List<SearchHitDTO> hits = new ArrayList<>();

  /**
   * The list of aggregations for this search.
   */
  private Map<String, Long> aggregations = new LinkedHashMap<>();

  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "SearchResultDTO [source=" + source + ", executionTime="
      + executionTime
      + ", timedOut="
      + timedOut
      + ", shardsTotal=" + shardsTotal + ", shardsSuccessful="
      + shardsSuccessful
      + ", shardsFailed="
      + shardsFailed + ", totalHits=" + totalHits + ", bestScore=" + bestScore
      + ", hasMore="
      + hasMore
      + ", hitList=" + hits + "]";
  }

}
