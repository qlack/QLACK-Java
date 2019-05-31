package com.eurodyn.qlack.fuse.cm.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author European Dynamics
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NodeAttributeDTO {

  private String nodeId;
  private String name;
  private String value;
}
