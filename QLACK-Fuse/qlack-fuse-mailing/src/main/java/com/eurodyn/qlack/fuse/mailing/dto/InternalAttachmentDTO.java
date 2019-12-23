package com.eurodyn.qlack.fuse.mailing.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Data Transfer Object for internal messages attachment.
 *
 * @author European Dynamics SA.
 */
@Getter
@Setter
public class InternalAttachmentDTO extends MailBaseDTO {

  private static final long serialVersionUID = 1L;

  /**
   * The id of the message the attachment is attached to
   */
  private String messagesId;

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
   * The attachment format
   */
  private String format;

}
