package com.eurodyn.qlack.fuse.aaa.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "aaa_verification")
@Getter
@Setter
public class VerificationToken extends AAAModel {

  private static final long serialVersionUID = 2487733740070965552L;

  @Column(name = "created_on")
  private long createdOn;

  @Column(name = "expires_on")
  private long expiresOn;

  private String data;

  // bi-directional many-to-one association to User.
  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

}
