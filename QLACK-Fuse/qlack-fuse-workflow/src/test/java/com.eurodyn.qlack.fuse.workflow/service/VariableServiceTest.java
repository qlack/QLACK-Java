package com.eurodyn.qlack.fuse.workflow.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.eurodyn.qlack.common.exception.QDoesNotExistException;
import org.activiti.engine.ActivitiObjectNotFoundException;
import org.activiti.engine.RuntimeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class VariableServiceTest {

  @InjectMocks
  private VariableService variableService;

  @Mock
  private RuntimeService runtimeService;

  final private String processInstanceId = "processInstanceId";

  final private String variableKey = "variableKey";

  @BeforeEach
  public void init() {
    variableService = new VariableService(runtimeService);
  }

  @Test
  public void getVariableInstanceTest() {
    variableService.getVariableInstance(processInstanceId, variableKey);
    verify(runtimeService, times(1))
      .getVariableInstance(processInstanceId, variableKey);
  }

  @Test
  public void getVariableInstanceActivitiObjectNotFoundExceptionTest() {
    assertThrows(QDoesNotExistException.class, () -> {
      when(runtimeService.getVariableInstance(processInstanceId, variableKey))
              .thenThrow(new ActivitiObjectNotFoundException("ex"));
      variableService.getVariableInstance(processInstanceId, variableKey);
      verify(runtimeService, times(1))
              .getVariableInstance(processInstanceId, variableKey);
    });
  }

  @Test
  public void setVariableInstanceTest() {
    variableService.setVariableInstance(processInstanceId, variableKey, this);
    verify(runtimeService, times(1))
      .setVariableLocal(processInstanceId, variableKey, this);
  }

  @Test
  public void setVariableInstanceActivitiObjectNotFoundExceptionTest() {
    assertThrows(QDoesNotExistException.class, () -> {
      doThrow(new ActivitiObjectNotFoundException("ex")).when(runtimeService)
              .setVariableLocal(processInstanceId, variableKey, this);
      variableService.setVariableInstance(processInstanceId, variableKey, this);
      verify(runtimeService, times(1))
              .setVariableLocal(processInstanceId, variableKey, this);
    });
  }

}
