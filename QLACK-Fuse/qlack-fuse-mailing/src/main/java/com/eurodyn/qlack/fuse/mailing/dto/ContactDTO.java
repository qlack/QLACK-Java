package com.eurodyn.qlack.fuse.mailing.dto;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * Data Transfer Object for Contact.
 *
 * @author European Dynamics SA.
 */
@Getter
@Setter
public class ContactDTO implements Serializable {

  private static final long serialVersionUID = 1L;
  /**
   * Id
   */
  private String id;

  /**
   * The id of the corresponding user
   */
  private String userID;

  /**
   * The contact first name
   */
  private String firstName;

  /**
   * The contact last name
   */
  private String lastName;

  /**
   * Email
   */
  private String email;

  /**
   * The contact default locale
   */
  private String locale;

}
