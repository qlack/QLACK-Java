package com.eurodyn.qlack.fuse.workflow.dto;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * This object class contains useful information about the workflow processes
 * instances.
 *
 * @author European Dynamics SA
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProcessInstanceDTO {


  /**
   * the id
   */
  private String id;

  /**
   * defines if the instance is suspended
   */
  private boolean suspended;

  /**
   * the variables of the process
   **/
  private Map<String, Object> variables;

}
