package com.eurodyn.qlack.fuse.search.mapper.response;

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

    /**
     * Total no of shards
     */
    private int total;

    /**
     * Flag to indicate success
     */
    private int successful;

    /**
     * Flag to indicate failure
     */
    private int failed;
  }

  /**
   * Hits class
   *
   * @author European Dynamics SA.
   */
  @Getter
  @Setter
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class Hits {

    /**
     * Total no of hitList
     */
    private int total;

    /**
     * Maximum score
     */
    @JsonProperty("max_score")
    private float maxScore;

    /**
     * Hits list
     */
    private List<Hit> hitList;

    public List<Hit> getHitList() {
      if (hitList == null) {
        hitList = new ArrayList<>();
      }
      return hitList;
    }

    /**
     * Hit class
     *
     * @author European Dynamics SA.
     */
    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Hit {

      /**
       * Index
       */
      @JsonProperty("_index")
      private String index;

      /**
       * Type
       */
      @JsonProperty("_type")
      private String type;

      /**
       * Id
       */
      @JsonProperty("_id")
      private String id;

      /**
       * Score
       */
      @JsonProperty("_score")
      private float score;

      /**
       * Source
       */
      @JsonProperty("_source")
      private Object source;

      /**
       * InnerHits
       */
      @JsonProperty("inner_hits")
      private Object innerHits;

      /**
       * Returns the Hit's source
       *
       * @return the source value
       */
      @JsonRawValue
      public String getSource() {
        return source != null ? source.toString() : null;
      }

      /**
       * Returns the inner hitList value
       *
       * @return the inner hitList value
       */
      @JsonRawValue
      public String getInnerHits() {
        return innerHits != null ? innerHits.toString() : null;
      }
    }
  }

  /**
   * Aggregations
   *
   * @author European Dynamics SA.
   */
  @Getter
  @Setter
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class Aggregations {

    /**
     * Aggregation
     */
    private Agg agg;

    /**
     * Aggregation
     *
     * @author European Dynamics SA.
     */
    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Agg {

      /**
       * Upper bound for document error count
       */
      private long docCountErrorUpperBound;

      /**
       * Sum for other document count
       */
      private long sumOtherDocCount;

      /**
       * Bucket list
       */
      private List<Bucket> buckets;

      /**
       * Bucket list getter method
       *
       * @return a list of {@link Bucket} objects
       */
      public List<Bucket> getBuckets() {
        if (buckets == null) {
          buckets = new ArrayList<>();
        }
        return buckets;
      }

      /**
       * Bucket class
       *
       * @author European Dynamics SA.
       */
      @Getter
      @Setter
      @JsonIgnoreProperties(ignoreUnknown = true)
      public static class Bucket {

        /**
         * Key
         */
        private long key;

        /**
         * Key as String
         */
        private String keyAsString;

        /**
         * Document count
         */
        private long docCount;

      }
    }
  }
}