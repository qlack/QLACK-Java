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

@Entity
@Table(name = "mai_internal_message")
@Getter
@Setter
public class InternalMessage extends MailingModel {

	private static final long serialVersionUID = 1L;

	@Column(name = "subject", nullable = false, length = 100)
	private String subject;

	@Column(name = "message", nullable = false, length = 65535)
	@Lob
	private String message;

	@Column(name = "mail_from", nullable = false, length = 36)
	private String mailFrom;

	@Column(name = "mail_to", nullable = false, length = 36)
	private String mailTo;

	@Column(name = "date_sent")
	private Long dateSent;

	@Column(name = "date_received")
	private Long dateReceived;

	@Column(name = "status", length = 7)
	private String status;

	@Column(name = "delete_type", nullable = false, length = 1)
	private String deleteType;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "messages")
	private Set<InternalAttachment> attachments = new HashSet<InternalAttachment>(0);

}
