package com.eurodyn.qlack.fuse.aaa.model;

import java.util.List;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.Setter;

/**
 * The persistent class for the aaa_session database table.
 *
 * @author European Dynamics SA
 */
@Entity
@Table(name = "aaa_session")
@Getter
@Setter
public class Session extends AAAModel {

  /**
   * the dbversion
   */
  @Version
  private long dbversion;

  /**
   * the application session Id
   */
  @Column(name = "application_session_id")
  private String applicationSessionId;

  /**
   * the created on date
   */
  @Column(name = "created_on")
  private long createdOn;

  /**
   * the terminated on date
   */
  @Column(name = "terminated_on")
  private Long terminatedOn;

  /**
   * bi-directional many-to-one association to User
   **/
  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  /**
   * bi-directional many-to-one association to SessionAttribute
   **/
  @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  private List<SessionAttribute> sessionAttributes;

  public Session() {
  }

}
