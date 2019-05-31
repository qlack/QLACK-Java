package com.eurodyn.qlack.fuse.aaa.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

/**
 * This is transfer object for AaaSession entity.
 *
 * @author European Dynamics SA
 */
@Getter
@Setter
public class SessionDTO extends BaseDTO {

  private String userId;
  private long createdOn;
  private Long terminatedOn;
  private String applicationSessionId;
  private Set<SessionAttributeDTO> sessionAttributes;

}