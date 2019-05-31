package com.eurodyn.qlack.fuse.mailing.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Data transfer object for internal messages attachments.
 *
 * @author European Dynamics SA.
 */
@Getter
@Setter
public class InternalAttachmentDTO extends MailBaseDTO {

	private static final long serialVersionUID = 1L;
	private String messagesId;
	private String filename;
	private String contentType;
	private byte[] data;
	private String format;

}
