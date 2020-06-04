package com.eurodyn.qlack.fuse.workflow.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.eurodyn.qlack.fuse.workflow.dto.ProcessHistoryDTO;
import com.eurodyn.qlack.fuse.workflow.dto.ProcessInstanceDTO;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.camunda.bpm.engine.history.HistoricProcessInstanceQuery;
import org.camunda.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.camunda.bpm.engine.impl.persistence.entity.HistoricProcessInstanceEntity;
import org.camunda.bpm.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.camunda.bpm.engine.repository.ProcessDefinitionQuery;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.runtime.ProcessInstanceQuery;
import org.camunda.bpm.engine.runtime.VariableInstance;
import org.camunda.bpm.engine.runtime.VariableInstanceQuery;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
public class CamundaWorkflowServiceTest {

  @InjectMocks
  private CamundaWorkflowService workflowService;
  @Mock
  private RuntimeService runtimeService;
  @Mock
  private HistoryService historyService;
  @Mock
  private RepositoryService repositoryService;
  @Mock
  private EntityManager entityManager;
  @Mock
  private ProcessInitService processInitService;

  @Mock
  private ProcessInstanceQuery processInstanceQuery;
  @Mock
  private HistoricProcessInstanceQuery historicProcessInstanceQuery;
  @Mock
  private ProcessDefinitionQuery processDefinitionQuery;
  @Mock
  private VariableInstanceQuery variableInstanceQuery;
  @Mock
  private Query query;

  @Test
  public void startWorkflowInstanceTest() {
    String processId = "processId";
    String expectedWorkflowId = "id";
    ExecutionEntity processInstance = new ExecutionEntity();
    processInstance.setId(expectedWorkflowId);
    Map<String, Object> variables = Collections.singletonMap("var1", this);
    when(runtimeService.startProcessInstanceByKey(processId, variables))
        .thenReturn(processInstance);

    String actualWorkflowId = workflowService.startWorkflowInstance(processId, variables);

    assertEquals(expectedWorkflowId, actualWorkflowId);
    verify(runtimeService, times(1)).startProcessInstanceByKey(processId, variables);
  }

  @Test
  public void resumeWorkflowInstanceTest() {
    String processInstanceId = "processInstanceId";
    workflowService.resumeWorkflowInstance(processInstanceId);
    verify(runtimeService, times(1)).activateProcessInstanceById(processInstanceId);
  }

  @Test
  public void suspendWorkflowInstanceTest() {
    String processInstanceId = "processInstanceId";
    workflowService.suspendWorkflowInstance(processInstanceId);
    verify(runtimeService, times(1)).suspendProcessInstanceById(processInstanceId);
  }

  @Test
  public void deleteWorkflowInstanceTest() {
    String processInstanceId = "processInstanceId";
    String reason = "reason";
    workflowService.deleteWorkflowInstance(processInstanceId, reason);
    verify(runtimeService, times(1)).deleteProcessInstance(processInstanceId, reason);
  }

  @Test
  public void getProcessInstancesByProcessIdTest() {
    String processId = "processId";

    ExecutionEntity processInstance = new ExecutionEntity();
    processInstance.setId("123");
    processInstance.setSuspensionState(1);
    processInstance.setProcessInstanceId("processInstanceId");

    //this is a hack as unable to simply initialise a VariableInstanceEntity with properties set
    VariableInstance variableInstance = mock(VariableInstance.class);
    when(variableInstance.getName()).thenReturn("varName");
    when(variableInstance.getValue()).thenReturn("varValue");
    List<VariableInstance> variableInstances = Collections.singletonList(variableInstance);

    List<ProcessInstance> processInstanceList = new ArrayList<>();
    processInstanceList.add(processInstance);

    when(runtimeService.createVariableInstanceQuery()).thenReturn(variableInstanceQuery);
    when(variableInstanceQuery.processInstanceIdIn(processInstance.getProcessInstanceId())).thenReturn(variableInstanceQuery);
    when(variableInstanceQuery.list()).thenReturn(variableInstances);

    when(runtimeService.createProcessInstanceQuery()).thenReturn(processInstanceQuery);
    when(processInstanceQuery.processDefinitionKey(processId)).thenReturn(processInstanceQuery);
    when(processInstanceQuery.list()).thenReturn(processInstanceList);

    List<ProcessInstanceDTO> actual = workflowService.getProcessInstancesByProcessId(processId);

    assertFalse(actual.isEmpty());

    verify(runtimeService, times(1)).createVariableInstanceQuery();
    verify(variableInstanceQuery, times(1)).processInstanceIdIn(processInstance.getProcessInstanceId());
    verify(variableInstanceQuery, times(1)).list();
    verify(runtimeService, times(1)).createProcessInstanceQuery();
    verify(processInstanceQuery, times(1)).processDefinitionKey(processId);
    verify(processInstanceQuery, times(1)).list();
  }

  @Test
  public void getProcessHistoryTest() {
    String processId = "processId";

    HistoricProcessInstanceEntity historicProcessInstance = new HistoricProcessInstanceEntity();
    historicProcessInstance.setProcessDefinitionId(processId);
    historicProcessInstance.setProcessDefinitionVersion(1);
    historicProcessInstance.setDeleteReason("reason");

    List<HistoricProcessInstance> historicProcessInstances = new ArrayList<>();
    historicProcessInstances.add(historicProcessInstance);

    when(historyService.createHistoricProcessInstanceQuery()).thenReturn(historicProcessInstanceQuery);
    when(historicProcessInstanceQuery.processDefinitionKey(processId)).thenReturn(historicProcessInstanceQuery);
    when(historicProcessInstanceQuery.list()).thenReturn(historicProcessInstances);

    ProcessDefinitionEntity processDefinition = new ProcessDefinitionEntity();
    processDefinition.setDeploymentId("deploymentId");
    when(repositoryService.createProcessDefinitionQuery()).thenReturn(processDefinitionQuery);
    when(processDefinitionQuery.processDefinitionId(processId)).thenReturn(processDefinitionQuery);
    when(processDefinitionQuery.singleResult()).thenReturn(processDefinition);

    when(entityManager.createNativeQuery(anyString())).thenReturn(query);
    when(query.getSingleResult()).thenReturn("new object".getBytes());

    List<ProcessHistoryDTO> actual = workflowService.getProcessHistory(processId);

    assertFalse(actual.isEmpty());
  }

  @Test
  public void updateProcessesFromResourcesTest() {
    workflowService.updateProcessesFromResources();
    verify(processInitService, times(1)).updateProcessesFromResources();
  }

}
