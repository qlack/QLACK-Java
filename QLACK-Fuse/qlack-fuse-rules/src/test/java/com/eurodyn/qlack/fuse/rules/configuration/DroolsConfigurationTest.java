package com.eurodyn.qlack.fuse.rules.configuration;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DroolsConfigurationTest {

  @InjectMocks
  private DroolsConfiguration droolsConfiguration;

  @Before
  public void init() {
    droolsConfiguration = new DroolsConfiguration();
  }

  @Test
  public void droolsConfigurationTest(){
    assertNotNull(droolsConfiguration.kieBase());
    assertNotNull(droolsConfiguration.kieSession());
    assertNotNull(droolsConfiguration.statelessKieSession());
    assertNotNull(droolsConfiguration.kiePostProcessor());
  }
}
