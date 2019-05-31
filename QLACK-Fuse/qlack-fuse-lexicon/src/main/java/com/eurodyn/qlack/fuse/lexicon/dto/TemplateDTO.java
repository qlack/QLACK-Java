package com.eurodyn.qlack.fuse.lexicon.dto;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TemplateDTO implements Serializable {

  private static final long serialVersionUID = 1L;
  private String id;
  private String name;
  private String languageId;
  private String content;

}
