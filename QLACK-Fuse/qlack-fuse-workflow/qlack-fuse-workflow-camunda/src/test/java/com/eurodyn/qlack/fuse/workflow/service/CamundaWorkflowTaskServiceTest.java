package com.eurodyn.qlack.fuse.workflow.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.impl.persistence.entity.TaskEntity;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.task.TaskQuery;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RunWith(SpringRunner.class)
public class CamundaWorkflowTaskServiceTest {

  @InjectMocks
  private CamundaWorkflowTaskService workflowTaskService;
  @Mock
  private TaskService taskService;
  @Mock
  private TaskQuery taskQuery;
  @Mock
  private TaskEntity task;

  @Test
  public void getTasksByProcessInstanceIdNonEmpty() {
    String processInstanceId = "processInstanceId";
    when(taskService.createTaskQuery()).thenReturn(taskQuery);
    when(taskService.getVariables(anyString())).thenReturn(Collections.singletonMap("key", "value"));
    when(taskQuery.processInstanceId(processInstanceId)).thenReturn(taskQuery);

    List<Task> tasks = new ArrayList<>();
    tasks.add(task);
    when(task.getId()).thenReturn("id");
    when(taskQuery.list()).thenReturn(tasks);

    assertFalse(workflowTaskService.getTasksByProcessInstanceId(processInstanceId).isEmpty());
    verify(taskService, times(1)).createTaskQuery();
    verify(taskQuery, times(1)).processInstanceId(processInstanceId);
    verify(taskService, times(1)).getVariables(anyString());
  }

  @Test
  public void getTasksByProcessInstanceIdEmpty() {
    String processInstanceId = "processInstanceId";
    when(taskService.createTaskQuery()).thenReturn(taskQuery);
    when(taskQuery.processInstanceId(processInstanceId)).thenReturn(taskQuery);
    when(taskQuery.list()).thenReturn(new ArrayList<>());

    assertTrue(workflowTaskService.getTasksByProcessInstanceId(processInstanceId).isEmpty());
    verify(taskService, times(1)).createTaskQuery();
    verify(taskQuery, times(1)).processInstanceId(processInstanceId);
    verify(taskService, never()).getVariables(anyString());
  }

  @Test
  public void completeTaskTask() {
    String taskId = "taskId";
    workflowTaskService.completeTask(taskId);
    verify(taskService, times(1)).complete(taskId);
  }
}
