package com.eurodyn.qlack.fuse.mailing.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * This class represents an internal attachment. An internal attachment is an attachment for
 * internal messages only.
 *
 * @author European Dynamics SA.
 */
@Entity
@Table(name = "mai_internal_attachment")
@Getter
@Setter
public class InternalAttachment extends MailingModel {

  private static final long serialVersionUID = 1L;

  /**
   * The message the attachment is attached to
   */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "messages_id", nullable = false)
  private InternalMessage messages;

  /**
   * The attachment filename
   */
  @Column(name = "filename", length = 254)
  private String filename;

  /**
   * A {@link java.lang.String} representing the Content-Type of the attachment
   */
  @Column(name = "content_type", length = 254)
  private String contentType;

  /**
   * The attachment actual data
   */
  @Column(name = "data")
  private byte[] data;

  /**
   * The attachment format
   */
  @Column(name = "format", length = 45)
  private String format;

}
