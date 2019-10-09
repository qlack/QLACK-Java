package com.eurodyn.qlack.fuse.cm.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileDTO extends NodeDTO {

  private String mimetype;
  private Long size;
  private List<VersionDTO> versions;

}
