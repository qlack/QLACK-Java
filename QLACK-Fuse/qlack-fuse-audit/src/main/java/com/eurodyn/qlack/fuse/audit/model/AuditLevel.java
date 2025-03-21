package com.eurodyn.qlack.fuse.audit.model;

import com.eurodyn.qlack.common.model.QlackBaseModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * The Audit Level entity, that holds all information about the level of an
 * Audit.
 *
 * @author European Dynamics SA.
 */
@Entity
@Table(name = "al_audit_level")
@Getter
@Setter
public class AuditLevel extends QlackBaseModel {

  /**
   * the name of the Audit level
   */
  private String name;

  /**
   * the description of the Audit level
   */
  private String description;

  /**
   * the id of the web session
   */

  @Column(name = "prin_session_id")
  private String prinSessionId;

  /**
   * a number representing the date the Audit level was created
   */
  @Column(name = "created_on")
  private Long createdOn;

  public AuditLevel() {
  }
}
