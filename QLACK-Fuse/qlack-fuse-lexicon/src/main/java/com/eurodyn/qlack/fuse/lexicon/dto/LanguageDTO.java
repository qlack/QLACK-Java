package com.eurodyn.qlack.fuse.lexicon.dto;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * A simple DTO (Data Transfer Object) class that does not contain any business
 * logic. It is used to store and retrieve a Language data such as the name of
 * it.
 *
 * @author European Dynamics SA
 */
@Getter
@Setter
public class LanguageDTO implements Serializable {

  /**
   * The language id
   */
  private String id;
  /**
   * The name of the language
   */
  private String name;
  /**
   * The locale that identifies tha language code
   */
  private String locale;
  /**
   * A boolean value showing if its active or not the language
   */
  private boolean active;

}
