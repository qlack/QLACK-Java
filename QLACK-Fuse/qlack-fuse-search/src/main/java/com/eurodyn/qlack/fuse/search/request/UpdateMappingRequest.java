package com.eurodyn.qlack.fuse.search.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateMappingRequest extends BaseRequest {

  private String indexName;
  private String typeName;
  private String indexMapping;

}
