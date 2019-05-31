package com.eurodyn.qlack.fuse.aaa.model;

import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import lombok.Getter;
import lombok.Setter;

/**
 * The persistent class for the aaa_user_attributes database table.
 */
@Entity
@Table(name = "aaa_user_attributes")
@Getter
@Setter
public class UserAttribute extends AAAModel {

  private static final long serialVersionUID = 1L;

  @Version
  private long dbversion;

  private byte[] bindata;

  @Column(name = "content_type")
  private String contentType;

  private String data;

  private String name;

  //bi-directional many-to-one association to User
  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  public UserAttribute() {
    setId(UUID.randomUUID().toString());
  }

}