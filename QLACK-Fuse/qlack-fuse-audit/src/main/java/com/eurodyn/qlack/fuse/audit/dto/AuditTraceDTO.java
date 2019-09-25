package com.eurodyn.qlack.fuse.audit.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The Audit Trace DTO, that holds the trace of an Audit.
 *
 * @author European Dynamics SA.
 */
@Getter
@Setter
@NoArgsConstructor
public class AuditTraceDTO extends AuditBaseDTO {

  /**
   * the data of the trace
   */
  private String traceData;

  /**
   * Copy constructor
   * @param traceData the data of the Audit trace
   */
  public AuditTraceDTO(String traceData) {
    setTraceData(traceData);
  }
}
