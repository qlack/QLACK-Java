package com.eurodyn.qlack.fuse.workflow.service;

import com.eurodyn.qlack.common.exception.QDoesNotExistException;
import lombok.RequiredArgsConstructor;
import org.activiti.engine.ActivitiObjectNotFoundException;
import org.activiti.engine.RuntimeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * {@inheritDoc}
 */
@Service
@Transactional
@RequiredArgsConstructor
public class ActivitiVariableService implements VariableService {

  private final RuntimeService runtimeService;

  /**
   * {@inheritDoc}
   */
  @Override
  public Object getVariableInstance(String processInstanceId, String variableKey) {
    try {
      return runtimeService.getVariableInstance(processInstanceId, variableKey);
    } catch (ActivitiObjectNotFoundException e) {
      throw new QDoesNotExistException(
          "There is no instance process with id " + processInstanceId);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setVariableInstance(String processInstanceId, String variableKey,
      Object variableValue) {
    try {
      runtimeService.setVariableLocal(processInstanceId, variableKey, variableValue);
    } catch (ActivitiObjectNotFoundException e) {
      throw new QDoesNotExistException(
          "There is no instance process with id " + processInstanceId);
    }
  }

}
