package com.eurodyn.qlack.fuse.search.mapper.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonRawValue;
import lombok.Getter;
import lombok.Setter;

/**
 * Internal mapping update request
 *
 * @author European Dynamics SA.
 */
@Getter
@Setter
public class InternalUpdateMappingRequest {

  /**
   * Properties
   */
  @JsonInclude(Include.NON_NULL)
  @JsonRawValue
  private String properties;
}
