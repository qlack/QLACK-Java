package com.eurodyn.qlack.fuse.aaa.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;
import lombok.Getter;
import lombok.Setter;

/**
 * The persistent class for the aaa_operation database table.
 */
@Entity
@Table(name = "aaa_operation")
@Getter
@Setter
public class Operation extends AAAModel {

  private static final long serialVersionUID = 1L;

  @Version
  private long dbversion;

  private String name;

  private String description;

  private boolean dynamic;

  @Lob
  @Column(name = "dynamic_code")
  private String dynamicCode;

  //bi-directional many-to-one association to UserGroupHasOperation
  @OneToMany(mappedBy = "operation")
  private List<UserGroupHasOperation> userGroupHasOperations;

  //bi-directional many-to-one association to OpTemplateHasOperation
  @OneToMany(mappedBy = "operation")
  private List<OpTemplateHasOperation> opTemplateHasOperations;

  //bi-directional many-to-one association to UserHasOperation
  @OneToMany(mappedBy = "operation")
  private List<UserHasOperation> userHasOperations;

  public Operation() {
    setId(UUID.randomUUID().toString());
  }

  public UserGroupHasOperation addGroupHasOperation(UserGroupHasOperation userGroupHasOperation) {
    if (this.getUserGroupHasOperations() == null) {
      setUserGroupHasOperations(new ArrayList<UserGroupHasOperation>());
    }
    this.getUserGroupHasOperations().add(userGroupHasOperation);
    userGroupHasOperation.setOperation(this);

    return userGroupHasOperation;
  }

  public UserGroupHasOperation removeGroupHasOperation(UserGroupHasOperation userGroupHasOperation) {
    this.getUserGroupHasOperations().remove(userGroupHasOperation);
    userGroupHasOperation.setOperation(null);

    return userGroupHasOperation;
  }

  public OpTemplateHasOperation addOpTemplateHasOperation(
      OpTemplateHasOperation opTemplateHasOperation) {
    if (getOpTemplateHasOperations() == null) {
      setOpTemplateHasOperations(new ArrayList<>());
    }
    getOpTemplateHasOperations().add(opTemplateHasOperation);
    opTemplateHasOperation.setOperation(this);

    return opTemplateHasOperation;
  }

  public OpTemplateHasOperation removeOpTemplateHasOperation(
      OpTemplateHasOperation opTemplateHasOperation) {
    getOpTemplateHasOperations().remove(opTemplateHasOperation);
    opTemplateHasOperation.setOperation(null);

    return opTemplateHasOperation;
  }

  public UserHasOperation addUserHasOperation(UserHasOperation userHasOperation) {
    getUserHasOperations().add(userHasOperation);
    userHasOperation.setOperation(this);

    return userHasOperation;
  }

  public UserHasOperation removeUserHasOperation(UserHasOperation userHasOperation) {
    getUserHasOperations().remove(userHasOperation);
    userHasOperation.setOperation(null);

    return userHasOperation;
  }

}