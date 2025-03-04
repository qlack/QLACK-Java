package com.eurodyn.qlack.fuse.aaa.model;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.Setter;

/**
 * The persistent class for the aaa_operation database table.
 *
 * @author European Dynamics SA
 */
@Entity
@Table(name = "aaa_operation")
@Getter
@Setter
public class Operation extends AAAModel {

  private static final long serialVersionUID = 1L;

  /**
   * the dbversion
   */
  @Version
  private long dbversion;

  /**
   * the name
   */
  private String name;

  /**
   * the description
   */
  private String description;

  /**
   * the dynamic
   */
  private boolean dynamic;

  /**
   * the dynamicCode
   */
  @Lob
  @Column(name = "dynamic_code")
  private String dynamicCode;

  /**
   * bi-directional many-to-one association to UserGroupHasOperation
   **/
  @OneToMany(mappedBy = "operation")
  private List<UserGroupHasOperation> userGroupHasOperations;

  /**
   * bi-directional many-to-one association to OpTemplateHasOperation
   **/
  @OneToMany(mappedBy = "operation")
  private List<OpTemplateHasOperation> opTemplateHasOperations;

  /**
   * bi-directional many-to-one association to UserHasOperation
   **/
  @OneToMany(mappedBy = "operation")
  private List<UserHasOperation> userHasOperations;

  public Operation() {
  }

  /**
   * A method that adds a userGroupHasOperation object
   *
   * @param userGroupHasOperation a @{@link UserGroupHasOperation} object
   * @return a @{@link UserGroupHasOperation} object
   */
  public UserGroupHasOperation addGroupHasOperation(
    UserGroupHasOperation userGroupHasOperation) {
    if (this.getUserGroupHasOperations() == null) {
      setUserGroupHasOperations(new ArrayList<UserGroupHasOperation>());
    }
    this.getUserGroupHasOperations().add(userGroupHasOperation);
    userGroupHasOperation.setOperation(this);

    return userGroupHasOperation;
  }

  /**
   * A method that removes a userGroupHasOperation object
   *
   * @param userGroupHasOperation a @{@link UserGroupHasOperation} object
   * @return the @{@link UserGroupHasOperation} object
   */
  public UserGroupHasOperation removeGroupHasOperation(
    UserGroupHasOperation userGroupHasOperation) {
    this.getUserGroupHasOperations().remove(userGroupHasOperation);
    userGroupHasOperation.setOperation(null);

    return userGroupHasOperation;
  }

  /**
   * A method that adds a opTemplateHasOperation object
   *
   * @param opTemplateHasOperation a @{@link OpTemplateHasOperation} object
   * @return a {@link OpTemplateHasOperation} object
   */
  public OpTemplateHasOperation addOpTemplateHasOperation(
    OpTemplateHasOperation opTemplateHasOperation) {
    if (getOpTemplateHasOperations() == null) {
      setOpTemplateHasOperations(new ArrayList<>());
    }
    getOpTemplateHasOperations().add(opTemplateHasOperation);
    opTemplateHasOperation.setOperation(this);

    return opTemplateHasOperation;
  }

  /**
   * A method that removes a {@link OpTemplateHasOperation} object
   *
   * @param opTemplateHasOperation a {@link OpTemplateHasOperation} object
   * @return a {@link OpTemplateHasOperation} object
   */
  public OpTemplateHasOperation removeOpTemplateHasOperation(
    OpTemplateHasOperation opTemplateHasOperation) {
    getOpTemplateHasOperations().remove(opTemplateHasOperation);
    opTemplateHasOperation.setOperation(null);

    return opTemplateHasOperation;
  }

  /**
   * A method that adds a userHasOperation object
   *
   * @param userHasOperation the @{@link UserHasOperation} object
   * @return the {@link UserHasOperation} object
   */
  public UserHasOperation addUserHasOperation(
    UserHasOperation userHasOperation) {
    getUserHasOperations().add(userHasOperation);
    userHasOperation.setOperation(this);

    return userHasOperation;
  }

  /**
   * A method that removes a userHasOperation object
   *
   * @param userHasOperation the @{@link UserHasOperation} object
   * @return a {@link UserHasOperation} object
   */
  public UserHasOperation removeUserHasOperation(
    UserHasOperation userHasOperation) {
    getUserHasOperations().remove(userHasOperation);
    userHasOperation.setOperation(null);

    return userHasOperation;
  }

}
