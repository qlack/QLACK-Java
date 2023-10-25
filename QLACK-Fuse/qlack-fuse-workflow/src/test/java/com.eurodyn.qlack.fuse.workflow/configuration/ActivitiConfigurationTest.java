package com.eurodyn.qlack.fuse.workflow.configuration;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ActivitiConfigurationTest {

  @InjectMocks
  private ActivitiConfiguration activitiConfiguration = new ActivitiConfiguration();

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
