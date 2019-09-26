package com.eurodyn.qlack.fuse.settings.dto;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * Data Transfer Object for Group
 *
 * @author European Dynamics SA.
 */
@Getter
@Setter
public class GroupDTO implements Serializable {

  private static final long serialVersionUID = -3330713494152798837L;

  /**
   * The name of the Group
   */
  private String name;

  @Override
  public String toString() {
    return "GroupDTO [name=" + name + "]";
  }
}
