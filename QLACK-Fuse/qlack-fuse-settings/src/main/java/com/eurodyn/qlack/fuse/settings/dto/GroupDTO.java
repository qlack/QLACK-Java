package com.eurodyn.qlack.fuse.settings.dto;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GroupDTO implements Serializable {

  private static final long serialVersionUID = -3330713494152798837L;
  private String name;

  @Override
  public String toString() {
    return "GroupDTO [name=" + name + "]";
  }
}
