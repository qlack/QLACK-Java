package com.eurodyn.qlack.fuse.cm.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FileDTO extends NodeDTO {

  private String mimetype;
  private Long size;
  private List<VersionDTO> versions;

}
