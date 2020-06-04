package com.eurodyn.qlack.fuse.workflow.service;

import com.eurodyn.qlack.fuse.workflow.dto.ProcessHistoryDTO;
import com.eurodyn.qlack.fuse.workflow.dto.ProcessInstanceDTO;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.runtime.VariableInstance;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * {@inheritDoc}
 */
@Service
@Transactional
@RequiredArgsConstructor
public class CamundaWorkflowService implements WorkflowService {

  private final RuntimeService runtimeService;
  private final HistoryService historyService;
  private final RepositoryService repositoryService;
  private final EntityManager entityManager;
  private final ProcessInitService processInitService;

  /**
   * {@inheritDoc}
   */
  @Override
  public String startWorkflowInstance(String processId, Map<String, Object> variables) {
    ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processId, variables);
    return processInstance.getId();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void resumeWorkflowInstance(String processInstanceId) {
    runtimeService.activateProcessInstanceById(processInstanceId);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void suspendWorkflowInstance(String processInstanceId) {
    runtimeService.suspendProcessInstanceById(processInstanceId);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void deleteWorkflowInstance(String processInstanceId, String reasonOfDeletion) {
    runtimeService.deleteProcessInstance(processInstanceId, reasonOfDeletion);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<ProcessInstanceDTO> getProcessInstancesByProcessId(String processId) {
    Function<ProcessInstance, ProcessInstanceDTO> toDTO = ee -> {
      Map<String, Object> variablesMap = runtimeService.createVariableInstanceQuery()
          .processInstanceIdIn(ee.getProcessInstanceId()).list().stream()
          .collect(Collectors.toMap(VariableInstance::getName, VariableInstance::getValue));

      return new ProcessInstanceDTO(ee.getId(), ee.isSuspended(), variablesMap);
    };

    List<ProcessInstance> processInstances = runtimeService
        .createProcessInstanceQuery()
        .processDefinitionKey(processId)
        .list();

    return processInstances.stream()
        .map(toDTO)
        .collect(Collectors.toList());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<ProcessHistoryDTO> getProcessHistory(String processId) {

    Function<HistoricProcessInstance, ProcessHistoryDTO> toDTO = hpi -> {
      ProcessDefinition processDefinition = repositoryService
          .createProcessDefinitionQuery()
          .processDefinitionId(hpi.getProcessDefinitionId())
          .singleResult();

      return buildProcessHistoryDTO(hpi, processDefinition);
    };

    List<HistoricProcessInstance> foundProcessHistory = historyService
        .createHistoricProcessInstanceQuery()
        .processDefinitionKey(processId)
        .list();

    return foundProcessHistory
        .stream()
        .map(toDTO)
        .collect(Collectors.toList());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void updateProcessesFromResources() {
    processInitService.updateProcessesFromResources();
  }

  private ProcessHistoryDTO buildProcessHistoryDTO(HistoricProcessInstance hpi,
      ProcessDefinition processDefinition) {
    String deploymentId = processDefinition.getDeploymentId();

    return new ProcessHistoryDTO(deploymentId, deploymentId, hpi.getProcessDefinitionVersion(),
        hpi.getDeleteReason(), processDefinition.getName(), processDefinition.getDescription(),
        getProcessData(deploymentId));
  }

  private byte[] getProcessData(String deploymentId) {
    Query query = entityManager
        .createNativeQuery(
            "SELECT BYTES_ FROM ACT_GE_BYTEARRAY WHERE DEPLOYMENT_ID_ = ?");
    query.setParameter(1, deploymentId);
    return (byte[]) query.getSingleResult();
  }

}
