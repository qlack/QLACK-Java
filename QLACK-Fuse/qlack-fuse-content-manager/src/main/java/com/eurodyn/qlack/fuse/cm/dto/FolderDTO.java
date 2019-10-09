package com.eurodyn.qlack.fuse.cm.dto;

import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FolderDTO extends NodeDTO {

  private Set<NodeDTO> children;

}
