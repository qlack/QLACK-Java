package com.eurodyn.qlack.fuse.workflow.service;

import com.eurodyn.qlack.fuse.workflow.dto.ProcessHistoryDTO;
import com.eurodyn.qlack.fuse.workflow.dto.ProcessInstanceDTO;

import java.util.List;
import java.util.Map;

/**
 * This service provides API methods related to the processes and workflow.
 *
 * @author European Dynamics
 */
public interface WorkflowService {

  /**
   * Given the id of a process (as defined in the bpmn file), a new workflow instance starts.
   *
   * @param processId the id of the process
   * @param variables the variables of the process
   * @return the id of the started workflow instance
   */
  String startWorkflowInstance(String processId, Map<String, Object> variables);

  /**
   * Given the id of a suspended process instance, it resumes the process instance.
   *
   * @param processInstanceId the id of the suspended process instance
   */
  void resumeWorkflowInstance(String processInstanceId);

  /**
   * Given the id of a started process instance, it suspends the found process instance.
   *
   * @param processInstanceId the id of the started process instance
   */
  void suspendWorkflowInstance(String processInstanceId);

  /**
   * Given the id of a started process instance, it deletes the found process instance.
   *
   * @param processInstanceId the id of the started process instance
   * @param reasonOfDeletion the reason for deletion
   */
  void deleteWorkflowInstance(String processInstanceId, String reasonOfDeletion);

  /**
   * Given the id of process, it returns all the active process instances.
   *
   * @param processId the id of the process
   * @return a list containing the active process instances
   */
  List<ProcessInstanceDTO> getProcessInstancesByProcessId(String processId);

  /**
   * Given the id of an existing process, it returns the history of this process and its data for
   * each version. If no process is found, it returns an empty list.
   *
   * @param processId the id of the process
   * @return a list containing all the history versions
   */
  List<ProcessHistoryDTO> getProcessHistory(String processId);

  /**
   * This method reads the bpmn files located under the resources/processes folder and reads their
   * content. If their content has already been persisted in the DB tables and no changes are
   * found, nothing happens. In any other case, a new version of the process is created.
   */
  void updateProcessesFromResources();

}
