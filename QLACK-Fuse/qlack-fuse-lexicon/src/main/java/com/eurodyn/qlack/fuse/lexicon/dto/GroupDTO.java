package com.eurodyn.qlack.fuse.lexicon.dto;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GroupDTO implements Serializable {

  private static final long serialVersionUID = 588067576420029887L;

  private String id;
  private String title;
  private String description;

}
