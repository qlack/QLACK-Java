package com.eurodyn.qlack.fuse.audit.dto;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * Superclass for the DTOs of qlack-fuse-audit module.
 *
 * @author European Dynamics SA.
 */
@Getter
@Setter
public class AuditBaseDTO implements Serializable {

  /**
   * Id
   */
  private String id;
}
