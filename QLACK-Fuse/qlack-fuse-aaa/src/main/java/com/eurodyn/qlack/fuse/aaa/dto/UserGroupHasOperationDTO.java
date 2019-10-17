package com.eurodyn.qlack.fuse.aaa.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * A simple DTO (Data Transfer Object) for UseGroupHasOperation. A class that contains information
 * about the operations that a user who belongs to a specific group name has
 *
 * @author European Dynamics SA
 */
@Getter
@Setter
public class UserGroupHasOperationDTO extends BaseDTO {

  /**
   * the @{@link UserGroupDTO} object
   */
  private UserGroupDTO userGroupDTO;
  /**
   * the @{@link OperationDTO} object
   */
  private OperationDTO operationDTO;
  /**
   * the {@link ResourceDTO} object
   */
  private ResourceDTO resourceDTO;
  /**
   * a check value whether its denied or not
   */
  private boolean deny;
}
