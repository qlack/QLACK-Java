package com.eurodyn.qlack.fuse.aaa.dto;

import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

/**
 * @author European Dynamics SA
 */
@Getter
@Setter
@NoArgsConstructor
public class UserGroupDTO extends BaseDTO implements GrantedAuthority {

  private String name;
  private String objectId;
  private String description;
  private String parentId;
  private Set<UserGroupDTO> children;

  public UserGroupDTO(String id) {
    setId(id);
  }

  @Override
  public String getAuthority() {
    return name;
  }
}
