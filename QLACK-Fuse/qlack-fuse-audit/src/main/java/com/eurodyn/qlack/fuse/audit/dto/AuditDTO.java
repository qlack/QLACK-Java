package com.eurodyn.qlack.fuse.audit.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.text.MessageFormat;

/**
 * The Audit DTO, that holds the data of an Audit.
 *
 * @author European Dynamics SA.
 */
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class AuditDTO extends AuditBaseDTO {

  /**
   * a number representing the date the Audit was created
   */
  private Long createdOn;

  /**
   * the id of the web session
   */
  private String prinSessionId;

  /**
   * a short description of the Audit event
   */
  private String shortDescription;

  /**
   * the actual Audit event
   */
  private String event;

  /**
   * the name of the group the Audit belongs to
   */
  private String groupName;

  /**
   * the id used to correlate the Audit with other Audits
   */
  private String correlationId;

  /**
   * the reference id of the Audit
   */
  private String referenceId;

  /**
   * optional field to store app-specific info
   */
  private String opt1;

  /**
   * optional field to store app-specific info
   */
  private String opt2;

  /**
   * optional field to store app-specific info
   */
  private String opt3;

  /**
   * the id of the Audit level, this Audit belongs to
   */
  private String level;

  /**
   * the Audit trace of this Audit
   */
  private AuditTraceDTO trace;

  public AuditDTO(String level, String event, String groupName,
    String description, String sessionID) {
    this.level = level;
    this.event = event;
    this.groupName = groupName;
    this.shortDescription = description;
    this.prinSessionId = sessionID;
  }

  /**
   * Sets the short description of the Audit as a formatted <tt>String</tt>.
   * @param message a formatted <tt>String</tt>
   * @param args formatted <tt>String</tt> values
   * @return an AuditDTO object
   */
  public AuditDTO setShortDescription(String message, Object... args) {
    this.shortDescription = MessageFormat.format(message, args);
    return this;
  }

  /**
   * Sets the short description of the Audit.
   * @param shortDescription a short description
   * @return an AuditDTO object
   */
  public AuditDTO setShortDescription(String shortDescription) {
    this.shortDescription = shortDescription;
    return this;
  }
}
