package com.eurodyn.qlack.fuse.aaa.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/**
 * The persistent class for the aaa_user database table.
 */
@Entity
@Table(name = "aaa_user")
@Getter
@Setter
public class User extends AAAModel {

  private static final long serialVersionUID = 1L;

  @Version
  private long dbversion;

  @Column(name = "pswd")
  private String password;

  private String salt;

  private byte status;

  private String username;

  private boolean superadmin;

  /**
   * An indicator that this user's password is not held in the database of AAA.
   */
  private boolean external = false;

  //bi-directional many-to-one association to UserHasOperation
  @OneToMany(mappedBy = "user")
  @OnDelete(action = OnDeleteAction.CASCADE)
  private List<UserHasOperation> userHasOperations;

  //bi-directional many-to-one association to Session
  @OneToMany(mappedBy = "user")
  @OnDelete(action = OnDeleteAction.CASCADE)
  private List<Session> sessions;

  //bi-directional many-to-many association to UserGroup
  @ManyToMany(mappedBy = "users")
  private List<UserGroup> userGroups;

  //bi-directional many-to-one association to UserAttribute
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  private List<UserAttribute> userAttributes;

  // bi-directional many-to-one association to VerificationToken.
  @OneToMany(mappedBy = "user")
  @OnDelete(action = OnDeleteAction.CASCADE)
  private List<VerificationToken> verificationTokens;

  public User() {
    setId(UUID.randomUUID().toString());
  }

  public UserHasOperation addUserHasOperation(UserHasOperation userHasOperations) {
    if (getUserHasOperations() == null) {
      setUserHasOperations(new ArrayList<UserHasOperation>());
    }
    getUserHasOperations().add(userHasOperations);
    userHasOperations.setUser(this);

    return userHasOperations;
  }

  public UserHasOperation removeUserHasOperation(UserHasOperation userHasOperations) {
    getUserHasOperations().remove(userHasOperations);
    userHasOperations.setUser(null);

    return userHasOperations;
  }

  public Session addSession(Session session) {
    getSessions().add(session);
    session.setUser(this);

    return session;
  }

  public Session removeSession(Session session) {
    getSessions().remove(session);
    session.setUser(null);

    return session;
  }

  public UserAttribute addUserAttribute(UserAttribute userAttribute) {
    getUserAttributes().add(userAttribute);
    userAttribute.setUser(this);

    return userAttribute;
  }

  public UserAttribute removeUserAttribute(UserAttribute userAttribute) {
    getUserAttributes().remove(userAttribute);
    userAttribute.setUser(null);

    return userAttribute;
  }

}