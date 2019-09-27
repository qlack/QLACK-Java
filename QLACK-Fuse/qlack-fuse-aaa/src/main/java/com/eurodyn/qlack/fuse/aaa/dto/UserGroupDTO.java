package com.eurodyn.qlack.fuse.aaa.dto;

import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

/** A simple DTO (Data Transfer Object) that does not contain any business logic. It is used
 * only to retrieve and save theirs UserGroup objects data.
 * @author European Dynamics SA
 */
@Getter
@Setter
@NoArgsConstructor
public class UserGroupDTO extends BaseDTO implements GrantedAuthority {

  /**
   * the name
   */
  private String name;
  /**
   * the objectId
   */
  private String objectId;
  /**
   * the description
   */
  private String description;
  /**
   * the parentId
   */
  private String parentId;
  /**
   * a set of {@link UserGroupDTO} children objects
   */
  private Set<UserGroupDTO> children;

  public UserGroupDTO(String id) {
    setId(id);
  }

  @Override
  public String getAuthority() {
    return name;
  }
}
