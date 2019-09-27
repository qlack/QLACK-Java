package com.eurodyn.qlack.fuse.search.mappers.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRawValue;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * Internal Search request
 *
 * @author European Dynamics SA.
 */
@Getter
@Setter
public class InternalSearchRequest {

  /**
   * From
   */
  @JsonInclude(Include.NON_NULL)
  private Integer from;

  /**
   * Size
   */
  @JsonInclude(Include.NON_NULL)
  private Integer size;

  /**
   * Whether or not to explain
   */
  @JsonInclude(Include.NON_NULL)
  private Boolean explain;

  /**
   * The query
   */
  @JsonInclude(Include.NON_NULL)
  @JsonRawValue
  private String query;

  /**
   * The query source
   */
  @JsonInclude(Include.NON_NULL)
  @JsonProperty("_source")
  private List<String> source;

  /**
   * The aggregates
   */
  @JsonInclude(Include.NON_NULL)
  @JsonRawValue
  private String aggs;

  /**
   * The sorting
   */
  @JsonRawValue
  @JsonInclude(Include.NON_NULL)
  private String sort;

}
