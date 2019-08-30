package com.eurodyn.qlack.fuse.lexicon.dto;

import java.io.Serializable;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KeyDTO implements Serializable {

  private static final long serialVersionUID = 1L;
  private String id;
  private String name;
  private String groupId;
  // The translations available for this key. The map key is
  // the language locale while the map value is the actual translation.
  private Map<String, String> translations;

}
