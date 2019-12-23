package com.eurodyn.qlack.fuse.aaa.dto;

import lombok.Getter;
import lombok.Setter;


/**
 * A Simple DTO (Data Transfer Object) that does not contain any business logic.
 * The usage of it is to save and retrieve data for @{@link UserHasOperationDTO}
 * objects.
 *
 * @author European Dynamics SA
 */
@Getter
@Setter
public class UserHasOperationDTO extends BaseDTO {

  /**
   * the operation an @{@link OperationDTO} type of object
   */
  private OperationDTO operation;

  /**
   * the resource an @{@link ResourceDTO} type of object
   */
  private ResourceDTO resource;

  /**
   * a check value that is testing if it is denied or not
   */
  private boolean deny;

}
