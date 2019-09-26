package com.eurodyn.qlack.fuse.aaa.dto;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * A simple DTO for OperationAccess .It is used to store and retrieve the data
 * of Operation Access object.
 *
 * @author European Dynamics SA
 */
@Getter
@Setter
public class OperationAccessDTO implements Serializable {

  /**
   * the operation
   */
  private OperationDTO operation;
  /**
   * the resource
   */
  private ResourceDTO resource;
  /**
   * a deny status of operation
   */
  private boolean deny;

}
