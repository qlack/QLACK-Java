package com.eurodyn.qlack.fuse.search.util;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import java.net.UnknownHostException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.util.ReflectionTestUtils;

@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration(locations = {"application.properties"})
public class EsConfigTest {

  @InjectMocks
  private EsConfig esConfig;

  @Mock
  private Environment env;

  @Before
  public void init() {
    esConfig = new EsConfig();
    when(env.getProperty("qlack.fuse.search.cluster.name"))
      .thenReturn("cluster.name");
    when(env.getProperty("qlack.fuse.search.host.name"))
      .thenReturn("localhost");
    when(env.getProperty("qlack.fuse.search.host.port")).thenReturn("9200");
    ReflectionTestUtils.setField(esConfig, "env", env);
  }

  @Test
  public void esConfigTest() throws UnknownHostException {
    assertNotNull(esConfig.client());
    assertNotNull(esConfig.elasticsearchTemplate());
  }

}
