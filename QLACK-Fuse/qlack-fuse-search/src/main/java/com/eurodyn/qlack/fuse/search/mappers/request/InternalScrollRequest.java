package com.eurodyn.qlack.fuse.search.mappers.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InternalScrollRequest {

  private String scroll;
  @JsonProperty("scroll_id")
  private String scrollId;
}
