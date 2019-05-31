package com.eurodyn.qlack.fuse.workflow.dto;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * This object class contains useful information about the workflow processes tasks.
 *
 * @author European Dynamics
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TaskDTO {

  //the identifier of the task
  private String id;

  //the name of the task
  private String name;

  //the id of the process instance which created the task
  private String processInstanceId;

  //the variables of the task
  private Map<String, Object> variables;

}
