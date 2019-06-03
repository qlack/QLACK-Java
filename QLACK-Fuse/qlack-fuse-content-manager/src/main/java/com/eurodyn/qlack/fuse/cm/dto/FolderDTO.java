package com.eurodyn.qlack.fuse.cm.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class FolderDTO extends NodeDTO {

  private Set<NodeDTO> children;

}
