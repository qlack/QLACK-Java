package com.eurodyn.qlack.fuse.workflow.configuration;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class CamundaConfigurationTest {

  @InjectMocks
  private CamundaConfiguration configuration;

  @Test
  public void processEngineConfigurationTest() {
    assertNotNull(configuration.processEngineConfiguration());
  }

  @Test
  public void processEngineTest() {
    assertNotNull(configuration.processEngine());
  }

  @Test
  public void repositoryServiceTest() {
    assertNotNull(configuration.repositoryService());
  }
}
