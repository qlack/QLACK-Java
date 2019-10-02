package com.eurodyn.qlack.fuse.workflow.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * This object class contains useful information about the history of workflow processes.
 *
 * @author European Dynamics SA
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProcessHistoryDTO {

  /**
   * the id of the process history
   */
  private String id;

  /**
   * the deployment Id
   */
  private String deploymentId;

  /**
   * the version
   */
  private Integer version;

  /**
   * the deletion reason
   */
  private String deleteReason;

  /**
   * the name
   */
  private String name;

  /**
   * the description
   */
  private String description;

  /**
   * the data
   */
  private byte[] data;
}
