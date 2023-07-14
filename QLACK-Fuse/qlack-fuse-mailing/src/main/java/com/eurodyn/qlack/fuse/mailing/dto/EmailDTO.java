package com.eurodyn.qlack.fuse.mailing.dto;

import com.eurodyn.qlack.fuse.mailing.util.EmailCharset;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Data Transfer Object for Email data.
 *
 * @author European Dynamics SA.
 */
@Getter
@Setter
@ToString
public class EmailDTO implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * Id
   */
  private String id;

  /**
   * The email subject
   */
  private String subject;

  /**
   * The email body
   */
  private String body;

  /**
   * The sender
   */
  private @NotBlank String fromEmail;

  /**
   * The email list of recipients
   */
  private List<String> toEmails;

  /**
   * The email list of recipients in Carbon Copy (cc)
   */
  private List<String> ccEmails;

  /**
   * The email list of recipients in Blind Carbon Copy (Bcc)
   */
  private List<String> bccEmails;

  /**
   * The 'Reply to' email list
   */
  private List<String> replyToEmails;

  /**
   * The {@link EMAIL_TYPE} email type
   */
  private @NotNull EMAIL_TYPE emailType;

  /**
   * The email status
   */
  private String status;

  /**
   * List of email attachments
   */
  private List<AttachmentDTO> attachments;

  /**
   * The date the email was sent
   */
  private Date dateSent;

  /**
   * A server response string
   */
  private String serverResponse;

  /**
   * The date that the response was sent from the server
   */
  private Date serverResponseDate;
  private String charset = EmailCharset.UTF_8.getValue();

  public EmailDTO() {
    this.emailType = EMAIL_TYPE.TEXT;
  }

  public void setToContact(List<String> toEmails) {
    this.toEmails = toEmails;
  }

  public void setToContact(String toContact) {
    List<String> l = new ArrayList<>();
    l.add(toContact);
    setToContact(l);
  }

  public void addAttachment(AttachmentDTO attachmentDTO) {
    if (attachments == null) {
      attachments = new ArrayList<>();
    }
    attachments.add(attachmentDTO);
  }

  public void setDateSent(Long dateSent) {
    if (dateSent != null) {
      this.dateSent = new Date(dateSent);
    }
  }

  public void resetAllRecipients() {
    this.toEmails = null;
    this.ccEmails = null;
    this.bccEmails = null;
  }

  public enum EMAIL_TYPE {
    TEXT, HTML
  }
}
