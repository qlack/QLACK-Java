package com.eurodyn.qlack.fuse.workflow.service;

import com.eurodyn.qlack.fuse.workflow.dto.TaskDTO;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.task.Task;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * {@inheritDoc}
 */
@Service
@Transactional
@RequiredArgsConstructor
public class CamundaWorkflowTaskService implements WorkflowTaskService {

  private final TaskService taskService;

  /**
   * {@inheritDoc}
   */
  @Override
  public List<TaskDTO> getTasksByProcessInstanceId(String processInstanceId) {
    Function<Task, TaskDTO> task2TaskDTO = te ->
        new TaskDTO(te.getId(), te.getName(), te.getProcessInstanceId(),
            taskService.getVariables(te.getId()).entrySet().stream()
                .collect(Collectors.toMap(Entry::getKey, Entry::getValue)));

    List<Task> tasks =
        taskService.createTaskQuery().processInstanceId(processInstanceId).list();

    return tasks.stream()
        .map(task2TaskDTO)
        .collect(Collectors.toList());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void completeTask(String taskId) {
    taskService.complete(taskId);
  }

}
