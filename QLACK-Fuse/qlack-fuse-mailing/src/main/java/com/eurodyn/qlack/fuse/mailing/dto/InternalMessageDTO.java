package com.eurodyn.qlack.fuse.mailing.dto;

import java.util.Date;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * Data Transfer Object for internal message.
 *
 * @author European Dynamics SA.
 */
@Getter
@Setter
public class InternalMessageDTO extends MailBaseDTO {

  private static final long serialVersionUID = 1L;

  /**
   * The message subject
   */
  private String subject;

  /**
   * The message body
   */
  private String message;

  /**
   * The sender
   */
  private String mailFrom;

  /**
   * The message recipient
   */
  private String mailTo;

  /**
   * The date the message was sent
   */
  private Date dateSent;

  /**
   * The date the message was received
   */
  private Date dateReceived;

  /**
   * Message status
   */
  private String status;

  /**
   * Message delete type
   */
  private String deleteType;

  /**
   * List of message attachments
   */
  private List<InternalAttachmentDTO> attachments;

  /**
   * Forwarded Attachment Id
   */
  private String fwdAttachmentId;

  /**
   * Converts and sets the sent date
   *
   * @param dateSent the date in format of milliseconds since January 1, 1970, 00:00:00 GMT
   */
  public void setDateSent(Long dateSent) {
    this.dateSent = new Date(dateSent);
  }

  /**
   * Converts and sets the received date
   *
   * @param dateReceived the date in format of milliseconds since January 1, 1970, 00:00:00 GMT
   */
  public void setDateReceived(Long dateReceived) {
    this.dateReceived = new Date(dateReceived);
  }

}
