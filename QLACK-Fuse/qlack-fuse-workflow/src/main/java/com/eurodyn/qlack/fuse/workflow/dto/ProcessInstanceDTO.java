package com.eurodyn.qlack.fuse.workflow.dto;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * This object class contains useful information about the workflow processes instances.
 *
 * @author European Dynamics
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProcessInstanceDTO {

  //the identifier of the task
  private String id;

  //defines if the instance is suspended
  private boolean suspended;

  //the variables of the process
  private Map<String, Object> variables;

}
