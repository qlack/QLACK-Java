package com.eurodyn.qlack.fuse.workflow.dto;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TaskDTOTest {

  private TaskDTO taskDTO;

  @Before
  public void init() {
    taskDTO = new TaskDTO();
  }

  @Test
  public void idTest() {
    String id = "id";
    taskDTO.setId(id);
    assertEquals(id, taskDTO.getId());
  }

  @Test
  public void nameTest() {
    String name = "name";
    taskDTO.setName(name);
    assertEquals(name, taskDTO.getName());
  }

  @Test
  public void processInstanceIdTest() {
    String processInstanceId = "processInstanceId";
    taskDTO.setProcessInstanceId(processInstanceId);
    assertEquals(processInstanceId, taskDTO.getProcessInstanceId());
  }

  @Test
  public void variablesTest() {
    Map<String, Object> variables = new HashMap<>();
    variables.put("key", this);
    taskDTO.setVariables(variables);
    assertEquals(variables, taskDTO.getVariables());
  }

}
