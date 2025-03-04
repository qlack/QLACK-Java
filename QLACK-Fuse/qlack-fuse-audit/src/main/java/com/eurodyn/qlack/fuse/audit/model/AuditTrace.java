package com.eurodyn.qlack.fuse.audit.model;

import com.eurodyn.qlack.common.model.QlackBaseModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * The Audit Trace entity, that holds the trace of an Audit.
 *
 * @author European Dynamics SA.
 */
@Entity
@Table(name = "al_audit_trace")
@Getter
@Setter
public class AuditTrace extends QlackBaseModel {

  /**
   * the data of the trace
   */
  @Column(name = "trace_data")
  private String traceData;

  /**
   * the Audit, which is referenced by this trace
   */
  @OneToOne(mappedBy = "trace")
  private Audit audit;

  public AuditTrace() {
  }
}
