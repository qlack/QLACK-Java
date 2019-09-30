package com.eurodyn.qlack.fuse.lexicon.dto;

import java.io.Serializable;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

/**
 * A simple DTO for Key class. The usage of it is to retrieve and store the data for the Key .
 *
 * @author European Dynamics SA
 */
@Getter
@Setter
public class KeyDTO implements Serializable {

  private static final long serialVersionUID = 1L;
  /**
   * The key id
   */
  private String id;
  /**
   * The name of a specific key
   */
  private String name;
  /**
   * The groupId
   */
  private String groupId;
  // The translations available for this key. The map key is
  // the language locale while the map value is the actual translation.
  /**
   * A map of available translations
   */
  private Map<String, String> translations;

}
