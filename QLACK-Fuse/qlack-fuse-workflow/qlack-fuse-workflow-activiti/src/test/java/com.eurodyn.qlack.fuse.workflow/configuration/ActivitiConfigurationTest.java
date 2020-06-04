package com.eurodyn.qlack.fuse.workflow.configuration;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ActivitiConfigurationTest {

  @InjectMocks
  private ActivitiConfiguration activitiConfiguration;

  @Test
  public void processEngineConfigurationTest() {
    assertNotNull(activitiConfiguration.processEngineConfiguration());
  }

  @Test
  public void processEngineTest() {
    assertNotNull(activitiConfiguration.processEngine());
  }

  @Test
  public void repositoryServiceTest() {
    assertNotNull(activitiConfiguration.repositoryService());
  }
}
