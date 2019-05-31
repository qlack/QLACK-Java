package com.eurodyn.qlack.fuse.aaa.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * User attributes DTO
 *
 * @author European Dynamics
 */
@Getter
@Setter
@NoArgsConstructor
public class UserAttributeDTO extends BaseDTO {

  private String name;
  private String data;
  private String userId;
  private byte[] binData;
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
    this.binData = columnBinData;
    this.userId = userID;
    this.data = columnData;
    this.contentType = contentType;
  }

}
