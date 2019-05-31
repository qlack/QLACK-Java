package com.eurodyn.qlack.fuse.workflow.service;

import com.eurodyn.qlack.common.exception.QDoesNotExistException;
import com.eurodyn.qlack.fuse.workflow.dto.TaskDTO;
import java.util.ArrayList;
import java.util.List;
import org.activiti.engine.ActivitiObjectNotFoundException;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * This service provides methods related to the tasks of Activiti.
 *
 * @author European Dynamics
 */
@Service
@Transactional
public class WorkflowTaskService {

  private final TaskService taskService;

  @Autowired
  public WorkflowTaskService(org.activiti.engine.TaskService taskService) {
    this.taskService = taskService;
  }

  /**
   * Given the id of a started process instance, a list of all the tasks are returned.
   *
   * @param processInstanceId the id of the started process instance
   * @return a list containing the information of the tasks
   */
  public List<TaskDTO> getTasksByProcessInstanceId(String processInstanceId) {
    List<TaskDTO> tasks = new ArrayList<>();

    List<Task> foundTasks = taskService.createTaskQuery().processInstanceId(processInstanceId).includeProcessVariables().list();
    foundTasks.stream().forEach(t -> tasks.add(new TaskDTO(t.getId(), t.getName(), t.getProcessInstanceId(), t.getProcessVariables())));

    return tasks;
  }

  /**
   * Given the id of a task, it completes the related task. If no task is found, an exception is thrown.
   *
   * @param taskId the id of the task
   */
  public void completeTask(String taskId) {
    try {
      taskService.complete(taskId);
    } catch (ActivitiObjectNotFoundException e) {
      throw new QDoesNotExistException("There is no active task with id " + taskId);
    }
  }

}
