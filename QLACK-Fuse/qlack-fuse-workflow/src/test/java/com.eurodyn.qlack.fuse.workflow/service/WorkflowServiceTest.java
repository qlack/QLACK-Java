package com.eurodyn.qlack.fuse.workflow.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.eurodyn.qlack.common.exception.QDoesNotExistException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.activiti.engine.ActivitiObjectNotFoundException;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class WorkflowServiceTest {

  @InjectMocks
  private WorkflowService workflowService;

  @Mock
  private RuntimeService runtimeService;

  @Mock
  private HistoryService historyService;

  @Mock
  private EntityManager entityManager;

  @Mock
  private ProcessInitService processInitService;

  @Mock
  private ProcessInstance processInstance;

  @Mock
  private ProcessInstanceQuery processInstanceQuery;

  @Mock
  private HistoricProcessInstanceQuery historicProcessInstanceQuery;

  @Mock
  private HistoricProcessInstance historicProcessInstance;

  @Mock
  private Query query;

  @Before
  public void init() {
    workflowService = new WorkflowService(runtimeService, historyService,
      entityManager,
      processInitService);
  }

  @Test
  public void startWorkflowInstanceTest() {
    String processId = "processId";
    Map<String, Object> variables = new HashMap<>();
    variables.put("var1", this);
    when(runtimeService.startProcessInstanceByKey(processId, variables))
      .thenReturn(processInstance);
    when(processInstance.getId()).thenReturn("id");
    assertEquals("id",
      workflowService.startWorkflowInstance(processId, variables));
  }

  @Test(expected = QDoesNotExistException.class)
  public void startWorkflowInstanceActivitiObjectNotFoundExceptionTest() {
    String processId = "processId";
    Map<String, Object> variables = new HashMap<>();
    variables.put("var1", this);
    when(runtimeService.startProcessInstanceByKey(processId, variables))
      .thenThrow(new ActivitiObjectNotFoundException("ex"));
    workflowService.startWorkflowInstance(processId, variables);
  }

  @Test
  public void resumeWorkflowInstanceTest() {
    String processInstanceId = "processInstanceId";
    workflowService.resumeWorkflowInstance(processInstanceId);
    verify(runtimeService, times(1))
      .activateProcessInstanceById(processInstanceId);
  }

  @Test(expected = QDoesNotExistException.class)
  public void resumeWorkflowInstanceActivitiObjectNotFoundExceptionTest() {
    String processInstanceId = "processInstanceId";
    doThrow(new ActivitiObjectNotFoundException("ex")).when(runtimeService)
      .activateProcessInstanceById(processInstanceId);
    workflowService.resumeWorkflowInstance(processInstanceId);
  }

  @Test
  public void suspendWorkflowInstanceTest() {
    String processInstanceId = "processInstanceId";
    workflowService.suspendWorkflowInstance(processInstanceId);
    verify(runtimeService, times(1))
      .suspendProcessInstanceById(processInstanceId);
  }

  @Test(expected = QDoesNotExistException.class)
  public void suspendWorkflowInstanceActivitiObjectNotFoundExceptionTest() {
    String processInstanceId = "processInstanceId";
    doThrow(new ActivitiObjectNotFoundException("ex")).when(runtimeService)
      .suspendProcessInstanceById(processInstanceId);
    workflowService.suspendWorkflowInstance(processInstanceId);
  }

  @Test
  public void deleteWorkflowInstanceTest() {
    String processInstanceId = "processInstanceId";
    String reason = "reason";
    workflowService.deleteWorkflowInstance(processInstanceId, reason);
    verify(runtimeService, times(1))
      .deleteProcessInstance(processInstanceId, reason);
  }

  @Test(expected = QDoesNotExistException.class)
  public void deleteWorkflowInstanceActivitiObjectNotFoundExceptionTest() {
    String processInstanceId = "processInstanceId";
    String reason = "reason";
    doThrow(new ActivitiObjectNotFoundException("ex")).when(runtimeService)
      .deleteProcessInstance(processInstanceId, reason);
    workflowService.deleteWorkflowInstance(processInstanceId, reason);
  }

  @Test
  public void getProcessInstancesByProcessIdTest() {
    String processId = "processId";
    when(runtimeService.createProcessInstanceQuery())
      .thenReturn(processInstanceQuery);
    when(processInstanceQuery.processDefinitionKey(processId))
      .thenReturn(processInstanceQuery);
    when(processInstanceQuery.includeProcessVariables())
      .thenReturn(processInstanceQuery);

    List<ProcessInstance> processInstanceList = new ArrayList<>();
    processInstanceList.add(processInstance);
    when(processInstanceQuery.list()).thenReturn(processInstanceList);

    assertFalse(
      workflowService.getProcessInstancesByProcessId(processId).isEmpty());
  }

  @Test
  public void getProcessHistoryTest() {
    String processId = "processId";
    when(historyService.createHistoricProcessInstanceQuery())
      .thenReturn(historicProcessInstanceQuery);
    when(historicProcessInstanceQuery.processDefinitionKey(processId))
      .thenReturn(historicProcessInstanceQuery);

    List<HistoricProcessInstance> historicProcessInstances = new ArrayList<>();
    historicProcessInstances.add(historicProcessInstance);
    when(historicProcessInstanceQuery.list())
      .thenReturn(historicProcessInstances);

    when(entityManager.createNativeQuery(anyString())).thenReturn(query);
    when(query.getSingleResult()).thenReturn("new object".getBytes());

    assertFalse(workflowService.getProcessHistory(processId).isEmpty());
  }

  @Test
  public void updateProcessesFromResourcesTest() {
    workflowService.updateProcessesFromResources();
    verify(processInitService, times(1)).updateProcessesFromResources();
  }

}
