package com.eurodyn.qlack.fuse.mailing.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * Data Transfer Object for distribution lists.
 *
 * @author European Dynamics SA.
 */
@Getter
@Setter
public class DistributionListDTO extends MailBaseDTO {

	private static final long serialVersionUID = 1L;

  /**
   * Distribution list name
   */
	private String name;

  /**
   * Description of the distribution list
   */
	private String description;

  /**
   * The username of the user that created
   * the distribution list
   */
	private String createdBy;

  /**
   * The date the distribution list was created
   * represented as a {@link java.lang.Long} number
   */
  private Long createdOn;

  /**
   * The list of the contacts assigned to the distribution list
   */
  private List<ContactDTO> contacts;
}
