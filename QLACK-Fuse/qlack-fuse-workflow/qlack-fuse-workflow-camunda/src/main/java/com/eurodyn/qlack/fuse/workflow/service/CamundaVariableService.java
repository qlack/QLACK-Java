package com.eurodyn.qlack.fuse.workflow.service;

import com.eurodyn.qlack.fuse.workflow.dto.VariableInstanceDTO;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.VariableInstance;
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
  public VariableInstanceDTO getVariableInstance(String processInstanceId, String variableKey) {
    VariableInstance vi = runtimeService.createVariableInstanceQuery()
        .processInstanceIdIn(processInstanceId)
        .variableName(variableKey)
        .singleResult();

    return new VariableInstanceDTO(vi.getId(), vi.getName(), vi.getProcessInstanceId(),
        vi.getExecutionId(), vi.getTaskId(), vi.getTypeName(), vi.getValue());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setVariableInstance(String processInstanceId, String variableKey, Object variableValue) {
    runtimeService.setVariableLocal(processInstanceId, variableKey, variableValue);
  }

}
