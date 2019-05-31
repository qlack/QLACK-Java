package com.eurodyn.qlack.fuse.mailing.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * Data transfer object for distribution lists.
 *
 * @author European Dynamics SA.
 */
@Getter
@Setter
public class DistributionListDTO extends MailBaseDTO {

	private static final long serialVersionUID = 1L;
	private String name;
	private String description;
	private List<ContactDTO> contacts;
	private String createdBy;
	private Long createdOn;

}
