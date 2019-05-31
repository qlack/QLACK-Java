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
public class InternalSearchRequest {

  @JsonInclude(Include.NON_NULL)
  private Integer from;
  @JsonInclude(Include.NON_NULL)
  private Integer size;
  @JsonInclude(Include.NON_NULL)
  private Boolean explain;
  @JsonInclude(Include.NON_NULL)
  @JsonRawValue
  private String query;
  @JsonInclude(Include.NON_NULL)
  @JsonProperty("_source")
  private List<String> source;
  @JsonInclude(Include.NON_NULL)
  @JsonRawValue
  private String aggs;
  @JsonRawValue
  @JsonInclude(Include.NON_NULL)
  private String sort;

}
