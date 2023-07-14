package com.eurodyn.qlack.fuse.mailing.model;

import com.eurodyn.qlack.fuse.mailing.dto.EmailDTO.EMAIL_TYPE;
import java.util.HashSet;
import java.util.Set;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Email
 *
 * @author European Dynamics SA.
 */
@Entity
@Table(name = "mai_email")
@Getter
@Setter
public class Email extends MailingModel {

  private static final long serialVersionUID = 1L;

  /**
   * The email subject
   */
  @Column(name = "subject", length = 254)
  private String subject;

  /**
   * The email body
   */
  @Column(name = "body", length = 16777215)
  @Lob
  private String body;

  /**
   * The sender
   */
  @Column(name = "from_email")
  private String fromEmail;

  /**
   * The email list of recipients
   */
  @Column(name = "to_emails", length = 1024)
  private String toEmails;

  /**
   * The email list of recipients in Carbon Copy (cc)
   */
  @Column(name = "cc_emails", length = 1024)
  private String ccEmails;

  /**
   * The email list of recipients in Blind Carbon Copy (Bcc)
   */
  @Column(name = "bcc_emails", length = 1024)
  private String bccEmails;

  /**
   * The 'Reply to' email list
   */
  @Column(name = "reply_to_emails", length = 1024)
  private String replyToEmails;

  /**
   * The email type. Can be one of {@link EMAIL_TYPE}
   */
  @Column(name = "email_type", length = 64)
  private String emailType;

  /**
   * The email status
   */
  @Column(name = "status", length = 32)
  private String status;

  /**
   * Number of tries to send the email
   */
  @Column(name = "tries", nullable = false)
  private byte tries;

  /**
   * Number of tries to send the email
   */
  @Column(name = "added_on_date", nullable = false)
  private long addedOnDate;

  /**
   * The date the email was sent
   */
  @Column(name = "date_sent")
  private Long dateSent;

  /**
   * The date that the response was sent from the server
   */
  @Column(name = "server_response_date")
  private Long serverResponseDate;

  /**
   * A server response string
   */
  @Column(name = "server_response", length = 1024)
  private String serverResponse;

  /**
   * The email charset encoding
   */
  @Column(name = "charset", length = 20)
  private String charset;

  /**
   * List of email attachments
   */
  @OneToMany(fetch = FetchType.LAZY, mappedBy = "email")
  private Set<Attachment> attachments = new HashSet<>(0);

}
