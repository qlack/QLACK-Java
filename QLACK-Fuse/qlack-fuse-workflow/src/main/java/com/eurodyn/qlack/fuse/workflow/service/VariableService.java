package com.eurodyn.qlack.fuse.workflow.service;

import com.eurodyn.qlack.common.exception.QDoesNotExistException;
import org.activiti.engine.ActivitiObjectNotFoundException;
import org.activiti.engine.RuntimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * This service provides methods related to the task/process variables of Activiti.
 *
 * @author European Dynamics
 */
@Service
@Transactional
public class VariableService {

  private final RuntimeService runtimeService;

  @Autowired
  public VariableService(RuntimeService runtimeService) {
    this.runtimeService = runtimeService;
  }

  /**
   * Given the id of the process instance and the key of the variable, it returns the value of the variable. If the process instance is
   * not found, then an exception is thrown. If the process instance does not have variable with the requested key, a null object is
   * returned.
   *
   * @param processInstanceId the id of the process instance
   * @param variableKey the key of the variable
   * @return the value of the variable
   */
  public Object getVariableInstance(String processInstanceId, String variableKey) {
    try {
      return runtimeService.getVariableInstance(processInstanceId, variableKey);
    } catch (ActivitiObjectNotFoundException e) {
      throw new QDoesNotExistException("There is no instance process with id " + processInstanceId);
    }
  }

  /**
   * Given the id of the process instance, it updates the value of the variable based on the provided key and value. If the process
   * instance is not found, then an exception is thrown. If the process instance does not have variable with the requested key, nothing
   * happens.
   *
   * @param processInstanceId the id of the process instance
   * @param variableKey the key of the variable
   * @param variableValue the value of the variable
   */
  public void setVariableInstance(String processInstanceId, String variableKey, Object variableValue) {
    try {
      runtimeService.setVariableLocal(processInstanceId, variableKey, variableValue);
    } catch (ActivitiObjectNotFoundException e) {
      throw new QDoesNotExistException("There is no instance process with id " + processInstanceId);
    }
  }
  
}
