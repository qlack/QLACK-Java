package com.eurodyn.qlack.fuse.aaa.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

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

  /**
   * A convenience method to return a specific attribute.
   * @param name The name of the attribute to return.
   */
  public Optional<UserAttributeDTO> getAttribute(String name) {
    return userAttributes.stream().filter(a -> a.getName().equals(name)).findFirst();
  }

  /**
   * A convenience method to return the data of a specific attribute.
   * @param name The name of the attribute to return.
   * @return The data of the requested attribute or null if the attribute does not exist.
   */
  public String getAttributeData(String name) {
    return getAttribute(name).map(UserAttributeDTO::getData).orElse(null);
  }

  /**
   * Setting an attribute. If the attribute exists, the data, bindata and contentType are updated.
   * @param attribute The attribute to update or create.
   */
  public void setAttribute(UserAttributeDTO attribute) {
    boolean found = false;
    if (userAttributes != null) {
      for (UserAttributeDTO userAttributesDTO : userAttributes) {
        if (userAttributesDTO.getName().equalsIgnoreCase(attribute.getName())) {
          userAttributesDTO.setData(attribute.getData());
          userAttributesDTO.setBindata(attribute.getBindata());
          userAttributesDTO.setContentType(attribute.getContentType());
          found = true;
          break;
        }
      }
    }
    if (!found) {
      if (userAttributes == null) {
        userAttributes = new HashSet<>();
      }
      attribute.setUserId(getId());
      userAttributes.add(attribute);
    }
  }
}
