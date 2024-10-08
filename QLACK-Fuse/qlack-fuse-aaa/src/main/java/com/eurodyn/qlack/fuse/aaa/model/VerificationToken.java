package com.eurodyn.qlack.fuse.aaa.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * A verification token written
 *
 * @author European Dynamics SA
 */
@Entity
@Table(name = "aaa_verification")
@Getter
@Setter
public class VerificationToken extends AAAModel {

  private static final long serialVersionUID = 2487733740070965552L;

  /**
   * the created on date
   */
  @Column(name = "created_on")
  private long createdOn;

  /**
   * the expires on date
   */
  @Column(name = "expires_on")
  private long expiresOn;

  /**
   * the data
   */
  private String data;

  /**
   * bi-directional many-to-one association to User.
   **/
  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

}
