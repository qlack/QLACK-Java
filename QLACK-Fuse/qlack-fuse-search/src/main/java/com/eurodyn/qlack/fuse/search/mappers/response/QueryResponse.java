package com.eurodyn.qlack.fuse.search.mappers.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRawValue;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * An Elastic Search response wrapper
 *
 * @author European Dynamics SA.
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class QueryResponse {

  /**
   * Time to respond
   */
  private int took;

  /**
   * Timeout
   */
  @JsonProperty("timed_out")
  private boolean timeOut;

  /**
   * Shards
   */
  @JsonProperty("_shards")
  private Shards shards;

  /**
   * Hits
   */
  private Hits hits;

  /**
   * Aggregations
   */
  @JsonInclude(Include.NON_NULL)
  private Aggregations aggregations;

  /**
   * Result count
   */
  private long count;

  /**
   * Scroll Id
   */
  @JsonProperty("_scroll_id")
  private String scrollId;

  /**
   * An Elastic Search response wrapper
   *
   * @author European Dynamics SA.
   */
  @Getter
  @Setter
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class Shards {

    private int total;
    private int successful;
    private int failed;
  }

  @Getter
  @Setter
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class Hits {

    private int total;
    @JsonProperty("max_score")
    private float maxScore;
    private List<Hit> hits;

    public List<Hit> getHits() {
      return hits == null ? hits = new ArrayList<>() : hits;
    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Hit {

      @JsonProperty("_index")
      private String index;
      @JsonProperty("_type")
      private String type;
      @JsonProperty("_id")
      private String id;
      @JsonProperty("_score")
      private float score;
      @JsonProperty("_source")
      private Object source;
      @JsonProperty("inner_hits")
      private Object innerHits;

      @JsonRawValue
      public String getSource() {
        return source != null ? source.toString() : null;
      }

      @JsonRawValue
      public String getInnerHits() {
        return innerHits != null ? innerHits.toString() : null;
      }
    }
  }

  @Getter
  @Setter
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class Aggregations {

    private Agg agg;

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Agg {

      private long docCountErrorUpperBound;
      private long sumOtherDocCount;
      private List<Bucket> buckets;

      public List<Bucket> getBuckets() {
        return (buckets == null) ? buckets = new ArrayList<>() : buckets;
      }

      @Getter
      @Setter
      @JsonIgnoreProperties(ignoreUnknown = true)
      public static class Bucket {

        private long key;
        private String keyAsString;
        private long docCount;

      }
    }
  }
}
