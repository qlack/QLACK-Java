package com.eurodyn.qlack.fuse.mailing.dto;

import java.util.Date;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * Data transfer object for internal messages.
 *
 * @author European Dynamics SA.
 */
@Getter
@Setter
public class InternalMessageDTO extends MailBaseDTO {

	private static final long serialVersionUID = 1L;
	private String subject;
	private String message;
	private String mailFrom;
	private String mailTo;
	private Date dateSent;
	private Date dateReceived;
	private String status;
	private String deleteType;
	private List<InternalAttachmentDTO> attachments;
	private String fwdAttachmentId;

	public void setDateSent(Long dateSent) {
		this.dateSent = new Date(dateSent);
	}

	public void setDateReceived(Long dateReceived) {
		this.dateReceived = new Date(dateReceived);
	}

}
