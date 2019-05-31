package com.eurodyn.qlack.fuse.audit.model;

import com.eurodyn.qlack.common.model.QlackBaseModel;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * The Audit entity, that holds the data of an Audit.
 *
 * @author European Dynamics SA
 */
@Entity
@Table(name = "al_audit")
@Getter
@Setter
public class Audit extends QlackBaseModel {

  /**
   * the id of the Audit level, this Audit belongs to
   */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "level_id")
  private AuditLevel levelId;

  /**
   * the id of the Audit trace of this Audit
   */
  @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JoinColumn(name = "trace_id")
  private AuditTrace trace;

  /**
   * the id of the web session‘
   */
  @Column(name = "prin_session_id")
  private String prinSessionId;

  /**
   * a short description of the Audit event
   */
  @Column(name = "short_description")
  private String shortDescription;

  /**
   * the actual Audit event
   */
  @Column(name = "event")
  private String event;

  /**
   * a number representing the date the Audit was created
   */
  @Column(name = "created_on")
  private Long createdOn;

  /**
   * the reference id of the Audit
   */
  @Column(name = "reference_id")
  private String referenceId;

  /**
   * the name of the group the Audit belongs to
   */
  @Column(name = "group_name")
  private String groupName;

  /**
   * the id used to correlate the Audit with other Audits
   */
  @Column(name = "correlation_id")
  private String correlationId;

  /**
   * optional field to store app-specific info
   */
  @Column(name = "opt1")
  private String opt1;

  /**
   * optional field to store app-specific info
   */
  @Column(name = "opt2")
  private String opt2;

  /**
   * optional field to store app-specific info
   */
  @Column(name = "opt3")
  private String opt3;

  public Audit() {
    setId(java.util.UUID.randomUUID().toString());
  }
}
