package com.eurodyn.qlack.fuse.workflow.service;

import com.eurodyn.qlack.common.exception.QDoesNotExistException;
import com.eurodyn.qlack.fuse.workflow.dto.ProcessHistoryDTO;
import com.eurodyn.qlack.fuse.workflow.dto.ProcessInstanceDTO;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import org.activiti.engine.ActivitiObjectNotFoundException;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * This service provides methods related to the processes and workflow of Activiti.
 *
 * @author European Dynamics
 */
@Service
@Transactional
public class WorkflowService {

  private final RuntimeService runtimeService;

  private final HistoryService historyService;

  private final EntityManager entityManager;

  private final ProcessInitService processInitService;

  @Autowired
  public WorkflowService(RuntimeService runtimeService, HistoryService historyService,
      EntityManager entityManager, ProcessInitService processInitService) {
    this.runtimeService = runtimeService;
    this.historyService = historyService;
    this.entityManager = entityManager;
    this.processInitService = processInitService;
  }

  /**
   * Given the id of a process (as defined in the xml), a new workflow instance starts. If no
   * process with the found id exists, an exception is thrown.
   *
   * @param processId the id of the process
   * @param variables the variables of the process
   * @return the id of the started workflow instance
   */
  public String startWorkflowInstance(String processId, Map<String, Object> variables) {
    try {
      ProcessInstance processInstance = runtimeService
          .startProcessInstanceByKey(processId, variables);
      return processInstance.getId();
    } catch (ActivitiObjectNotFoundException e) {
      throw new QDoesNotExistException("Process with id " + processId + " does not exist.");
    }
  }

  /**
   * Given the id of a suspended process instance, it resumes the process instance. If no instance
   * is found or if the instance is already running, it throws an exception.
   *
   * @param processInstanceId the id of the suspended process instance
   */
  public void resumeWorkflowInstance(String processInstanceId) {
    try {
      runtimeService.activateProcessInstanceById(processInstanceId);
    } catch (ActivitiObjectNotFoundException e) {
      throw new QDoesNotExistException("There is no instance process with id " + processInstanceId);
    }
  }

  /**
   * Given the id of a started process instance, it suspends the found process instance. If no
   * instance is found or if the instance is already suspended, it throws an exception.
   *
   * @param processInstanceId the id of the started process instance
   */
  public void suspendWorkflowInstance(String processInstanceId) {
    try {
      runtimeService.suspendProcessInstanceById(processInstanceId);
    } catch (ActivitiObjectNotFoundException e) {
      throw new QDoesNotExistException("There is no instance process with id " + processInstanceId);
    }
  }

  /**
   * Given the id of a started process instance, it deletes the found process instance. If no
   * instance is found, it throws an exception.
   *
   * @param processInstanceId the id of the started process instance
   * @param reasonOfDeletion the reason for deletion
   */
  public void deleteWorkflowInstance(String processInstanceId, String reasonOfDeletion) {
    try {
      runtimeService.deleteProcessInstance(processInstanceId, reasonOfDeletion);
    } catch (ActivitiObjectNotFoundException e) {
      throw new QDoesNotExistException("There is no instance process with id " + processInstanceId);
    }
  }

  /**
   * Given the id of process, it returns all the active process instances.
   *
   * @param processId the id of the process
   * @return a list containing the active process instances
   */
  public List<ProcessInstanceDTO> getProcessInstancesByProcessId(String processId) {
    List<ProcessInstanceDTO> processInstances = new ArrayList<>();

    List<ProcessInstance> foundProcessInstances = runtimeService.createProcessInstanceQuery()
        .processDefinitionKey(processId)
        .includeProcessVariables().list();
    foundProcessInstances.stream()
        .forEach(i -> processInstances
            .add(new ProcessInstanceDTO(i.getId(), i.isSuspended(), i.getProcessVariables())));

    return processInstances;
  }

  /**
   * Given the id of an existing process, it returns the history of this process and its data for
   * each version. If no process is found, it returns an empty list.
   *
   * @param processId the id of the process
   * @return a list containing all the history versions
   */
  public List<ProcessHistoryDTO> getProcessHistory(String processId) {
    List<ProcessHistoryDTO> processHistory = new ArrayList<>();

    List<HistoricProcessInstance> foundProcessHistory = historyService
        .createHistoricProcessInstanceQuery().processDefinitionKey(processId)
        .list();

    foundProcessHistory.stream().forEach(
        h -> processHistory.add(new ProcessHistoryDTO(h.getDeploymentId(), h.getDeploymentId(),
            h.getProcessDefinitionVersion(), h.getDeleteReason(), h.getName(), h.getDescription(),
            getProcessData(h.getDeploymentId()))));

    return processHistory;
  }

  /**
   * This method reads the .xml files located under the resources/processes folder and reads their
   * content. If their content has already been persisted in the Activiti tables and no changes are
   * found, nothing happens. In any other case, a new version of the process is created.
   */
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
    return (byte[]) entityManager.createNativeQuery(
        "SELECT BYTES_ FROM act_ge_bytearray WHERE DEPLOYMENT_ID_ = " + deploymentId)
        .getSingleResult();
  }

}
