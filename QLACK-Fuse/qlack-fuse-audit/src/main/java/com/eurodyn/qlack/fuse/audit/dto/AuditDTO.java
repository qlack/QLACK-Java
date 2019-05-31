package com.eurodyn.qlack.fuse.audit.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.text.MessageFormat;

@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class AuditDTO extends AuditBaseDTO {

  private Long createdOn;

  private String prinSessionId;

  private String shortDescription;

  private String event;

  private String groupName;

  private String correlationId;

  private String referenceId;

  private String opt1;

  private String opt2;

  private String opt3;

  private String level;

  private AuditTraceDTO trace;

  public AuditDTO(String level, String event, String groupName,
    String description, String sessionID) {
    this.level = level;
    this.event = event;
    this.groupName = groupName;
    this.shortDescription = description;
    this.prinSessionId = sessionID;
  }

  public AuditDTO setShortDescription(String message, Object... args) {
    this.shortDescription = MessageFormat.format(message, args);
    return this;
  }

  public AuditDTO setShortDescription(String shortDescription) {
    this.shortDescription = shortDescription;
    return this;
  }
}
