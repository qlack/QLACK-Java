package com.eurodyn.qlack.fuse.workflow.service;

import com.eurodyn.qlack.fuse.workflow.dto.ProcessHistoryDTO;
import com.eurodyn.qlack.fuse.workflow.dto.ProcessInstanceDTO;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * {@inheritDoc}
 */
@Service
@Transactional
@RequiredArgsConstructor
public class ActivitiWorkflowService implements WorkflowService {

  private final RuntimeService runtimeService;
  private final HistoryService historyService;
  private final EntityManager entityManager;
  private final ProcessInitService processInitService;

  /**
   * {@inheritDoc}
   */
  @Override
  public String startWorkflowInstance(String processId, Map<String, Object> variables) {
    ProcessInstance processInstance = runtimeService
        .startProcessInstanceByKey(processId, variables);
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
  public void deleteWorkflowInstance(String processInstanceId,
      String reasonOfDeletion) {
    runtimeService.deleteProcessInstance(processInstanceId, reasonOfDeletion);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<ProcessInstanceDTO> getProcessInstancesByProcessId(
      String processId) {
    List<ProcessInstanceDTO> processInstances = new ArrayList<>();

    List<ProcessInstance> foundProcessInstances = runtimeService
        .createProcessInstanceQuery()
        .processDefinitionKey(processId)
        .includeProcessVariables().list();
    foundProcessInstances
        .forEach(i -> processInstances
            .add(new ProcessInstanceDTO(i.getId(), i.isSuspended(),
                i.getProcessVariables())));

    return processInstances;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<ProcessHistoryDTO> getProcessHistory(String processId) {
    List<ProcessHistoryDTO> processHistory = new ArrayList<>();

    List<HistoricProcessInstance> foundProcessHistory = historyService
        .createHistoricProcessInstanceQuery().processDefinitionKey(processId)
        .list();

    foundProcessHistory.forEach(
        h -> processHistory
            .add(new ProcessHistoryDTO(h.getDeploymentId(), h.getDeploymentId(),
                h.getProcessDefinitionVersion(), h.getDeleteReason(), h.getName(),
                h.getDescription(),
                getProcessData(h.getDeploymentId()))));

    return processHistory;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void updateProcessesFromResources() {
    processInitService.updateProcessesFromResources();
  }

  /**
   * Custom query to retrieve the byte array of the process data.
   *
   * @param deploymentId the id of the deployment of the version
   * @return the found data in byte array representation
   */
  private byte[] getProcessData(String deploymentId) {
    Query query = entityManager
        .createNativeQuery(
            "SELECT BYTES_ FROM ACT_GE_BYTEARRAY WHERE DEPLOYMENT_ID_ = ?");
    query.setParameter(1, deploymentId);
    return (byte[]) query.getSingleResult();
  }

}
