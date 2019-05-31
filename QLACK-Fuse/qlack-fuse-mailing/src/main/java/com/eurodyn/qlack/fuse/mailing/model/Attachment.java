package com.eurodyn.qlack.fuse.mailing.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "mai_attachment")
@Getter
@Setter
public class Attachment extends MailingModel {

	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "email_id", nullable = false)
	private Email email;

	@Column(name = "filename", nullable = false, length = 254)
	private String filename;

	@Column(name = "content_type", nullable = false, length = 254)
	private String contentType;

	@Lob
	@Column(name = "data", nullable = false)
	private byte[] data;

	@Column(name = "attachment_size")
	private Long attachmentSize;

}
