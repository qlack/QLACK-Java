package com.eurodyn.qlack.fuse.aaa.model;

import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import lombok.Getter;
import lombok.Setter;

/**
 * The persistent class for the aaa_session_attributes database table.
 *
 * @author European Dynamics SA
 */
@Entity
@Table(name = "aaa_session_attributes")
@Getter
@Setter
public class SessionAttribute extends AAAModel {

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
   * the value
   */
  private String value;

  /**
   * bi-directional many-to-one association to Session
   **/
  @ManyToOne
  @JoinColumn(name = "session_id")
  private Session session;

  public SessionAttribute() {
    setId(UUID.randomUUID().toString());
  }

}
