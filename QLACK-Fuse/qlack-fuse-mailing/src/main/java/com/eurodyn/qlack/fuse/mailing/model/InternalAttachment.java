package com.eurodyn.qlack.fuse.mailing.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "mai_internal_attachment")
@Getter
@Setter
public class InternalAttachment extends MailingModel {

	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "messages_id", nullable = false)
	private InternalMessage messages;

	@Column(name = "filename", length = 254)
	private String filename;

	@Column(name = "content_type", length = 254)
	private String contentType;

	@Column(name = "data")
	private byte[] data;

	@Column(name = "format", length = 45)
	private String format;

}
