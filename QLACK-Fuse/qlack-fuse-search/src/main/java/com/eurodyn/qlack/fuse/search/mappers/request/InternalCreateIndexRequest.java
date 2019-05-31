package com.eurodyn.qlack.fuse.search.mappers.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRawValue;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InternalCreateIndexRequest {

  private Settings settings;

  @JsonInclude(Include.NON_NULL)
  @JsonRawValue
  private String mappings;

  @Getter
  @Setter
  public static class Settings {

    private Index index;
    private Analysis analysis;

    @Getter
    @Setter
    public static class Index {

      @JsonProperty("number_of_shards")
      private String numberOfShards;
      @JsonProperty("number_of_replicas")
      private String numberOfReplicas;
    }

    @Getter
    @Setter
    public static class Analysis {

      private Filter filter;

      @Getter
      @Setter
      public static class Filter {

        @JsonProperty("my_stop")
        private MyStop myStop;

        @Getter
        @Setter
        public static class MyStop {

          private String type = "stop";
          private List<String> stopwords;

        }
      }
    }
  }
}
