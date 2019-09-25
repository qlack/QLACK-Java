package com.eurodyn.qlack.fuse.audit.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The Audit Level DTO, that holds all information about the level of an Audit.
 *
 * @author European Dynamics SA.
 */
@Getter
@Setter
@NoArgsConstructor
public class AuditLevelDTO extends AuditBaseDTO {
  /**
   * the name of the Audit level
   */
  private String name;

  /**
   * the description of the Audit level
   */
  private String description;

  /**
   * the id of the web session
   */
  private String prinSessionId;

  /**
   * a number representing the date the Audit level was created
   */
  private Long createdOn;

  /**
   * parameterized Constructor
   *
   * @param name the name of the level
   */
  public AuditLevelDTO(String name) {
    this.setName(name);
  }

  /**
   * parameterized Constructor
   *
   * @param id the id of the level
   * @param name the name of the level
   */
  public AuditLevelDTO(String id, String name) {
    this.setName(name);
    this.setId(id);
  }
}
