package com.eurodyn.qlack.fuse.settings.dto;

import java.io.Serializable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author European Dynamics SA
 */
@Getter
@Setter
@NoArgsConstructor
public class SettingDTO implements Serializable {

  private static final long serialVersionUID = -1745622761507844077L;
  private String id;
  private String owner;
  private String group;
  private String key;
  private String val;
  private long createdOn;
  private boolean sensitive;
  private boolean password;

  public SettingDTO(String key, String val) {
    this.key = key;
    this.val = val;
  }

  public int getValAsInt() {
    return Integer.valueOf(val);
  }

  public long getValAsLong() {
    return Long.valueOf(val);
  }

  public boolean getValAsBoolean() {
    return Boolean.valueOf(val);
  }
}
