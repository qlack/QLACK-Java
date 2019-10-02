package com.eurodyn.qlack.fuse.workflow.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.eurodyn.qlack.common.exception.QDoesNotExistException;
import java.util.ArrayList;
import java.util.List;
import org.activiti.engine.ActivitiObjectNotFoundException;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class WorkflowTaskServiceTest {

  @InjectMocks
  private WorkflowTaskService workflowTaskService;

  @Mock
  private TaskService taskService;

  @Mock
  private TaskQuery taskQuery;

  @Mock
  private Task task;

  @Before
  public void init() {
    workflowTaskService = new WorkflowTaskService(taskService);
  }

  @Test
  public void getTasksByProcessInstanceIdTest() {
    String processInstanceId = "processInstanceId";
    when(taskService.createTaskQuery()).thenReturn(taskQuery);
    when(taskQuery.processInstanceId(processInstanceId)).thenReturn(taskQuery);
    when(taskQuery.includeProcessVariables()).thenReturn(taskQuery);

    List<Task> tasks = new ArrayList<>();
    tasks.add(task);
    when(task.getId()).thenReturn("id");
    when(taskQuery.list()).thenReturn(tasks);

    assertFalse(workflowTaskService.getTasksByProcessInstanceId(processInstanceId).isEmpty());
  }

  @Test
  public void getTasksByProcessInstanceIdNoResultsTrue() {
    String processInstanceId = "processInstanceId";
    when(taskService.createTaskQuery()).thenReturn(taskQuery);
    when(taskQuery.processInstanceId(processInstanceId)).thenReturn(taskQuery);
    when(taskQuery.includeProcessVariables()).thenReturn(taskQuery);
    when(taskQuery.list()).thenReturn(new ArrayList<>());

    assertTrue(workflowTaskService.getTasksByProcessInstanceId(processInstanceId).isEmpty());
  }

  @Test
  public void completeTaskTask() {
    String taskId = "taskId";
    workflowTaskService.completeTask(taskId);
    verify(taskService, times(1)).complete(taskId);
  }

  @Test(expected = QDoesNotExistException.class)
  public void getTasksByProcessInstanceIdActivitiObjectNotFoundExceptionTest() {
    String taskId = "taskId";
    doThrow(new ActivitiObjectNotFoundException("ex")).when(taskService).complete(taskId);
    workflowTaskService.completeTask(taskId);
    verify(taskService, times(1)).complete(taskId);
  }

}
