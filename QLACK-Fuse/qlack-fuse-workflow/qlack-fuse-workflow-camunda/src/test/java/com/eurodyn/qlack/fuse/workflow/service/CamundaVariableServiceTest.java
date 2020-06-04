package com.eurodyn.qlack.fuse.workflow.service;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.impl.persistence.entity.VariableInstanceEntity;
import org.camunda.bpm.engine.runtime.VariableInstance;
import org.camunda.bpm.engine.runtime.VariableInstanceQuery;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class CamundaVariableServiceTest {

  @InjectMocks
  private CamundaVariableService variableService;
  @Mock
  private RuntimeService runtimeService;
  @Mock
  private VariableInstanceQuery variableInstanceQuery;

  private static final String processInstanceId = "processInstanceId";
  private static final String variableKey = "variableKey";

  @Test
  public void getVariableInstanceTest() {
    when(runtimeService.createVariableInstanceQuery()).thenReturn(variableInstanceQuery);
    when(variableInstanceQuery.processInstanceIdIn(processInstanceId)).thenReturn(variableInstanceQuery);
    when(variableInstanceQuery.variableName(variableKey)).thenReturn(variableInstanceQuery);
    when(variableInstanceQuery.singleResult()).thenReturn(new VariableInstanceEntity());

    variableService.getVariableInstance(processInstanceId, variableKey);

    verify(runtimeService, times(1)).createVariableInstanceQuery();
    verify(variableInstanceQuery, times(1)).processInstanceIdIn(processInstanceId);
    verify(variableInstanceQuery, times(1)).variableName(variableKey);
    verify(variableInstanceQuery, times(1)).singleResult();
  }

  @Test
  public void setVariableInstanceTest() {
    variableService.setVariableInstance(processInstanceId, variableKey, this);
    verify(runtimeService, times(1)).setVariableLocal(processInstanceId, variableKey, this);
  }
}
