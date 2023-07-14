package com.eurodyn.qlack.fuse.aaa.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.Setter;

/**
 * The persistent class for the aaa_resource database table.
 *
 * @author European Dynamics SA
 */
@Entity
@Table(name = "aaa_resource")
@Getter
@Setter
public class Resource extends AAAModel {

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
   * the object id
   */
  @Column(name = "object_id")
  private String objectId;

  /**
   * bi-directional many-to-one association to UserHasOperation
   **/
  @OneToMany(mappedBy = "resource")
  private List<UserHasOperation> userHasOperations;

  /**
   * bi-directional many-to-one association to UserGroupHasOperation
   **/
  @OneToMany(mappedBy = "resource")
  private List<UserGroupHasOperation> userGroupHasOperations;

  /**
   * bi-directional many-to-one association to OpTemplateHasOperation
   **/
  @OneToMany(mappedBy = "resource")
  private List<OpTemplateHasOperation> opTemplateHasOperations;

  public Resource() {
    setId(UUID.randomUUID().toString());
  }

  /**
   * A method that adds a {@link UserHasOperation} object
   *
   * @param userHasOperation a {@link UserHasOperation} type Object
   * @return a userHasOperation object
   */
  public UserHasOperation addUserHasOperation(
    UserHasOperation userHasOperation) {
    if (getUserHasOperations() == null) {
      setUserHasOperations(new ArrayList<UserHasOperation>());
    }
    getUserHasOperations().add(userHasOperation);
    userHasOperation.setResource(this);

    return userHasOperation;
  }

  /**
   * A method that removes a {@link UserHasOperation} object
   *
   * @param userHasOperation a userHasOperation object
   * @return a @{@link UserHasOperation} object
   */
  public UserHasOperation removeUserHasOperation(
    UserHasOperation userHasOperation) {
    getUserHasOperations().remove(userHasOperation);
    userHasOperation.setResource(null);

    return userHasOperation;
  }

  /**
   * A method that adds a {@link UserGroupHasOperation} object
   *
   * @param userGroupHasOperation the {@link UserGroupHasOperation} object
   * @return the {@link UserGroupHasOperation} object
   */
  public UserGroupHasOperation addGroupHasOperation(
    UserGroupHasOperation userGroupHasOperation) {
    if (this.getUserGroupHasOperations() == null) {
      setUserGroupHasOperations(new ArrayList<UserGroupHasOperation>());
    }
    this.getUserGroupHasOperations().add(userGroupHasOperation);
    userGroupHasOperation.setResource(this);

    return userGroupHasOperation;
  }

  /**
   * A method that removes a {@link UserGroupHasOperation} object
   *
   * @param userGroupHasOperation the {@link UserGroupHasOperation} object
   * @return a {@link UserGroupHasOperation} object
   */
  public UserGroupHasOperation removeGroupHasOperation(
    UserGroupHasOperation userGroupHasOperation) {
    this.getUserGroupHasOperations().remove(userGroupHasOperation);
    userGroupHasOperation.setResource(null);

    return userGroupHasOperation;
  }

  /**
   * A method that adds a {@link OpTemplateHasOperation} object
   *
   * @param opTemplateHasOperation a OpTemplateHasOperation object
   * @return the {@link OpTemplateHasOperation} object
   */
  public OpTemplateHasOperation addOpTemplateHasOperation(
    OpTemplateHasOperation opTemplateHasOperation) {
    if (getOpTemplateHasOperations() == null) {
      setOpTemplateHasOperations(new ArrayList<OpTemplateHasOperation>());
    }
    getOpTemplateHasOperations().add(opTemplateHasOperation);
    opTemplateHasOperation.setResource(this);

    return opTemplateHasOperation;
  }

  /**
   * A method that removes a {@link OpTemplateHasOperation} object
   *
   * @param opTemplateHasOperation a OpTemplateHasOperation object
   * @return a {@link OpTemplateHasOperation} object
   */
  public OpTemplateHasOperation removeOpTemplateHasOperation(
    OpTemplateHasOperation opTemplateHasOperation) {
    getOpTemplateHasOperations().remove(opTemplateHasOperation);
    opTemplateHasOperation.setResource(null);

    return opTemplateHasOperation;
  }

}
