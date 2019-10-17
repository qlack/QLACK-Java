package com.eurodyn.qlack.fuse.aaa.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A simple DTO (Data Transfer Object) that is used to represent an operation/resource pair for
 * authorization
 *
 * @author European Dynamics SA
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResourceOperationDTO {

  /**
   * the operation
   */
  String operation;
  /**
   * the resource Id
   */
  String resourceId;
}
