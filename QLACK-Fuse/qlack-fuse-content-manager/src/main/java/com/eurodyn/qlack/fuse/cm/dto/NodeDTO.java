package com.eurodyn.qlack.fuse.cm.dto;

import java.util.List;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NodeDTO {

  private String id;
  private String name;
  private String parentId;

  private boolean lockable;
  private boolean versionable;

  private long createdOn;
  private String createdBy;
  private long lastModifiedOn;
  private String lastModifiedBy;
  private boolean locked;
  private Long lockedOn;
  private String lockedBy;
  private Set<NodeAttributeDTO> attributes;
  private List<BreadcrumbPartDTO> path;
}
