package com.eurodyn.qlack.fuse.settings.dto;

import java.io.Serializable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data Transfer Object for Setting
 *
 * @author European Dynamics SA.
 */
@Getter
@Setter
@NoArgsConstructor
public class SettingDTO implements Serializable {

  private static final long serialVersionUID = -1745622761507844077L;

  /**
   * Id
   */
  private String id;

  /**
   * Owner
   */
  private String owner;

  /**
   * Group
   */
  private String group;

  /**
   * Key
   */
  private String key;

  /**
   * Value
   */
  private String val;

  /**
   * The date the setting was created on
   */
  private long createdOn;

  /**
   * Whether the setting is sensitive
   */
  private boolean sensitive;

  /**
   * Password
   */
  private boolean password;

  /**
   * Copy constructor
   *
   * @param key key
   * @param val value
   */
  public SettingDTO(String key, String val) {
    this.key = key;
    this.val = val;
  }

  /**
   * Returns value as int
   *
   * @return value as integer
   */
  public int getValAsInt() {
    return Integer.valueOf(val);
  }

  /**
   * Returns value as long
   *
   * @return value as long
   */
  public long getValAsLong() {
    return Long.valueOf(val);
  }

  /**
   * Returns value as boolean
   *
   * @return value as boolean
   */
  public boolean getValAsBoolean() {
    return Boolean.valueOf(val);
  }
}
