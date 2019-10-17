package com.eurodyn.qlack.fuse.aaa.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A simple DTO that is used to retrieve and set data to Operation
 *
 * @author European Dynamics SA
 */
@Getter
@Setter
@NoArgsConstructor
public class OperationDTO extends BaseDTO {

  /**
   * the name
   */
  private String name;
  /**
   * the dynamic
   */
  private boolean dynamic;
  /**
   * the dynamic code
   */
  private String dynamicCode;
  /**
   * the description of the operation
   */
  private String description;

  public OperationDTO(String name) {
    this.name = name;
  }

}
