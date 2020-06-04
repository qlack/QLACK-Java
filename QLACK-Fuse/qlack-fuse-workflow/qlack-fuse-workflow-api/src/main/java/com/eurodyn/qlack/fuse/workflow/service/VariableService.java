package com.eurodyn.qlack.fuse.workflow.service;

/**
 * This service provides API methods related to the task/process variables.
 *
 * @author European Dynamics
 */
public interface VariableService {

  /**
   * Given the id of the process instance and the key of the variable, it
   * returns the value of the variable. If the process instance is not found,
   * then an exception is thrown. If the process instance does not have
   * variable with the requested key, a null object is returned.
   *
   * @param processInstanceId the id of the process instance
   * @param variableKey the key of the variable
   * @return the value of the variable
   */
  Object getVariableInstance(String processInstanceId, String variableKey);

  /**
   * Given the id of the process instance, it updates the value of the
   * variable based on the provided key and value. If the process instance is
   * not found, then an exception is thrown. If the process instance does not
   * have variable with the requested key, nothing happens.
   *
   * @param processInstanceId the id of the process instance
   * @param variableKey the key of the variable
   * @param variableValue the value of the variable
   */
  void setVariableInstance(String processInstanceId, String variableKey, Object variableValue);

}
