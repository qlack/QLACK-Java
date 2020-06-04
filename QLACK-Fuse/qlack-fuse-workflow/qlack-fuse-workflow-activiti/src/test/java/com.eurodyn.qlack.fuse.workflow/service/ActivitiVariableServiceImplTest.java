package com.eurodyn.qlack.fuse.workflow.service;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.eurodyn.qlack.common.exception.QDoesNotExistException;
import org.activiti.engine.ActivitiObjectNotFoundException;
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

  private String processInstanceId = "processInstanceId";

  private String variableKey = "variableKey";

  @Before
  public void init() {
    variableService = new ActivitiVariableService(runtimeService);
  }

  @Test
  public void getVariableInstanceTest() {
    variableService.getVariableInstance(processInstanceId, variableKey);
    verify(runtimeService, times(1))
      .getVariableInstance(processInstanceId, variableKey);
  }

  @Test(expected = QDoesNotExistException.class)
  public void getVariableInstanceActivitiObjectNotFoundExceptionTest() {
    when(runtimeService.getVariableInstance(processInstanceId, variableKey))
      .thenThrow(new ActivitiObjectNotFoundException("ex"));
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

  @Test(expected = QDoesNotExistException.class)
  public void setVariableInstanceActivitiObjectNotFoundExceptionTest() {
    doThrow(new ActivitiObjectNotFoundException("ex")).when(runtimeService)
      .setVariableLocal(processInstanceId, variableKey, this);
    variableService.setVariableInstance(processInstanceId, variableKey, this);
    verify(runtimeService, times(1))
      .setVariableLocal(processInstanceId, variableKey, this);
  }

}
