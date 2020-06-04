package com.eurodyn.qlack.fuse.workflow.service;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.camunda.bpm.engine.RuntimeService;
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

  private static final String processInstanceId = "processInstanceId";
  private static final String variableKey = "variableKey";

  @Test
  public void getVariableInstanceTest() {
    variableService.getVariableInstance(processInstanceId, variableKey);
    verify(runtimeService, times(1)).getVariable(processInstanceId, variableKey);
  }

  @Test
  public void setVariableInstanceTest() {
    variableService.setVariableInstance(processInstanceId, variableKey, this);
    verify(runtimeService, times(1)).setVariableLocal(processInstanceId, variableKey, this);
  }
}
