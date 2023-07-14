package com.eurodyn.qlack.fuse.mailing.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * This class represents an email attachment.
 *
 * @author European Dynamics SA.
 */
@Entity
@Table(name = "mai_attachment")
@Getter
@Setter
public class Attachment extends MailingModel {

  private static final long serialVersionUID = 1L;

  /**
   * The email the attachment is attached to
   */
  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "email_id", nullable = false)
  private Email email;

  /**
   * The filename
   */
  @Column(name = "filename", nullable = false, length = 254)
  private String filename;

  /**
   * A {@link java.lang.String} representing the Content-Type of the
   * attachment
   */
  @Column(name = "content_type", nullable = false, length = 254)
  private String contentType;

  /**
   * The attachment actual data
   */
  @Lob
  @Column(name = "data", nullable = false)
  private byte[] data;

  @Column(name = "attachment_size")
  private Long attachmentSize;

}
