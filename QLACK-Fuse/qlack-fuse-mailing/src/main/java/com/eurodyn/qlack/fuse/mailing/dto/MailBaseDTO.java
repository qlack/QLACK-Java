package com.eurodyn.qlack.fuse.mailing.dto;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MailBaseDTO extends AttributeDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;

	/**
	 * This user id is used to identify the user calling an action in order to use
	 * it when generating notification messages or when this information should be
	 * stored along with the relevant item in the db. Please note that it should
	 * *not* be used for security checks.
	 */
	private String srcUserId;

}
