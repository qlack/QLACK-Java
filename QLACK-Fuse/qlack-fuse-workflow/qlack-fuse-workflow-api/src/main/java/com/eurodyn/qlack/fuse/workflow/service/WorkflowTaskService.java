package com.eurodyn.qlack.fuse.workflow.service;

import com.eurodyn.qlack.fuse.workflow.dto.TaskDTO;

import java.util.List;

/**
 * This service provides methods related to tasks.
 *
 * @author European Dynamics
 */
public interface WorkflowTaskService {

  /**
   * Given the id of a started process instance, a list of all the tasks are
   * returned.
   *
   * @param processInstanceId the id of the started process instance
   * @return a list containing the information of the tasks
   */
  List<TaskDTO> getTasksByProcessInstanceId(String processInstanceId);

  /**
   * Given the id of a task, it completes the related task.
   *
   * @param taskId the id of the task
   */
  void completeTask(String taskId);

}
