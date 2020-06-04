package com.eurodyn.qlack.fuse.workflow.service;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.activiti.engine.RuntimeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ActivitiVariableServiceImplTest {

  @InjectMocks
  private ActivitiVariableService variableService;
  @Mock
  private RuntimeService runtimeService;

  private final String processInstanceId = "processInstanceId";

  private final String variableKey = "variableKey";

  @Test
  public void getVariableInstanceTest() {
    variableService.getVariableInstance(processInstanceId, variableKey);
    verify(runtimeService, times(1))
      .getVariableInstance(processInstanceId, variableKey);
  }

  @Test
  public void setVariableInstanceTest() {
    variableService.setVariableInstance(processInstanceId, variableKey, this);
    verify(runtimeService, times(1))
      .setVariableLocal(processInstanceId, variableKey, this);
  }

}
