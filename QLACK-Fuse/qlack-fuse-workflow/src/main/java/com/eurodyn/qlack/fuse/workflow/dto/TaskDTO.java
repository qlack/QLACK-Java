package com.eurodyn.qlack.fuse.workflow.dto;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * This object class contains useful information about the workflow processes tasks.
 *
 * @author European Dynamics SA
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskDTO {

  /**
   * the id of the task
   */
  private String id;

  /**
   * the name of the task
   */
  private String name;

  /**
   * the id of the process instance which created the task
   **/
  private String processInstanceId;

  /**
   * the variables of the task
   **/
  private Map<String, Object> variables;

}
