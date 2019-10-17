package com.eurodyn.qlack.fuse.aaa.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A DTO for SessionAttribute. It is used to retrieve and save SessionAttributes data
 *
 * @author European Dynamics S.A
 */
@Getter
@Setter
@NoArgsConstructor
public class SessionAttributeDTO extends BaseDTO {

  /**
   * the name
   */
  private String name;
  /**
   * the value
   */
  private String value;
  /**
   * the id of the session
   */
  private String sessionId;

  public SessionAttributeDTO(String name, String value, String sessionId) {
    this.name = name;
    this.value = value;
    this.sessionId = sessionId;
  }

  public SessionAttributeDTO(String name, String value) {
    this.name = name;
    this.value = value;
  }
}
