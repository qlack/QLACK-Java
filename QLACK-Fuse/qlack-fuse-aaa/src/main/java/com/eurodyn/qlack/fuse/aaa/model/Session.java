package com.eurodyn.qlack.fuse.aaa.model;

import java.util.List;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;
import lombok.Getter;
import lombok.Setter;

/**
 * The persistent class for the aaa_session database table.
 */
@Entity
@Table(name = "aaa_session")
@Getter
@Setter
public class Session extends AAAModel {

  @Version
  private long dbversion;

  @Column(name = "application_session_id")
  private String applicationSessionId;

  @Column(name = "created_on")
  private long createdOn;

  @Column(name = "terminated_on")
  private Long terminatedOn;

  //bi-directional many-to-one association to User
  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  //bi-directional many-to-one association to SessionAttribute
  @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  private List<SessionAttribute> sessionAttributes;

  public Session() {
    setId(UUID.randomUUID().toString());
  }

}