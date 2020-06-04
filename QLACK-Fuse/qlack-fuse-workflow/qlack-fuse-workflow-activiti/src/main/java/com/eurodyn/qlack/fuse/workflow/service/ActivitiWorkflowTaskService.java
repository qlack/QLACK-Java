package com.eurodyn.qlack.fuse.workflow.service;

import com.eurodyn.qlack.fuse.workflow.dto.TaskDTO;
import lombok.RequiredArgsConstructor;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * {@inheritDoc}
 */
@Service
@Transactional
@RequiredArgsConstructor
public class ActivitiWorkflowTaskService implements WorkflowTaskService {

  private final TaskService taskService;

  /**
   * {@inheritDoc}
   */
  @Override
  public List<TaskDTO> getTasksByProcessInstanceId(String processInstanceId) {
    Function<Task, TaskDTO> task2TaskDTO = t ->
        new TaskDTO(t.getId(), t.getName(), t.getProcessInstanceId(),
            t.getProcessVariables());

    List<Task> foundTasks = taskService.createTaskQuery()
        .processInstanceId(processInstanceId)
        .includeProcessVariables().list();

    return foundTasks.stream().map(task2TaskDTO).collect(Collectors.toList());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void completeTask(String taskId) {
    taskService.complete(taskId);
  }

}
