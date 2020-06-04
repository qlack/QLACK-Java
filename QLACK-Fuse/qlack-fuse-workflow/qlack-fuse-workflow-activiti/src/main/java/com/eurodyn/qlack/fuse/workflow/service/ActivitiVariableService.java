package com.eurodyn.qlack.fuse.workflow.service;

import lombok.RequiredArgsConstructor;
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
    return runtimeService.getVariableInstance(processInstanceId, variableKey);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setVariableInstance(String processInstanceId, String variableKey,
      Object variableValue) {
    runtimeService.setVariableLocal(processInstanceId, variableKey, variableValue);
  }

}
