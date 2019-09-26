package com.eurodyn.qlack.fuse.search.request;

import lombok.Getter;
import lombok.Setter;

/**
 * Request for mapping update
 *
 * @author European Dynamics SA.
 */
@Getter
@Setter
public class UpdateMappingRequest extends BaseRequest {

  /**
   * Name of the index
   */
  private String indexName;

  /**
   * Name of the type
   */
  private String typeName;

  /**
   * Mapping for the index
   */
  private String indexMapping;
}
