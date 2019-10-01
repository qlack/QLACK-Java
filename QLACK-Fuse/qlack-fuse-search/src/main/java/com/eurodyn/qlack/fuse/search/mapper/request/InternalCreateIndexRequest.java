package com.eurodyn.qlack.fuse.search.mapper.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRawValue;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * Internal request for creating indexes
 *
 * @author European Dynamics SA.
 */
@Getter
@Setter
public class InternalCreateIndexRequest {

  /**
   * settings
   */
  private Settings settings;

  /**
   * mappings
   */
  @JsonInclude(Include.NON_NULL)
  @JsonRawValue
  private String mappings;


  /**
   * Settings class
   *
   * @author European Dynamics SA.
   */
  @Getter
  @Setter
  public static class Settings {

    /**
     * index
     */
    private Index index;

    /**
     * analysis
     */
    private Analysis analysis;

    /**
     * Index class
     *
     * @author European Dynamics SA.
     */
    @Getter
    @Setter
    public static class Index {

      /**
       * number of shards
       */
      @JsonProperty("number_of_shards")
      private String numberOfShards;

      /**
       * number of replicas
       */
      @JsonProperty("number_of_replicas")
      private String numberOfReplicas;
    }

    /**
     * Analysis class
     *
     * @author European Dynamics SA.
     */
    @Getter
    @Setter
    public static class Analysis {

      /**
       * filter
       */
      private Filter filter;

      /**
       * Filter class
       *
       * @author European Dynamics SA.
       */
      @Getter
      @Setter
      public static class Filter {

        /**
         * stopwords object
         */
        @JsonProperty("my_stop")
        private MyStop myStop;

        /**
         * MyStop class
         *
         * @author European Dynamics SA.
         */
        @Getter
        @Setter
        public static class MyStop {

          /**
           * type
           */
          private String type = "stop";

          /**
           * stopwords
           */
          private List<String> stopwords;

        }
      }
    }
  }
}
