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
@Table(name = "mai_email")
@Getter
@Setter
public class Email extends MailingModel {

	private static final long serialVersionUID = 1L;

	@Column(name = "subject", length = 254)
	private String subject;

	@Column(name = "body", length = 65535)
	@Lob
	private String body;

	@Column(name = "from_email")
	private String fromEmail;

	@Column(name = "to_emails", length = 1024)
	private String toEmails;

	@Column(name = "cc_emails", length = 1024)
	private String ccEmails;

	@Column(name = "bcc_emails", length = 1024)
	private String bccEmails;

	@Column(name = "reply_to_emails", length = 1024)
	private String replyToEmails;

	@Column(name = "email_type", length = 64)
	private String emailType;

	@Column(name = "status", length = 32)
	private String status;

	@Column(name = "tries", nullable = false)
	private byte tries;

	@Column(name = "added_on_date", nullable = false)
	private long addedOnDate;

	@Column(name = "date_sent")
	private Long dateSent;

	@Column(name = "server_response_date")
	private Long serverResponseDate;

	@Column(name = "server_response", length = 1024)
	private String serverResponse;

	@Column(name = "charset", length = 20)
	private String charset;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "email")
	private Set<Attachment> attachments = new HashSet<Attachment>(0);

}
