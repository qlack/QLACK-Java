package com.eurodyn.qlack.fuse.lexicon.dto;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * A simple DTO(Data Transfer Object) that does not contain any business logic
 * but is used in order to retrieve and store Group data information.
 *
 * @author European Dynamics SA
 */
@Getter
@Setter
public class GroupDTO implements Serializable {

  private static final long serialVersionUID = 588067576420029887L;

  /**
   * The group id
   */
  private String id;
  /**
   * The title refers the section that mapping is taking place
   */
  private String title;
  /**
   * A brief description that is related to the title of group
   */
  private String description;

}
