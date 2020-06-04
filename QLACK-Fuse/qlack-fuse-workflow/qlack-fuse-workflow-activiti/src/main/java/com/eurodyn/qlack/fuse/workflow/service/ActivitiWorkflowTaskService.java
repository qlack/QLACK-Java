package com.eurodyn.qlack.fuse.workflow.service;

import com.eurodyn.qlack.common.exception.QDoesNotExistException;
import com.eurodyn.qlack.fuse.workflow.dto.TaskDTO;
import lombok.RequiredArgsConstructor;
import org.activiti.engine.ActivitiObjectNotFoundException;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * {@inheritDoc}
 */
@Service
@Transactional
@RequiredArgsConstructor
public class ActivitiWorkflowTaskService implements WorkflowTaskService{

  private final TaskService taskService;

  /**
   * {@inheritDoc}
   */
  @Override
  public List<TaskDTO> getTasksByProcessInstanceId(String processInstanceId) {
    List<TaskDTO> tasks = new ArrayList<>();

    List<Task> foundTasks = taskService.createTaskQuery()
      .processInstanceId(processInstanceId)
      .includeProcessVariables().list();
    foundTasks.forEach(t -> tasks.add(
      new TaskDTO(t.getId(), t.getName(), t.getProcessInstanceId(),
        t.getProcessVariables())));

    return tasks;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void completeTask(String taskId) {
    try {
      taskService.complete(taskId);
    } catch (ActivitiObjectNotFoundException e) {
      throw new QDoesNotExistException(
        "There is no active task with id " + taskId);
    }
  }

}
