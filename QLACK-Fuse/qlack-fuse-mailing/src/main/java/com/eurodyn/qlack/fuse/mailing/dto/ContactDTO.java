package com.eurodyn.qlack.fuse.mailing.dto;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * Data transfer object for contacts.
 *
 * @author European Dynamics SA.
 */
@Getter
@Setter
public class ContactDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	private String id;
	private String userID;
	private String firstName;
	private String lastName;
	private String email;
	private String locale;

}
