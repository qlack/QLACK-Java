package com.eurodyn.qlack.fuse.workflow.service;

import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.RuntimeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * {@inheritDoc}
 */
@Service
@Transactional
@RequiredArgsConstructor
public class CamundaVariableService implements VariableService {

  private final RuntimeService runtimeService;

  /**
   * {@inheritDoc}
   */
  @Override
  public Object getVariableInstance(String processInstanceId, String variableKey) {
    return runtimeService.createVariableInstanceQuery()
        .processInstanceIdIn(processInstanceId)
        .variableName(variableKey)
        .singleResult();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setVariableInstance(String processInstanceId, String variableKey, Object variableValue) {
    runtimeService.setVariableLocal(processInstanceId, variableKey, variableValue);
  }

}
