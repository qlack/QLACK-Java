package com.eurodyn.qlack.fuse.aaa.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Implements Spring's UserDetails interface.
 *
 * @author EUROPEAN DYNAMICS SA
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailsDTO extends BaseDTO implements UserDetails {

  /**
   * the username
   */
  private String username;

  /**
   * the password
   */
  private String password;

  /**
   * the salt
   */
  private String salt;

  /**
   * the status
   */
  private byte status;

  /**
   * the superAdmin
   */
  private boolean superadmin;

  private boolean external;

  /**
   * The session Id created for this user. Expect this to be populated only when attempting to login
   * the user.
   */
  private String sessionId;

  private List<UserHasOperationDTO> userHasOperations = new ArrayList<>();

  private List<UserGroupHasOperationDTO> userGroupHasOperations = new ArrayList<>();

  private List<UserGroupDTO> userGroups = new ArrayList<>();

  /**
   * AAA groups are matched to Spring authorities.
   *
   * @return AAA groups as authorities
   */
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return userGroups;
  }

  /**
   * An account non expired option does not exist in AAA domain, so it is always set to true.
   *
   * @return true
   */
  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  /**
   * An account non locked option does not exist in AAA domain, so it is always set to true.
   *
   * @return true
   */
  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  /**
   * A credentials non expired option does not exist in AAA domain, so it is always set to true.
   *
   * @return true
   */
  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return status == 1;
  }

}