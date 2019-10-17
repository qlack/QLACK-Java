package com.eurodyn.qlack.fuse.aaa.dto;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * A simple DTO (Data Transfer Object) for Base that does not contain any business logic . It used
 * only to set and retrieve Base data
 *
 * @author European Dynamics SA
 */
@Getter
@Setter
public class BaseDTO implements Serializable {

  /**
   * the id
   */
  private String id;

}
