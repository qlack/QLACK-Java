package com.eurodyn.qlack.fuse.aaa.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Set;

@Getter
@Setter
@Accessors(chain = true)
public class UserDTO extends BaseDTO {

  private String username;

  private String password;

  private byte status;

  private boolean superadmin;

  private boolean external;

  private Set<UserAttributeDTO> userAttributes;

  // The session Id created for this user. Expect this to be populated, only, when attempting to
  // login the user.
  private String sessionId;
}
