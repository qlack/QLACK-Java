package com.eurodyn.qlack.fuse.rules.dto;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ExecutionResultsDTOTest {

  private ExecutionResultsDTO executionResultsDTO;

  @Before
  public void init() {
    executionResultsDTO = new ExecutionResultsDTO();
  }

  @Test
  public void globalsTest() {
    Map<String, byte[]> globals = new HashMap<>();
    executionResultsDTO.setGlobals(globals);
    assertEquals(globals, executionResultsDTO.getGlobals());
  }

  @Test
  public void factsTest() {
    List<byte[]> facts = new ArrayList<>();
    executionResultsDTO.setFacts(facts);
    assertEquals(facts, executionResultsDTO.getFacts());
  }

}
