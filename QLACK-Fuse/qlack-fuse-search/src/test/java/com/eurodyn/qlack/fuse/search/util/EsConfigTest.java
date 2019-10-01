package com.eurodyn.qlack.fuse.search.util;

import static org.junit.Assert.assertNotNull;

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
    ReflectionTestUtils.setField(esConfig, "clusterName", "cluster.name");
    ReflectionTestUtils.setField(esConfig, "hostName", "localhost");
    ReflectionTestUtils.setField(esConfig, "hostPort", "9200");
  }

  @Test
  public void esConfigTest() throws UnknownHostException {
    assertNotNull(esConfig.client());
    assertNotNull(esConfig.elasticsearchTemplate());
  }

}
