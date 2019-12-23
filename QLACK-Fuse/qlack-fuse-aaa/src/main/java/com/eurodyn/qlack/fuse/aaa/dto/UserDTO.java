package com.eurodyn.qlack.fuse.aaa.dto;

import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * A simple DTO ( Data Transfer Object) for User.It is used to get the User's
 * data
 *
 * @author European Dynamcis SA
 */
@Getter
@Setter
@Accessors(chain = true)
public class UserDTO extends BaseDTO {

  /**
   * the username
   */
  private String username;

  /**
   * the password
   */
  private String password;

  /**
   * the status
   */
  private byte status;

  private boolean superadmin;

  /**
   * the external
   */
  private boolean external;

  /**
   * the  user attributes
   */
  private Set<UserAttributeDTO> userAttributes;

  /**
   * The session Id created for this user. Expect this to be populated, only,
   * when attempting to login the user.
   **/
  private String sessionId;
}
