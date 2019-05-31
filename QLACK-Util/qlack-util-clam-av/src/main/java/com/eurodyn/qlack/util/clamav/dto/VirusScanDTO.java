package com.eurodyn.qlack.util.clamav.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Holds the necessary data to
 * perform a file virus scan
 * and return its result
 *
 * @author European Dynamics
 */
@Getter
@Setter
public class VirusScanDTO {

  /**
   * Flag to indicate if the file is virus free
   */
  private boolean virusFree;
  /**
   * Result of the virus scan
   */
  private String virusScanDescription;
}
