package com.eurodyn.qlack.fuse.audit.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AuditTraceDTO extends AuditBaseDTO {

  private String traceData;

  public AuditTraceDTO(String traceData) {
    setTraceData(traceData);
  }
}
