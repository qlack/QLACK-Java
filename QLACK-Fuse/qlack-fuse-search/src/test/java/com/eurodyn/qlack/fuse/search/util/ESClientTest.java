package com.eurodyn.qlack.fuse.search.util;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ESClientTest {

  @InjectMocks
  private ESClient esClient;

  private Properties createProperties() {
    Properties properties = new Properties();
    properties.setEsHosts("http://localhost:9200");
    properties.setEsPassword("password");
    properties.setEsUsername("userName");
    return properties;
  }

  @Before
  public void init() {
    esClient = new ESClient(createProperties());
  }

  @Test
  public void initTest() {
    esClient.init();
    assertNotNull(esClient.getClient());
  }

  @Test
  public void initTestEmptyUsername() {
    Properties properties = createProperties();
    properties.setEsUsername(null);
    esClient = new ESClient(properties);
    esClient.init();
    assertNotNull(esClient.getClient());
  }

  @Test
  public void initTestEmptyPassword() {
    Properties properties = createProperties();
    properties.setEsPassword(null);
    esClient = new ESClient(properties);
    esClient.init();
    assertNotNull(esClient.getClient());
  }

  @Test
  public void shutdownTest() throws IOException {
    esClient.shutdown();
    assertNull(esClient.getClient());
  }

  @Test
  public void getClientTest() {
    //client is null because is not set
    assertNull(esClient.getClient());
  }

}
