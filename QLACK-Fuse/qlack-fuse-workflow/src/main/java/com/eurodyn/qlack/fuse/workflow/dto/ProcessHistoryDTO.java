package com.eurodyn.qlack.fuse.workflow.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * This object class contains useful information about the history of workflow processes.
 *
 * @author European Dynamics
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProcessHistoryDTO {

  private String id;

  private String deploymentId;

  private Integer version;

  private String deleteReason;

  private String name;

  private String description;

  private byte[] data;
}
