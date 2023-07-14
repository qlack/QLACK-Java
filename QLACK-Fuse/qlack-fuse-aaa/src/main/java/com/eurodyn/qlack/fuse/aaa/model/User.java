package com.eurodyn.qlack.fuse.aaa.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/**
 * The persistent class for the aaa_user database table.
 *
 * @author European Dynamics SA
 */
@Entity
@Table(name = "aaa_user")
@Getter
@Setter
public class User extends AAAModel {

  private static final long serialVersionUID = 1L;

  /**
   * the dbversion
   */
  @Version
  private long dbversion;

  /**
   * the password
   */
  @Column(name = "pswd")
  private String password;

  /**
   * the salt
   */
  private String salt;

  /**
   * the status
   */
  private byte status;

  /**
   * the username
   */
  private String username;

  /**
   * the superadmin
   */
  private boolean superadmin;

  /**
   * An indicator that this user's password is not held in the database of
   * AAA.
   */
  private boolean external = false;

  /**
   * bi-directional many-to-one association to UserHasOperation
   **/
  @OneToMany(mappedBy = "user")
  @OnDelete(action = OnDeleteAction.CASCADE)
  private List<UserHasOperation> userHasOperations;

  /**
   * bi-directional many-to-one association to Session
   **/
  @OneToMany(mappedBy = "user")
  @OnDelete(action = OnDeleteAction.CASCADE)
  private List<Session> sessions;

  /**
   * bi-directional many-to-many association to UserGroup
   **/
  @ManyToMany(mappedBy = "users")
  private List<UserGroup> userGroups;

  /**
   * bi-directional many-to-one association to UserAttribute
   **/
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  private List<UserAttribute> userAttributes;

  /**
   * bi-directional many-to-one association to VerificationToken.
   **/
  @OneToMany(mappedBy = "user")
  @OnDelete(action = OnDeleteAction.CASCADE)
  private List<VerificationToken> verificationTokens;

  public User() {
    setId(UUID.randomUUID().toString());
  }

  /**
   * a method that adds a {@link UserHasOperation} object
   *
   * @param userHasOperations a {@link UserHasOperation} object
   * @return a userHasOperation object
   */
  public UserHasOperation addUserHasOperation(
    UserHasOperation userHasOperations) {
    if (getUserHasOperations() == null) {
      setUserHasOperations(new ArrayList<UserHasOperation>());
    }
    getUserHasOperations().add(userHasOperations);
    userHasOperations.setUser(this);

    return userHasOperations;
  }

  /**
   * A method that removes a {@link UserHasOperation} object
   *
   * @param userHasOperations a {@link UserHasOperation} object
   * @return a {@link UserHasOperation} object
   */
  public UserHasOperation removeUserHasOperation(
    UserHasOperation userHasOperations) {
    getUserHasOperations().remove(userHasOperations);
    userHasOperations.setUser(null);

    return userHasOperations;
  }

  /**
   * Adds a session
   *
   * @param session the session object
   * @return the added session
   */
  public Session addSession(Session session) {
    getSessions().add(session);
    session.setUser(this);

    return session;
  }

  /**
   * Removes a session
   *
   * @param session the session
   * @return the removed session
   */
  public Session removeSession(Session session) {
    getSessions().remove(session);
    session.setUser(null);

    return session;
  }

  /**
   * Adds a userAttribute
   *
   * @param userAttribute the userAttribute
   * @return an added userAttribute
   */
  public UserAttribute addUserAttribute(UserAttribute userAttribute) {
    getUserAttributes().add(userAttribute);
    userAttribute.setUser(this);

    return userAttribute;
  }

  /**
   * Removes the userAttribute
   *
   * @param userAttribute the userAttribute object
   * @return the removed userAttribute
   */
  public UserAttribute removeUserAttribute(UserAttribute userAttribute) {
    getUserAttributes().remove(userAttribute);
    userAttribute.setUser(null);

    return userAttribute;
  }

}
