package com.eurodyn.qlack.fuse.aaa.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author European Dynamics S.A.
 */
@Getter
@Setter
@NoArgsConstructor
public class SessionAttributeDTO extends BaseDTO {

  private String name;
  private String value;
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
