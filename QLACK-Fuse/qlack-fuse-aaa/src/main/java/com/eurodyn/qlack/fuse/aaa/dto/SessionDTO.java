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

  /**
   * the userId
   */
  private String userId;
  /**
   * the date that is created on the session
   */
  private long createdOn;
  /**
   * the terminated on
   */
  private Long terminatedOn;
  /**
   * the application session Id
   */
  private String applicationSessionId;
  /**
   * a set of attributes
   */
  private Set<SessionAttributeDTO> sessionAttributes;

}