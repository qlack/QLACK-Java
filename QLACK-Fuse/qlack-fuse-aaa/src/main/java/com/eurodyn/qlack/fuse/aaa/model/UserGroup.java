package com.eurodyn.qlack.fuse.aaa.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/**
 * The persistent class for the aaa_user_group database table.
 *
 * @author European Dynamics SA
 */
@Entity
@Table(name = "aaa_user_group")
@Getter
@Setter
public class UserGroup extends AAAModel {

  private static final long serialVersionUID = 1L;

  /**
   * the dbversion
   */
  @Version
  private long dbversion;

  /**
   * the description
   */
  private String description;

  /**
   * the name
   */
  private String name;

  /**
   * the object id
   */
  @Column(name = "object_id")
  private String objectId;

  /**
   * the parent
   */
  @ManyToOne
  @JoinColumn(name = "parent")
  private UserGroup parent;

  /**
   * a list of @{@link UserGroup} children
   */
  @OneToMany(mappedBy = "parent")
  private List<UserGroup> children;

  /**
   * bi-directional many-to-one association to UserGroupHasOperation
   **/
  @OneToMany(mappedBy = "userGroup")
  @OnDelete(action = OnDeleteAction.CASCADE)
  private List<UserGroupHasOperation> userGroupHasOperations;

  /**
   * bi-directional many-to-many association to UserGroup
   **/
  @ManyToMany
  @JoinTable(
      name = "aaa_user_has_group",
      joinColumns = {
          @JoinColumn(name = "user_group_id")
      },
      inverseJoinColumns = {
          @JoinColumn(name = "user_id")
      })
  private List<User> users;

  public UserGroup() {
    setId(UUID.randomUUID().toString());
  }

  /**
   * Adds a {@link UserGroupHasOperation} object
   *
   * @param userGroupHasOperation a @{@link UserGroupHasOperation} object
   * @return the added {@link UserGroupHasOperation} object
   */
  public UserGroupHasOperation addGroupHasOperation(UserGroupHasOperation userGroupHasOperation) {
    if (this.getUserGroupHasOperations() == null) {
      setUserGroupHasOperations(new ArrayList<UserGroupHasOperation>());
    }
    this.getUserGroupHasOperations().add(userGroupHasOperation);
    userGroupHasOperation.setUserGroup(this);

    return userGroupHasOperation;
  }

  /**
   * Removes a {@link UserGroupHasOperation} object
   *
   * @param userGroupHasOperation the userGroupHasOperation object
   * @return the removed {@link UserGroupHasOperation} object
   */
  public UserGroupHasOperation removeGroupHasOperation(
      UserGroupHasOperation userGroupHasOperation) {
    this.getUserGroupHasOperations().remove(userGroupHasOperation);
    userGroupHasOperation.setUserGroup(null);

    return userGroupHasOperation;
  }

}