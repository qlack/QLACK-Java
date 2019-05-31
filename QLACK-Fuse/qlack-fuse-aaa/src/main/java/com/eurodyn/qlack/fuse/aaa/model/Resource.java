package com.eurodyn.qlack.fuse.aaa.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;
import lombok.Getter;
import lombok.Setter;

/**
 * The persistent class for the aaa_resource database table.
 */
@Entity
@Table(name = "aaa_resource")
@Getter
@Setter
public class Resource extends AAAModel {

  private static final long serialVersionUID = 1L;

  @Version
  private long dbversion;

  private String name;

  private String description;

  @Column(name = "object_id")
  private String objectId;

  //bi-directional many-to-one association to UserHasOperation
  @OneToMany(mappedBy = "resource")
  private List<UserHasOperation> userHasOperations;

  //bi-directional many-to-one association to UserGroupHasOperation
  @OneToMany(mappedBy = "resource")
  private List<UserGroupHasOperation> userGroupHasOperations;

  //bi-directional many-to-one association to OpTemplateHasOperation
  @OneToMany(mappedBy = "resource")
  private List<OpTemplateHasOperation> opTemplateHasOperations;

  public Resource() {
    setId(UUID.randomUUID().toString());
  }

  public UserHasOperation addUserHasOperation(UserHasOperation userHasOperation) {
    if (getUserHasOperations() == null) {
      setUserHasOperations(new ArrayList<UserHasOperation>());
    }
    getUserHasOperations().add(userHasOperation);
    userHasOperation.setResource(this);

    return userHasOperation;
  }

  public UserHasOperation removeUserHasOperation(UserHasOperation userHasOperation) {
    getUserHasOperations().remove(userHasOperation);
    userHasOperation.setResource(null);

    return userHasOperation;
  }

  public UserGroupHasOperation addGroupHasOperation(UserGroupHasOperation userGroupHasOperation) {
    if (this.getUserGroupHasOperations() == null) {
      setUserGroupHasOperations(new ArrayList<UserGroupHasOperation>());
    }
    this.getUserGroupHasOperations().add(userGroupHasOperation);
    userGroupHasOperation.setResource(this);

    return userGroupHasOperation;
  }

  public UserGroupHasOperation removeGroupHasOperation(UserGroupHasOperation userGroupHasOperation) {
    this.getUserGroupHasOperations().remove(userGroupHasOperation);
    userGroupHasOperation.setResource(null);

    return userGroupHasOperation;
  }

  public OpTemplateHasOperation addOpTemplateHasOperation(
      OpTemplateHasOperation opTemplateHasOperation) {
    if (getOpTemplateHasOperations() == null) {
      setOpTemplateHasOperations(new ArrayList<OpTemplateHasOperation>());
    }
    getOpTemplateHasOperations().add(opTemplateHasOperation);
    opTemplateHasOperation.setResource(this);

    return opTemplateHasOperation;
  }

  public OpTemplateHasOperation removeOpTemplateHasOperation(
      OpTemplateHasOperation opTemplateHasOperation) {
    getOpTemplateHasOperations().remove(opTemplateHasOperation);
    opTemplateHasOperation.setResource(null);

    return opTemplateHasOperation;
  }

}