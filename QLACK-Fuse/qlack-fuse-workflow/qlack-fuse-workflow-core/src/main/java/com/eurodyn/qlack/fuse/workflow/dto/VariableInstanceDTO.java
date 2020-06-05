package com.eurodyn.qlack.fuse.workflow.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * This object class contains useful information about the workflow processes
 * tasks.
 *
 * @author European Dynamics SA
 */
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VariableInstanceDTO {

  protected String id;
  protected String name;
  protected String processInstanceId;
  protected String executionId;
  protected String taskId;

  private String type;
  private Object value;

}
