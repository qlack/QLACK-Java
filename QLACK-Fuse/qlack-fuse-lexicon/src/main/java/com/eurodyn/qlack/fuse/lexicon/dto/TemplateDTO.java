package com.eurodyn.qlack.fuse.lexicon.dto;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/** A simple DTO (Data Transfer Object) that does not contain any business logic. It is used to
 * retrieve and store the Template data .
 * @author European Dynamics
 */
@Getter
@Setter
public class TemplateDTO implements Serializable {

  private static final long serialVersionUID = 1L;
  /**
   * the id that identifies uniquely the template
   */
  private String id;
  /**
   * The name of the Template
   */
  private String name;
  /**
   * The language id that is referred to
   */
  private String languageId;
  /**
   *  The content
   */
  private String content;

}
