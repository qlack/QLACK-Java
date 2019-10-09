package com.eurodyn.qlack.fuse.cm.dto;

import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VersionDTO {

  private String id;
  private String name;
  private boolean active;
  private long createdOn;
  private String createdBy;
  private long lastModifiedOn;
  private String lastModifiedBy;
  private Set<VersionAttributeDTO> attributes;
  private String mimetype;
  private Long size;
  private String filename;
  private String nodeId;

}
