package com.eurodyn.qlack.fuse.aaa.model;

import java.io.Serializable;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import lombok.Getter;
import lombok.Setter;

/**
 * The persistent class for the aaa_group_has_operation database table.
 */
@Entity
@Table(name = "aaa_user_has_operation")
@Getter
@Setter
public class UserHasOperation extends AAAModel {

  private static final long serialVersionUID = 1L;

  @Version
  private long dbversion;

  //bi-directional many-to-one association to UserGroup
  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  //bi-directional many-to-one association to Operation
  @ManyToOne
  @JoinColumn(name = "operation")
  private Operation operation;

  //bi-directional many-to-one association to Resource
  @ManyToOne
  @JoinColumn(name = "resource_id")
  private Resource resource;

  private boolean deny;

  public UserHasOperation() {
    setId(UUID.randomUUID().toString());
  }

}