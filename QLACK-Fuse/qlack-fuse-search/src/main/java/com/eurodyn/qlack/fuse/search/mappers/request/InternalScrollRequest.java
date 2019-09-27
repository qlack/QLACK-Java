package com.eurodyn.qlack.fuse.search.mappers.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Internal request for scrolls
 *
 * @author European Dynamics SA.
 */
@Getter
@Setter
public class InternalScrollRequest {

  /**
   * The scroll size
   */
  private String scroll;

  /**
   * The scroll id
   */
  @JsonProperty("scroll_id")
  private String scrollId;
}
