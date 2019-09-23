package com.eurodyn.qlack.fuse.mailing.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * Internal Message. It is an email for internal recipients only.
 *
 * @author European Dynamics SA.
 */
@Entity
@Table(name = "mai_internal_message")
@Getter
@Setter
public class InternalMessage extends MailingModel {

	private static final long serialVersionUID = 1L;

  /**
   * The message subject
   */
	@Column(name = "subject", nullable = false, length = 100)
	private String subject;

  /**
   * The message body
   */
	@Column(name = "message", nullable = false, length = 65535)
	@Lob
	private String message;

  /**
   * The sender
   */
	@Column(name = "mail_from", nullable = false, length = 36)
	private String mailFrom;

  /**
   * The message recipient
   */
	@Column(name = "mail_to", nullable = false, length = 36)
	private String mailTo;

  /**
   * The date the message was sent
   */
	@Column(name = "date_sent")
	private Long dateSent;

  /**
   * The date the message was received
   */
	@Column(name = "date_received")
	private Long dateReceived;

  /**
   * Message status
   */
	@Column(name = "status", length = 7)
	private String status;

  /**
   * Message delete type
   */
	@Column(name = "delete_type", nullable = false, length = 1)
	private String deleteType;

	/**
   * List of message attachments
   */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "messages")
	private Set<InternalAttachment> attachments = new HashSet<>(0);

}
