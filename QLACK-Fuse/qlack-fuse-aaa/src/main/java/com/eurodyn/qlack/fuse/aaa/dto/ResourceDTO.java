package com.eurodyn.qlack.fuse.aaa.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * A simple DTO (Data Transfer Object) that is used to set and retrieve data of Resource Object
 *
 * @author European Dynamics
 */
@Getter
@Setter
public class ResourceDTO extends BaseDTO {

  /**
   * the name
   */
  private String name;
  /**
   * the description
   */
  private String description;
  /**
   * the object Id
   */
  private String objectId;

}
