package com.eurodyn.qlack.fuse.aaa.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author European Dynamics SA
 */
@Getter
@Setter
@NoArgsConstructor
public class OperationDTO extends BaseDTO {

  private String name;
  private boolean dynamic;
  private String dynamicCode;
  private String description;

  public OperationDTO(String name) {
    this.name = name;
  }

}
