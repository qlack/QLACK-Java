package com.eurodyn.qlack.fuse.lexicon.dto;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LanguageDTO implements Serializable {

  private String id;
  private String name;
  private String locale;
  private boolean active;

}
