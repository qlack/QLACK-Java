package com.eurodyn.qlack.fuse.workflow.dto;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ProcessInstanceDTOTest {

  private ProcessInstanceDTO processInstanceDTO;

  @Before
  public void init(){
    processInstanceDTO = new ProcessInstanceDTO();
  }

  @Test
  public void idTest(){
    String id = "id";
    processInstanceDTO.setId(id);
    assertEquals(id, processInstanceDTO.getId());
  }

  @Test
  public void suspendedTest(){
    processInstanceDTO.setSuspended(true);
    assertEquals(true, processInstanceDTO.isSuspended());
  }

  @Test
  public void variablesTest(){
    Map<String, Object> variables = new HashMap<>();
    variables.put("key", this);
    processInstanceDTO.setVariables(variables);
    assertEquals(variables, processInstanceDTO.getVariables());
  }

}
