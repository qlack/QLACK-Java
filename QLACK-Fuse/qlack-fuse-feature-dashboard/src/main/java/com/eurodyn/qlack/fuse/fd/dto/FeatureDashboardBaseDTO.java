package com.eurodyn.qlack.fuse.fd.dto;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;


/**
 * Superclass for the DTOs of qlack-feature-dashboard module.
 *
 * @author European Dynamics SA.
 */
@Getter
@Setter
public class FeatureDashboardBaseDTO implements Serializable {

  /**
   * Id
   */
  private String id;
}
