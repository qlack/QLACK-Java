package com.eurodyn.qlack.fuse.mailing.dto;

import java.io.Serializable;
import java.util.Arrays;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data Transfer Object for Attachment.
 *
 * @author European Dynamics SA.
 */
@Getter
@Setter
@NoArgsConstructor
public class AttachmentDTO implements Serializable {

  private static final long serialVersionUID = 1L;
  /**
   * Id
   */
  private String id;

  /**
   * The attachment filename
   */
  private String filename;

  /**
   * A {@link java.lang.String} representing the Content-Type of the
   * attachment
   */
  private String contentType;

  /**
   * The attachment actual data
   */
  private byte[] data;

  /**
   * Parameterized Constructor
   *
   * @param id The ID of the attachment
   * @param filename The file name to be attached
   * @param contentType The content type for attachment
   */
  public AttachmentDTO(String id, String filename, String contentType) {
    this.id = id;
    this.filename = filename;
    this.contentType = contentType;
  }

  @Override
  public String toString() {
    StringBuilder strBuf = new StringBuilder();
    strBuf.append("Attachment id is :").append(getId()).append("file name is:")
      .append(getFilename()).append("content type is :")
      .append(getContentType());
    return strBuf.toString();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final AttachmentDTO other = (AttachmentDTO) obj;
    if ((this.getId() == null) ? (other.getId() != null)
      : !this.id.equals(other.id)) {
      return false;
    }
    if ((this.getFilename() == null) ? (other.getFilename() != null)
      : !this.filename.equals(other.filename)) {
      return false;
    }
    if ((this.getContentType() == null) ? (other.getContentType() != null)
      : !this.contentType.equals(other.contentType)) {
      return false;
    }
    return Arrays.equals(this.data, other.data);
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 13 * hash + (this.getId() != null ? this.getId().hashCode() : 0);
    hash =
      13 * hash + (this.getFilename() != null ? this.getFilename().hashCode()
        : 0);
    hash = 13 * hash + (this.getContentType() != null ? this.getContentType()
      .hashCode() : 0);
    hash = 13 * hash + Arrays.hashCode(this.getData());
    return hash;
  }
}
