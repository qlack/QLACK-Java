package com.eurodyn.qlack.fuse.aaa.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A simple DTO for Attribute that does not contain any business logic. The
 * usage is to retrieve and save the user's data attributes
 *
 * @author European Dynamics
 */
@Getter
@Setter
@NoArgsConstructor
public class UserAttributeDTO extends BaseDTO {

  /**
   * the attribute name
   */
  private String name;
  /**
   * the data
   */
  private String data;
  /**
   * the userId
   */
  private String userId;
  private byte[] bindata;
  private String contentType;

  public UserAttributeDTO(String name, String data) {
    this.name = name;
    this.data = data;
  }

  public UserAttributeDTO(String columnName, String columnData, String userId) {
    this.name = columnName;
    this.data = columnData;
    this.userId = userId;
  }

  public UserAttributeDTO(String columnName, String columnData,
    byte[] columnBinData, String userID, String contentType) {
    this.name = columnName;
    this.bindata = columnBinData;
    this.userId = userID;
    this.data = columnData;
    this.contentType = contentType;
  }

}
